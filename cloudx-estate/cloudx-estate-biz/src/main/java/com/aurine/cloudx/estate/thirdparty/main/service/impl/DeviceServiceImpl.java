package com.aurine.cloudx.estate.thirdparty.main.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.config.LiftCardConfig;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.DeviceStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.subscribe.factory.EventFactory;
import com.aurine.cloudx.estate.subscribe.service.EventSubscribeService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceStatusMessage;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseSearchRecordDTO;
import com.aurine.cloudx.estate.thirdparty.main.service.DeviceService;
import com.aurine.cloudx.estate.thirdparty.transport.mq.kafka.KafkaProducer;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.WebSocketNotifyUtil;
import com.aurine.cloudx.estate.vo.DeviceParamJsonVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.aurine.cloudx.wjy.entity.Project;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.aurine.cloudx.estate.constant.decidedTypeConstant.DEVICE_TYPE;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07
 * @Copyright:
 */
@Service
@Slf4j
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    private ProjectDeviceCallEventService projectDeviceCallEventService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectDeviceModifyLogService projectDeviceModifyLogService;
    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;
    @Resource
    private ProjectAlarmHandleService projectAlarmHandleService;
    @Resource
    ProjectConfigService projectConfigService;
    @Resource
    private ProjectWebSocketService projectWebSocketService;
    @Resource
    private EventFactory eventFactory;
    @Resource
    private LiftCardConfig liftCardConfig;
    @Resource
    private ProjectCardHisService projectCardHisService;

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();


    /**
     * 设备状态变更
     *
     * @param eventDeviceNoticeDTO
     */
    @Override
    @Async
    public void deviceStatusUpdate(EventDeviceNoticeDTO eventDeviceNoticeDTO) {
        String thirdPartyCode = eventDeviceNoticeDTO.getThirdPartyCode();
        String deviceSn = eventDeviceNoticeDTO.getDeviceSn();
        String status = eventDeviceNoticeDTO.getStatus();
        ProjectDeviceInfoProxyVo deviceVo = getDeviceInfo(thirdPartyCode, deviceSn);
        ProjectDeviceInfo deviceInfo = null;

        if (deviceVo == null) {
            log.error("[设备统一接口] SN:{}  或 第三方ID:{} 在系统中不存在", deviceSn, thirdPartyCode);
            return;
        } else {
            ProjectContextHolder.setProjectId(deviceVo.getProjectId());
            TenantContextHolder.setTenantId(1);
            deviceInfo = projectDeviceInfoService.getById(deviceVo.getDeviceId());
            log.info("[设备统一接口] 获取到设备 {} 状态变更信息：设备当前状态为：{}", deviceInfo.getDeviceName(), deviceInfo.getStatus());
        }
        /**
         * 功能更替，设备只有在首次在线或离线时（激活时），刷新设备授权
         * 该方法用于替代新增设备的刷新权限操作
         * （业务逻辑变更为，设备首次上线后才能够被操作）
         * @Author:王伟
         * @since 2021-05-28 9:05
         */
        //获取原有设备的状态，如果是未激活则进行触发
        boolean doRefreshAuth = StringUtils.equals(deviceInfo.getStatus(), DeviceStatusEnum.DEACTIVE.code);

        //更新状态
        ProjectContextHolder.setProjectId(deviceVo.getProjectId());

        //比对上报的状态和库的状态,状态一致跳过此次设备状态更新
        boolean flag = deviceInfo.getStatus().equals(status);
        if(!flag){
            if (StringUtils.isEmpty(thirdPartyCode) && StringUtils.isNotEmpty(deviceSn)) {
                projectDeviceInfoService.updateStatusByDeviceSn(deviceSn, status);
            } else {
                projectDeviceInfoService.updateStatusByThirdPartyCode(thirdPartyCode, status);
            }
            deviceInfo.setStatus(status);
            deviceInfo.setConfigured("1");
            /**********************开放平台openv2的数据实时推送*********************/
            KafkaProducer.sendMessage(TopicConstant.DEVICE_STATUS,deviceInfo);
            /*********************************************************************/
        }
        //更新权限
        if (doRefreshAuth) {
//            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
//            TenantContextHolder.setTenantId(1);
            if (deviceInfo.getDeviceType().equals(DeviceTypeConstants.GATE_DEVICE) || deviceInfo.getDeviceType().equals(DeviceTypeConstants.LADDER_WAY_DEVICE)) {
                projectPersonDeviceService.refreshAddDevice(deviceInfo);
                log.info("[设备统一接口] 设备已更新通行权限,并下发凭证");
            }
        } else {
            /**
             * 如果设备上线，则重新下发正在下载状态的所有凭证
             * 如该功能为中台2.0特有，请将该代码转移到2.0平台对应回调方法中。
             * @author: 王伟
             * @since 2020-09-10
             */
            if (status.equals(DeviceStatusEnum.ONLINE.code)) {
                projectRightDeviceService.resendByDeviceId(deviceInfo.getDeviceId(), PassRightCertDownloadStatusEnum.DOWNLOADING);
                projectRightDeviceService.resendByDeviceId(deviceInfo.getDeviceId(), PassRightCertDownloadStatusEnum.DELETING);
                //设置分层控制器加密密钥
                if(StrUtil.equals(deviceInfo.getDeviceType(), DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE)){
                    ObjectNode paramNode = new ObjectMapper().createObjectNode();
                    ObjectNode liftDeviceParamsObj = new ObjectMapper().createObjectNode();
                    liftDeviceParamsObj.put("secretKey", liftCardConfig.getProjectSecret());
                    paramNode.set("LiftDeviceParamsObj", liftDeviceParamsObj);
                    List<ProjectDeviceParamSetResultVo> resultVoList =
                            projectDeviceInfoService.setDeviceParam(paramNode, deviceInfo.getDeviceId());
                    boolean isFailed = projectDeviceInfoService.handleDeviceParamSetResult(resultVoList, false);
                }
            }
        }

        // 通知设备状态更新
        EventSubscribeService eventSubscribeService = eventFactory.GetProduct(DEVICE_TYPE);
        EventDeviceStatusMessage msg = new EventDeviceStatusMessage();
        msg.setDeviceId(deviceVo.getDeviceId());
        msg.setDeviceSn(eventDeviceNoticeDTO.getDeviceSn());
        msg.setStatus(eventDeviceNoticeDTO.getStatus());
        msg.setThirdPartyCode(eventDeviceNoticeDTO.getThirdPartyCode());
        msg.setDeviceName(deviceVo.getDeviceName());

        eventSubscribeService.send((JSONObject) JSONObject.toJSON(msg), deviceVo.getProjectId(), DEVICE_TYPE);
    }

    /**
     * 同步设备
     *
     * @param responseSearchRecordDTO
     */
    @Override
    public void syncDevice(ResponseSearchRecordDTO responseSearchRecordDTO) {

    }

    /**
     * 更新设备参数
     *
     * @param responseOperateDTO
     */
    @Override
    @Async
    public void updateDeviceParam(ResponseOperateDTO responseOperateDTO) {

        String thirdPartyCode = responseOperateDTO.getThirdPartyCode();
        String deviceSn = responseOperateDTO.getDeviceSn();
        if (responseOperateDTO.getDeviceInfo() != null) {
            ProjectDeviceInfoProxyVo deviceInfo = getDeviceInfo(thirdPartyCode, deviceSn);
            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
            TenantContextHolder.setTenantId(1);
            String deviceId = deviceInfo.getDeviceId();
            BeanUtils.copyProperties(responseOperateDTO.getDeviceInfo(), deviceInfo);
            projectDeviceModifyLogService.saveDeviceModifyLog(deviceId, deviceInfo);
            deviceInfo.setDeviceId(deviceId);
            projectDeviceInfoService.updateById(deviceInfo);
        }

        log.info("[冠林云平台] 已更新设备属性");

    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Async
    public void updateIotDeviceParam(ResponseOperateDTO responseOperateDTO) throws JsonProcessingException {
        String serviceId = responseOperateDTO.getDeviceParamJsonVo().getServiceId();
        String thirdPartyCode = responseOperateDTO.getThirdPartyCode();
        String deviceSn = responseOperateDTO.getDeviceSn();
        DeviceParamJsonVo deviceParamJsonVo = responseOperateDTO.getDeviceParamJsonVo();
        log.info("[冠林云平台] 更新Iot设备 serviceId:{} {}", serviceId, deviceParamJsonVo.getJson());
        ProjectDeviceInfoProxyVo deviceInfo = getDeviceInfo(thirdPartyCode, deviceSn);
//        DeviceParamEnum paramEnum = DeviceParamEnum.getEnum();
        if (StrUtil.isNotEmpty(deviceParamJsonVo.getKey())) {
            ProjectDeviceParamInfo deviceParamInfo = projectDeviceParamInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
                    .eq(ProjectDeviceParamInfo::getServiceId, serviceId)
                    .eq(ProjectDeviceParamInfo::getDeviceId, deviceInfo.getDeviceId()));
            ObjectNode objectNode;
            if (deviceParamInfo != null) {
                objectNode = objectMapper.readValue(deviceParamInfo.getDeviceParam(), ObjectNode.class);
            } else {
                deviceParamInfo = new ProjectDeviceParamInfo();
                deviceParamInfo.setDeviceId(deviceInfo.getDeviceId());
                deviceParamInfo.setServiceId(serviceId);
                deviceParamInfo.setProjectId(deviceInfo.getProjectId());
                deviceParamInfo.setTenant_id(deviceInfo.getTenantId());
                objectNode = objectMapper.createObjectNode();
            }
            objectNode.set(deviceParamJsonVo.getKey(), objectMapper.readTree(deviceParamJsonVo.getJson()));
            deviceParamInfo.setDeviceParam(objectNode.toString());
            projectDeviceParamInfoService.saveOrUpdate(deviceParamInfo);
        }

    }

    @Override
    @Async
    public void updateDeviceOtherParam(ResponseOperateDTO responseOperateDTO) throws JsonProcessingException {
        String thirdPartyCode = responseOperateDTO.getThirdPartyCode();
        String deviceSn = responseOperateDTO.getDeviceSn();
        DeviceParamJsonVo deviceParamJsonVo = responseOperateDTO.getDeviceParamJsonVo();
        log.info("[冠林云平台] 更新设备额外参数 serviceId:{} {}", deviceParamJsonVo.getKey(), deviceParamJsonVo.getJson());
        ProjectDeviceInfoProxyVo projectDeviceInfo = getDeviceInfo(thirdPartyCode, deviceSn);
        if (projectDeviceInfo != null) {

            ProjectContextHolder.setProjectId(projectDeviceInfo.getProjectId());
            TenantContextHolder.setTenantId(1);

            String deviceId = projectDeviceInfo.getDeviceId();
            List<ProjectDeviceParamInfo> deviceParamInfoList = projectDeviceParamInfoService.list(new QueryWrapper<ProjectDeviceParamInfo>()
                    .lambda().eq(ProjectDeviceParamInfo::getDeviceId, deviceId)
                    .eq(ProjectDeviceParamInfo::getServiceId, deviceParamJsonVo.getKey()));
            ObjectNode deviceParamNode;
            ProjectDeviceParamInfo projectDeviceParamInfo;
            // 这里进行判断如果是第一次添加这台设备的参数则创建参数json则创建否则在原json基础上进行更新
            if (CollUtil.isNotEmpty(deviceParamInfoList)) {
                projectDeviceParamInfo = deviceParamInfoList.get(0);
                try {
                    deviceParamNode = (ObjectNode) objectMapper.readTree(projectDeviceParamInfo.getDeviceParam());
                    if (deviceParamNode.isMissingNode()) {
                        deviceParamNode = objectMapper.createObjectNode();
                    }
                } catch (Exception e) {
                    // 这里出现异常的情况一般是json为null的情况
                    deviceParamNode = objectMapper.createObjectNode();
                    e.printStackTrace();
                }
            } else {
                projectDeviceParamInfo = new ProjectDeviceParamInfo();
                projectDeviceParamInfo.setDeviceId(deviceId);
                deviceParamNode = objectMapper.createObjectNode();
            }
        /*log.info("接收到的json-键值：{}", deviceParamJsonVo.getKey());
        log.info("接收到的json-数据：{}", deviceParamJsonVo.getJson());*/
            JsonNode jsonNode = objectMapper.readTree(deviceParamJsonVo.getJson());

            // 这里更新或者添加这个参数到json中
            deviceParamNode.set(deviceParamJsonVo.getKey(), jsonNode);
            projectDeviceParamInfo.setDeviceParam(deviceParamNode.toString());
            projectDeviceParamInfo.setServiceId(deviceParamJsonVo.getKey());
            projectDeviceParamInfo.setProjectId(projectDeviceInfo.getProjectId());
            // 这里如果之前没有这个设备的参数就添加否则就对参数进行修改
            projectDeviceParamInfoService.saveOrUpdate(projectDeviceParamInfo);
            log.info("[冠林云平台] 已更新设备额外属性");
        } else {
            log.error("[冠林云平台] 无法找到这台设备 thirdPartyCode:{}, deviceSn:{}", thirdPartyCode, deviceSn);
        }


    }

    /**
     * 更新设备框架
     *
     * @param responseOperateDTO
     */
    @Override
    @Async
    public void updateDeviceFrame(ResponseOperateDTO responseOperateDTO) {


        ProjectDeviceInfoProxyVo deviceInfo = getDeviceInfo(responseOperateDTO.getThirdPartyCode(), responseOperateDTO.getDeviceSn());
        projectBuildingInfoService.addThirdCode(deviceInfo, responseOperateDTO.getDeviceFrameNo());
        // 更新设备编号信息
        deviceInfo.setDeviceCode(responseOperateDTO.getDeviceFrameNo());
        projectDeviceInfoService.updateById(deviceInfo);

        log.info("[冠林云平台] 已根据设备更新所在房屋框架号");

    }

    /**
     * 更新设备上的通行凭证
     *
     * @param responseOperateDTO
     */
    @Override
    @Async
    public void updateCert(ResponseOperateDTO responseOperateDTO) {
        this.updateCertBusiness(responseOperateDTO.getRightDevice(), responseOperateDTO.getRespondStatus());
    }

    /**
     * 更新设备上的通行凭证状态
     *
     * @param responseOperateDTO
     */
    //@Async("myAsyncThreadPoolTaskExecutor")
    //@Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCerts(ResponseOperateDTO responseOperateDTO) {
        List<ProjectRightDevice> list = responseOperateDTO.getRightDeviceList();
        if (list!=null && list.size()>0) {
            ProjectRightDevice rightDevice = list.get(0);
            if (CertmediaTypeEnum.Card.code.equals(rightDevice.getCertMedia())) {
                log.info("更新门禁卡凭证状态：{}", responseOperateDTO);
                updateCardCert(responseOperateDTO.getRightDeviceList(), responseOperateDTO.getRespondStatus());
            }
            else {
                log.info("更新人脸凭证状态：{}", responseOperateDTO);
                for (ProjectRightDevice projectRightDevice : list) {
                    this.updateCertBusiness(projectRightDevice, responseOperateDTO.getRespondStatus());
                }
            }
        }
    }

    private void updateCardCert(List<ProjectRightDevice> list, String respondStatus){
        String dlStatus = respondStatus;
        boolean delFlag = false;
        List<String> ids = list.stream().map(rd -> rd.getUid()).collect(Collectors.toList());
        ProjectRightDevice rightDevice = list.get(0);
        if (PassRightCertDownloadStatusEnum.DELETING.code.equals(rightDevice.getDlStatus())) {
            delFlag = true;
        }
        else if (PassRightCertDownloadStatusEnum.DOWNLOADING.code.equals(rightDevice.getDlStatus())) {
            dlStatus = respondStatus;
        }
        else if (PassRightCertDownloadStatusEnum.FREEZING.code.equals(rightDevice.getDlStatus())) {
            dlStatus = PassRightCertDownloadStatusEnum.FREEZE.code;
        }
        //更新信息
        if (delFlag) {
            log.info("批量删除凭证，凭证ID：{}", ids);
            projectRightDeviceService.removeByIds(ids);
        } else {
            log.info("批量更新凭证，凭证状态：{},凭证ID：{}", dlStatus, ids);
            projectRightDeviceService.update(Wrappers.lambdaUpdate(ProjectRightDevice.class).set(ProjectRightDevice::getDlStatus, dlStatus).in(ProjectRightDevice::getUid, ids));
        }
        for (ProjectRightDevice projectRightDevice : list) {
            projectCardHisService.updateState(projectRightDevice, true);
        }
    }

    @Override
    @Async
    public void saveCallEvent(EventDeviceNoticeDTO noticeDTO) {
        if (noticeDTO.getDeviceCallEvent() != null) {
            projectDeviceCallEventService.save(noticeDTO.getDeviceCallEvent());
        }

    }

    @Override
    @Async
    public void updateDeviceStatus(ResponseOperateDTO operateDTO) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(operateDTO.getThirdPartyCode());
        if (deviceInfo != null) {
            ProjectDeviceParamInfo deviceParamInfo = projectDeviceParamInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceParamInfo>()
                    .eq(ProjectDeviceParamInfo::getDeviceId, deviceInfo.getDeviceId())
                    .eq(ProjectDeviceParamInfo::getServiceId, operateDTO.getDeviceParamJsonVo().getKey()));
            if (deviceParamInfo == null) {
                deviceParamInfo = new ProjectDeviceParamInfo();
            }
            deviceParamInfo.setDeviceParam(operateDTO.getDeviceParamJsonVo().getJson());
            deviceParamInfo.setDeviceId(deviceInfo.getDeviceId());
            deviceParamInfo.setServiceId(operateDTO.getDeviceParamJsonVo().getKey());
            deviceParamInfo.setProjectId(deviceInfo.getProjectId());
            projectDeviceParamInfoService.saveOrUpdate(deviceParamInfo);
            log.info("[冠林云平台] 已更新设备额外状态");
        } else {
            log.error("[冠林云平台] 无法找到这台设备 thirdPartyCode:{}", operateDTO.getThirdPartyCode());
        }
    }

    @Override
    @Async
    public void updateAlarmStatus(ResponseOperateDTO operateDTO) {
        Integer eventCode = operateDTO.getEventCode();
        String alarmStatus = operateDTO.getAlarmStatus();

        ProjectEntranceAlarmEvent projectEntranceAlarmEvent = null;

        // 为了防止eventCode（事件流水ID）重复（一般来说不会重复），所以使用list方法，取最新的一条数据
        List<ProjectEntranceAlarmEvent> projectEntranceAlarmEventList = projectEntranceAlarmEventService.list(new LambdaQueryWrapper<ProjectEntranceAlarmEvent>()
                .eq(ProjectEntranceAlarmEvent::getEventCode, eventCode)
                .orderByDesc(ProjectEntranceAlarmEvent::getEventTime));

        if (projectEntranceAlarmEventList != null && projectEntranceAlarmEventList.size() > 0) {
            projectEntranceAlarmEvent = projectEntranceAlarmEventList.get(0);
        }
        if (projectEntranceAlarmEvent == null) {
            log.error("[冠林云平台] 无法找到这台设备 thirdPartyCode:{}", operateDTO.getThirdPartyCode());
        } else {
            TenantContextHolder.setTenantId(1);
            projectEntranceAlarmEvent.setStatus(alarmStatus);
            projectEntranceAlarmEvent.setProjectId(ProjectContextHolder.getProjectId());
            projectEntranceAlarmEvent.setPersonName("管理员");

            ProjectAlarmHandle projectAlarmHandle = new ProjectAlarmHandle();
            BeanUtils.copyProperties(projectEntranceAlarmEvent, projectAlarmHandle);

            LocalDateTime now = LocalDateTime.now();
            projectAlarmHandle.setHandleBeginTime(now);
            projectAlarmHandle.setHandleEndTime(now);
            projectAlarmHandle.setDealDuration("0");
            projectAlarmHandle.setResult("已处理");

            if (projectEntranceAlarmEventService.checkOverrun(projectEntranceAlarmEvent.getEventTime(), now)) {
                projectAlarmHandle.setTimeLeave(projectEntranceAlarmEventService.getOverrun());
            } else {
                projectAlarmHandle.setTimeLeave(projectEntranceAlarmEventService.getNotOverrun());
            }
            projectAlarmHandleService.save(projectAlarmHandle);

            projectEntranceAlarmEventService.updateById(projectEntranceAlarmEvent);

            WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
            log.info("[冠林云平台] 获取到报警事件 {} 变更信息：当前报警状态为：{}", projectEntranceAlarmEvent.getEventId(), projectEntranceAlarmEvent.getStatus());
        }
    }

    private void updateCertBusiness(ProjectRightDevice projectRightDevice, String certStatus) {
        boolean isSuccess = PassRightCertDownloadStatusEnum.SUCCESS.code.equals(certStatus);
        boolean delFlag = false;

        //如果是正在下载，成功为下载完成，失败为下载失败
        if (projectRightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)) {
            projectRightDevice.setDlStatus(certStatus);
            // 这里除了成功状态其他都算失败
            if (isSuccess) {

//             下载成功，且来源为APP或微信，调用接口检查是否全部成功，并执行相关业务：全部成功且存在旧的来自同一个平台的非WEB源面部信息，则删除旧的面部信息
                if (projectRightDevice.getCertMedia().equals(CertmediaTypeEnum.Face.code)) {
                    //检查是否为来自微信端数据
                    projectFaceResourcesService.removeOldAppFace(projectRightDevice.getCertMediaId());
                }
            } else {
                if (projectRightDevice.getCertMedia().equals(CertmediaTypeEnum.Face.code)) {
                    //                projectFaceResourcesService.sendFailNotice(projectRightDevice.getCertMediaId());
                    projectFaceResourcesService.sendNotice(projectRightDevice.getCertMediaId(), false);
                }

            }

            //如果是正在删除，成功直接删除，失败为删除失败
        } else if (projectRightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DELETING.code)) {
            if (isSuccess) {
                delFlag = true;
            } else {
                log.info("[冠林云平台] 通行凭证删除失败 ,{}", projectRightDevice);
            }
        } else if (projectRightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.FREEZING.code)) {
            if (isSuccess) {
                log.info("[冠林云平台] 通行凭证停用成功 ,{}", projectRightDevice);
                projectRightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZE.code);
            } else {
                log.info("[冠林云平台] 通行凭证停用失败 ,变更状态为已停用 ,{}", projectRightDevice);
                //下载失败的数据，继续删除有时候会返回失败状态
                projectRightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZE.code);
//                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FAIL.code);
            }
        }

        //更新信息
        if (delFlag) {
            projectRightDeviceService.removeById(projectRightDevice.getUid());
        } else {
            projectRightDeviceService.updateById(projectRightDevice);
        }

        if(projectRightDevice.getCertMedia().equals(CertmediaTypeEnum.Card.code)){
            projectCardHisService.updateState(projectRightDevice,isSuccess);
        }
    }

    private ProjectDeviceInfoProxyVo getDeviceInfo(String thirdPartyCode, String deviceSn) {
        ProjectDeviceInfoProxyVo deviceInfo = null;

        if (StringUtils.isEmpty(thirdPartyCode) && StringUtils.isNotEmpty(deviceSn)) {
            deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
        } else {
            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        }

        return deviceInfo;
    }
}
