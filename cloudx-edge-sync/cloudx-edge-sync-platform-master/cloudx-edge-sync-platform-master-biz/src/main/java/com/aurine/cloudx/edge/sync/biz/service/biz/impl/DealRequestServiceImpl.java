package com.aurine.cloudx.edge.sync.biz.service.biz.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.*;
import com.aurine.cloudx.edge.sync.biz.service.biz.DealRequestService;
import com.aurine.cloudx.edge.sync.biz.service.handler.Base64ToImageChain;
import com.aurine.cloudx.edge.sync.biz.service.remote.*;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.biz.util.ImageHandleUtil;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategy;
import com.aurine.cloudx.edge.sync.common.config.CommandStrategyFactory;
import com.aurine.cloudx.edge.sync.common.constant.TableIdConstant;
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
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;
import feign.RetryableException;
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
    private RequestErrorService requestErrorService;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    @Resource
    private ExecuteSqlService executeSqlService;
    @Resource
    private EdgeCascadeProcessMasterStrategy edgeCascadeProcessMasterStrategy;
    @Resource
    private EdgeSyncLogStrategy edgeSyncLogStrategy;
    @Resource
    private ImageHandleUtil imageHandleUtil;
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
        log.info("[{}] MQTT RECEIVE REQUEST = {}", taskInfoDto.getMsgId(), JSONObject.toJSONString(taskInfoDto));
        // 转换第三方projectUUID为自己的uuid
        Boolean changeProjectUuidResult = changeProjectUUIDToOwner(taskInfoDto);
        if (!changeProjectUuidResult) {
            return;
        }
        // 判断是否已经处理此条流水，处理了则直接返回处理成功
        TaskInfo taskInfoByTaskId = taskInfoService.getTaskInfoByTaskId(taskInfoDto.getTaskId());
        if (ObjectUtil.isNotEmpty(taskInfoByTaskId)) {
            log.info("[{}] 数据在流水表中已存在, taskId = {}", taskInfoDto.getMsgId(), taskInfoDto.getTaskId());
            TaskInfoDto respTaskInfoDto = EntityChangeUtil.taskInfoToTaskInfoDto(taskInfoByTaskId);
            respTaskInfoDto.setOldMd5(respTaskInfoDto.getNewMd5());
            sendSuccessResponse(respTaskInfoDto);
            return;
        }
        //处理图片下载
        try {
            imageHandleUtil.downloadImages(taskInfoDto.getData(), taskInfoDto.getServiceName());
        } catch (Exception e) {
            log.debug("[{}] 图片处理出现异常", taskInfoDto.getMsgId());
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
            case COMMAND:
                dealCommandRequest(taskInfoDto);
                break;
            case OTHER:
                deaOtherRequest(taskInfoDto);
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
        ProjectRelation byProjectUUID = projectRelationService.getByProjectUUID(taskInfoDto.getProjectUUID());
        if (ServiceNameEnum.CASCADE_UNBIND.name.equals(serviceName)) {
            switch (CascadeTypeEnum.getByName(taskInfoDto.getType())) {
                case APPLY:
                    if (CascadeDictEnum.CASCADE.name.equals(Constants.CASCADE_TYPE)) {
                        return cascadeUnbindService.accept(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                    } else {
                        return cascadeUnbindService.apply(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                    }
                case ACCEPT:
                    return cascadeUnbindService.accept(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                case REJECT:
                    return cascadeUnbindService.reject(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                case REVOKE:
                    return cascadeUnbindService.revoke(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                default:
                    return null;
            }
        } else if (ServiceNameEnum.CASCADE_APPLY.name.equals(serviceName)) {
            switch (CascadeTypeEnum.getByName(taskInfoDto.getType())) {
                case ACCEPT:
                    return cascadeApplyService.accept(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                case REJECT:
                    return cascadeApplyService.reject(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                case REVOKE:
                    return cascadeApplyService.revoke(byProjectUUID.getProjectCode(), buildOpenApiHeader(taskInfoDto));
                default:
                    return null;
            }
        }
        return null;
    }

    private OpenApiModel getOpenApiModel(TaskInfoDto taskInfoDto) {
        OpenApiModel openApiModel = new OpenApiModel();
        OpenApiHeader openApiHeader = new OpenApiHeader();
        openApiHeader.setAppId(Constants.appId);
        openApiHeader.setTenantId(1);
        openApiHeader.setProjectUUID(taskInfoDto.getProjectUUID());
        openApiModel.setHeader(openApiHeader);
        Object o = JSONObject.parse(taskInfoDto.getData());
        openApiModel.setData(o);
        return openApiModel;
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
            // 入云 同意操作
            if (ServiceNameEnum.CASCADE_APPLY.name.equals(taskInfoDto.getServiceName())
                    && CascadeTypeEnum.ACCEPT.name.equals(taskInfoDto.getType())) {
                scribeCallbackService.addScribeCallbackWithoutCascade(taskInfoDto.getProjectUUID());
            }
            // 入云 拒绝操作
            if (ServiceNameEnum.CASCADE_APPLY.name.equals(taskInfoDto.getServiceName())
                    && CascadeTypeEnum.REJECT.name.equals(taskInfoDto.getType())) {
                Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
                if (removeRes) {
                    // 删除 OpenApi 级联订阅回调地址
                    scribeCallbackService.deleteCascadeScribeCallback(taskInfoDto.getProjectUUID());
                }
            }
            // 解绑 同意操作
            else if (ServiceNameEnum.CASCADE_UNBIND.name.equals(taskInfoDto.getServiceName())
                    && CascadeTypeEnum.ACCEPT.name.equals(taskInfoDto.getType())) {
                Boolean removeRes = projectRelationService.removeProjectRelation(taskInfoDto.getProjectUUID());
                if (removeRes) {
                    // 删除 OpenApi 订阅回调地址
                    scribeCallbackService.deleteScribeCallback(taskInfoDto.getProjectUUID());
                    // 清空数据库
                    cleanDataBase(taskInfoDto.getProjectUUID());
                    // 清空redis数据缓存
                    cleanRedisCaches(taskInfoDto.getProjectUUID());
                }
            }
        }
    }

    /**
     * 删除数据库数据
     */
    private void cleanDataBase(String projectUUID) {
        // 删除uuid关系表关系
        uuidRelationService.remove(Wrappers.lambdaQuery(UuidRelation.class).eq(UuidRelation::getProjectUUID,projectUUID));
        // 删除taskInfo数据表关系
        taskInfoService.remove(Wrappers.lambdaQuery(TaskInfo.class).eq(TaskInfo::getProjectUUID,projectUUID));
    }

    /**
     * 删除redis缓存
     */
    private void cleanRedisCaches(String projectUUID) {
        // 删除正在发送的事件消息缓存
        String eventQueueKey = QueueUtil.getQueueKey(OpenPushSubscribeCallbackTypeEnum.EVENT.name, projectUUID);
        if (RedisUtil.hasKey(eventQueueKey)) {
            RedisUtil.del(eventQueueKey);
        }
        // 删除正在发送的非事件消息缓存
        String queueKey = QueueUtil.getQueueKey(null, projectUUID);
        if (RedisUtil.hasKey(queueKey)) {
            RedisUtil.del(queueKey);
        }
        // 删除项目订阅缓存
        String projectKey  = QueueUtil.getUuidMapKey(projectUUID);
        if (RedisUtil.hasKey(projectKey)) {
            RedisUtil.del(projectKey);
        }
        // 删除发送状态记录缓存
        String sendingTaskKey = QueueUtil.getSendingTaskKey(projectUUID);
        if (RedisUtil.hasKey(sendingTaskKey)) {
            RedisUtil.del(sendingTaskKey);
        }
        // 删除重试次数记录缓存
        String retireCountKey = QueueUtil.getRetireCountKey(projectUUID);
        if (RedisUtil.hasKey(retireCountKey)) {
            RedisUtil.del(retireCountKey);
        }
    }

    //---------------------通用增删改业务类型-------------------------

    /**
     * 处理通用增删改业务请求
     */
    private void dealCommonRequest(TaskInfoDto taskInfoDto) {
        // 新增操作且uuid关系已经存在忽略此消息
        if (OperateTypeEnum.ADD.name.equals(taskInfoDto.getType())) {
            UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
            if (ObjectUtil.isNotEmpty(byThirdUuid)) {
                uuidRelationService.removeById(byThirdUuid);
                //return;
            }
        }

        // 转换第三方Uuid为自己的Uuid
        if (!changeUuidToOwner(taskInfoDto)) {
            log.info("[{}] MQTT REQUEST 收到Mqtt请求找不到uuid关系,参数为{}", taskInfoDto.getMsgId(), JSONObject.toJSONString(taskInfoDto));
            requestErrorService.dealRequestError(taskInfoDto, "找不到主键关系异常");
            return;
        }
        // 校验Md5
        if (!checkMd5(taskInfoDto)) {
            // 发送mqtt Response 成功消息
            sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
            return;
        }
        // 分发请求
        OpenRespVo resp = dispatchCommonRequest(taskInfoDto);
        if (resp == null) {
            return;
        }
        if (resp.getR() != null) {
            if (resp.getR().getCode() == 0) {
                // 成功请求后将错误信息置空，保证taskId过滤幂等
                taskInfoDto.setErrMsg(null);
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
                return false;
            } else if (SyncTypeEnum.SECONDARY.code.equals(byProjectUUID.getSyncType())) {
                // 从端返回成功继续处理业务
                log.info("oldMd5校验失败，从库继续业务");
                return true;
            }
            log.error("oldMd5校验失败，异常请检查数据库关系表");
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
        log.info("baseStrategy:{}", baseStrategy);
        try {
            switch (OperateTypeEnum.getByName(taskInfoDto.getType())) {
                case ADD:
                    return baseStrategy.doAdd(buildOpenApiModel(taskInfoDto));
                case UPDATE:
                    return baseStrategy.doUpdate(buildOpenApiModel(taskInfoDto), taskInfoDto.getUuid());
                case DELETE:
                    return baseStrategy.doDelete(Constants.appId, taskInfoDto.getProjectUUID(), taskInfoDto.getTenantId(), taskInfoDto.getUuid());
                default:
                    requestErrorService.dealRequestError(taskInfoDto, RequestExceptionEnum.UNDEFINED_TYPE.name);
                    return new OpenRespVo();
            }
        } catch (RetryableException c) {
            // open服务连接不上不返回数据等待服务恢复后继续业务
            c.printStackTrace();
            return null;
        } catch (RuntimeException r) {
            // open服务找不到不返回数据等待服务恢复后继续业务
            boolean contains = r.getMessage().contains("com.netflix.client.ClientException");
            if (contains) {
                r.printStackTrace();
                return null;
            } else {
                throw r;
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
        log.info("[{}] dispatchOtherRequest{}", taskInfoDto.getMsgId(), taskInfoDto);
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
            UuidRelation uuidRelation = uuidRelationService.getByThirdUuid(taskInfoDto);
            if (uuidRelation == null) {
                if (TableIdConstant.TABLE_ID_LIST.contains(taskInfoDto.getServiceName())) {
                    return false;
                }
                // uuid为空并且主键不为seq 重新建立uuid的关系
                uuidRelation = new UuidRelation();
                uuidRelation.setUuid(taskInfoDto.getUuid());
                uuidRelation.setThirdUuid(taskInfoDto.getUuid());
                uuidRelation.setTenantId(taskInfoDto.getTenantId());
                uuidRelation.setProjectUUID(taskInfoDto.getProjectUUID());
                uuidRelation.setServiceName(taskInfoDto.getServiceName());
                uuidRelation.setOldMd5(taskInfoDto.getOldMd5());
                uuidRelationService.save(uuidRelation);
            }
            taskInfoDto.setUuid(uuidRelation.getUuid());
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
                // 保存uuid关系,先查询不存在数据的才插入
                if (ObjectUtil.isEmpty(uuidRelationService.getByUuid(taskInfoDto))) {
                    uuidRelationService.saveUuidRelation(taskInfoDto);
                    log.debug("[{}] 新增操作保存uuidRelation uuid成功", taskInfoDto.getMsgId());
                }
            }
        }
        // 修改操作
        if (taskInfoDto.getType().equals(OperateTypeEnum.UPDATE.name)) {
            // 更新uuid关系
            uuidRelationService.updateOldMd5ByUuid(taskInfoDto);
            log.debug("[{}] 修改操作更新uuidRelation uuid成功", taskInfoDto.getMsgId());
        }
        // 删除操作 删除数据库关系表对应关系记录
        if (taskInfoDto.getType().equals(OperateTypeEnum.DELETE.name)) {
            // 删除uuid关系
            Boolean deleteRes = uuidRelationService.deleteByUuid(taskInfoDto);
            if (!deleteRes) {
                log.info("删除Uuid关系失败，ServiceName为{}, 操作类型为{}，ProjectUuid为{}，uuid为{}",
                        taskInfoDto.getServiceName(), taskInfoDto.getType(), taskInfoDto.getProjectUUID(), taskInfoDto.getUuid());
            }
        }
        // dto转父类po对象
        TaskInfo taskInfo = EntityChangeUtil.taskInfoDtoToTaskInfo(taskInfoDto);
        // 更新发送状态为已完成
        taskInfo.setState(MessageSendStateEnum.COMPLETE.code);
        // 新增或者更新数据库数据
        taskInfoService.saveOrUpdate(taskInfo);
        log.debug("[{}] taskInfo入库成功，入库业务处理完成，taskId为{}", taskInfoDto.getMsgId(), taskInfoDto.getTaskId());
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
                // 成功请求后将错误信息置空，保证taskId过滤幂等
                taskInfoDto.setErrMsg(null);
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
        log.info("[{}] 处理其他操作{}", taskInfoDto.getMsgId(), taskInfoDto);
        // 分发请求
        OpenRespVo resp = dispatchOtherRequest(taskInfoDto);
        if (resp.getR() != null) {
            if (resp.getR().getCode() == 0) {
                // 成功请求后将错误信息置空，保证taskId过滤幂等
                taskInfoDto.setErrMsg(null);
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
            RedisUtil.set(taskInfoDto.getMsgId(), "", 10);
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
        // 置空data里面的值,减少mqtt通讯请求大小
        taskInfoDto.setData(null);
        // 发送mqtt Response 消息
        mqttConnector.publishResponseMessage(taskInfoDto);
        // 延时删除redis中的msgId缓存
        if (RedisUtil.hasKey(msgId)) {
            RedisUtil.expire(msgId, 60);
        }
        log.info("[{}]发送响应完成", taskInfoDto.getMsgId());
    }

    /**
     * 修改第三方projectUUID为自己的projectUUID
     *
     * @param taskInfoDto
     */
    private Boolean changeProjectUUIDToOwner(TaskInfoDto taskInfoDto) {
        // 根据第三方ProjectUuid获取ProjectUuid
        ProjectRelation byProjectCode = projectRelationService.getByProjectCode(taskInfoDto.getProjectUUID());
        if (ObjectUtil.isEmpty(byProjectCode)) {
            log.debug("MQTT REQUEST 找不到ProjectUuid对应关系");
            return false;
        }
        taskInfoDto.setProjectUUID(byProjectCode.getProjectUUID());
        return true;
    }

    /**
     * 非正常业务返回成功请求结果
     * @param taskInfoDto
     */
    private void sendSuccessResponse(TaskInfoDto taskInfoDto) {
        taskInfoDto.setOldMd5(taskInfoDto.getNewMd5());
        // 事件以及操作类型对uuid进行处理
        if (OpenPushSubscribeCallbackTypeEnum.OPERATE.name.equals(taskInfoDto.getServiceType()) ||
                OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(taskInfoDto.getServiceType())) {
            if (OperateTypeEnum.ADD.name.equals(taskInfoDto.getType())) {
                UuidRelation byThirdUuid = uuidRelationService.getByThirdUuid(taskInfoDto);
                if (ObjectUtil.isNotEmpty(byThirdUuid) && StringUtil.isNotEmpty(byThirdUuid.getUuid())) {
                    taskInfoDto.setThirdUuid(byThirdUuid.getUuid());
                }
            } else if (OperateTypeEnum.UPDATE.name.equals(taskInfoDto.getType())) {
                changeUuidToOwner(taskInfoDto);
            } if (OperateTypeEnum.DELETE.name.equals(taskInfoDto.getType())) {
                taskInfoDto.setUuid(null);
            }
        }
        // 发送消息，清空msgId缓存
        sendResponseMessage(taskInfoDto, taskInfoDto.getMsgId());
    }
}
