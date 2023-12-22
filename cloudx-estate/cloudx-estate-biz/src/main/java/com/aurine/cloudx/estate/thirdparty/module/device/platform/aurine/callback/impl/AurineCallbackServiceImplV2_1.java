package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.callback.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
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
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.callback.AurineCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineRequestObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle.AurineCertSendHandleV2_1;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventTypeMapEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.config.WR20ConfigProperties;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
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
public class AurineCallbackServiceImplV2_1 implements AurineCallbackService {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    @Resource
    private WR20ConfigProperties wr20ConfigProperties;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;

    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;

    @Resource
    private AurineCertSendHandleV2_1 aurineCertSendHandleV2_1;

    @Resource
    private ProjectBuildingInfoService projectBuildingInfoService;
    @Resource
    private ProjectNoticeService projectNoticeService;
    @Resource
    private ProjectNoticeDeviceService projectNoticeDeviceService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 获取版本
     *
     * @return
     */
    @Override
    public String getVersion() {
        return VersionEnum.V2_1.code;
    }


    /**
     * 设备状态变化通知 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceStatusUpdate(JSONObject requestJson) {

        String accessToken = requestJson.getString("accessToken");
        String projectId = requestJson.getString("communityId");

        String time = requestJson.getString("time");
        LocalDateTime eventDate = DateUtil.parseLocalDateTime(time, "yyyy-MM-dd HH:mm:ss");//事件时间

        String eventType = requestJson.getString("msgType");//：1、门禁事件

        /**
         * 事件代码 0、异步通知； 1、开门事件； 2、门禁异常事件； 3、门禁告警事件； 4、通话记录上报事件； 5、安防告警事件； 6、门状态事件； 7、设备运行状态
         */
        String eventCode = requestJson.getString("eventCode");
        JSONObject eventData = requestJson.getJSONObject("eventData");


        String deviceSn = eventData.getString("devsn");
        String devCode = eventData.getString("devcode");
        String status = eventData.getString("online");

        if ("0".equals(status)) {//offline
            status = DeviceStatusEnum.OFFLINE.code;
        } else if ("1".equals(status)) {
            /**
             * 当设备重启上线后，发送设备号码信息，同步更新对应楼栋、房屋框架号
             *
             * @since 2021 02 01
             */
            ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(deviceSn);
            if (deviceInfo == null) {
                log.error("[冠林中台] 设备：{} 更新框架号失败，未找到该设备", deviceSn);
            } else {
                ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode()) && StringUtils.isNotEmpty(devCode)) {
                    log.info("[冠林中台]  设备返回第三方编号{}，开始对楼栋单元房屋进行添加运算", devCode);
                    projectBuildingInfoService.addThirdCode(deviceInfo, devCode);
                }
            }


            status = DeviceStatusEnum.ONLINE.code;
        }

        EventDeviceNoticeDTO noticeDTO = new EventDeviceNoticeDTO();
        noticeDTO.setAction(EventDeviceNoticeConstant.ACTION_DEVICE_STATUS_UPDATE);
        noticeDTO.setCompany(PlatformEnum.AURINE_MIDDLE.name());
        noticeDTO.setStatus(status);
        noticeDTO.setDeviceSn(deviceSn);

        sendMsg(TopicConstant.SDI_EVENT_DEVICE_NOTICE, noticeDTO);
    }


    /**
     * 设备操作指令的响应 数据清洗
     *
     * @param requestJson
     */
    @Override
    public void deviceCommandResponse(JSONObject requestJson) {

        /**
         * {
         *     "eventCode": "0",
         *     "eventData": {
         *         "devsn": "PROQK20M0802905XQX0V",
         *         "errorcode": "0",
         *         "modelid": "020302",
         *         "msgid": "1597835726771"
         *     },
         *     "eventType": "1",
         *     "time": "2020-08-19 19:15:26",
         *     "accessToken": "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk",
         *     "communityId": "S1000000272"
         * }
         */

        String eventCode = requestJson.getString("eventCode");
        String communityId = requestJson.getString("communityId");
        JSONObject eventData = requestJson.getJSONObject("eventData");

        String msgId = AurineConstant.MESSAGE_ID_PRE + eventData.getString("msgid");
        String noticMsgId = AurineConstant.MESSAGE_NOTICE_ID_PRE + eventData.getString("msgid");
        String openDoorMsgId = AurineConstant.MESSAGE_OPEN_DOOR_ID_PRE + eventData.getString("msgid");
        String errorCode = eventData.getString("errorcode");
        String devSn = eventData.getString("devsn");
        boolean delFlag = false;

        //查询缓存是否存在对应指令数据

        if (RedisUtil.hasKey(msgId)) {

            //如果返回的异常数据需要继续重新下发，则直接跳出当前操作，等待线程重新下发
            if (!AurineErrorCodeEnum.getByCode(errorCode).submitFlag) {
                log.info("[冠林中台] 当前回调结果为 {} ，系统不予处理，等待自动重试", AurineErrorCodeEnum.getByCode(errorCode).desc);
                return;
            }

            String redisData = (String) RedisUtil.get(msgId);

            log.info("操作指令响应 异步回调 msgId {} 接收成功", msgId);

            //目前中台2.0只能支持单一凭证下发
            List<AurineCertVo> aurineCertVoList = JSONObject.parseArray(redisData, AurineCertVo.class);
            AurineCertVo aurineCertVo = aurineCertVoList.get(0);

            //获取设备
            ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(devSn);
            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());

            String uid = aurineCertVo.getUid();

            //Test
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //通过设备、通行凭证ID，获取到关联表信息
            List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getDeviceId, deviceInfo.getDeviceId())
                    .eq(ProjectRightDevice::getCertMediaId, uid));

            //获取到对应的下载状态
            if (CollUtil.isNotEmpty(rightDeviceList)) {
                ProjectRightDevice rightDevice = rightDeviceList.get(0);

                //如果是正在下载，成功为下载完成，失败为下载失败
                if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code)) {
                    if ("0".equals(errorCode)) {
                        log.info("[冠林中台] 异步响应处理 msgId= {} 状态为：下载成功", msgId);
                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.SUCCESS.code);

                        /**
                         * @since 2020-09-14
                         * @author: 王伟
                         * 下载成功，且来源为APP或微信，调用接口检查是否全部成功，并执行相关业务：全部成功且存在旧的来自同一个平台的非WEB源面部信息，则删除旧的面部信息
                         */
                        if (rightDevice.getCertMedia().equals(CertmediaTypeEnum.Face.code)) {

                            //检查是否为来自微信端数据
                            ProjectContextHolder.setProjectId(deviceInfo.getProjectId());
                            projectFaceResourcesService.removeOldAppFace(rightDevice.getCertMediaId());
                            log.info("[冠林中台] 异步响应处理 msgId= {} 处理旧面部信息流程完成", msgId);
                            ProjectContextHolder.clear();
                        }


                    } else {
                        if (AurineErrorCodeEnum.getByCode(errorCode) == AurineErrorCodeEnum.FULL) {
                            rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
                        } else {
                            rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FAIL.code);
                        }
                        rightDevice.setErrMsg(errorCode + " " + AurineErrorCodeEnum.getByCode(errorCode).desc);
                        log.info("[冠林中台] 异步响应处理 msgId= {} 状态为：下载失败，原因为：{}", msgId, AurineErrorCodeEnum.getByCode(errorCode).desc);
//                        projectFaceResourcesService.sendFailNotice(rightDevice.getCertMediaId());
                        projectFaceResourcesService.sendNotice(rightDevice.getCertMediaId(), false);

                    }

                    //如果是正在删除，成功直接删除，失败为删除失败
                } else if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.DELETING.code)) {
                    if ("0".equals(errorCode)) {
                        log.info("[冠林中台] 异步响应处理 msgId= {} 状态为：删除成功", msgId);
                        delFlag = true;
                    } else {
                        log.info("[冠林中台] 异步响应处理 msgId= {} 状态为：删除失败 ,{}", msgId, requestJson);
//                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FAIL.code);
                    }
                } else if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.FREEZING.code)) {
                    if ("0".equals(errorCode)) {
                        log.info("[冠林中台] 异步响应处理 msgId= {}  状态为：停用成功", msgId);
                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZE.code);
                    } else {
                        log.info("[冠林中台] 异步响应处理 msgId= {} 停用失败,变更状态为已停用 {}", msgId, requestJson);
                        //下载失败的数据，继续删除有时候会返回失败状态
                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FREEZE.code);
//                        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.FAIL.code);
                    }
                }

                //更新信息
                if (delFlag) {
                    projectRightDeviceService.removeById(rightDevice.getSeq());
                    log.info("[冠林中台] 异步响应处理 msgId= {} 删除完成", msgId);
                } else {
                    projectRightDeviceService.updateById(rightDevice);
                    log.info("[冠林中台] 异步响应处理 msgId= {} 状态修改完成", msgId);
                }


            } else {
                log.error("通行凭证数据不存在，SN={},DeviceId={}，DeviceName={}, JSON={}", devSn, deviceInfo.getDeviceId(), deviceInfo.getDeviceName(), aurineCertVoList.toString());
            }


            /**
             * 响应消费完成后，根据sn，写入完成标识
             *
             * @author: 王伟
             * @since 2020-12-07 11:18
             */
            RedisUtil.lSet(AurineCacheConstant.AURINE_CERT_DONE_LIST, AurineCacheConstant.AURINE_CERT_DONE_STR_PER + devSn, 3000);


            //处理完后清空缓存
            if (StringUtil.isNotEmpty(msgId)) {
                RedisUtil.del(msgId);
            }


        } else if (RedisUtil.hasKey(noticMsgId)) {//处理下发信息数据


            /**
             * {
             *     "eventCode":"0",
             *     "eventData":{
             *         "devsn":"PYOQS276080299I9MK5U",
             *         "errorcode":"0",
             *         "modelid":"020302",
             *         "msgid":"1612256520718"
             *     },
             *     "eventType":"1",
             *     "time":"2021-02-02 17:02:01",
             *     "accessToken":"5yaUYyvtcgowZoXcuVE58aWxXio64Lo9",
             *     "communityId":"S1000000513"
             * }
             */
            String redisData = (String) RedisUtil.get(noticMsgId);

            log.info("操作指令响应 异步回调 msgId {} 接收成功", msgId);
            //目前中台2.0只能支持单一凭证下发
            ProjectNotice notice = JSONObject.parseObject(redisData, ProjectNotice.class);
            ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByDeviceSn(devSn);

            if (deviceInfo != null && notice != null) {
                List<ProjectNoticeDevice> noticeDevicelist = projectNoticeDeviceService.list(new QueryWrapper<ProjectNoticeDevice>().lambda()
                        .eq(ProjectNoticeDevice::getNoticeId, notice.getNoticeId())
                        .eq(ProjectNoticeDevice::getDeviceId, deviceInfo.getDeviceId()));

                if (CollUtil.isNotEmpty(noticeDevicelist)) {
                    ProjectNoticeDevice noticeDevice = noticeDevicelist.get(0);
                    if (AurineErrorCodeEnum.getByCode(errorCode) == AurineErrorCodeEnum.SUCCESS) {
                        log.info("[冠林中台] 下发信息到设备成功 {} ，", AurineErrorCodeEnum.getByCode(errorCode).desc);
                        noticeDevice.setDlStatus("1");//已下载
                    } else {
                        log.info("[冠林中台] 下发信息到设备失败 {} ，", AurineErrorCodeEnum.getByCode(errorCode).desc);
                        noticeDevice.setDlStatus("2");//失败
                    }

                    projectNoticeDeviceService.updateById(noticeDevice);
                }
            }

        } else if (RedisUtil.hasKey(openDoorMsgId)) {
            //开门指令匹配
            /**
             * {
             *     "eventCode": "0",
             *     "eventData": {
             *         "devsn": "PYOQS276080299I9MK5U",
             *         "errorcode": "0",
             *         "modelid": "020302",
             *         "msgid": "1613786557439"
             *     },
             *     "eventType": "1",
             *     "time": "2021-02-20 10:02:37",
             *     "accessToken": "iT7MO9NaFteEYNsh3xKTFbwnW6MBU9q1",
             *     "communityId": "S1000000696"
             * }
             */

            JSONObject redisDataObj = (JSONObject) RedisUtil.get(openDoorMsgId);
//            String redisData = (String) RedisUtil.get(openDoorMsgId);
//            AurineRequestObject requestObject = JSONObject.parseObject(redisData, AurineRequestObject.class);
//            String personId = requestObject.getRequestObjectJson().getString("personId");
//            String personType = requestObject.getRequestObjectJson().getString("personType");
            String personId = redisDataObj.getString("personId");
            String personType = redisDataObj.getString("personType");

            //收到开门指令回调，发送开门指令
            if (StringUtils.isNotEmpty(personType)) {
                //添加开门指令事件

                EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();

//                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON);
                eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.name());
                eventDeviceGatePassDTO.setDeviceSn(devSn);

                String eventTime = requestJson.getString("time");//事件时间
                LocalDateTime eventDate = DateUtil.parseLocalDateTime(eventTime, "yyyy-MM-dd HH:mm:ss");//事件时间
                eventDeviceGatePassDTO.setEventTime(eventDate);

                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setDesc("使用远程开门");
                if (StringUtils.isNotEmpty(personId) && StringUtils.isNotEmpty(personType)) {
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON);
                    eventDeviceGatePassDTO.setPersonCode(personId);
                    eventDeviceGatePassDTO.setPersonType(personType);
                } else {
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE);
                }

                sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);
            }


        } else {
            log.info("操作指令响应 异步回调 msgId {} 不存在", msgId);
        }

    }

    /**
     * 设备事件上报 数据清洗
     * 调用 设备事件通知 主题
     *
     * @param requestJson
     */
    @Override
    public void deviceEventReport(JSONObject requestJson) {

        /**
         * {
         *     "eventCode": "1",
         *     "eventData": {
         *         "devsn": "PROQK20M0802905XQX0V",
         *         "enterType": "2",
         *         "keyid": "",
         *         "keytype": "1",
         *         "keyvalue": "6621733",
         *         "modelid": "020302"
         *     },
         *     "eventType": "1",
         *     "time": "2020-08-19 17:48:03",
         *     "accessToken": "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk",
         *     "communityId": "S1000000272"
         * }
         */

        String eventCode = requestJson.getString("eventCode");
        String communityId = requestJson.getString("communityId");
        Integer projectId = Integer.valueOf(communityId.substring(1));
        ProjectContextHolder.setProjectId(projectId);
        JSONObject eventData = requestJson.getJSONObject("eventData");
        String eventTime = requestJson.getString("time");//事件时间
        LocalDateTime eventDate = DateUtil.parseLocalDateTime(eventTime, "yyyy-MM-dd HH:mm:ss");//事件时间

        String eventContent = "";


        if (eventCode.equals(AurineEventEnum.OPEN_DOOR.code)) {//开门事件

            String devSn = eventData.getString("devsn");
            String enterType = eventData.getString("enterType");
            String keyType = eventData.getString("keytype");
            String keyValue = eventData.getString("keyvalue");
            String keyId = eventData.getString("keyid");
            String extValue = "";//面部识别图片

            EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
            switch (enterType) {
                case "2":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_CAED);
                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Card.code);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventContent = "使用卡片开门，卡号：" + keyValue;
                    break;
                case "1":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_PASSWORD);
                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Password.code);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventContent = "使用密码开门";
                    break;
                case "4":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_FACE);
                    eventDeviceGatePassDTO.setCertMediaType(CertmediaTypeEnum.Face.code);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventContent = "使用面部识别开门";
                    break;
                case "20":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventContent = "使用二维码开门";
                    eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);
                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(keyValue, " 住户"));
                    break;
                case "21":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventContent = "使用授权二维码开门";
                    break;
                case "22":
                    //访客二维码（区分有被访人，无被访人）
                    //找不到房间号
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_QR_CODE_WITH_PERSON);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    extValue = eventData.getString("extValue");
                    eventDeviceGatePassDTO.setImgUrl(extValue);
                    eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.VISITOR.code);
                    eventContent = "使用临时二维码开门";
                    eventDeviceGatePassDTO.setPersonName(this.getHouseName(keyValue, " 访客"));
                    break;
                case "11":
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE);
                    eventDeviceGatePassDTO.setCertMediaType(null);
                    eventContent = "使用远程开门";
                    /**
                     * 20210220 忽略远程开门回调事件，因为无法获取到开门人信息
                     */
                    return;
                default:
//                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.AC);
                    break;
            }

            eventDeviceGatePassDTO.setDesc(eventContent);
//            eventDeviceGatePassDTO.setPersonType(PersonTypeEnum.PROPRIETOR.code);//目前中台、WR20只开放了住户对接接口，无员工、访客信息
            eventDeviceGatePassDTO.setDeviceSn(devSn);
            eventDeviceGatePassDTO.setEventTime(eventDate);
            eventDeviceGatePassDTO.setCertMediaCode(keyId);
//            //华为云 冠林大门区口机演示用映射（因为该设备并不在4.0平台管控内，社区ID在系统里将查询不到）
//            if (projectId == 37) projectId = 1000000292;

            eventDeviceGatePassDTO.setProjectId(projectId);
            eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.name());
            eventDeviceGatePassDTO.setJsonObject(requestJson);
            eventDeviceGatePassDTO.setImgUrl(this.changeImgUrl(eventDeviceGatePassDTO.getImgUrl()));
            sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);


        } else if (eventCode.equals(AurineEventEnum.DOOR_ERROR.code)) {//异常事件

            String code = eventData.getString("code");
            String devsn = eventData.getString("devsn");
            String value = eventData.getString("value");//卡号
            String extValue = eventData.getString("extValue");
            ;//面部识别图片

            switch (code) {

                case "2":
                    eventContent = AurineEventErrorEnum.ERR_CARD.desc + ":" + value;
                    break;
                case "3":
                    eventContent = AurineEventErrorEnum.ERR_QR.desc;
                    break;
                case "4":
                    eventContent = AurineEventErrorEnum.OUT_DATE_QR.desc;
                    break;
                case "5":
                    eventContent = AurineEventErrorEnum.STRANGER_STAY.desc;
                    break;
                case "100":
                default:
                    eventContent = "异常操作";
                    break;
            }

            /**
             * 异常通行改为通行记录
             */

            EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();
            eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_ERROR_PASS);
            eventDeviceGatePassDTO.setCertMediaType(null);
            extValue = eventData.getString("extValue");
//            eventDeviceGatePassDTO.setImgUrl(extValue);


//            EventWarningErrorDTO eventDeviceGatePassDTO = new EventWarningErrorDTO();
//            eventDeviceGatePassDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
            eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.value);
            eventDeviceGatePassDTO.setJsonObject(requestJson);

            eventDeviceGatePassDTO.setEventTime(eventDate);
            eventDeviceGatePassDTO.setDesc(eventContent);
            eventDeviceGatePassDTO.setDeviceSn(devsn);
//            eventDeviceGatePassDTO.setImgUrl(extValue);
            eventDeviceGatePassDTO.setImgUrl(this.changeImgUrl(extValue));

//            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventDeviceGatePassDTO);
            sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);


        } else if (eventCode.equals(AurineEventEnum.DOOR_WARNING.code)) {//门禁警告

            String code = eventData.getString("code");
            String devsn = eventData.getString("devsn");
            String value = eventData.getString("value");//卡号
            String extValue = eventData.getString("extValue");
            ;//面部识别图片


            //事件代码
            //1、挟持开门；
            //2、3次密码错误告警；
            //3、长时间逗留报警；
            //4、3次刷卡错误告警；
            //5、3次指纹错误告警；
            //10、强行开门报警；
            //11、门未关报警；
            //12、防拆报警；
            //20、黑名单告警；

//            switch (code) {
//
//                case "1":
//                    eventContent = "挟持开门";
//                    break;
//                case "2":
//                    eventContent = "3次密码错误告警";
//                    break;
//                case "3":
//                    eventContent = "长时间逗留报警";
//                    break;
//                case "4":
//                    eventContent = "3次刷卡错误告警" + value;
//                    break;
//                case "5":
//                    eventContent = "3次指纹错误告警";
//                    break;
//                case "10":
//                    eventContent = "强行开门报警";
//                    break;
//                case "11":
//                    eventContent = "门未关报警";
//                    break;
//                case "12":
//                    eventContent = "防拆报警";
//                    break;
//                case "20":
//                    eventContent = "黑名单告警";
//                    break;
//                default:
//                    eventContent="未知告警";
//                    break;
//            }

            eventContent = AurineAlarmEnum.getByCode(code).desc;

            EventWarningErrorDTO eventDeviceGatePassDTO = new EventWarningErrorDTO();
            eventDeviceGatePassDTO.setAction(EventWarningErrorConstant.ACTION_ERROR);
            eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.value);
            eventDeviceGatePassDTO.setJsonObject(requestJson);

            eventDeviceGatePassDTO.setEventTime(eventDate);
            eventDeviceGatePassDTO.setDesc(eventContent);
            eventDeviceGatePassDTO.setDeviceSn(devsn);
//            eventDeviceGatePassDTO.setImgUrl(extValue);
            eventDeviceGatePassDTO.setImgUrl(this.changeImgUrl(extValue));

            ProjectDeviceInfoProxyVo byDeviceSn = projectDeviceInfoProxyService.getByDeviceSn(devsn);
            if (byDeviceSn == null){
                log.error("无sn码对应的设备");
                return;
            }else {
                EventTypeEnum cloudEnum = AurineEventTypeMapEnum.getCloudEnum(code,DeviceTypeEnum.getByCode(byDeviceSn.getDeviceType()));
                eventDeviceGatePassDTO.setEventTypeId(cloudEnum.eventTypeId);
            }

            sendMsg(TopicConstant.SDI_EVENT_WARNING_ERROR, eventDeviceGatePassDTO);
        }
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
     * 将2.0的url地址进行专访，并访问，并获取新的图片地址
     *
     * @param imgUrl
     */
    private String changeImgUrl(String imgUrl) {
        String resultUrl = imgUrl;
        if (StringUtils.isNotEmpty(imgUrl) && (imgUrl.indexOf("http://139.9.139.196:7000/aid/") >= 0 || imgUrl.indexOf("http://icloudobs.aurine.cn:7000/aid/") >= 0)) {
            imgUrl = imgUrl.replace("http://139.9.139.196:7000/aid/", "https://icloudobs.aurine.cn:7003/aid/");
            imgUrl = imgUrl.replace("http://icloudobs.aurine.cn:7000/aid/", "https://icloudobs.aurine.cn:7003/aid/");
            try {
                // 创建一个URL对象
                URL targetUrl = new URL(imgUrl);
                // 从URL对象中获得一个连接对象
                HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
                // 设置请求方式 注意这里的POST或者GET必须大写
                conn.setRequestMethod("GET");
                // 设置POST请求是有请求体的
                conn.setDoOutput(false);
                resultUrl = conn.getHeaderField("Location");// 从请求头获取跳转的链接地址
                resultUrl = resultUrl.replace("http://139.9.139.196:7000/", "https://icloudobs.aurine.cn:7003/");
                resultUrl = resultUrl.replace("http://icloudobs.aurine.cn:7000/", "https://icloudobs.aurine.cn:7003/");
            } catch (Exception e) {
                log.error("[冠林中台] 图片转存失败");
                e.printStackTrace();
            }
        } else if (StringUtils.isNotEmpty(imgUrl) && imgUrl.indexOf("http://icloudobs.aurine.cn:7000/") >= 0) {
            resultUrl = resultUrl.replace("http://icloudobs.aurine.cn:7000/", "https://icloudobs.aurine.cn:7003/");
        }
        return resultUrl;
    }


    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        log.info("冠林中台 回调 向系统MQ主题： {} 发送信息： {}", topic, JSONObject.toJSONString(message));
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }


}
