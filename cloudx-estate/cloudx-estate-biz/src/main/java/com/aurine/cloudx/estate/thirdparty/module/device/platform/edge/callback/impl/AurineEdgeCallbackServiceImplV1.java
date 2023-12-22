package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.annotation.ChainManager;
import com.aurine.cloudx.estate.constant.enums.CallTypeEnum;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FrameLevelEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.consumer.EventDeviceNoticeConsumer;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceGatePassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceNoticeConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventWarningErrorConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceNoticeDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventWarningErrorDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.AurineEdgeCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler.AurineEdgeObjectManageChain;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.event.OpendoorEventObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.event.RecordEventObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.DevMessageData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.ObjManagerData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.config.WR20ConfigProperties;
import com.aurine.cloudx.estate.thirdparty.remote.edge.ali.entity.constant.AliEdgePerimeterAlarmEnum;
import com.aurine.cloudx.estate.util.AurineQrCodeUtil;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 对接实现，用于拼接对接参数等逻辑
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-03
 * @Copyright:
 */
@Service
@Slf4j
public class AurineEdgeCallbackServiceImplV1 implements AurineEdgeCallbackService {

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();
    private static final int SECOND = 10;
    private static final int MILLISECOND = 13;
    private static final String COMMUNITY_ID = "communityId";

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private WR20ConfigProperties wr20ConfigProperties;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;


    @Resource
    private ProjectFrameInfoService projectFrameInfoService;

    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;

    @Resource
    private ProjectPerimeterAlarmEventService projectPerimeterAlarmEventService;
    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private  ImgConvertUtil imgConvertUtil;

    @Resource
    private AurineQrCodeUtil aurineQrCodeUtil;
    @Resource
    private ProjectPasscodeRegisterRecordService passcodeRegisterRecordService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectBlacklistAttrService projectBlacklistAttrService;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V1.code;
    }


    /**
     * 设备激活通知
     *
     * @param requestJson
     * @return
     */
    @Override
    @Async
    public void deviceActive(JSONObject requestJson) {
        boolean b = projectInfoService.checkCommunityIdIsOriginProject(requestJson.toJSONString(), COMMUNITY_ID);
        if (!b) {
            log.info("[冠林边缘网关] 级联设备-跳过设备激活通知处理");
            return;
        }
        EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
        eventDeviceNoticeDTO.setAction("");
        eventDeviceNoticeDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, requestJson);
    }

    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Override
    @Async
    public void deviceStatusUpdate(JSONObject requestJson) {

        boolean b = projectInfoService.checkCommunityIdIsOriginProject(requestJson.toJSONString(), COMMUNITY_ID);
        if (!b) {
            log.info("[冠林边缘网关] 级联设备-跳过设备状态变化通知");
            return;
        }

        String subscriptionId = requestJson.getString("subscriptionId");
        String deviceId = requestJson.getJSONObject("onNotifyData").getString("devId");
        String status = requestJson.getJSONObject("onNotifyData").getString("status");


        EventDeviceNoticeDTO noticeDTO = new EventDeviceNoticeDTO();
        noticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE);
        noticeDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
        noticeDTO.setStatus(AurineEdgeOnlineStatusEnum.getByCode(status).cloudCode);//转换为云系统字典值
        noticeDTO.setThirdPartyCode(deviceId);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, noticeDTO);
    }

    /**
     * 设备属性变化通知 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    @Async
    public void deviceDataUpdate(JSONObject requestJson) {
        boolean b = projectInfoService.checkCommunityIdIsOriginProject(requestJson.toJSONString(), COMMUNITY_ID);
        if (!b) {
            log.info("[冠林边缘网关] 级联设备-跳过设备属性变化通知");
            return;
        }
        String json = requestJson.toJSONString();
        try {
            // 获取到整个json的根节点
            JsonNode rootNode = objectMapper.readTree(json);
            // 这里获取到msgId（虽然好像没什么用）
            String msgId = rootNode.findPath("msgId").asText();
            // 第三方设备编码 可通过'_'字符分割出设备sn码
            String thirdPartyCode = rootNode.findPath("devId").asText();

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.value);

            ProjectDeviceInfo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
            SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                    .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
            // 这里根据不同厂商和设备类型去决定如何处理回调的设备属性
            DeviceParamServiceFactory paramServiceFactory = DeviceParamFactoryProducer.getFactory(deviceInfo.getProjectId(), deviceInfo.getDeviceType());
            if (paramServiceFactory != null) {
                paramServiceFactory.getParamDataService(productMap.getManufacture(), deviceInfo.getDeviceType()).deviceDataUpdate(json, deviceInfo);
            } else {
                log.error("未获取到设备参数服务，无法更新设备参数 requestJson：{}", requestJson.toJSONString());
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


    /**
     * 设备对象管理信息回应 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    @Async
    public void deviceObjManagerReport(JSONObject requestJson) {

        /**
         * 人脸下发返回结果
         * {
         *     "onNotifyData": {
         *         "changeTime": "1606459052",
         *         "devId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
         *         "gatewayId": "5ed9dc514b495808c82eca87_PEOQL20M08029761S9DJ",
         *         "msgId": "01131f916f8b4869aaa",
         *         "objManagerData": {
         *             "action": "ADD",
         *             "objInfo": {
         *                 "passNo": "ebb6e107220d3b30cd9acbc89bf27ae7"
         *             },
         *             "objName": "objName",
         *             "serviceId": "FaceManager"
         *         },
         *         "productId": "5"
         *     },
         *     "resource": "device.objmanager",
         *     "event": "report",
         *     "subscriptionId": "21"
         * }
         *
         */
        //init
        //requestJson.getJSONObject("onNotifyData").getJSONObject("objManagerData").put("objInfo",null);
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        String msgId = messageData.getMsgId();
        String thirdPartyCode = messageData.getDevId();
        String gatewayId = messageData.getGatewayId();

        ObjManagerData objManagerData = messageData.getObjManagerData();

        String serviceId = objManagerData.getServiceId();

        String objName = objManagerData.getObjName();
        String action = objManagerData.getAction();

        boolean b = projectInfoService.checkCommunityIdIsOriginProject(objManagerData.getCommunityId());
        if (!b) {
            log.info("[冠林边缘网关] 级联设备-跳过设备对象回调事件");
            return;
        }

        JSONObject objInfo = null;
        String passNo = "";

        List<ProjectRightDevice> rightDeviceList = null;

        Object requestRedisObjStr = RedisUtil.get(AurineEdgeCacheConstant.AURINE_EDGE_REQUEST_PER + msgId);
        AurineEdgeRequestObject aurineEdgeRequestObject = null;
        if (requestRedisObjStr != null) {
            aurineEdgeRequestObject = JSONObject.parseObject(requestRedisObjStr.toString(), AurineEdgeRequestObject.class);
//            ProjectContextHolder.setProjectId(aurineEdgeRequestObject.getProjectId());
        }

//        //请求回调数据
//        if (aurineEdgeRequestObject != null) {
//            log.debug("[冠林边缘网关] 从Redis key: {} 中获取请求对象：{}", AurineEdgeCacheConstant.AURINE_EDGE_REQUEST_PER + msgId, aurineEdgeRequestObject);
//            //直连设备
//            aurineEdgeEventDeviceCallbackHandle(serviceId, thirdPartyCode, aurineEdgeRequestObject, objManagerData, requestJson);
//
//        } else {
        log.debug("[冠林边缘网关] 获取到主动上报事件 {}", requestJson);
        //调用主动上报事件 处理链
        ChainManager.getChain(AurineEdgeObjectManageChain.class).doChain(callBackData);

//            switch (AurineEdgeServiceEnum.getByCode(serviceId)) {
//                case DEVICE_INFO:
//                    switch (action) {
//                        case AurineEdgeActionConstant.ADD:
//                            log.debug("[冠林边缘网关] 获取到设备自主上报添加事件 requestJson:{}", requestJson);
//                            break;
//                        default:
//                            log.error("[冠林边缘网关] 未定义的上报事件 serviceId :{},action:{}", serviceId, action);
//                            break;
//                    }
//
//                    break;
//                default:
//                    log.error("[冠林边缘网关] 未定义的上报事件 serviceId :{},action:{}", serviceId, action);
//                    break;
//
//            }
//        }

    }


    /**
     * 冠林边缘网关 直连设备 请求回调处理函数
     */
/*
    private void aurineEdgeEventDeviceCallbackHandle(String serviceId, String thirdPartyCode, AurineEdgeRequestObject aurineEdgeRequestObject, ObjManagerData objManagerData, JSONObject requestJson) {
        List<ProjectRightDevice> rightDeviceList = null;
        JSONObject objInfo = null;
        String passNo = "";
        String keyid = "";
        //配置返回结果
//        JSONObject resultObj = objManagerData.getResult();
//        if (resultObj != null) {
        log.info("[冠林边缘网关] 获取到直连设备回调数据：{}", requestJson);
        ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
        responseOperateDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.value);

//            String result = resultObj.getString("result");// 返回结果


        //do business
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
        if (deviceInfo == null) {
            log.error("[冠林边缘网关] 回调函数 未获取到设备{}", thirdPartyCode);
        } else {
            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
            responseOperateDTO.setDeviceInfo(deviceInfo);


            //人脸回应
            if (serviceId.equalsIgnoreCase(AurineEdgeServiceEnum.CERT_FACE.code)) {
                objInfo = objManagerData.getObjInfo();
//                    passNo = objInfo.getString("passNo");
//                    keyid = objInfo.getString("keyid");

                //获取下发的人脸
                passNo = objInfo.getJSONObject("doorAccess").getString("passNo");
                ProjectFaceResources faceResources = projectFaceResourcesService.getByCode(passNo, ProjectContextHolder.getProjectId());

//                keyid = faceResources.getFaceId();
                String result = objInfo.getJSONObject("doorAccess").getString("result");

            rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getCertMediaCode,passNo)
                    .eq(ProjectRightDevice::getDeviceId,deviceInfo.getDeviceId())
            );

//                rightDeviceList = projectRightDeviceMapper.listByDeviceIdAndCertmediaId(deviceInfo.getDeviceId(), keyid);

                if (CollUtil.isNotEmpty(rightDeviceList)) {

                    AurineEdgeFaceResultCodeEnum certStatusByResult = AurineEdgeFaceResultCodeEnum.getCertStatusByResult(result);
                    log.info("[冠林边缘网关] 人脸介质passNo：{} 介质下发或删除结果：{} 设备端回调结果code：{}", passNo, certStatusByResult.desc, result);
                    if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
                        projectRightDeviceService.addCerts(rightDeviceList, true);
                        // 后面就没有必要执行了，因为重新下载（重试下载）是一个操作而不是状态
                        return;
                    } else {
                        responseOperateDTO.setRespondStatus(certStatusByResult.cetDownloadStatus);
                    }

                    responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS);
                    responseOperateDTO.setRightDevice(rightDeviceList.get(0));

                    this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);
                } else {
                    log.error("[冠林边缘网关] 回调函数 在设备 {} {} 下未获取到通行凭证 {} ", deviceInfo.getDeviceName(), deviceInfo.getDeviceId(), keyid);
                }

            } else {
                //其他凭证回调
            }
        }
//        } else {
//            log.info("[冠林边缘网关] 获取到未定义的设备对象信息：{}", requestJson);
//        }
    }
*/

    /**
     * 设备操作指令的响应 数据清洗
     *
     * @param requestJson
     */
    @Override
    @Async
    public void deviceCommandResponse(JSONObject requestJson) {
        boolean b = projectInfoService.checkCommunityIdIsOriginProject(requestJson.toJSONString(), COMMUNITY_ID);
        if (!b) {
            log.info("[冠林边缘网关] 级联设备-跳过设备指令响应处理");
            return;
        }
        sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, requestJson);
    }


    /**
     * 设备事件上报 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    @Async
    public void deviceEventReport(JSONObject requestJson) {
        //事件分类
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        ObjManagerData objManagerData = messageData.getObjManagerData();
        if (objManagerData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }
        String communityId = objManagerData.getCommunityId();
        if (StrUtil.isNotEmpty(communityId)) {
            boolean result = projectInfoService.checkCommunityIdIsOriginProject(communityId);
            if (!result) {
                log.info("[冠林边缘网关] 级联设备-跳过设备事件处理流程");
                return;
            }
        }

        String gatewayId = messageData.getGatewayId();
        String deviceId = messageData.getDevId();

        String eventCode = objManagerData.getEventCode();
        String eventName = objManagerData.getEventName();
        String eventSrc = objManagerData.getEventSrc();

//        Long eventTime = Long.valueOf(objManagerData.getString("eventTime"));//事件时间
//        LocalDateTime eventDate = DateUtil.toLocalDateTime(Instant.ofEpochSecond(eventTime));//事件时间
        String eventTime = objManagerData.getEventTime();

        //应对接口中出现的多种事件格式
        LocalDateTime eventDate = null;
        if (StringUtil.isNumeric(eventTime)) {
            if (eventTime.length() == SECOND) {
                Instant instant = Instant.ofEpochSecond(Long.valueOf(eventTime));
                eventDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            } else if (eventTime.length() == MILLISECOND) {
                Instant instant = Instant.ofEpochMilli(Long.valueOf(eventTime));
                eventDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
//

        } else {
            eventDate = DateUtil.parseLocalDateTime(eventTime);
        }

        String eventType = objManagerData.getEventType();
        String objName = objManagerData.getObjName();
        String serviceId = objManagerData.getServiceId();//事件类型


        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(deviceId);
        if (deviceInfo == null) {
            log.error("[冠林边缘网关] 事件上报 未找到该上报设备 deviceId = {}", deviceId);
            throw new RuntimeException("[冠林边缘网关] 事件上报 未找到该上报设备");
        }
//        if (deviceInfo != null) {
//            // 对设备事件进行处理
//            try {
//                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
//                        .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
//                DeviceEventServiceFactory.getInstance(productMap.getManufacture(), deviceInfo.getDeviceType())
//                        .eventHandle(requestJson.toJSONString(), deviceInfo);
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.info("事件解析异常：{}", requestJson);
//            }
//        }


        //根据事件类型，自主拼接的事件内容，用于原生事件描述不完整的事件。
        String eventContent = "";
        AurineEdgeEventTypeEnum eventTypeEnum = AurineEdgeEventTypeEnum.getByCode(eventName);
        AurineEdgeEventEnum eventEnum = AurineEdgeEventEnum.getByCode(eventCode);
        eventContent = eventContent + eventTypeEnum.desc + " " + eventEnum.desc + " ";
        //判断为乘梯识别终端设备，修改设备描述
        if (DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE.equals(deviceInfo.getDeviceType())) {
            AurineEdgeLiftEventEnum liftEventEnum = AurineEdgeLiftEventEnum.getByCode(eventCode);
            eventContent = liftEventEnum.desc + " ";
        }
        JSONObject dataJson = objManagerData.getData();


        switch (eventTypeEnum) {
            //开门事件，使用通行对象
            case DjOpenDoorEvent:
                handleDjOpenDoorEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId,messageData.getMsgId());
                break;
            //异常事件
            case DjAbnormalEvent:
                handleDjAbnormalEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId, requestJson);
                break;
            //报警事件
            case DjAlarmEvent:
            case AIFQControlEvent:
                log.info("报警事件处理");
                EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
                eventWarningErrorDTO.setEventTime(eventDate);
                eventWarningErrorDTO.setDesc(eventContent);
                eventWarningErrorDTO.setImgUrl(dataJson.getString("snapPic"));
                eventWarningErrorDTO.setThirdPartyCode(deviceId);
                eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
                eventWarningErrorDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
                eventWarningErrorDTO.setGatewayCode(gatewayId);
                // TODO: 2021/9/30  暂定
                eventWarningErrorDTO.setEventCode((dataJson.getInteger("flowId")));
                eventWarningErrorDTO.setAreaNo((dataJson.getInteger("areaNo")));
                String deviceType = deviceInfo.getDeviceType();

//                if (AurineEdgeEventEnum.ALERT_EQUIPMENT_ERROR.code.equals(eventCode)) {
//                    //获取事件类型ID
//
//                    //烟感的错误字段
//                    String eventTypeAlarm = dataJson.getString("event_type");
//                    //路灯的错误字段
//                    String alarmType = dataJson.getString("alarmType");
//                    //获取错误字段的code
//                    List<String> alarmCode = this.getAlarmCode(dataJson, eventTypeAlarm, alarmType);
//                    //没有错误字段
//
//                    if (alarmCode.size() == 0) {
//                        break;
//                    } else {
//                        for (String code : alarmCode) {
//                            EventTypeEnum eventTypeEnumByAlarmCode = HuaweiEventTypeMapEnum.getEventTypeEnumByAlarmCode(code, deviceType);
//                            eventWarningErrorDTO.setEventTypeId(eventTypeEnumByAlarmCode.eventTypeId);
//                            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);
//                        }
//
//                    }
//                } else {
                EventTypeEnum cloudEnum = AurineEdgeEventTypeMapEnum.getCloudEnum(eventCode, DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()));
                eventWarningErrorDTO.setEventTypeId(cloudEnum.eventTypeId);
                sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);
//                }


                break;

//            //WR20网关事件
//            case GwNormalEvent:
//                if (!AurineEdgeEventEnum.IOT_DEVICE_DATA_REPORTING.code.equals(eventCode)) {
//                    log.info("[WR20] - 获取到网关凭证状态变化通知：{}", objManagerData);
//                    WR20CallbackFactory.getFactoryInstance().getRightService(VersionEnum.V1.code)
//                            .changeCertStateByWR20(JSONObject.parseObject(JSONObject.toJSONString(objManagerData)), gatewayId, eventCode);
//                }
//                break;
            // 通话记录事件
            case DjTalkRecordEvent:
                // 呼叫方设备id
                String callDeviceId = dataJson.getString("deviceId");
                handleDjTalkRecordEvent(dataJson, eventContent, eventEnum, eventDate, callDeviceId, gatewayId, requestJson);
                // handleDjTalkRecordEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId, requestJson);
                break;
            case PerimeterAlarmEvent:
                handlePerimeterAlarmEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId);
                break;
            case RemovePerimeterAlarmEvent:
                //handleRemovePerimeterAlarmEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId);
            default:
                log.error("[冠林边缘网关] 暂未实现该事件类型：{}，{}", eventName, requestJson);
                break;
        }

    }

    private void handleRemovePerimeterAlarmEvent(JSONObject dataJson, String eventContent, AurineEdgeEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId) {
        String errorType = dataJson.getString("alarmType");
        String channelNo = dataJson.getString("channelID");
        String module = channelNo.substring(6, 9);
        String channel = channelNo.substring(13, 16);
        projectPerimeterAlarmEventService.updateAlarmEvent(deviceId, module, channel, errorType, channelNo);


    }

    private void handlePerimeterAlarmEvent(JSONObject dataJson, String eventContent, AurineEdgeEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId) {
        String errorType = dataJson.getString("alarmType");
        String channelNo = dataJson.getString("channelID");

        String module = channelNo.substring(6, 9);
        String channel = channelNo.substring(13, 16);

        projectPerimeterAlarmEventService.saveAlarm(deviceId, module, channel, errorType);
    }

    @Override
    @Async
    public void edgeChannelReport(JSONObject requestJson) {

        //事件分类
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        ObjManagerData objManagerData = messageData.getObjManagerData();
        if (objManagerData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
        String channelNo = objManagerData.getObjInfo().getString("channelID");
        String errorType = objManagerData.getObjInfo().getString("alarmType");

        errorType = AliEdgePerimeterAlarmEnum.getEnumByAliCode(errorType).getCloudCode();


        eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_CHANNEL_ALARM);
        eventWarningErrorDTO.setDeviceSn(messageData.getDevId());
        eventWarningErrorDTO.setEventTime(LocalDateTime.now());
        eventWarningErrorDTO.setErrorType(errorType);
        eventWarningErrorDTO.setErrorDeviceId(channelNo);

        kafkaTemplate.send(TopicConstant.SDI_EVENT_WARNING_ERROR, JSONObject.toJSONString(eventWarningErrorDTO));
        log.info("[阿里边缘] 周界告警 告警事件已推送到云服务");

    }

    @Override
    @Async
    public void edgeChannelRepeat(JSONObject requestJson) {
        //事件分类
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        ObjManagerData objManagerData = messageData.getObjManagerData();
        if (objManagerData == null) {
            log.error("[冠林边缘网关] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[冠林边缘网关] 接收到事件信息格式错误");
        }

        EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
        String channelNo = objManagerData.getObjInfo().getString("channelID");
        String errorType = objManagerData.getObjInfo().getString("alarmType");

        errorType = AliEdgePerimeterAlarmEnum.getEnumByAliCode(errorType).getCloudCode();


        eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_CHANNEL_ALARM);
        eventWarningErrorDTO.setDeviceSn(messageData.getDevId());
        eventWarningErrorDTO.setEventTime(LocalDateTime.now());
        eventWarningErrorDTO.setErrorType(errorType);
        eventWarningErrorDTO.setErrorDeviceId(channelNo);

        kafkaTemplate.send(TopicConstant.SDI_EVENT_WARNING_ERROR, JSONObject.toJSONString(eventWarningErrorDTO));
        log.info("[阿里边缘] 周界告警 告警事件已推送到云服务");

    }

    /**
     * 处理通话记录事件
     *
     * @param dataJson
     * @param eventContent
     * @param eventEnum
     * @param eventDate
     * @param deviceId
     * @param gatewayId
     * @param requestJson
     */
    private void handleDjTalkRecordEvent(JSONObject dataJson, String eventContent, AurineEdgeEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId, JSONObject requestJson) {
        log.info("[冠林边缘网关] - 获取到通话记录事件通知：{}", requestJson);
        EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
        ProjectDeviceCallEvent deviceCallEvent = new ProjectDeviceCallEvent();
        //设置呼叫方
        deviceCallEvent.setCaller(ReUtil.replaceAll(deviceId, "-", ""));
        //解析呼叫事件对象
        RecordEventObj recordEventObj = JSONObject.parseObject(dataJson.toJSONString(), RecordEventObj.class);
        String userDevno = recordEventObj.getUserDevNo();
        // 设置通话类型
        switch (AurineEdgeCallTypeEnum.getByCode(recordEventObj.getCallType())) {
            case CALL_APP:
            case CALL_ROOM:
                // 呼叫室内-TRTC呼叫室内机肯定是呼叫业主
                // 如果是呼叫APP，区/梯口机无法呼叫物业，所以只能是呼叫业主
                deviceCallEvent.setCallType(CallTypeEnum.CALL_OWNER.code);
                break;
            case CALL_CENTER:
                // 呼叫中心
                deviceCallEvent.setCallType(CallTypeEnum.CALL_CENTER.code);
                break;
            case CALL_FORWARDING:
                // 呼叫转移
                deviceCallEvent.setCallType(CallTypeEnum.CALL_FORWARDING.code);
                break;
            default:
                break;
        }
        //获取呼叫方设备信息
        ProjectDeviceInfo callerDevice = projectDeviceInfoProxyService.getByThirdPartyCode(deviceId);
        ProjectContextHolder.setProjectId(callerDevice.getProjectId());
        //设置呼叫方名称
        deviceCallEvent.setCallerName(StrUtil.isNotEmpty(callerDevice.getDeviceDesc()) ? callerDevice.getDeviceDesc() : callerDevice.getDeviceName());
        //设置项目id
//        deviceCallEvent.setProjectId(callerDevice.getProjectId());
        //接听方默认为空
        deviceCallEvent.setAnswerer("");

        //TODO:记录时间：2021-02-23；原因：呼叫方类型还不知道怎么判断，这里先默认设置为设备了
        deviceCallEvent.setCallerType("1");
        //设置接听类型同呼叫类型
        deviceCallEvent.setAnswererType(AurineEdgeCallTypeEnum.getTypeByCode(recordEventObj.getCallType()));
        /*
         * 1、呼叫室内机按室内机的设备编号发，呼叫管理员发101；
         * 2、呼叫咚咚云对讲，按房号或者0000（管理员机）；
         * 3、抓拍记录：按室内设备编号或者0000（管理员机）
         *
         */
        boolean answerIsAdmin = false;
        //记录类型为呼叫抓拍
        if ("2".equals(recordEventObj.getRecordMode())) {
            if ("0000".equals(userDevno)) {
                deviceCallEvent.setAnswererName("中心机");
            } else {
                ProjectDeviceInfo answerDevice = projectDeviceInfoProxyService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceCode, userDevno).last("limit 1"));
                deviceCallEvent.setAnswererName(StrUtil.isNotEmpty(answerDevice.getDeviceDesc()) ? answerDevice.getDeviceDesc() : answerDevice.getDeviceName());
                deviceCallEvent.setAnswerer(answerDevice.getDeviceId());
            }
        } else {
            //呼叫室内
            if (AurineEdgeCallTypeEnum.CALL_ROOM.code.equals(recordEventObj.getCallType())) {
                ProjectDeviceInfo answerDevice = projectDeviceInfoProxyService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceCode, userDevno).last("limit 1"));
                deviceCallEvent.setAnswererName(StrUtil.isNotEmpty(answerDevice.getDeviceDesc()) ? answerDevice.getDeviceDesc() : answerDevice.getDeviceName());
                deviceCallEvent.setAnswerer(answerDevice.getDeviceId());
                //呼叫中心
            } else if (AurineEdgeCallTypeEnum.CALL_CENTER.code.equals(recordEventObj.getCallType()) || "0000".equals(recordEventObj.getUserDevNo())) {
                deviceCallEvent.setAnswererName("中心机");
                //呼叫APP或云电话
            } else {
                ProjectFrameInfo houseInfo = projectFrameInfoService.getOne(new LambdaQueryWrapper<ProjectFrameInfo>().eq(ProjectFrameInfo::getEntityCode, userDevno));
                String houseName = projectFrameInfoService.getHouseFullNameByHouseId(houseInfo.getEntityId());
                deviceCallEvent.setAnswererName(houseName);
                deviceCallEvent.setAnswerer(houseInfo.getEntityId());
            }
        }

        //设置呼叫时间
        if (StrUtil.isNotEmpty(recordEventObj.getRecordTime())) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            deviceCallEvent.setCallTime(LocalDateTime.parse(recordEventObj.getRecordTime(), dateFormat));
        }

        //TODO:记录时间：2021-02-23；原因：设备有三种呼叫结果 0：未接 1：已接 2：呼叫抓拍，系统只有前两种
        //设置呼叫结果
        deviceCallEvent.setCallResponse(recordEventObj.getRecordMode());
        //设置通话时常
        deviceCallEvent.setCallDuration(recordEventObj.getDuration());

        //保存抓拍图片
        if (StrUtil.isNotEmpty(recordEventObj.getSnapPic())) {
            String fistPicUrl = imgConvertUtil.convertToLocalUrl(recordEventObj.getSnapPic());
            deviceCallEvent.setFirstPicUrl(fistPicUrl);
        }

        //封装呼叫记录传输对象
        eventDeviceNoticeDTO.setDeviceCallEvent(deviceCallEvent);
        //设置传输对象动作
        eventDeviceNoticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_CALL_EVENT);
        log.info("[冠林边缘网关] - 解析结果{}", eventDeviceNoticeDTO.toString());
        //用kafka
        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, eventDeviceNoticeDTO);
    }

    /**
     * 处理异常信息
     */
    private void handleDjAbnormalEvent(JSONObject dataJson, String eventContent, AurineEdgeEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId, JSONObject requestJson) {
        EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();

        String desc = dataJson.getString("alarmDesc");
        if (StringUtils.isEmpty(desc)) {
            desc = dataJson.getString("EventDesc");
        }
        if (StringUtils.isNotEmpty(desc)) {
            eventContent = eventContent + "," + desc;//事件描述
        }

        switch (eventEnum) {
            case ABNORMA_CARD:
                String passID = dataJson.getString("passId");
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Card.code);
                //卡号如果少于8位，统一补0到8位
                passID = String.format("%08d", Integer.valueOf(passID));
                eventDeviceGatePassDTO.setCertMediaValue(passID);
                //事件描述中不存在卡号时，描述内容补充卡号信息
                if (eventContent.indexOf(passID) == -1) {
                    eventContent += " 卡号：" + passID;
                }
                break;
            case ABNORMA_CARD_OUT_TIME:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                break;
            case ABNORMA_FACE_OUT_TIME:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                break;
            case ABNORMA_FACE_BLACKLIST:
                // project_face_resources 的 faceCode
                String faceCode= dataJson.getString("passId");
                // 找到faceId
                LambdaQueryWrapper<ProjectFaceResources> queryWrapper = new QueryWrapper<ProjectFaceResources>().lambda()
                        .eq(ProjectFaceResources::getFaceCode,faceCode)
                        .last("limit 1");
                ProjectFaceResources faceResources  = projectFaceResourcesService.getOne(queryWrapper);
                if (faceResources != null) {
                    String faceId = faceResources.getFaceId();
                    // 根据faceId找第三方人脸黑名单id
                    ProjectBlacklistAttr blacklistAttr = projectBlacklistAttrService.getByFaceId(faceId);
                    if (blacklistAttr != null) {
                        eventDeviceGatePassDTO.setThirdCertMediaId(blacklistAttr.getThirdFaceId());
                    }
                }
            default:
                //其他异常事件
                break;
        }


        eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
        eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_ERROR_PASS);
        eventDeviceGatePassDTO.setCertMediaType(null);
        eventDeviceGatePassDTO.setImgUrl(dataJson.getString("snapPic"));


        eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
        eventDeviceGatePassDTO.setJsonObject(requestJson);

        eventDeviceGatePassDTO.setEventTime(eventDate);
        eventDeviceGatePassDTO.setDesc(eventContent);
        eventDeviceGatePassDTO.setThirdPartyCode(deviceId);
        eventDeviceGatePassDTO.setGatewayCode(gatewayId);


        sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);
    }

    /**
     * 处理门禁通行事件
     *
     * @param dataJson
     * @param eventContent
     * @param eventEnum
     * @param eventDate
     * @param deviceId
     * @param gatewayId
     */
    private void handleDjOpenDoorEvent(JSONObject dataJson, String eventContent, AurineEdgeEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId,String msgId) {
        String openMode = "";//开门方式
        String userId = "";//住户第三方ID
        String userName = "";//住户第三方ID
        String passID = "";//凭证id
        String thirdDeviceId = "";//凭证id
        String userThirdId = "";//室内机房号
        String qrCode = "";
        String userDesc = "";
        OpendoorEventObj opendoorEventObj = dataJson.toJavaObject(OpendoorEventObj.class);
        openMode = opendoorEventObj.getOpenMode();
        passID = opendoorEventObj.getPassId();
        userName = opendoorEventObj.getUserName();
        thirdDeviceId = opendoorEventObj.getThirdDeviceId();
        userId = opendoorEventObj.getUserId();
        userThirdId = opendoorEventObj.getUserThirdId();
        qrCode = opendoorEventObj.getQrCode();
        userDesc = opendoorEventObj.getUserDesc();
//        eventContent = eventContent ;//事件描述
//        eventContent = eventContent + " " + opendoorEventObj.getEventDesc();//事件描述

        EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
        eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
        if (StringUtils.isNotEmpty(opendoorEventObj.getSnapImg())) {
            eventDeviceGatePassDTO.setImgUrl(opendoorEventObj.getSnapImg());
        } else {
            eventDeviceGatePassDTO.setImgUrl(opendoorEventObj.getFacePic());
        }
        if(StringUtils.isNotEmpty(opendoorEventObj.getTemperature())){
            eventDeviceGatePassDTO.setTemperature(opendoorEventObj.getTemperature());
        }
        eventDeviceGatePassDTO.setDesc(eventContent);


        switch (eventEnum) {
            case OPEN_DOOR_CARD:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.CARD_OPEN_MODE.openMode);
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Card.code);
                //卡号如果少于8位，统一补0到8位
                passID = String.format("%08d", Integer.valueOf(passID));
                eventDeviceGatePassDTO.setCertMediaValue(passID);
                //事件描述中不存在卡号时，描述内容补充卡号信息
                if (eventContent.indexOf(passID) == -1) {
                    eventContent += " 卡号：" + passID;
                }
                eventDeviceGatePassDTO.setDesc(eventContent);
                break;
            case OPEN_DOOR_FACE:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.FACE_OPEN_MODE.openMode);
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Face.code);
                eventDeviceGatePassDTO.setCertMediaCode(passID);
                break;
            case OPEN_DOOR_FINGER:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FINGER);
                break;

            case OPEN_DOOR_QR:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.QRCODE_OPEN_MODE.openMode);
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);
                eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(userDesc, " 住户") : userName);
                eventDeviceGatePassDTO.setAddrDesc(StringUtils.isEmpty(userName) ? eventDeviceGatePassDTO.getPersonName().replace(" 住户","房") : "");
                eventDeviceGatePassDTO.setQrcode(opendoorEventObj.getQrCode());
                //解析二维码参数
                parseQrCodeParam(eventDeviceGatePassDTO,userName,userDesc,qrCode,eventContent);
                break;
            case REMOTE_VALID_DOOR_OPEN:
                passcodeRegisterRecordService.remoteValidOpenDoor((String)dataJson.get("data"),msgId,deviceId);
                return;
            case OPEN_DOOR_QR_TEMP:
            case OPEN_DOOR_QR_AUTH:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.QRCODE_OPEN_MODE.openMode);
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.VISITOR.code);
                eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(userDesc, " 访客") : userName);
                eventDeviceGatePassDTO.setAddrDesc(StringUtils.isEmpty(userName) ?  eventDeviceGatePassDTO.getPersonName().replace(" 访客","房") : "");

                //解析二维码参数
                parseQrCodeParam(eventDeviceGatePassDTO,userName,userDesc,qrCode,eventContent);
                break;
            case OPEN_DOOR_CALL_INDOOR:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.LONG_RANGE_OPEN_MODE.openMode);
                eventDeviceGatePassDTO.setDeviceType(DeviceTypeConstants.INDOOR_DEVICE);
                eventDeviceGatePassDTO.setPersonName(this.getHouseName(userThirdId.substring(0, userThirdId.length() - 1)));
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                break;
            case OPEN_DOOR_CALL_CENTER:
                eventDeviceGatePassDTO.setDeviceType(DeviceTypeConstants.CENTER_DEVICE);
                ProjectDeviceInfo projectDeviceInfoProxyServiceOne = projectDeviceInfoProxyService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceCode, userThirdId)
                        .eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.CENTER_DEVICE));
                eventDeviceGatePassDTO.setPersonName(projectDeviceInfoProxyServiceOne != null ? projectDeviceInfoProxyServiceOne.getDeviceName() : "");
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.LONG_RANGE_OPEN_MODE.openMode);
                break;
            case OPEN_DOOR_MONITOR:
                /**
                 * 中心机开头为六个000000
                 */
                if (StrUtil.startWith(userThirdId, "000000")) {
                    eventDeviceGatePassDTO.setDeviceType(DeviceTypeConstants.CENTER_DEVICE);
                    ProjectDeviceInfo projectDeviceInfoProxyServiceTwo = projectDeviceInfoProxyService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>().eq(ProjectDeviceInfo::getDeviceCode, userThirdId)
                            .eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.CENTER_DEVICE));
                    eventDeviceGatePassDTO.setPersonName(projectDeviceInfoProxyServiceTwo != null ? projectDeviceInfoProxyServiceTwo.getDeviceName() : "");
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                } else {
                    eventDeviceGatePassDTO.setDeviceType(DeviceTypeConstants.INDOOR_DEVICE);
                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(userThirdId.substring(0, userThirdId.length() - 1)));
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                }
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.LONG_RANGE_OPEN_MODE.openMode);
                break;
            case OPEN_DOOR_PSW:
                eventDeviceGatePassDTO.setOpenMode(AurineOpenModeEnum.PASSWORD_CODE_MODE.openMode);
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_PASSWORD);
                break;
            default:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                break;
        }

        eventDeviceGatePassDTO.setThirdPartyCode(deviceId);
        eventDeviceGatePassDTO.setEventTime(eventDate);
        eventDeviceGatePassDTO.setGatewayCode(gatewayId);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);

    }

    /**
     * 解析二维码参数
     * *
     * @param eventDeviceGatePassDTO
     * @param userName
     * @param userDesc
     * @param qrCode
     * @param eventContent
     */
    private void parseQrCodeParam(EventDeviceGatePassDTO eventDeviceGatePassDTO,String userName,String userDesc, String qrCode,String eventContent) {
        if(StrUtil.isEmpty(qrCode)){
            return;
        }
        AurineQrCodeUtil.QrCode analysis = aurineQrCodeUtil.analysis(qrCode);
        if(analysis != null){
            eventDeviceGatePassDTO.setUserId(Integer.valueOf(analysis.getUserNo()));
            eventDeviceGatePassDTO.setExtStr(JSONObject.toJSONString(analysis));

            //由于协议的原因 暂时由平台来判断二维码类型  @author zy
            boolean bool = "1".equals(analysis.getType());
            eventDeviceGatePassDTO.setPersonType(bool ? PersonTypeEnum.PROPRIETOR.code : PersonTypeEnum.VISITOR.code);
            eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(userDesc, bool ? " 住户":" 访客") : userName);
            if(!bool){
                eventContent = eventContent.replace(AurineEdgeEventEnum.OPEN_DOOR_QR.desc, AurineEdgeEventEnum.OPEN_DOOR_QR_TEMP.desc);
                eventDeviceGatePassDTO.setDesc(eventContent);
            }
        }
    }

    @Autowired
    EventDeviceNoticeConsumer eventDeviceNoticeConsumer;

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }

    /**
     * 校验是否为WR20设备
     *
     * @param gateWayId
     * @return
     */
    private boolean checkIsWR20(String gateWayId) {
        return wr20ConfigProperties.getProjectList().stream().filter(a -> a.get("wr20id") == gateWayId).count() >= 1;
    }

    /**
     * 通过房屋框架号，获取房屋名称
     *
     * @param keyValue
     * @return
     */
    private String getHouseName(String keyValue, String postfix) {
        String houseNo = "";
        String houseName = "";
        String groupName = "";
        if (StringUtils.isEmpty(keyValue)) {
            return "";
        }

        //如果存在多个房间号，获取第一个
        if (keyValue.indexOf(",") >= 0) {
            String[] houseNoArray = keyValue.split(",");
            for (String no : houseNoArray) {
                if (no.length() >= 5) {
                    houseNo = no;
                    break;
                }
            }
        } else {
            houseNo = keyValue;
        }

        //调用业务服务，获取房间名称
        ProjectFrameInfo frameInfo = projectFrameInfoService.getByFrameNo(houseNo);
        if (frameInfo != null) {
            houseName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", null, FrameLevelEnum.BUILDING.code);
            groupName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", FrameLevelEnum.GROUP4.code, null);
        }

        if (StringUtils.equals(houseNo, "00000000")) {
            houseName = "公共区域";
        }

        /**
         * DEMO:组团5-组团4-楼栋单元房屋 + 后缀名词
         */
        houseName = StringUtils.isEmpty(houseName) ? "" : houseName;
        groupName = StringUtils.isEmpty(groupName) ? "" : groupName + "-";

        String resultStr = groupName + houseName;

        resultStr = StringUtils.isEmpty(resultStr) ? "" : resultStr + postfix;

        return resultStr;
    }

    /**
     * 通过房屋框架号，获取房屋名称
     *
     * @param keyValue
     * @return
     */
    private String getHouseName(String keyValue) {
        String houseNo = "";
        String houseName = "";
        String groupName = "";
        if (StringUtils.isEmpty(keyValue)) {
            return "";
        }

        //如果存在多个房间号，获取第一个
        if (keyValue.indexOf(",") >= 0) {
            String[] houseNoArray = keyValue.split(",");
            for (String no : houseNoArray) {
                if (no.length() >= 5) {
                    houseNo = no;
                    break;
                }
            }
        } else {
            houseNo = keyValue;
        }

        //调用业务服务，获取房间名称
        ProjectFrameInfo frameInfo = projectFrameInfoService.getByFrameNo(houseNo);
        if (frameInfo != null) {
            houseName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", null, FrameLevelEnum.BUILDING.code);
            groupName = projectFrameInfoService.getFrameNameByEntityId(frameInfo.getEntityId(), "-", FrameLevelEnum.GROUP4.code, null);
        }

        if (StringUtils.equals(houseNo, "00000000")) {
            houseName = "公共区域";
        }

        /**
         * DEMO:组团5-组团4-楼栋单元房屋 + 后缀名词
         */
        houseName = StringUtils.isEmpty(houseName) ? "" : houseName;
        groupName = StringUtils.isEmpty(groupName) ? "" : groupName + "-";

        String resultStr = groupName + houseName;

        resultStr = StringUtils.isEmpty(resultStr) ? "" : resultStr;

        return resultStr;
    }


    /**
     * TODO 等待3.0设备 V2版本二维码 校验返回的事件数据结构 @since 2021-05-06 王伟
     * 获取版本2 二维码事件表述信息
     *
     * @param eventDeviceGatePassDTO
     * @param userNo                 当前访问返回的userNo(序列编码)
     */
    private void getQRV2PassEventDesc(EventDeviceGatePassDTO eventDeviceGatePassDTO, String userNo) {

        Object qrDtoObject = RedisUtil.get("QR_" + userNo);
        if (qrDtoObject == null) {
            //未获取到数据（超时或其他异常原因，如redis重启，清空）

        } else {
            ProjectDevicePassQRDTO qrDto = (ProjectDevicePassQRDTO) qrDtoObject;
            eventDeviceGatePassDTO.setPersonName(qrDto.getPersonName());
            eventDeviceGatePassDTO.setPersonCode(qrDto.getPersonId());
            eventDeviceGatePassDTO.setPersonType(qrDto.getPersonType().code);
        }

    }

//    /**
//     * 获取需要的异常字段数组,没有异常返回空
//     *
//     * @param data
//     * @param smokeType      烟感
//     * @param streetLampType 路灯
//     * @return
//     */
//    public List<String> getAlarmCode(JSONObject data, String smokeType, String streetLampType) {
//        List<String> codeList = new ArrayList<>();
//
//        if (smokeType != null) {
//            codeList.add(HuaweiSmokeAlarmEnum.getDescByCode(smokeType));
//            return codeList;
//        }
//        if (streetLampType != null) {
//            String descByCode = HuaweiStreetLampAlarmEnum.getDescByCode(streetLampType);
//            codeList.add(descByCode);
//            return codeList;
//        }
//
//        for (String code : data.keySet()) {
//            boolean status = code.contains("Alarm");
//            //获取异常事件的字段
//            if (status || "fault".equals(code)) {
//                //判断状态是否是异常
//                if (HuaweiAlarmStateEnum.STATE_ABNORMAL.code.equals(String.valueOf(data.get(code)))) {
//                    codeList.add(code);
//                }
//
//            }
//        }
//        return codeList;
//    }
}

