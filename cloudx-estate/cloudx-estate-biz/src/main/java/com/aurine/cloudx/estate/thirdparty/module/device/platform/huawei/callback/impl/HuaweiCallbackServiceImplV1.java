package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.FrameLevelEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.ProjectDeviceEventCallbackService;
import com.aurine.cloudx.estate.mapper.ProjectRightDeviceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.consumer.EventDeviceNoticeConsumer;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.EventTypeEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceEventServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.HuaweiCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event.OpendoorEventObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.event.RecordEventObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.DevMessageData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.ObjManagerData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.factory.WR20CallbackFactory;
import com.aurine.cloudx.estate.thirdparty.module.wr20.config.WR20ConfigProperties;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20ServiceEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
public class HuaweiCallbackServiceImplV1 implements HuaweiCallbackService {

    private static ObjectMapper objectMapper = ObjectMapperUtil.instance();
    private static final int SECOND = 10;
    private static final int MILLISECOND = 13;

    @Resource
    private SysEventTypeConfService sysEventTypeConfService;
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private WR20ConfigProperties wr20ConfigProperties;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectRightDeviceMapper projectRightDeviceMapper;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectDeviceEventCallbackService projectDeviceEventCallbackService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;


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
    public void deviceActive(JSONObject requestJson) {
        /**
         * TODO: 设备事件 -  设备激活通知 数据清洗
         */
        EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
        eventDeviceNoticeDTO.setAction("");
        eventDeviceNoticeDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, requestJson);
    }

    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceStatusUpdate(JSONObject requestJson) {

        String subscriptionId = requestJson.getString("subscriptionId");
        String deviceId = requestJson.getJSONObject("onNotifyData").getString("devId");
        String status = requestJson.getJSONObject("onNotifyData").getString("status");


        EventDeviceNoticeDTO noticeDTO = new EventDeviceNoticeDTO();
        noticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE);
        noticeDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        noticeDTO.setStatus(HuaweiOnlineStatusEnum.getByCode(status).cloudCode);//转换为云系统字典值
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
    public void deviceDataUpdate(JSONObject requestJson) {
        String json = requestJson.toJSONString();
        try {
            // 获取到整个json的根节点
            JsonNode rootNode = objectMapper.readTree(json);
            // 这里获取到msgId（虽然好像没什么用）
            String msgId = rootNode.findPath("msgId").asText();
            // 第三方设备编码 可通过'_'字符分割出设备sn码
            String thirdPartyCode = rootNode.findPath("devId").asText();

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);

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
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
        }

        String msgId = messageData.getMsgId();
        String thirdPartyCode = messageData.getDevId();
        String gatewayId = messageData.getGatewayId();

        ObjManagerData objManagerData = messageData.getObjManagerData();

        String serviceId = objManagerData.getServiceId();

        String objName = objManagerData.getObjName();
        String action = objManagerData.getAction();

        JSONObject objInfo = null;
        String passNo = "";

        List<ProjectRightDevice> rightDeviceList = null;

        /**
         *  判断处理方法，如果是WR20,直接回调WR20的业务
         */
        Object requestRedisObjStr = RedisUtil.get(HuaweiCacheConstant.HUAWEI_REQUEST_PER + msgId);
        HuaweiRequestObject huaweiRequestObject = null;
        if (requestRedisObjStr != null) {
            huaweiRequestObject = JSONObject.parseObject(requestRedisObjStr.toString(), HuaweiRequestObject.class);
            ProjectContextHolder.setProjectId(huaweiRequestObject.getProjectId());
        }


        //请求回调数据
        if (huaweiRequestObject != null) {
            log.info("[华为中台] 从Redis key: {} 中获取请求对象：{}", HuaweiCacheConstant.HUAWEI_REQUEST_PER + msgId, huaweiRequestObject);

            if (huaweiRequestObject != null && ThirdPartyBusinessPlatformEnum.WR20.code.equals(huaweiRequestObject.getSource())) {
                //WR20回调数据
                wr20RequestCallbackHandle(serviceId, action, huaweiRequestObject, objManagerData, gatewayId, requestJson, msgId);
            } else {
                //直连设备
                huaweiEventDeviceCallbackHandle(serviceId, thirdPartyCode, huaweiRequestObject, objManagerData, requestJson);
            }

        } else {
            //主动上报数据
            //根据service和action，分发处理业务
            switch (WR20ServiceEnum.getByCode(serviceId)) {

                case TENEMENT_MANAGER:
                    switch (action) {
                        case WR20ActionConstant.ADD:
                            log.info("[华为中台] 获取到 [WR20] 添加住户上报事件,暂不做处理:{}", requestJson);
//                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).addPersonByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        case WR20ActionConstant.UPDATE:
                            log.info("[华为中台] 获取到 [WR20] 更新住户上报事件，暂不做处理:{}", requestJson);
//                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).updatePersonByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        case WR20ActionConstant.DELETE:
                            log.info("[华为中台] 获取到 [WR20] 删除住户上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).delPersonByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        default:
                            log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                            break;
                    }
                    break;
                case WORKER_MANAGER:
                    switch (action) {
//                        case WR20ActionConstant.GET:
//                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).syncStaff(objManagerDataJson.getObjInfo(), gatewayId);
//                            break;
                        case WR20ActionConstant.ADD:
                            log.info("[华为中台] 获取到 [WR20] 添加员工上报事件,暂不做处理::{}", requestJson);
//                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).addStaffByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        case WR20ActionConstant.UPDATE:
                            log.info("[华为中台] 获取到 [WR20] 更新员工上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).updateStaffByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        case WR20ActionConstant.DELETE:
                            log.info("[华为中台] 获取到 [WR20] 删除员工上报事件:{}", requestJson);
                            WR20CallbackFactory.getFactoryInstance().getPersonService(VersionEnum.V1.code).delStaffByWR20(objManagerData.getObjInfo(), gatewayId);
                            break;
                        default:
                            log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                            break;
                    }
                    break;
                default:
                    log.error("[华为中台] 获取到未定义的 对象信息 事件上报：{}", requestJson);


            }

        }

    }

    private void wr20RequestCallbackHandle(String serviceId, String action, HuaweiRequestObject huaweiRequestObject, ObjManagerData objManagerData, String gatewayId, JSONObject requestJson, String msgId) {
        //根据service和action，分发处理业务

        switch (WR20ServiceEnum.getByCode(serviceId)) {
            case FRAME_INFO_MANAGER:
                switch (action) {
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getFrameService(huaweiRequestObject.getSourceVersion()).syncFrame(objManagerData.getObjInfo(), gatewayId);
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                        break;
                }
                break;
            case DEVICE_INFO_MANAGER:
                switch (action) {
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getDeviceService(huaweiRequestObject.getSourceVersion()).syncDevice(objManagerData.getObjInfo(), huaweiRequestObject.getWr20DeviceType(), gatewayId);
                        break;
                    case WR20ActionConstant.CHECKOUT:
                        log.info("[华为中台] 获取到 [WR20] 迁出多房住户回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).checkOutPerson(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.CHECKIN:
                        log.info("[华为中台] 获取到 [WR20] 迁入多房住户回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).checkInPerson(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                        break;
                }
                break;
            case TENEMENT_MANAGER:
                switch (action) {
                    case WR20ActionConstant.GET:
                        //将返回数据信息写入Redis
                        RedisUtil.set("HUAWEI_SYNC_RESP_" + msgId, JSONObject.toJSONString(requestJson), 15);
//                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).syncTenement(objManagerData.getObjInfo(), gatewayId);
                        break;
                    case WR20ActionConstant.SYNC:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).syncTenement(objManagerData.getObjInfo(), gatewayId);
                        break;
                    case WR20ActionConstant.ADD:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addPerson(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.CHECKIN:
                        log.info("[华为中台] 获取到 [WR20] 迁入多房住户回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).checkInPerson(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.CHECKOUT:
                        log.info("[华为中台] 获取到 [WR20] 迁出多房住户回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).checkOutPerson(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 迁出住户回调:{}", requestJson);
                        JSONObject objInfo = objManagerData.getObjInfo();
                        if (objInfo != null) {
                            log.info("[WR20] 迁出用户结果 ", objInfo.getString("message"));
                        }
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                        break;
                }
                break;
            case WORKER_MANAGER:
                switch (action) {
                    case WR20ActionConstant.GET:
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).syncStaff(objManagerData.getObjInfo(), gatewayId);
                        log.info("[华为中台] 获取到 [WR20] 查询员工回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加员工回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addStaff(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除员工回调:{}", requestJson);
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 未定义事件：{}", requestJson);
                        break;
                }
                break;
            case CERT_CARD:
                switch (action) {
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除卡片回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加卡片回调:{}", requestJson);
                        String result = objManagerData.getObjInfo().getString("result");
                        if (StringUtils.isNotEmpty(result)) {
                            switch (result) {
                                case "0":
                                    log.info("[WR20] 添加卡片成功");
                                    WR20CallbackFactory.getFactoryInstance().getRightService(huaweiRequestObject.getSourceVersion()).addCard(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                                    break;
                                case "3":
                                    log.error("[WR20] 添加卡片失败，卡片已存在");
                                    break;
                                default:
                                    log.info("[WR20] 未定义事件：{}", result);
                                    break;
                            }
                        }
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 卡片未定义事件：{}", requestJson);
                        break;
                }
                break;
            case CERT_FACE:
                switch (action) {
                    case WR20ActionConstant.DELETE:
                        log.info("[华为中台] 获取到 [WR20] 删除人脸回调:{}", requestJson);
                        break;
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加人脸回调:{}", requestJson);
                        WR20CallbackFactory.getFactoryInstance().getRightService(huaweiRequestObject.getSourceVersion()).addFace(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 面部未定义事件：{}", requestJson);
                        break;
                }
                break;
            case VISITOR_MANAGER:
                switch (action) {
                    case WR20ActionConstant.ADD:
                        log.info("[华为中台] 获取到 [WR20] 添加访客回调:{}", requestJson);
                        /**
                         * {"onNotifyData":{"changeTime":"1611909327","devId":"5ebb5524f2d7e4035a3327a7_A97B934A-91CC-4152-BC76-B7ECBB0A9088","gatewayId":"5ebb5524f2d7e4035a3327a7_A97B934A-91CC-4152-BC76-B7ECBB0A9088","msgId":"ee8289937d984b1387e57cde25a52499","objManagerData":{"action":"ADD","objInfo":{"ThirdID":"","VIPK":20,"TeneID":1,"NotifyRepsonseID":"2ef6e13e-1547-47da-ae1f-c626d7aa59a6"},"objName":"VisitorInfo","serviceId":"VisitorManager"},"productId":"2"},"resource":"device.objmanager","event":"report","subscriptionId":"47"}
                         */
                        WR20CallbackFactory.getFactoryInstance().getPersonService(huaweiRequestObject.getSourceVersion()).addVisitor(objManagerData.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
                        break;
//                    case WR20ActionConstant.ADD:
//                        log.info("[华为中台] 获取到 [WR20] 添加人脸回调:{}", requestJson);
//                        WR20CallbackFactory.getFactoryInstance().getRightService(huaweiRequestObject.getSourceVersion()).addFace(objManagerDataJson.getObjInfo(), gatewayId, huaweiRequestObject.getUuid());
//                        break;
                    default:
                        log.info("[华为中台] 获取到 [WR20] 访客未定义事件：{}", requestJson);
                        break;
                }
                break;
            default:
                log.error("[WR20] 未找到服务：{}", requestJson);
        }
    }

    /**
     * 华为中台 直连设备 请求回调处理函数
     */
    private void huaweiEventDeviceCallbackHandle(String serviceId, String thirdPartyCode, HuaweiRequestObject huaweiRequestObject, ObjManagerData objManagerData, JSONObject requestJson) {
        List<ProjectRightDevice> rightDeviceList = null;
        JSONObject objInfo = null;
        String passNo = "";
        String keyid = "";
        //配置返回结果
        JSONObject resultObj = objManagerData.getResult();
        if (resultObj != null) {
            log.info("[华为中台] 获取到直连设备回调数据：{}", requestJson);
            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);

            String result = resultObj.getString("result");// 返回结果


            //do business
            ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(thirdPartyCode);
            if (deviceInfo == null) {
                log.error("[华为中台] 回调函数 未获取到设备{}", thirdPartyCode);
            } else {
                ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                responseOperateDTO.setDeviceInfo(deviceInfo);

                //人脸回应
                if (serviceId.equalsIgnoreCase(HuaweiServiceEnum.CERT_FACE.code)) {
                    objInfo = objManagerData.getObjInfo();
                    passNo = objInfo.getString("passNo");
                    keyid = objInfo.getString("keyid");

                    rightDeviceList = projectRightDeviceMapper.listByDeviceIdAndCertmediaId(deviceInfo.getDeviceId(), keyid);

                    if (CollUtil.isNotEmpty(rightDeviceList)) {

                        HuaweiFaceResultCodeEnum certStatusByResult = HuaweiFaceResultCodeEnum.getCertStatusByResult(result);
                        log.info("[华为中台] 人脸介质keyid：{} 介质下发或删除结果：{} 设备端回调结果code：{}", keyid, certStatusByResult.desc, result);
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
                        log.error("[华为中台] 回调函数 在设备 {} {} 下未获取到通行凭证 {} ", deviceInfo.getDeviceName(), deviceInfo.getDeviceId(), keyid);
                    }

                } else {
                    //其他凭证回调
                }
            }
        } else {
            log.info("[华为中台] 获取到未定义的设备对象信息：{}", requestJson);
        }
    }

    /**
     * 设备操作指令的响应 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceCommandResponse(JSONObject requestJson) {
        sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, requestJson);
    }


    /**
     * 设备事件上报 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    public void deviceEventReport(JSONObject requestJson) {

        //事件分类
        CallBackData callBackData = requestJson.toJavaObject(CallBackData.class);
        if (callBackData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
        }

        DevMessageData messageData = callBackData.getOnNotifyData();

        if (messageData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
        }

        ObjManagerData objManagerData = messageData.getObjManagerData();
        if (objManagerData == null) {
            log.error("[华为中台] 接收到事件信息格式错误:{}", requestJson);
            throw new RuntimeException("[华为中台] 接收到事件信息格式错误");
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
        if (deviceInfo != null) {
            // 对设备事件进行处理
            try {
                SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>()
                        .eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
                DeviceEventServiceFactory.getInstance(productMap.getManufacture(), deviceInfo.getDeviceType())
                        .eventHandle(requestJson.toJSONString(), deviceInfo);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("事件解析异常：{}", requestJson);
            }
        }


        //根据事件类型，自主拼接的事件内容，用于原生事件描述不完整的事件。
        String eventContent = "";
        HuaweiEventTypeEnum eventTypeEnum = HuaweiEventTypeEnum.getByCode(eventName);
        HuaweiEventEnum eventEnum = HuaweiEventEnum.getByCode(eventCode);
        eventContent = eventContent + eventTypeEnum.desc + " " + eventEnum.desc + " ";

        JSONObject dataJson = objManagerData.getData();


        switch (eventTypeEnum) {
            //开门事件，使用通行对象
            case DjOpenDoorEvent:
                handleDjOpenDoorEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId);
                break;
            //异常事件
            case DjAbnormalEvent:
                handleDjAbnormalEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId, requestJson);
                break;
            //报警事件
            case DjAlarmEvent:
                EventWarningErrorDTO eventWarningErrorDTO = new EventWarningErrorDTO();
                eventWarningErrorDTO.setEventTime(eventDate);
                eventWarningErrorDTO.setDesc(eventContent);
                eventWarningErrorDTO.setImgUrl(dataJson.getString("snapPic"));
                eventWarningErrorDTO.setThirdPartyCode(deviceId);
                eventWarningErrorDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
                eventWarningErrorDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
                eventWarningErrorDTO.setGatewayCode(gatewayId);
                String deviceType = deviceInfo.getDeviceType();

                if (HuaweiEventEnum.ALERT_EQUIPMENT_ERROR.code.equals(eventCode)) {

                    /**
                     * {
                     *     "onNotifyData":{
                     *         "changeTime":"1628049905",
                     *         "devId":"61079c2a0ad1ed0286344d11_868530027500526",
                     *         "gatewayId":"61079c2a0ad1ed0286344d11_868530027500526",
                     *         "msgId":"df13ecfd-0f42-44d2-a5eb-6e14189a3562",
                     *         "objManagerData":{
                     *             "action":"UPDATE",
                     *             "data":{
                     *                 "event_id":2,
                     *                 "event_type":2,
                     *                 "Imei":"868530027500526",
                     *                 "cmd":"event",
                     *                 "event_time":20210804
                     *             },
                     *             "eventCode":"402000",
                     *             "eventName":"DjAlarmEvent",
                     *             "eventSrc":"gateway",
                     *             "eventTime":"1628049514463",
                     *             "eventType":"gwcm",
                     *             "objName":"AlarmEvent",
                     *             "serviceId":"AlarmEvent"
                     *         }
                     *     },
                     *     "resource":"device.event",
                     *     "event":"report",
                     *     "subscriptionId":"15"
                     * }
                     */
                    //获取事件类型ID

                    //烟感的错误字段
                    String eventTypeAlarm = dataJson.getString("event_type");
                    //路灯的错误字段
                    String alarmType = dataJson.getString("alarmType");
                    //获取错误字段的code
                    List<String> alarmCode = this.getAlarmCode(dataJson, eventTypeAlarm, alarmType);
                    //没有错误字段

                    if (alarmCode.size() == 0) {
                        break;
                    } else {
                        for (String code : alarmCode) {
                            EventTypeEnum eventTypeEnumByAlarmCode = HuaweiEventTypeMapEnum.getEventTypeEnumByAlarmCode(code, deviceType);
                            eventWarningErrorDTO.setEventTypeId(eventTypeEnumByAlarmCode.eventTypeId);
                            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);
                        }

                    }
                } else {
                    EventTypeEnum cloudEnum = HuaweiEventTypeMapEnum.getCloudEnum(eventCode, DeviceTypeEnum.getByCode(deviceInfo.getDeviceType()));
                    eventWarningErrorDTO.setEventTypeId(cloudEnum.eventTypeId);
                    sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventWarningErrorDTO);
                }


                break;

            //WR20网关事件
            case GwNormalEvent:
                if (!HuaweiEventEnum.IOT_DEVICE_DATA_REPORTING.code.equals(eventCode)) {
                    log.info("[WR20] - 获取到网关凭证状态变化通知：{}", objManagerData);
                    WR20CallbackFactory.getFactoryInstance().getRightService(VersionEnum.V1.code)
                            .changeCertStateByWR20(JSONObject.parseObject(JSONObject.toJSONString(objManagerData)), gatewayId, eventCode);
                }
                break;
            // 通话记录事件
            case DjTalkRecordEvent:
                handleDjTalkRecordEvent(dataJson, eventContent, eventEnum, eventDate, deviceId, gatewayId, requestJson);
                break;
            default:
                log.error("[华为中台] 暂未实现该事件类型：{}，{}", eventName, requestJson);
                break;
        }

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
    private void handleDjTalkRecordEvent(JSONObject dataJson, String eventContent, HuaweiEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId, JSONObject requestJson) {
        log.info("[华为中台] - 获取到通话记录事件通知：{}", requestJson);
        EventDeviceNoticeDTO eventDeviceNoticeDTO = new EventDeviceNoticeDTO();
        ProjectDeviceCallEvent deviceCallEvent = new ProjectDeviceCallEvent();
        RecordEventObj recordEventObj = JSONObject.parseObject(dataJson.toJSONString(), RecordEventObj.class);


        //TODO:记录时间：2021-02-24  ；原因：这里得等后面对接
            /*List<ProjectDeviceInfo> callerDeviceList = projectDeviceInfoProxyService.list(new QueryWrapper<ProjectDeviceInfo>()
                    .lambda().eq(ProjectDeviceInfo::getDeviceCode, deviceId));
            if (CollUtil.isNotEmpty(callerDeviceList)) {
                ProjectDeviceInfo callerDevice = callerDeviceList.get(0);
                deviceCallEvent.setCallerName(StrUtil.isNotEmpty(callerDevice.getDeviceDesc()) ? callerDevice.getDeviceDesc() : callerDevice.getDeviceName());
                deviceCallEvent.setCaller(callerDevice.getDeviceId());
                deviceCallEvent.setProjectId(callerDevice.getProjectId());
            }*/

        //TODO:记录时间：2021-02-23；原因：呼叫方类型还不知道怎么判断，这里先默认设置为设备了
        deviceCallEvent.setCallerType("1");
        deviceCallEvent.setAnswererType(HuaweiCallTypeEnum.getTypeByCode(recordEventObj.getCallType()));

        //TODO:记录时间：2021-02-24  ；原因：这里得等后面对接
            /*List<ProjectDeviceInfo> answerList = projectDeviceInfoProxyService.list(new QueryWrapper<ProjectDeviceInfo>()
                    .lambda().eq(ProjectDeviceInfo::getDeviceCode, recordEventObj.getDeviceId()));
            if (CollUtil.isNotEmpty(answerList)) {
                ProjectDeviceInfo answerDevice = answerList.get(0);
                deviceCallEvent.setAnswererName(StrUtil.isNotEmpty(answerDevice.getDeviceDesc()) ? answerDevice.getDeviceDesc() : answerDevice.getDeviceName());
                deviceCallEvent.setAnswerer(answerDevice.getDeviceId());
            }*/

        //TODO:记录时间：2021-03-01；原因：暂时写死给设备测试用
        ProjectDeviceInfoProxyVo byThirdPartyCode = projectDeviceInfoProxyService.getByThirdPartyCode(deviceId);
        if (byThirdPartyCode != null) {
//            deviceCallEvent.setProjectId(byThirdPartyCode.getProjectId());
        }
        deviceCallEvent.setCallerName("陈光椿");
        deviceCallEvent.setCaller("32213123123213");
        deviceCallEvent.setAnswererName("王明星");
        deviceCallEvent.setAnswerer("1231424124214214");

        if (StrUtil.isNotEmpty(recordEventObj.getRecordTime())) {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            deviceCallEvent.setCallTime(LocalDateTime.parse(recordEventObj.getRecordTime(), dateFormat));
        }

        //TODO:记录时间：2021-02-23；原因：设备有三种呼叫结果 0：未接 1：已接 2：呼叫抓拍，系统只有前两种
        deviceCallEvent.setCallResponse(recordEventObj.getRecordMode());

        deviceCallEvent.setCallDuration(recordEventObj.getDuration());

        eventDeviceNoticeDTO.setDeviceCallEvent(deviceCallEvent);
        eventDeviceNoticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_CALL_EVENT);
        log.info("[华为中台] - 解析结果{}", eventDeviceNoticeDTO.toString());
        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, eventDeviceNoticeDTO);
    }

    /**
     * 处理异常信息
     */
    private void handleDjAbnormalEvent(JSONObject dataJson, String eventContent, HuaweiEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId, JSONObject requestJson) {
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
            case ABNORMA_CARD_OUT_TIME:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                break;
            case ABNORMA_FACE_OUT_TIME:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                break;
            default:
                //其他异常事件
                break;
        }


        eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_ERROR_PASS);
        eventDeviceGatePassDTO.setCertMediaType(null);
        eventDeviceGatePassDTO.setImgUrl(dataJson.getString("snapPic"));


        eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
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
    private void handleDjOpenDoorEvent(JSONObject dataJson, String eventContent, HuaweiEventEnum eventEnum, LocalDateTime eventDate, String deviceId, String gatewayId) {
        String openMode = "";//开门方式
        String userId = "";//住户第三方ID
        String userName = "";//住户第三方ID
        String passID = "";//凭证id

        OpendoorEventObj opendoorEventObj = dataJson.toJavaObject(OpendoorEventObj.class);
        openMode = opendoorEventObj.getOpenMode();
        passID = opendoorEventObj.getPassID();
        userName = opendoorEventObj.getUserName();
        userId = opendoorEventObj.getUserId();
//        eventContent = eventContent ;//事件描述
//        eventContent = eventContent + " " + opendoorEventObj.getEventDesc();//事件描述

        EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
        eventDeviceGatePassDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.name());
        eventDeviceGatePassDTO.setImgUrl(opendoorEventObj.getSnapImg());
        if(opendoorEventObj.getTemperature() != null){
            eventDeviceGatePassDTO.setTemperature(opendoorEventObj.getTemperature());
        }
        switch (eventEnum) {
            case OPEN_DOOR_CARD:
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
            case OPEN_DOOR_FACE:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Face.code);
                eventDeviceGatePassDTO.setCertMediaCode(passID);
                break;
            case OPEN_DOOR_FINGER:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FINGER);
                break;

            case OPEN_DOOR_QR:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);
//                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(passID, " 住户"));
                eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(passID, " 住户") : userName);
                break;
            case OPEN_DOOR_QR_TEMP:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.VISITOR.code);
//                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(passID, " 访客"));
                eventDeviceGatePassDTO.setPersonName(StringUtils.isEmpty(userName) ? this.getHouseName(passID, " 访客") : userName);
                break;
            default:
                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.OTHER);
                break;
        }

        eventDeviceGatePassDTO.setDesc(eventContent);
        eventDeviceGatePassDTO.setThirdPartyCode(deviceId);
        eventDeviceGatePassDTO.setEventTime(eventDate);
        eventDeviceGatePassDTO.setGatewayCode(gatewayId);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);

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

    /**
     * 获取需要的异常字段数组,没有异常返回空
     *
     * @param data
     * @param smokeType      烟感
     * @param streetLampType 路灯
     * @return
     */
    public List<String> getAlarmCode(JSONObject data, String smokeType, String streetLampType) {
        List<String> codeList = new ArrayList<>();

        if (smokeType != null) {
            codeList.add(HuaweiSmokeAlarmEnum.getDescByCode(smokeType));
            return codeList;
        }
        if (streetLampType != null) {
            String descByCode = HuaweiStreetLampAlarmEnum.getDescByCode(streetLampType);
            codeList.add(descByCode);
            return codeList;
        }

        for (String code : data.keySet()) {
            boolean status = code.contains("Alarm");
            //获取异常事件的字段
            if (status || "fault".equals(code)) {
                //判断状态是否是异常
                if (HuaweiAlarmStateEnum.STATE_ABNORMAL.code.equals(String.valueOf(data.get(code)))) {
                    codeList.add(code);
                }

            }
        }
        return codeList;
    }
}

