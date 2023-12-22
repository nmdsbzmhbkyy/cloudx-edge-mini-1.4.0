package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.EdgeSyncTransferService;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.RequestErrorService;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.service.UuidRelationService;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealRequestService;
import com.aurine.cloudx.edge.sync.biz.service.handler.Base64ToImageChain;
import com.aurine.cloudx.edge.sync.biz.service.remote.CascadeApplyService;
import com.aurine.cloudx.edge.sync.biz.service.remote.CascadeUnbindService;
import com.aurine.cloudx.edge.sync.biz.service.remote.ExecuteSqlService;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.entity.po.UuidRelation;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.edge.sync.common.enums.CascadeDictEnum;
import com.aurine.cloudx.edge.sync.common.enums.DataSourceEnum;
import com.aurine.cloudx.edge.sync.common.enums.MessageSendStateEnum;
import com.aurine.cloudx.edge.sync.common.enums.SyncTypeEnum;
import com.aurine.cloudx.edge.sync.common.exception.RequestExceptionEnum;
import com.aurine.cloudx.edge.sync.common.utils.EntityChangeUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.CascadeTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OperateTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 9:03
 * @Package: com.aurine.cloudx.edge.sync.biz.service.impl
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@Service
public class DealRequestServiceImpl implements DealRequestService {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private UuidRelationService uuidRelationService;

    @Resource
    private ProjectRelationService projectRelationService;

    @Resource
    private EdgeSyncTransferService edgeSyncTransferService;

    @Resource
    private MqttConnector mqttConnector;

    @Resource
    private CascadeApplyService cascadeApplyService;

    @Resource
    private CascadeUnbindService cascadeUnbindService;

    @Resource
    private Base64ToImageChain base64ToImageChain;

    @Resource
    private RequestErrorService requestErrorService;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    @Resource
    private ExecuteSqlService executeSqlService;
    /**
     * 处理mqtt收到request请求
     * 校验数据
     * 转换参数
     * 分发请求调用open接口更新数据
     * 生成返回消息，更新本地数据库
     * 发送mqtt response 请求返回结果
     *
     * @param taskInfoDto
     */
    @Override
    public void dealRequest(TaskInfoDto taskInfoDto) {
        // 校验msgId
        if (!checkMsgId(taskInfoDto)) {
            return;
        }
        log.info("MQTT RECEIVE REQUEST = {}", JSONObject.toJSONString(taskInfoDto));
        // 转换第三方projectUUID为自己的uuid
        changeProjectUUIDToOwner(taskInfoDto);
        /*新增接收端收到请求将请求和流水表最新一条数据比对，相同取出这条数据直接返回，避免重复处理*/
        Boolean isRepeatMessage = taskInfoService.checkIsRepeatMessage(taskInfoDto.getProjectUUID(), taskInfoDto.getServiceType(), taskInfoDto.getNewMd5());
        if (isRepeatMessage) {
            // 发送消息，清空msgId缓存
            sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
        }
        // 新增操作 责任链判断是否需要base64转图片并保存到minio中
        try {
            if (OperateTypeEnum.ADD.name.equals(taskInfoDto.getType())) {
                base64ToImageChain.doChain(taskInfoDto);
            }
        } catch (Exception e) {
            requestErrorService.dealRequestError(taskInfoDto, e.getMessage());
            e.printStackTrace();
            return;
        }
        // 分发操作
        switch (OpenPushSubscribeCallbackTypeEnum.getByName(taskInfoDto.getServiceType())) {
            case CASCADE:
                dealCascadeRequest(taskInfoDto);
                break;
            case OPERATE:
            case EVENT:
                dealCommonRequest(taskInfoDto);
                break;
            case OTHER:
                deaOtherRequest(taskInfoDto);
                break;
            case COMMAND:
                dealCommandRequest(taskInfoDto);
                break;
            default:
                requestErrorService.dealRequestError(taskInfoDto, RequestExceptionEnum.UNDEFINED_SERVICE_NAME.name);
                break;
        }
    }

    //---------------------级联类型-------------------------

    /**
     * 处理级联请求
     */
    private void dealCascadeRequest(TaskInfoDto taskInfoDto) {
        // 级联操作请求单独处理
        R resp = dispatchCascadeRequest(taskInfoDto);
        // dto转父类po对象
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        // 更新发送状态为已完成
        taskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        // 新增数据库流水
        taskInfoService.saveOrUpdate(taskInfo);
        if (resp.getCode() == 0) {
            dealRequestCascade(taskInfoDto);
            taskInfoDto.setData(JSONObject.toJSONString(resp));
        } else {
            // 抛出异常由异常处理器统一处理
            requestErrorService.dealRequestError(taskInfoDto, resp);
            return;
        }
        // 发送消息, 清空msgId缓存
        sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
        dealRequestCascade(taskInfoDto);
    }

    /**
     * 级联操作
     *
     * @param taskInfoDto
     * @return
     */
    private R dispatchCascadeRequest(TaskInfoDto taskInfoDto) {
        String serviceName = taskInfoDto.getServiceName();
        if (ServiceNameEnum.CASCADE_UNBIND.name.equals(serviceName)) {
            switch (CascadeTypeEnum.getByName(taskInfoDto.getType())) {
                case APPLY:
                    if (CascadeDictEnum.CASCADE.name.equals(Constants.CASCADE_TYPE)) {
                        return cascadeUnbindService.accept(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                    } else {
                        return cascadeUnbindService.apply(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                    }
                case ACCEPT:
                    return cascadeUnbindService.accept(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                case REJECT:
                    return cascadeUnbindService.reject(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                case REVOKE:
                    return cascadeUnbindService.revoke(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                default:
                    return null;
            }
        } else if (ServiceNameEnum.CASCADE_APPLY.name.equals(serviceName)) {
            switch (CascadeTypeEnum.getByName(taskInfoDto.getType())) {
                case ACCEPT:
                    return cascadeApplyService.accept(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                case REJECT:
                    return cascadeApplyService.reject(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                case REVOKE:
                    return cascadeApplyService.revoke(taskInfoDto.getProjectUUID(), buildOpenApiHeader(taskInfoDto));
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * 级联处理
     * 级联 拒绝操作 删除projectRelation 关系表
     * 解绑 同意操作 删除projectRelation 关系表
     *
     * @param taskInfoDto
     */
    private void dealRequestCascade(TaskInfoDto taskInfoDto) {
        if (OpenPushSubscribeCallbackTypeEnum.CASCADE.name.equals(taskInfoDto.getServiceType())) {
            // 级联 撤销操作
            if (ServiceNameEnum.CASCADE_APPLY.name.equals(taskInfoDto.getServiceName())
                    && CascadeTypeEnum.REVOKE.name.equals(taskInfoDto.getType())) {
                Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
                // 删除 OpenApi 订阅回调地址
                if (removeRes) {
                scribeCallbackService.deleteCascadeScribeCallback(taskInfoDto.getProjectUUID());
                }
            }
            // 解绑 申请操作
            else if (ServiceNameEnum.CASCADE_UNBIND.name.equals(taskInfoDto.getServiceName())
                    && CascadeTypeEnum.APPLY.name.equals(taskInfoDto.getType())) {
                Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
                // 删除 OpenApi 订阅回调地址
                if (removeRes) {
                    scribeCallbackService.deleteScribeCallback(taskInfoDto.getProjectUUID());
                }
            }
        }
    }

    //---------------------通用增删改业务类型-------------------------

    /**
     * 处理通用增删改业务请求
     */
    private void dealCommonRequest(TaskInfoDto taskInfoDto) {
        // 转换第三方Uuid为自己的Uuid
        if (!changeUuidToOwner(taskInfoDto)) {
            return;
        }
        // 校验Md5
        if (!checkMd5(taskInfoDto)) {
            return;
        }
        // 分发请求
        OpenRespVo resp = dispatchCommonRequest(taskInfoDto);
        if (resp.getR() != null) {
            if (resp.getR().getCode() == 0) {
                if (DataSourceEnum.MASTER.equals(Constants.SOURCE)) {
                    // 转发操作消息
                    edgeSyncTransferService.sendOperateToEdgeSync(taskInfoDto);
                }
            } else {
                // 抛出异常由异常处理器统一处理
                requestErrorService.dealRequestError(taskInfoDto, resp.getR());
                return;
            }
        } else {
            return;
        }
        // 更新本地数据库数据，新增流水
        updateDatabaseInfo(taskInfoDto, resp);

        taskInfoDto.setThirdUuid(taskInfoDto.getThirdUuid());
        // 发送消息，清空msgId缓存
        sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
    }

    private boolean checkMd5(TaskInfoDto taskInfoDto) {
        // 新增操作跳过检验md5操作
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            return true;
        }
        // 校验Md5值
        if (!uuidRelationService.checkOldMd5(taskInfoDto.getOldMd5())) {
            // 判断是否为主
            ProjectRelation byProjectUUID = projectRelationService.getByProjectUUID(taskInfoDto.getProjectUUID());
            if (SyncTypeEnum.MAIN.code.equals(byProjectUUID.getSyncType())) {
                // 主端不处理业务返回成功跳过请求
                log.info("oldMd5校验失败,主库跳过此操作返回结果");
                // 发送mqtt Response 成功消息
                mqttConnector.publishResponseMessage(taskInfoDto);
                return false;
            } else if (SyncTypeEnum.SECONDARY.code.equals(byProjectUUID.getSyncType())) {
                // 从端返回成功继续处理业务
                log.info("oldMd5校验失败，从库继续业务");
                return true;
            }
            log.info("oldMd5校验失败，异常请检查数据库关系表");
            return false;
        }
        return true;
    }

    /**
     * 分发器实现
     * 判断请求
     * 通过策略工厂调用对应业务访问OpenApi
     * 返回OpenApi返回值
     */
    private OpenRespVo dispatchCommonRequest(TaskInfoDto taskInfoDto) {
        BaseStrategy baseStrategy = BaseStrategyFactory.getStrategy(taskInfoDto.getServiceName());
        try {
            switch (OperateTypeEnum.getByName(taskInfoDto.getType())) {
                case ADD:
                    return baseStrategy.doAdd(buildOpenApiModel(taskInfoDto));
                case UPDATE:
                    return baseStrategy.doUpdate(buildOpenApiModel(taskInfoDto), taskInfoDto.getUuid());
                case DELETE:
                    return baseStrategy.doDelete(Constants.appId, taskInfoDto.getProjectUUID(), taskInfoDto.getTenantId(), taskInfoDto.getUuid());
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestErrorService.dealRequestError(taskInfoDto, e.getMessage());
            return null;
        }
    }

    /**
     * 分发器实现
     * 判断请求
     * 通过策略工厂调用对应业务访问OpenApi
     * 返回OpenApi返回值
     */
    private OpenRespVo dispatchOtherRequest(TaskInfoDto taskInfoDto) {
        log.info("dispatchOtherRequest{}",taskInfoDto);
        try {
            switch (OperateTypeEnum.getByName(taskInfoDto.getType())) {
                case ADD:
                    return executeSqlService.doAdd(buildOpenApiModel(taskInfoDto));
                case UPDATE:
                    return executeSqlService.doUpdate(buildOpenApiModel(taskInfoDto));
                case DELETE:
                    return executeSqlService.doDelete(buildOpenApiModel(taskInfoDto));
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestErrorService.dealRequestError(taskInfoDto, e.getMessage());
            return null;
        }
    }
    /**
     * 转换第三方uuid
     *
     * @param taskInfoDto
     * @return
     */
    private Boolean changeUuidToOwner(TaskInfoDto taskInfoDto) {
        // 新增操作将传入的Uuid转为thirdUuid并置空原Uuid,返回结果
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            taskInfoDto.setThirdUuid(taskInfoDto.getUuid());
            taskInfoDto.setUuid(null);
        } else {
            // 根据第三方Uuid获取Uuid
            UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
            if (byThirdUuid == null) {
                log.info("接收端收到Mqtt请求找不到uuid关系,参数为{}", JSONObject.toJSONString(taskInfoDto));
                requestErrorService.dealRequestError(taskInfoDto, CloudxOpenErrorEnum.EMPTY_RESULT);
                return false;
            }
            taskInfoDto.setUuid(byThirdUuid.getUuid());
        }
        return true;
    }

    /**
     * 更新数据库关系表，新增流水
     *
     * @param taskInfoDto
     * @param resp
     * @return
     */
    private void updateDatabaseInfo(TaskInfoDto taskInfoDto, OpenRespVo resp) {
        // 新增操作
        if (taskInfoDto.getType().equals(OperateTypeEnum.ADD.name)) {
            if (StringUtil.isNotEmpty(resp.getId())) {
                taskInfoDto.setUuid(resp.getId());
                taskInfoDto.setOldMd5(taskInfoDto.getNewMd5());
                // 保存uuid关系
                uuidRelationService.saveUuidRelation(taskInfoDto);
            }
        }
        // 修改操作
        if (taskInfoDto.getType().equals(OperateTypeEnum.UPDATE.name)) {
            // 更新uuid关系
            uuidRelationService.updateOldMd5ByUuid(taskInfoDto);
        }
        // 删除操作 删除数据库关系表对应关系记录
        if (taskInfoDto.getType().equals(OperateTypeEnum.DELETE.name)) {
            // 删除uuid关系
            Boolean deleteRes = uuidRelationService.deleteByUuid(taskInfoDto);
            if (!deleteRes) {
                log.info("删除Uuid关系失败，ServiceName为{},操作类型为{}，ProjectUuid为{}，uuid为{}",
                        taskInfoDto.getServiceName(), taskInfoDto.getType(), taskInfoDto.getProjectUUID(), taskInfoDto.getUuid());
            }
        }
        // dto转父类po对象
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        // 更新发送状态为已完成
        taskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        // 新增或者更新数据库数据
        taskInfoService.saveOrUpdate(taskInfo);
    }

    //---------------------命令业务-------------------------

    /**
     * 处理命令请求
     */
    private void dealCommandRequest(TaskInfoDto taskInfoDto) {
        // 处理请求
        OpenRespVo resp = dispatchCommandRequest(taskInfoDto);
        if (resp == null) {
            return;
        }
        // dto转父类po对象
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        // 更新发送状态为已完成
        taskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        // 新增数据库流水
        taskInfoService.saveOrUpdate(taskInfo);
        if (resp.getR() != null) {
            if (resp.getR().getCode() == 0) {
                taskInfoDto.setData(JSONObject.toJSONString(resp.getR()));
            } else {
                requestErrorService.dealRequestError(taskInfoDto, resp.getR());
                return;
            }
        }
        // 发送消息, 清空msgId缓存
        sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
    }

    private void deaOtherRequest(TaskInfoDto taskInfoDto) {
        log.info("处理其他操作{}",taskInfoDto);
        // 分发请求
        OpenRespVo resp = dispatchOtherRequest(taskInfoDto);
        if (resp.getR() != null) {
            if (resp.getR().getCode() == 0) {
                if (DataSourceEnum.MASTER.equals(Constants.SOURCE)) {
                    // 转发操作消息
                    edgeSyncTransferService.sendOtherToEdgeSync(taskInfoDto);
                }
            } else {
                // 抛出异常由异常处理器统一处理
                requestErrorService.dealRequestError(taskInfoDto, resp.getR());
                return;
            }
        } else {
            return;
        }
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        // 更新发送状态为已完成
        taskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        // 新增或者更新数据库数据
        taskInfoService.saveOrUpdate(taskInfo);
        // 更新本地数据库数据，新增流水
//        updateDatabaseInfo(taskInfoDto, resp);

//        taskInfoDto.setThirdUuid(taskInfoDto.getThirdUuid());
        // 发送消息，清空msgId缓存
        sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
    }
    /**
     * 处理命令请求
     */
    private OpenRespVo dispatchCommandRequest(TaskInfoDto taskInfoDto) {
        CommandStrategy commandStrategy = CommandStrategyFactory.getStrategy(taskInfoDto.getType());
        try {
            return commandStrategy.doHandler(taskInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            requestErrorService.dealRequestError(taskInfoDto, e.getMessage());
            return null;
        }
    }


    //---------------------公共业务-------------------------

    /**
     * 校验msgId
     *
     * @param taskInfoDto
     * @return
     */
    private boolean checkMsgId(TaskInfoDto taskInfoDto) {
        // 校验msgId
        if (RedisUtil.hasKey(taskInfoDto.getMsgId())) {
            log.info("重复的msgId，msgId为{}", taskInfoDto.getMsgId());
            return false;
        } else {
            // 保存 key为 msgId的消息到redis中
            RedisUtil.set(taskInfoDto.getMsgId(), "", 30);
        }
        return true;
    }

    /**
     * 获取调用openApi接口参数
     *
     * @param requestObj
     * @return
     */
    private OpenApiModel buildOpenApiModel(TaskInfoDto requestObj) {
        OpenApiModel openApiModel = new OpenApiModel();
        if (requestObj.getData() != null) {
            openApiModel.setData(JSONObject.parseObject(requestObj.getData()));
        }
        openApiModel.setHeader(buildOpenApiHeader(requestObj));
        return openApiModel;
    }

    /**
     * 获取调用openApi接口参数
     *
     * @param requestObj
     * @return
     */
    private OpenApiHeader buildOpenApiHeader(TaskInfoDto requestObj) {
        OpenApiHeader openApiHeader = new OpenApiHeader();
        openApiHeader.setTenantId(requestObj.getTenantId());
        openApiHeader.setAppId(Constants.appId);
        openApiHeader.setProjectUUID(requestObj.getProjectUUID());
        return openApiHeader;
    }

    /**
     * 补充公共参数
     * 返回消息
     * 删除缓存
     *
     * @param taskInfoDto
     * @param msgId
     */
    private void sendResponseMessage(TaskInfoDto taskInfoDto, String msgId) {
        // 构建返回参数
        // 发送mqtt Response 消息
        mqttConnector.publishResponseMessage(taskInfoDto);
        // 延时删除redis中的msgId缓存
        if (RedisUtil.hasKey(msgId)) {
            RedisUtil.expire(msgId, 60);
        }
        log.info("发送响应完成");
    }

    /**
     * 修改第三方projectUUID为自己的projectUUID
     *
     * @param taskInfoDto
     */
    private void changeProjectUUIDToOwner(TaskInfoDto taskInfoDto) {
        // 根据第三方ProjectUuid获取ProjectUuid
        ProjectRelation byProjectCode = projectRelationService.getByProjectCode(taskInfoDto.getProjectUUID());
        taskInfoDto.setProjectUUID(byProjectCode.getProjectUUID());
    }
}
