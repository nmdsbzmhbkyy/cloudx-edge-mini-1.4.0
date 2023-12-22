package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.core.util.AurineUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineMessageTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineMessageDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.vo.AurineDeviceVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle.AurineCertHandle;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.remote.factory.AurineRemoteDeviceServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util.AurineCertHandleThreadUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 冠林中台 V1 版本 对接业务实现
 *
 * @ClassName: PassWayDeviceServiceImplByAurineV1
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-12 14:00
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class PassWayDeviceServiceImplByAurineV2_1 extends DeviceServiceImplByAurineV2_1 implements PassWayDeviceService {

    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private SysThirdPartyInterfaceConfigService sysThirdPartyInterfaceConfigService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectFingerprintsService projectFingerprintsService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectPasswdService projectPasswdService;
    @Resource
    private ProjectCardService projectCardService;

    @Lazy
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private AurineCertHandleThreadUtil aurineCertHandleThreadUtil;
    @Resource
    private AurineCertHandle aurineCertHandle;


    @Value("${server.base-uri}")
    private String baseUriPath;

    /**
     * 开门
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean openDoor(String deviceId) {

        return this.openDoor(deviceId, null, null);
//        //Aurine中台的开门业务处理
//        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
//
//        //获取对接配置
//        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineConfigDTO.class);
//
//        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
//            //调用对接业务
//            return AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).openDoor(config, deviceInfo.getSn());
//        } else {
//            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
//        }

    }

    @Override
    public boolean remoteOpenDoor(String deviceId, RemoteOpenDoorResultModel resultModel,String msgId) {
        return true;
    }

    @Override
    public boolean setZnSupperPasscode(ProjectDeviceInfo deviceInfo, SetSupperPasscodeVo vo, String msgId) {
        return false;
    }

    /**
     * 开门 带开门者信息
     *
     * @param deviceId   必填
     * @param personId   人员id
     * @param personType 人员类型
     * @return
     */
    @Override
    public boolean openDoor(String deviceId, String personId, String personType) {

        //Aurine中台的开门业务处理
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineConfigDTO.class);

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getSn())) {
            //调用对接业务
            return AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).openDoor(config, deviceInfo.getSn(), personId, personType);
        } else {
            throw new RuntimeException("设备 :" + deviceInfo.getDeviceName() + " 缺失SN参数");
        }
    }


    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    @Override
//    @Async
    public void addCert(List<ProjectRightDevice> certList) {
        this.doCert(certList, "add");
    }

    /**
     * 删除通行权限
     *
     * @param certList
     * @return
     */
    @Override
//    @Async
    public void delCert(List<ProjectRightDevice> certList) {
        this.doCert(certList, "del");
    }

    /**
     * 清空设备凭证
     *
     * @param deviceId          要清空的设备
     * @param certmediaTypeEnum 要清空的凭证类型枚举
     * @return
     */
    @Override
    public boolean clearCerts(String deviceId, CertmediaTypeEnum certmediaTypeEnum) {
        return projectRightDeviceService.clearCerts(deviceId, certmediaTypeEnum);
    }

    /**
     * 为梯口机、区口机发送消息
     *
     * @param notice
     */
    @Override
    public boolean sendTextMessage(String deviceId, ProjectNotice notice) {
        log.info("[冠林中台] 开始下发信息到门禁设备");
        //获取对接配置
        ProjectDeviceInfo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (deviceInfo == null) {
            log.error("[冠林中台] 下发信息到门禁失败，系统中未找到要下发的设备：{}", deviceId);
            throw new RuntimeException("未找到设备，请联系管理员");
        }

        if (notice.getNoticeTitle().length() > 20) {
            log.error("[冠林中台] 下发信息到门禁失败，由于标题超出20位长度限制：{}", notice);
            throw new RuntimeException("标题过长");
        }

        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineConfigDTO.class);

        List<AurineMessageDTO> messageList = new ArrayList<>();
        AurineMessageDTO messageDTO = new AurineMessageDTO();

//        messageDTO.setMsgid(notice.getSeq());

        messageDTO.setMsgid(Long.valueOf(notice.getNoticeId()));
        messageDTO.setContent(notice.getContent());
        messageDTO.setMsgtype(AurineMessageTypeEnum.getByCloudCode(notice.getContentType()).aurineMiddleCode);
        messageDTO.setTitle(notice.getNoticeTitle());
        messageDTO.setValidity_day(356 * 6);
        //获取13位时间戳
//        messageDTO.setTime(String.valueOf(new Date().getTime()));
        messageDTO.setTime(String.valueOf(System.currentTimeMillis()));

        messageList.add(messageDTO);
        JSONObject result = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).sendMessages(config, deviceInfo.getSn(), messageList);

        if (result != null) {
            String msgId = "AURINE_MIDDLE_REQUEST_NOTICE_" + result.getJSONObject("body").getString("msgid");
            RedisUtil.set(msgId, JSONObject.toJSONString(notice), AurineCacheConstant.AURINE_MESSAGE_CACHE_TTL);

        }
        return true;
    }

    @Override
    public void cleanTextMessage(String deviceId) {

    }

    /**
     * 订阅消息
     *
     * @return
     */
    @Override
    public boolean subscribe(String deviceType, int projectId, int tenantId) {
        log.info("开始订阅");
        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId, AurineConfigDTO.class);
        boolean result = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).subscribe(config);

        if (result) {
            SysThirdPartyInterfaceConfig sysThirdPartyInterfaceConfig = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId);
            sysThirdPartyInterfaceConfig.setSubscribeStatus("1");
            sysThirdPartyInterfaceConfigService.updateById(sysThirdPartyInterfaceConfig);
        }

        return true;
    }


    /***
     * 同步设备
     * @return
     */
    @Override
    public boolean syncDecvice(String deviceType, int projectId, int tenantId) {
        //获取对接配置
        AurineConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceType, projectId, tenantId, AurineConfigDTO.class);
        JSONObject result = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).syncDevice(config, 0, 0);
        return false;
    }

    /**
     * 添加或删除通行凭证
     *
     * @param certList
     * @param type
     */
    private boolean doCert(List<ProjectRightDevice> certList, String type) {
        if (CollUtil.isEmpty(certList)){
            return true;
        }

        //初始化
        ProjectDeviceInfoProxyVo deviceInfo;
        //获取对接配置
        AurineConfigDTO config;

        List<ProjectFingerprints> fingerList = new ArrayList<>();
        List<ProjectFaceResources> faceList = new ArrayList<>();
        List<ProjectPasswd> passwordList = new ArrayList<>();
        List<ProjectCard> cardList = new ArrayList<>();

        List<String> fingerIdsList = new ArrayList<>();
        List<String> faceIdList = new ArrayList<>();
        List<String> passwordIdList = new ArrayList<>();
        List<String> cardIdList = new ArrayList<>();

        /**
         * 4类通行凭证列表 矩阵
         *
         * 不同类型通行凭证需要分页传输，每次最多100条数据
         *
         * 指纹列表1  指纹列表2
         * 面部识别1
         * 卡片列表1  卡片列表2     卡片列表3     卡片列表4
         */
        List<List<AurineCertVo>[]> certVoListMatrix;
        List<AurineCertVo> certVoList;
        AurineCertVo certVo;

        //將列表按照设备ID的不同，进行拆分
        Set<String> deviceIdSet = certList.stream().map(e -> e.getDeviceId()).collect(Collectors.toSet());

        //业务数据按照设备id分组对接
        List<List<ProjectRightDevice>> deviceCertListList = new ArrayList<>();
        for (String deviceId : deviceIdSet) {
            deviceCertListList.add(certList.stream().filter(e -> e.getDeviceId().equals(deviceId)).collect(Collectors.toList()));
        }


        /***********按设备ID分组***********/
        for (List<ProjectRightDevice> deviceCertList : deviceCertListList) {
            if (CollUtil.isEmpty(deviceCertList)){
                continue;
            }

            deviceInfo = projectDeviceInfoProxyService.getVoById(deviceCertList.get(0).getDeviceId());
            /**
             * 解决数据异常问题（删除设备后未删除对应凭证导致）
             * @since 2020-09-03
             * @author: 王伟
             */
            if (deviceInfo == null) {
                continue;
            }

            config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), AurineConfigDTO.class);

            certVoListMatrix = new ArrayList<>();

            //根据类型分组凭证，形成矩阵行
            for (ProjectRightDevice cert : deviceCertList) {
                if (cert.getCertMedia().equals(CertmediaTypeEnum.Finger.code)) {
                    fingerIdsList.add(cert.getCertMediaId());
                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Face.code)) {
                    faceIdList.add(cert.getCertMediaId());
                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Card.code)) {
                    cardIdList.add(cert.getCertMediaId());
                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Password.code)) {
                    passwordIdList.add(cert.getCertMediaId());
                }
            }

            fingerList = CollUtil.isNotEmpty(fingerIdsList) ? projectFingerprintsService.listByIds(fingerIdsList) : null;
            faceList = CollUtil.isNotEmpty(faceIdList) ? projectFaceResourcesService.listByIds(faceIdList) : null;
            passwordList = CollUtil.isNotEmpty(passwordIdList) ? projectPasswdService.listByIds(passwordIdList) : null;
            cardList = CollUtil.isNotEmpty(cardIdList) ? projectCardService.listByIds(cardIdList) : null;

            //卡片
            if (CollUtil.isNotEmpty(cardList)) {
                certVoList = new ArrayList<>();

                for (ProjectCard card : cardList) {
                    certVo = new AurineCertVo();
                    certVo.setKeytype("1");
                    certVo.setKeyvalue(card.getCardNo());
                    certVo.setKeyid(card.getCardCode());
                    certVo.setUid(card.getCardId());

                    certVo.setRoomcode(getRoomNo(card.getPersonId(), card.getPersonType(), deviceInfo));

                    certVoList.add(certVo);
                }

                //矩阵列
                certVoListMatrix.add(splitAurineCertList(certVoList, config));
            }

            //密码
            if (CollUtil.isNotEmpty(passwordList)) {
                certVoList = new ArrayList<>();

                for (ProjectPasswd password : passwordList) {
                    certVo = new AurineCertVo();
                    certVo.setKeytype("2");
                    certVo.setKeyvalue(password.getPasswd());
                    certVo.setKeyid(password.getPassCode());
                    certVo.setUid(password.getPassId());
                    certVoList.add(certVo);
                }

                //矩阵列
                certVoListMatrix.add(splitAurineCertList(certVoList, config));
            }

            //面部
            if (CollUtil.isNotEmpty(faceList)) {
                certVoList = new ArrayList<>();

                for (ProjectFaceResources face : faceList) {
                    certVo = new AurineCertVo();
                    certVo.setKeytype("3");
                    certVo.setKeyvalue(baseUriPath + face.getPicUrl());
//                    certVo.setKeyvalue( face.getPicUrl());

                    certVo.setRoomcode(getRoomNo(face.getPersonId(), face.getPersonType(), deviceInfo));

                    certVo.setKeyid(face.getFaceCode());

                    /**
                     * 由于结构改造，删除凭证情况下，处理有可能出现设备无法关联的情况
                     * @since 2021-04-12
                     */
                    if (StringUtils.isEmpty(face.getFaceCode())) {
                        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, face.getFaceId()));
                        if (CollUtil.isNotEmpty(rightDeviceList)) {
                            certVo.setKeyid(rightDeviceList.get(0).getCertMediaCode());
                        }
                    }

                    certVo.setUid(face.getFaceId());
                    certVoList.add(certVo);
                }

                //矩阵列
                certVoListMatrix.add(splitAurineCertList(certVoList, config));
            }


            //调用对接业务
            AurineDeviceVo vo = AurineUtil.createDeviceVo(deviceInfo, "");
            long startTime;
            long endTime;
            //迭代矩阵
            /***********按通行凭证类型 以及 分页进行分组***********/
            for (List<AurineCertVo>[] certVoListArray : certVoListMatrix) {
                for (int i = 0; i < certVoListArray.length; i++) {
                    if ("add".equals(type)) {


//                        if (certVoListArray[i].get(0).getKeytype().equals("3")) {
                        /**
                         *   由于使用该中台的设备同时只能下发一个凭证，发送数据时，将要发送的数据根据设备ID,分主题发送到MQ，转为同步消费
                         * @author: 王伟
                         * @since：2020-11-06 11:47
                         */
                        AurineCertSendKafkaDTO aurineCertSendKafkaDTO = new AurineCertSendKafkaDTO(config, vo, certVoListArray[i], "add");
                        this.sendMsg(vo.getDevsn(), config, aurineCertSendKafkaDTO);

//                        this.sendMsg(AurineTopicUtil.getTopicByDeviceSn(vo.getDevsn()), aurineCertSendKafkaDTO);
//                        } else {
//                            JSONObject resultJson = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).addCert(config, vo, certVoListArray[i]);
//                            String asyncMsgId = this.resetThirdCodeToCert(resultJson, certVoListArray[i]);//获取到异步的消息ID
//                        }

//
//
//                        JSONObject resultJson = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).addCert(config, vo, certVoListArray[i]);
//                        String asyncMsgId = this.resetThirdCodeToCert(resultJson, certVoListArray[i]);//获取到异步的消息ID
//
//                        /**
//                         如果是面部凭证，异步需要改为同步。通过阻塞，等待同步接口返回msgId被异步回调消息消费后（设备返回操作接口），在发送下一条记录。
//                         * @since 2020-10-20 15:17
//                         * @author: 王伟
//                         */
//                        if (certVoListArray[i].get(0).getKeytype().equals("3")) {
//                            log.info("第{}条人脸：{} 开始等待消费", i + 1, asyncMsgId);
//                            startTime = System.currentTimeMillis();   //获取开始时间
//
//                            //当异步id被消费，或者已过期（硬件没有响应），再发送下一条数据到硬件
//                            while (RedisUtil.hasKey(asyncMsgId)) {
//                                try {
//                                    Thread.sleep(1000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            endTime = System.currentTimeMillis(); //获取结束时间
//                            log.info("第{}条 人脸：{} 异步消费成功，耗时{} ms", i + 1, asyncMsgId, endTime - startTime);
//
//                        }


                    } else {

                        /**
                         *  由于设备原因，将删除指令异步转同步
                         * @author: 王伟
                         * @since：2020-11-06 11:47
                         */
                        AurineCertSendKafkaDTO aurineCertSendKafkaDTO = new AurineCertSendKafkaDTO(config, vo, certVoListArray[i], "del");
                        this.sendMsg(vo.getDevsn(), config, aurineCertSendKafkaDTO);
//                        this.sendMsg(AurineTopicUtil.getTopicByDeviceSn(vo.getDevsn()), aurineCertSendKafkaDTO);
//
//                        JSONObject resultJson = AurineRemoteDeviceServiceFactory.getInstance(VersionEnum.V2_1).delCert(config, vo, certVoListArray[i]);
//                        this.cacheToRedis(resultJson, certVoListArray[i]);
//
//
//                        /**
//                         *验证删除时是否发生异常，如果返回 “对象不存在” 异常，直接删除凭证
//                         * 当前方法仅适用于中台2.0 一次只能调用一个凭证操作的情况
//                         * @author: 王伟
//                         * @since 2020-09-23
//                         */
//                        Object errorObj = resultJson.get("errorCode");
//                        if (errorObj != null && errorObj.toString().equals(AurineErrorEnum.WRONG_OBJECT.code)) {
//                            //伪造json请求，调用回调，出触发删除业务
//
//                            /**
//                             * {
//                             *     "eventCode": "0",
//                             *     "eventData": {
//                             *         "devsn": "PROQK20M0802905XQX0V",
//                             *         "errorcode": "0",
//                             *         "modelid": "020302",
//                             *         "msgid": "1597835726771"
//                             *     },
//                             *     "eventType": "1",
//                             *     "time": "2020-08-19 19:15:26",
//                             *     "accessToken": "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk",
//                             *     "communityId": "S1000000272"
//                             * }
//                             */
//                            JSONObject delRespJson = new JSONObject();
//                            delRespJson.put("eventCode", "0");
//                            delRespJson.put("eventType", "1");
//                            delRespJson.put("time", "2020-08-19 19:15:26");
//                            delRespJson.put("accessToken", "NfSgqZzdshTrrVLHKSfagy42bP2L2hFk");
//                            delRespJson.put("communityId", "S1000000272");
//
//                            JSONObject delRespDataJson = new JSONObject();
//                            delRespDataJson.put("devsn", resultJson.getString("devsn"));
//                            delRespDataJson.put("errorcode", "0");
//                            delRespDataJson.put("modelid", "020302");
//                            delRespDataJson.put("msgid", resultJson.getString("msgid"));
//
//                            delRespJson.put("eventData", delRespDataJson);
//
//                            aurineCallbackServiceImplV2_1.deviceCommandResponse(delRespJson);
//                        }
                    }

                }
            }
        }
        return true;
    }

    /**
     * 获取住户的房屋信息
     *
     * @return
     */
    private String getRoomNo(String personId, String personType, ProjectDeviceInfoProxyVo deviceInfoProxyVo) {
        String houseCode = "010101010";

        if (StringUtils.equals(personType, PersonTypeEnum.PROPRIETOR.code)) {

            //校验设备类型，如果是梯口机，获取楼栋数据
            boolean isLadder = StringUtils.equals(deviceInfoProxyVo.getDeviceType(), DeviceTypeEnum.LADDER_WAY_DEVICE.getCode());

            List<ProjectHouseDTO> houseList = projectHousePersonRelService.listHouseByPersonId(personId);
            if (CollUtil.isNotEmpty(houseList)) {
                for (ProjectHouseDTO houseDTO : houseList) {
                    if (StringUtils.isNotEmpty(houseDTO.getHouseCode())) {

                        //不匹配梯口机所在楼栋单元时,不予添加
                        if (isLadder) {
                            if (!StringUtils.equals(houseDTO.getUnitId(), deviceInfoProxyVo.getUnitId())) {
                                continue;
                            }
                        }

                        houseCode = houseDTO.getHouseCode()+"0";
                        break;
                    }


                }
            }
        }

        return houseCode;
    }

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
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_MIDDLE.code;
    }

    /**
     * 最大整除法
     * 341/100 = 4
     * 300/100 = 3
     * 299/100 = 3
     * 200/100 = 2
     *
     * @param value
     * @param divisor
     * @return
     */
    private int maxDivision(int value, int divisor) {
        if (divisor == 0){
            return 0;
        }

        if (value == 0){
            return 0;
        }

        int y = value / divisor;

        if (value % divisor >= 1) {
            y++;
        }

        return y;
    }

    /**
     * 拆分通行凭证列表为列表数组
     *
     * @param certVoList
     * @return
     */
    private List<AurineCertVo>[] splitAurineCertList(List<AurineCertVo> certVoList, AurineConfigDTO config) {

        int bufferSize = Integer.valueOf(config.getTransferBufferSize());//分页单页容量

        List<List<AurineCertVo>> resultList = CollUtil.split(certVoList, bufferSize);
        return resultList.toArray(new List[resultList.size()]);
    }

    /**
     * 将获取的第三方code设置到系统通行凭证中
     *
     * @param json
     * @param certVoList
     * @return
     */
    private String resetThirdCodeToCert(JSONObject json, List<AurineCertVo> certVoList) {
        if (json == null || CollUtil.isEmpty(certVoList)){
            return "";
        }

        /**
         *  "keys": [
         *             {
         *                 "keyvalue": "06621733",
         *                 "keyid": "1",
         *                 "keytype": "1"
         *             },
         *             {
         *                 "keyvalue": "11111111",
         *                 "keyid": "4",
         *                 "keytype": "1"
         *             }
         *         ],
         *         "msgid": "1597816041574"
         */

        JSONArray keyArray = json.getJSONArray("keys");
        String msgId = "AURINE_MIDDLE_REQUEST_" + json.getString("msgid");

        //因中台2.0机制，目前buffer只能为1

        //存入redis，等待异步回调处理
        RedisUtil.set(msgId, JSONObject.toJSONString(certVoList), 600);

        List<AurineCertVo> keyList = keyArray.toJavaList(AurineCertVo.class);

        //设置第三方key(确定与传入顺序完全一致)
        for (int i = 0; i < keyList.size(); i++) {
            certVoList.get(i).setKeyid(keyList.get(i).getKeyid());
        }

        //不确定传入顺序情况下，请启用下列代码段
//        for (AurineCertVo key : keyList) {
//            for (AurineCertVo orgKey : certVoList) {
//                if(orgKey.getKeyvalue().equals(key.getKeyvalue())){
//                    orgKey.setKeyid(key.getKeyid());
//                    break;
//                }
//            }
//        }

        //根据类型，批量修改通行凭证
        if ("1".equals(certVoList.get(0).getKeytype())) {//card

            ProjectCard card;
            List<ProjectCard> cardList = new ArrayList<>();
            for (AurineCertVo certVo : certVoList) {
                card = new ProjectCard();
                card.setCardId(certVo.getUid());
                card.setCardCode(certVo.getKeyid());
                cardList.add(card);
            }

//            return projectCardService.updateBatchById(cardList);
            projectCardService.updateBatchById(cardList);
            return msgId;

        } else if ("2".equals(certVoList.get(0).getKeytype())) {//password

            ProjectPasswd passwd;
            List<ProjectPasswd> passwdList = new ArrayList<>();
            for (AurineCertVo certVo : certVoList) {
                passwd = new ProjectPasswd();
                passwd.setPassId(certVo.getUid());
                passwd.setPassCode(certVo.getKeyid());
                passwdList.add(passwd);
            }

//            return projectPasswdService.updateBatchById(passwdList);
            projectPasswdService.updateBatchById(passwdList);
            return msgId;

        } else if ("3".equals(certVoList.get(0).getKeytype())) {//face

            ProjectFaceResources face;
            List<ProjectFaceResources> faceResourcesList = new ArrayList<>();
            for (AurineCertVo certVo : certVoList) {
                face = new ProjectFaceResources();
                face.setFaceId(certVo.getUid());
                face.setFaceCode(certVo.getKeyid());
                faceResourcesList.add(face);
            }

//            return projectFaceResourcesService.updateBatchById(faceResourcesList);

            projectFaceResourcesService.updateBatchById(faceResourcesList);
            return msgId;
        }
        return msgId;

    }

    /**
     * @param json
     * @param certVoList
     * @return
     */
    private boolean cacheToRedis(JSONObject json, List<AurineCertVo> certVoList) {
        if (json == null || CollUtil.isEmpty(certVoList)){
            return true;
        }


//        JSONArray keyArray = json.getJSONArray("keys");
        String msgId = json.getString("msgid");
        //因中台2.0机制，目前buffer只能为1
        //存入redis，等待异步回调处理
        RedisUtil.set("AURINE_MIDDLE_REQUEST_" + msgId, JSONObject.toJSONString(certVoList), 600);


        return true;
    }

    /**
     * 发送消息
     *
     * @param sn          主题
     * @param certSendDto 内容体
     */
    private void sendMsg(String sn, AurineConfigDTO aurineConfigDTO, AurineCertSendKafkaDTO certSendDto) {
//        //1、动态创建消费者
//        String topic = aurineConsumerThreadUtil.initConsumerThread(sn, aurineConfigDTO, this.aurineCertSendConsumer);
//        log.info("[冠林中台] 发送通行凭证指令到kafka： 主题:{} 内容：{}", topic, message);
//        //2、发送信息
//        kafkaTemplate.send(topic, JSONObject.toJSONString(message));

        /**
         * 发送信息到缓存队列
         */
        //检查队列是否存在数据
        long cacheListSize = RedisUtil.lGetListSize(AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn);
//        boolean startLock = RedisUtil.hasKey(AurineCacheConstant.AURINE_CERT_COMMAND_LOCK_PER + sn);
        log.info("[冠林中台] 准备发送通行凭证指令到队列： 队列长度{} ,列名:{} 内容：{}", cacheListSize, AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn, certSendDto);


        //将请求封装设置到缓存队列中
        RedisUtil.lSet(AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn, JSONObject.toJSONString(certSendDto), AurineCacheConstant.AURINE_CERT_COMMAND_TTL);

        //设置设备SN集合，用于处理线程遍历列表用
        RedisUtil.sSet(AurineCacheConstant.AURINE_CERT_DEVICE_SET, sn);

        aurineCertHandleThreadUtil.initThread(aurineConfigDTO, aurineCertHandle);
//
//        //无数据为初始状态，调用同步下发，开始队列消费，并右传值进入队列
//        if (cacheListSize == 0 && !startLock) {
//            //启动下发
//            RedisUtil.set(AurineCacheConstant.AURINE_CERT_COMMAND_LOCK_PER + sn, "1", AurineCacheConstant.AURINE_CERT_COMMAND_TTL);
//
//            if ("add".equalsIgnoreCase(certSendDto.getCommand())) {
//                log.info("[冠林中台] 执行凭证下发流程， 内容 ：{}", certSendDto);
//                aurineCertSendHandleV2_1.addCert(certSendDto.getConfigDTO(), certSendDto.getDeviceVo(), certSendDto.getCertList());
//            } else {
//                log.info("[冠林中台] 执行凭证删除流程， 内容 ：{}", certSendDto);
//                aurineCertSendHandleV2_1.delCert(certSendDto.getConfigDTO(), certSendDto.getDeviceVo(), certSendDto.getCertList());
//            }
//
//        } else {
//            //无需主动处理，添加到列表后等待回调函数处理即可。
//
//
//        }

    }
}
