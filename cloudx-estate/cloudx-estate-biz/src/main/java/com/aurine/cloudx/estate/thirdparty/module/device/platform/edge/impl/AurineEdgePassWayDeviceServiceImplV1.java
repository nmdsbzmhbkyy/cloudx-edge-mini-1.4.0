package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.estate.cert.controller.CertAdownController;
import com.aurine.cloudx.estate.cert.remote.RemoteCertAdownService;
import com.aurine.cloudx.estate.cert.vo.CertAdownRequestVO;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonnelTypeOfDeviceEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceGatePassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.AurineEdgeDeviceCertVo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door.DoorAccessObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.door.DoorKeyObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.message.NoticeMessageInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.util.AurineEdgeDeviceDataUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums.MsgTypeEnum;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.ShortUUIDUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 冠林边缘网关 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class AurineEdgePassWayDeviceServiceImplV1 implements PassWayDeviceService {
    @Value("${server.base-uri}")
    private String baseUriPath;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectFingerprintsService projectFingerprintsService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectPasswdService projectPasswdService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private EdgeCascadeRequestMasterService edgeCascadeRequestMasterService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    @Resource
//    private RemoteCertAdownService remoteCertAdownService;

    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private OpenApiMessageService openApiMessageService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private CertAdownController certAdownController;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectBlacklistAttrService projectBlacklistAttrService;

    // 用于存储因为半夜重启需要过五分钟再下发的介质
    private Set<JsonNode> certNoSet = new HashSet();

    /**
     * 开门
     *
     * @param deviceId
     * @return
     */
    @Override
    public boolean openDoor(String deviceId) {
        return this.openDoor(deviceId, null, null);
    }

    @Override
    public boolean remoteOpenDoor(String deviceId, RemoteOpenDoorResultModel resultModel,String msgId) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);

        AurineEdgeRespondDTO aurineEdgeRespondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
                getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_CONTROL.code, AurineEdgeCommandConstant.REMOTE_OPEN_DOOR, JSONObject.parseObject(JSONObject.toJSONString(resultModel)), null, msgId);
//        return openDoor(deviceId,null,null,AurineEdgeCommandConstant.REMOTE_OPEN_DOOR,EventDeviceGatePassConstant.ACTION_REMOTE_VALID_WITH_PERSON,AurineOpenModeEnum.QRCODE_OPEN_MODE,JSONObject.parseObject(JSONObject.toJSONString(resultModel)),msgId);
        return aurineEdgeRespondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS;
    }

    @Override
    public boolean setZnSupperPasscode(ProjectDeviceInfo deviceInfo, SetSupperPasscodeVo vo, String msgId) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceInfo.getDeviceId(), AurineEdgeConfigDTO.class);
        AurineEdgeRespondDTO aurineEdgeRespond = AurineEdgeRemoteDeviceOperateServiceFactory.
                getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_INFO.code, AurineEdgeCommandConstant.SET_SUPPER_PASSCODE, JSONObject.parseObject(JSONObject.toJSONString(vo)), null, msgId);
        log.info("【智能多门门禁】超级密码设置响应：{}",aurineEdgeRespond);
        return aurineEdgeRespond.getErrorEnum()== AurineEdgeErrorEnum.SUCCESS;
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
        return openDoor(deviceId, personId, personType, AurineEdgeCommandConstant.OPEN_DOOR, EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON, AurineOpenModeEnum.LONG_RANGE_OPEN_MODE, null, null);
    }


    private boolean openDoor(String deviceId, String personId, String personType, String command, String action, AurineOpenModeEnum openMode, JSONObject paramsJson, String msgId) {
        //冠林边缘网关的开门业务处理
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
            AurineEdgeRespondDTO aurineEdgeRespondDTO;
            //调用对接业务
            if(StringUtils.isNotBlank(msgId)){
                aurineEdgeRespondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
                        getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_CONTROL.code, command, paramsJson, null,msgId);
            }else{
                aurineEdgeRespondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
                        getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_CONTROL.code, command, paramsJson, null);
            }

/*            // 如果是级联项目设备这里跳过事件记录流程
            Integer originProjectId = edgeCascadeRequestMasterService.getOriginProjectId();
            if (!originProjectId.equals(deviceInfo.getProjectId())) {
                return true;
            }*/
//            return AurineEdgeRespondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS;
            /**
             * 远程开门发送指令后，就记录开门者信息
             * @since:2021-02-22
             */
            if (aurineEdgeRespondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {

                EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();

//                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON);
                eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.name());
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setDesc("使用远程开门");
                eventDeviceGatePassDTO.setEventTime(LocalDateTime.now());
                eventDeviceGatePassDTO.setThirdPartyCode(deviceInfo.getThirdpartyCode());
                if (StringUtils.isNotEmpty(personId) && StringUtils.isNotEmpty(personType)) {
                    eventDeviceGatePassDTO.setAction(action);
                    eventDeviceGatePassDTO.setPersonCode(personId);
                    eventDeviceGatePassDTO.setPersonType(personType);
                } else {
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE);
                }
                eventDeviceGatePassDTO.setOpenMode(openMode.openMode);
                sendMsg(TopicConstant.SDI_EVENT_DEVICE_GATE_PASS, eventDeviceGatePassDTO);

                return true;
            } else {
                return false;
            }
        } else {
            throw new RuntimeException("设备 " + deviceId + " 缺失对接参数");
        }
    }
    /**
     * 添加通行凭证
     *
     * @param certList
     * @return
     */
    @Override
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
    public void delCert(List<ProjectRightDevice> certList) {
        this.doCert(certList, "del");
    }

    /**
     * 添加或删除通行凭证
     *
     * @param certList
     * @param type
     */
    private boolean doCert(List<ProjectRightDevice> certList, String type) {
        // doCertAnchorCard doCertAnchorFace
        if (CollUtil.isEmpty(certList)) {
            return true;
        }

        //初始化
        ProjectDeviceInfoProxyVo deviceInfo;
        //获取对接配置
        AurineEdgeConfigDTO config;

        // List<ProjectFingerprints> fingerList = new ArrayList<>();
        List<ProjectFaceResources> faceList = new ArrayList<>();
        //List<ProjectPasswd> passwordList = new ArrayList<>();
        List<ProjectCard> cardList = new ArrayList<>();

        //List<String> fingerIdsList = new ArrayList<>();
        List<String> faceIdList = new ArrayList<>();
        //List<String> passwordIdList = new ArrayList<>();
        List<String> cardIdList = new ArrayList<>();

        JSONArray paramsJsonArray = null;

        /**
         * 4类通行凭证列表 矩阵
         *
         * 不同类型通行凭证需要分页传输，每次最多100条数据
         *
         * 指纹列表1  指纹列表2
         * 面部识别1
         * 卡片列表1  卡片列表2     卡片列表3     卡片列表4
         */
//        List<List<DoorKeyObj>[]> doorKeyVoListMatrix;
        Map<AurineEdgeServiceEnum, List<DoorKeyObj>[]> doorKeyVoListMatrix = null;
        List<DoorKeyObj>[] doorKeyVoArrayList = null;
        List<String> doorKeyIdList = null;
        List<DoorKeyObj> doorKeyVoList;
        DoorKeyObj doorKeyVo;

        //將列表按照设备ID的不同，进行拆分
        Set<String> deviceIdSet = certList.stream().map(ProjectRightDevice::getDeviceId).collect(Collectors.toSet());


        //业务数据按照设备id分组对接
        List<List<ProjectRightDevice>> deviceCertListList = new ArrayList<>();
        for (String deviceId : deviceIdSet) {
            deviceCertListList.add(certList.stream().filter(e -> e.getDeviceId().equals(deviceId)).collect(Collectors.toList()));
        }


        /***********按设备ID分组***********/
        for (List<ProjectRightDevice> deviceCertList : deviceCertListList) {
            if (CollUtil.isEmpty(deviceCertList)) {
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
            if (StringUtils.isEmpty(deviceInfo.getThirdpartyCode())) {
                log.error("[冠林边缘网关] 设备{} 无第三方id，不执行下发操作.   {}", deviceInfo.getDeviceName(), deviceInfo);
                continue;
            }

            config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), AurineEdgeConfigDTO.class);

            doorKeyVoListMatrix = new HashMap<>();

            //List<ProjectRightDevice> fingerCertList = new ArrayList<>();
            List<ProjectRightDevice> faceCertList = new ArrayList<>();
            List<ProjectRightDevice> cardCertList = new ArrayList<>();
            //人脸黑名单List
            List<ProjectRightDevice> blacklistFaceCertList = new ArrayList<>();

            // List<ProjectRightDevice> passwordCertList = new ArrayList<>();

            //根据类型分组凭证，形成矩阵行
            for (ProjectRightDevice cert : deviceCertList) {
                if (cert.getCertMedia().equals(CertmediaTypeEnum.Face.code)) {
                    faceIdList.add(cert.getCertMediaId());
                    faceCertList.add(cert);
                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Card.code)) {
                    cardIdList.add(cert.getCertMediaId());
                    cardCertList.add(cert);
//                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Finger.code)) {
//                    fingerIdsList.add(cert.getCertMediaId());
//                    fingerCertList.add(cert);
//                } else if (cert.getCertMedia().equals(CertmediaTypeEnum.Password.code)) {
//                    passwordIdList.add(cert.getCertMediaId());
//                    passwordCertList.add(cert);
                }else if (cert.getCertMedia().equals(CertmediaTypeEnum.Blacklist_Face.code)) {
                    blacklistFaceCertList.add(cert);
                }
            }


            //fingerList = CollUtil.isNotEmpty(fingerIdsList) ? projectFingerprintsService.listByIds(fingerIdsList) : null;
            faceList = CollUtil.isNotEmpty(faceIdList) ? projectFaceResourcesService.listByIds(faceIdList) : null;
            // passwordList = CollUtil.isNotEmpty(passwordIdList) ? projectPasswdService.listByIds(passwordIdList) : null;
            cardList = CollUtil.isNotEmpty(cardIdList) ? projectCardService.listByIds(cardIdList) : null;

            //卡片
            if (CollUtil.isNotEmpty(cardCertList)) {
                doorKeyVoList = new ArrayList<>();

//                for (ProjectCard card : cardList) {
//                    doorKeyVo = new DoorKeyObj();
//                    doorKeyVo.setPassNo(card.getCardNo());
//                    doorKeyVo.setKeyid(card.getCardId());
//                    doorKeyVoList.add(doorKeyVo);
//                }

                for (ProjectRightDevice card : cardCertList) {
                    String houseCode = "";
                    List<String> houseCodeList = new ArrayList<>();
                    if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
                        houseCodeList = projectRightDeviceService.getHouseCodeByUnit(card.getCertMediaId(), deviceInfo.getDeviceId());
                    } else {
                        houseCodeList = projectRightDeviceService.getHouseCode(card.getCertMediaId());
                    }
                    if (CollUtil.isNotEmpty(houseCodeList)) {
                        houseCode = houseCodeList.get(0);
                    } else {
                        houseCode = "0";
                    }
                    doorKeyVo = new DoorKeyObj();
                    doorKeyVo.setPassNo(card.getCertMediaInfo());
                    doorKeyVo.setKeyId(card.getCertMediaId());
                    doorKeyVo.setRoomNo(houseCode);
                    doorKeyVo.setPeriod(getPeriod(card.getPersonId(), card.getPersonType()));
                    if (StrUtil.isNotBlank(doorKeyVo.getPeriod())) {
                        doorKeyVo.setLifecycle("-4");
                    }
                    doorKeyVo.setAttri(PersonnelTypeOfDeviceEnum.getCodeByPersonTypeCode(card.getPersonType()));
                    doorKeyVoList.add(doorKeyVo);
                }

                //矩阵列
                doorKeyVoListMatrix.put(AurineEdgeServiceEnum.CERT_CARD, splitAurineEdgeCertList(doorKeyVoList, config));
            }

            //密码
//            if (CollUtil.isNotEmpty(passwordList)) {
//                doorKeyVoList = new ArrayList<>();
//
//                for (ProjectPasswd password : passwordList) {
//                    doorKeyVo = new DoorKeyObj();
//                    doorKeyVo.setPassNo(password.getPasswd());
//                    doorKeyVo.setKeyId(password.getPassId());
////                    doorKeyVo.setUid(password.getPassId());
//                    doorKeyVo.setPeriod(getPeriod(password.getPersonId(), password.getPersonType()));
//                    if(StrUtil.isNotBlank(doorKeyVo.getPeriod())){
//                        doorKeyVo.setLifecycle("-4");
//                    }
//                    doorKeyVoList.add(doorKeyVo);
//                }
//
//                //矩阵列
//                doorKeyVoListMatrix.put(AurineEdgeServiceEnum.CERT_PASSWORD, splitAurineEdgeCertList(doorKeyVoList, config));
//            }

            //面部
            if (CollUtil.isNotEmpty(faceCertList)) {
                doorKeyVoList = new ArrayList<>();
                String houseCode = "";
                Date date = new Date();
//                for (ProjectFaceResources face : faceList) {
//                    List<String> houseCodeList = projectRightDeviceService.getHouseCode(face.getFaceId());
//                    if (CollUtil.isNotEmpty(houseCodeList)) {
//                        houseCode = houseCodeList.get(0);
//                    } else {
//                        houseCode = "0";
//                    }
//                    doorKeyVo = new DoorKeyObj();
//                    /**
//                     * 如果面部信息中已经存在第三方编码，则直接获取，不再生成
//                     * 新生成的第三方编码，需要保存到所有的face\rightDevice表记录中
//                     * 中台3.0 passNo规则修改，传输的passNo改为 per-框架号-面部seq 其中框架号约束最高18位，总长度最高32位
//                     *
//                     * @auther: 王伟
//                     * @since:2021-04-23 10:04
//                     */
//
//                    // 这里截取到尽可能长的uuid
////                    String uuid = face.getFaceId()
////                            .substring(0, 32 - (config.getFaceUuidPre().length() - 4 + houseCode.length() + 10));
//                    String passNo;
//                    if (StringUtil.isEmpty(face.getFaceCode())) {
////                        String uuid = String.valueOf(face.getSeq());
//                        String uuid = ShortUUIDUtil.shortUUID();
//                        passNo = String.format(config.getFaceUuidPre(), houseCode + "0", uuid);
//                    } else {
//                        passNo = face.getFaceCode();
//                    }
//
//                    doorKeyVo.setPassNo(passNo);
//                    doorKeyVo.setKeyid(face.getFaceId());
//                    doorKeyVo.setExturl(baseUriPath + face.getPicUrl());
//                    doorKeyVoList.add(doorKeyVo);
//
//                }
                for (ProjectRightDevice face : faceCertList) {
                    List<String> houseCodeList = new ArrayList<>();
                    if (deviceInfo.getDeviceType().equals(DeviceTypeEnum.LADDER_WAY_DEVICE.getCode())) {
                        houseCodeList = projectRightDeviceService.getHouseCodeByUnit(face.getCertMediaId(), deviceInfo.getDeviceId());
                    } else {
                        houseCodeList = projectRightDeviceService.getHouseCode(face.getCertMediaId());
                    }
                    if (CollUtil.isNotEmpty(houseCodeList)) {
                        houseCode = houseCodeList.get(0);
                    } else {
                        houseCode = "0";
                    }
                    doorKeyVo = new DoorKeyObj();
                    /**
                     * 如果面部信息中已经存在第三方编码，则直接获取，不再生成
                     * 新生成的第三方编码，需要保存到所有的face\rightDevice表记录中
                     * 中台3.0 passNo规则修改，传输的passNo改为 per-框架号-面部seq 其中框架号约束最高18位，总长度最高32位
                     *
                     * @auther: 王伟
                     * @since:2021-04-23 10:04
                     */

                    String passNo;
                    if (StringUtil.isEmpty(face.getCertMediaCode())) {
//                        String uuid = String.valueOf(face.getSeq());
                        String uuid = ShortUUIDUtil.shortUUID();
                        passNo = String.format(config.getFaceUuidPre(), houseCode + "0", uuid);
                    } else {
                        passNo = face.getCertMediaCode();
                    }

                    doorKeyVo.setPassNo(passNo);
                    doorKeyVo.setKeyId(face.getCertMediaId());
                    doorKeyVo.setExtUrl(baseUriPath + face.getCertMediaInfo());
                    doorKeyVo.setPeriod(getPeriod(face.getPersonId(), face.getPersonType()));
                    if (StrUtil.isNotBlank(doorKeyVo.getPeriod())) {
                        doorKeyVo.setLifecycle("-4");
                    }
                    doorKeyVo.setAttri(PersonnelTypeOfDeviceEnum.getCodeByPersonTypeCode(face.getPersonType()));
                    doorKeyVoList.add(doorKeyVo);

                }

                //矩阵列
                doorKeyVoListMatrix.put(AurineEdgeServiceEnum.CERT_FACE, splitAurineEdgeFaceCertList(doorKeyVoList, config));
            }


            // 人脸黑名单
            if (CollUtil.isNotEmpty(blacklistFaceCertList)) {
                // 获取项目全局设置黑名单是否可以通行
                ProjectConfig projectConfig = projectConfigService.getConfig();


                doorKeyVoList = new ArrayList<>();
                for (ProjectRightDevice blacklistFace : blacklistFaceCertList) {
                    doorKeyVo = new DoorKeyObj();
                    String passNo;
                    if (StringUtil.isEmpty(blacklistFace.getCertMediaCode())) {
                        // 获取第三方传入时的faceId
                        ProjectBlacklistAttr attr =  projectBlacklistAttrService.getByFaceId(blacklistFace.getCertMediaId());
                        // 传10代表是黑名单
                        String uuid = ShortUUIDUtil.shortUUID();
                        passNo = String.format(config.getFaceUuidPre(), PersonTypeEnum.BLACKLIST.code, uuid);
                    } else {
                        passNo = blacklistFace.getCertMediaCode();
                    }

                    doorKeyVo.setPassNo(passNo);
                    doorKeyVo.setKeyId(blacklistFace.getCertMediaId());
                    doorKeyVo.setExtUrl(baseUriPath + blacklistFace.getCertMediaInfo());
                    // 根据全局配置的是否允许黑名单开门设值
                    if (projectConfig.getBlackOpen() == 0) {
                        doorKeyVo.setAttri(PersonnelTypeOfDeviceEnum.getCodeByPersonTypeCode(PersonTypeEnum.BLACKLIST.code));
                    } else if (projectConfig.getBlackOpen() == 1) {
                        // 跟中台对接规则，最高位1控制是否开门，其余位为人员类型
                        int i = 1;
                        i = i << 31;
                        Integer blacklistCode = PersonnelTypeOfDeviceEnum.getCodeByPersonTypeCode(PersonTypeEnum.BLACKLIST.code);
                        if (blacklistCode!=null) {
                            // blacklistCode肯定小于32，所以高位与地位高位不会发生改变
                            Integer attri = i|blacklistCode;
                            doorKeyVo.setAttri(attri);
                        }
                    }
                    doorKeyVoList.add(doorKeyVo);
                }
                //矩阵列
                doorKeyVoListMatrix.put(AurineEdgeServiceEnum.CERT_FACE, splitAurineEdgeFaceCertList(doorKeyVoList, config));
            }

            //迭代矩阵
            /***********按通行凭证类型 以及 分页进行分组***********/

            // doCertAnchorCard
            //卡片分页处理（同步）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(AurineEdgeServiceEnum.CERT_CARD);
            if (ArrayUtil.isNotEmpty(doorKeyVoArrayList)) {
                log.info("[边缘网关-通行权限] 操作类型：{} 卡片介质列表：{}", type, JSON.toJSONString(doorKeyVoArrayList));
                for (int i = 0; i < doorKeyVoArrayList.length; i++) {

                    //生成回传数据，处理传输数据
                    doorKeyIdList = doorKeyVoArrayList[i].stream().map(e -> e.getKeyId()).collect(Collectors.toList());
                    doorKeyVoArrayList[i].forEach(e -> e.setKeyId(null));

                    if ("add".equals(type)) {
                        //新增下发
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        DoorAccessObj paramJsonObj = new DoorAccessObj();
                        paramJsonObj.setDoorAccess(doorKeyVoArrayList[i]);
//                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_CARD.code, AurineEdgeActionConstant.ADD, paramsJsonArray, doorKeyIdList);
                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_CARD.code, AurineEdgeActionConstant.ADD,
                                JSONObject.parseObject(JSONObject.toJSONString(paramJsonObj)), doorKeyIdList);
                    } else {
                        //删除
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        DoorAccessObj paramJsonObj = new DoorAccessObj();
                        paramJsonObj.setDoorAccess(doorKeyVoArrayList[i]);
//                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_CARD.code, AurineEdgeActionConstant.DELETE, paramsJsonArray, doorKeyIdList);
                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_CARD.code, AurineEdgeActionConstant.DELETE, JSONObject.parseObject(JSONObject.toJSONString(paramJsonObj)), doorKeyIdList);
                    }

                }
            }

            // doCertAnchorFace
            //人脸分页处理（异步）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(AurineEdgeServiceEnum.CERT_FACE);
            if (ArrayUtil.isNotEmpty(doorKeyVoArrayList)) {
                log.info("[边缘网关-通行权限] 操作类型：{} 人脸介质列表：{}", type, JSON.toJSONString(doorKeyVoArrayList));
                for (int i = 0; i < doorKeyVoArrayList.length; i++) {

                    //生成回传数据，处理传输数据
                    doorKeyIdList = doorKeyVoArrayList[i].stream().map(e -> e.getKeyId()).collect(Collectors.toList());
//                    doorKeyVoArrayList[i].forEach(e -> e.setKeyid(null));
                    if ("add".equals(type)) {
                        //生成请求
//                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        DoorAccessObj paramJsonObj = new DoorAccessObj();
                        paramJsonObj.setDoorAccess(doorKeyVoArrayList[i]);

//                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_FACE.code, AurineEdgeActionConstant.ADD, paramsJsonArray, doorKeyIdList);
                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_FACE.code, AurineEdgeActionConstant.ADD, JSONObject.parseObject(JSONObject.toJSONString(paramJsonObj)), doorKeyIdList);
                    } else {
                        //生成请求
                        DoorAccessObj paramJsonObj = new DoorAccessObj();
                        paramJsonObj.setDoorAccess(doorKeyVoArrayList[i]);

                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
//                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_FACE.code, AurineEdgeActionConstant.DELETE, paramsJsonArray, doorKeyIdList);
                        this.sendCert(config, deviceInfo, AurineEdgeServiceEnum.CERT_FACE.code, AurineEdgeActionConstant.DELETE, JSONObject.parseObject(JSONObject.toJSONString(paramJsonObj)), doorKeyIdList);

                    }
                }
            }


           /* //密码分页处理（未对接）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(AurineEdgeServiceEnum.CERT_PASSWORD);
            if (ArrayUtil.isNotEmpty(doorKeyVoArrayList)) {
                for (int i = 0; i < doorKeyVoArrayList.length; i++) {

                    //生成回传数据，处理传输数据
                    doorKeyIdList = doorKeyVoArrayList[i].stream().map(e -> e.getKeyid()).collect(Collectors.toList());
                    doorKeyVoArrayList[i].forEach(e -> e.setKeyid(null));

                    if (type.equals("add")) {
                        //生成请求

                        //调用

                        //生成返回消息

                    } else {

                    }

                }
            }*/
        }

        /*fixedThreadPool.shutdown();
        // 这里等待所有线程任务执行完成
        while (true) {
            if (fixedThreadPool.isTerminated()) {
                break;
            }
        }
*/
        return true;
    }

    /**
     * 根据人员获取凭证访问周期
     *
     * @return
     */
    private String getPeriod(String personId, String personType) {
        String period = "";
        if (StrUtil.isBlank(personId) || StrUtil.isBlank(personType)) {
            return period;
        }
        //当前支持访客周期
        if (StrUtil.equals(personType, PersonTypeEnum.VISITOR.code)) {
            ProjectVisitorHis projectVisitorHis = this.projectVisitorHisService.getById(personId);
            if (projectVisitorHis != null) {
                return projectVisitorHis.getPeriod();
            }
        }
        return period;
    }
    /**
     * <p>
     * 同步方式发送命令下发请求
     * </p>
     *
     * @param action          下发行为：新增、删除
     * @param certType        介质类型：人脸、门禁卡
     * @param doorKeyIdList   本次下发介质ID列表
     * @param paramsJsonArray 本次下发json数组（原来的接口都是这么调用的直接套过来了）
     * @author: 王良俊
     */
    public void sendCert(AurineEdgeConfigDTO config, ProjectDeviceInfo deviceInfo, String certType, String action, JSONObject paramsJsonArray, List<String> doorKeyIdList) {
        List<CertAdownRequestVO> certAdownRequestVOList = new ArrayList<>();
        if (certType.equals(CertmediaTypeEnum.Face.code) || certType.equals(CertmediaTypeEnum.Blacklist_Face.code)) {
            for (int i = 0; i < doorKeyIdList.size(); i++) {
                List<String> list = new ArrayList<>();
                list.add(doorKeyIdList.get(i));
                AurineEdgeDeviceCertVo aurineEdgeDeviceCertVo = new AurineEdgeDeviceCertVo();
                aurineEdgeDeviceCertVo.setConfig(config);
                aurineEdgeDeviceCertVo.setDeviceInfo(deviceInfo);
                aurineEdgeDeviceCertVo.setCertType(certType);
                aurineEdgeDeviceCertVo.setAction(action);

                List doorAccess = paramsJsonArray.getObject("doorAccess", List.class);
                DoorKeyObj doorKeyObj = JSONObject.parseObject(JSONObject.toJSONString(doorAccess.get(i)), DoorKeyObj.class);

                DoorAccessObj newDoorAccess = new DoorAccessObj();
                List<DoorKeyObj> newDoorKeyObjList = new ArrayList<>();
                newDoorKeyObjList.add(doorKeyObj);
                newDoorAccess.setDoorAccess(newDoorKeyObjList);
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(newDoorAccess));

                aurineEdgeDeviceCertVo.setParamsJsonArray(jsonObject);
                aurineEdgeDeviceCertVo.setDoorKeyIdList(list);
                aurineEdgeDeviceCertVo.setPriotity(3);
                //发送到下发服务
                CertAdownRequestVO certAdownRequestVO = AurineEdgeDeviceDataUtil.toVO(aurineEdgeDeviceCertVo);
                certAdownRequestVOList.add(certAdownRequestVO);
            }
        } else {
            AurineEdgeDeviceCertVo aurineEdgeDeviceCertVo = new AurineEdgeDeviceCertVo();
            aurineEdgeDeviceCertVo.setConfig(config);
            aurineEdgeDeviceCertVo.setDeviceInfo(deviceInfo);
            aurineEdgeDeviceCertVo.setCertType(certType);
            aurineEdgeDeviceCertVo.setAction(action);
            aurineEdgeDeviceCertVo.setPriotity(3);
            aurineEdgeDeviceCertVo.setParamsJsonArray(paramsJsonArray);
            aurineEdgeDeviceCertVo.setDoorKeyIdList(doorKeyIdList);
            //发送到下发服务
            CertAdownRequestVO certAdownRequestVO = AurineEdgeDeviceDataUtil.toVO(aurineEdgeDeviceCertVo);
            certAdownRequestVOList.add(certAdownRequestVO);
        }

        boolean bool = projectInfoService.checkProjectIdIsOriginProject(deviceInfo.getProjectId());
        //正常下发
        if (bool) {
            certAdownController.requestList(certAdownRequestVOList);
            log.info("[冠林边缘网关] 发送至下发服务：{}", JSONObject.toJSONString(certAdownRequestVOList));
        } else {
            //调用openApi接口传输
            String projectUUID = projectInfoService.getProjectUUID(deviceInfo.getProjectId());
            //传输对象
            OpenApiEntity openApiEntity = new OpenApiEntity();
            openApiEntity.setTenantId(1);
            openApiEntity.setProjectUUID(projectUUID);
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
            openApiEntity.setServiceName(certType.equals(AurineEdgeServiceEnum.CERT_CARD.code) ? OpenApiServiceNameEnum.CARD_INFO.name : OpenApiServiceNameEnum.FACE_INFO.name);
            openApiEntity.setCommandType(action.equals(AurineEdgeActionConstant.ADD) ? OpenApiCommandTypeEnum.DOWN.name : OpenApiCommandTypeEnum.DELETE.name);

            JSONObject data = new JSONObject();
            data.put("certData", certAdownRequestVOList);
            openApiEntity.setData(data);
            log.info("传输数据：{}", JSONObject.toJSONString(openApiEntity));
            openApiMessageService.sendMessageByServiceType(openApiEntity);
        }



       /* AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer())
                .objectManage(config, deviceInfo.getThirdpartyCode(), certType, action, "DoorKeyObj", paramsJsonArray, null);
        AurineEdgeFaceResultCodeEnum certStatusByResult = AurineEdgeFaceResultCodeEnum.getCertStatusByResult(respondDTO.getErrorEnum().code);
        log.info("[冠林边缘网关] {}介质id：{}；介质下发或删除结果：{}；设备端同步返回结果code：{}；", certType, paramsJsonArray.toString(), certStatusByResult.desc, certStatusByResult.resultCode);
        if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
            if (AurineEdgeFaceResultCodeEnum.RESTART_IN_THE_MIDDLE_OF_THE_NIGHT.resultCode.equals(certStatusByResult.resultCode)) {
                // 这里使用时间轮进行延时重下发
                TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
                    this.sendCert(config, deviceInfo, certType, action, paramsJsonArray, doorKeyIdList);
                }, 5, TimeUnit.MINUTES);
            }
        } else {
            // 人脸是否下发成功需要等设备回调，但是如果是失败的话要直接更新，这里跳过成功状态
            if (AurineEdgeServiceEnum.CERT_FACE.code.equals(certType) && certStatusByResult.cetDownloadStatus.equals(PassRightCertDownloadStatusEnum.SUCCESS.code)) {
                return;
            }
            this.sendCardToMq(deviceInfo, doorKeyIdList, certStatusByResult.cetDownloadStatus);
        }*/
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
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);

        if (AurineEdgeServiceEnum.getByCloudCode(certmediaTypeEnum.code) == null) {
            throw new RuntimeException("不支持清空该类型凭证");
        }

        String command = "";
        switch (certmediaTypeEnum) {
            case Face:
                command = AurineEdgeCommandConstant.FACE_CLEAN;
                break;

            case Card:
                command = AurineEdgeCommandConstant.CARD_CLEAN;
                break;

            default:
                log.error("[冠林边缘网关] 不支持清空该凭证类型{}", certmediaTypeEnum);
                throw new RuntimeException("不支持清空该凭证类型");
        }


//        AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.getByCloudCode(certmediaTypeEnum.code).code, AurineEdgeActionConstant.CLEAN, "DoorKeyObj", new JSONArray(), null);
        AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.
                getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_CONTROL.code, command, null, null);

        if (respondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            return true;

        } else {

            return false;
        }
    }

    /**
     * 为梯口机、区口机发送消息
     * 这里要根据设备能力集进行判断
     * textNotice 文本公告
     * richTextNotice 富文本公告
     * mediaAdvert 支持媒体广告
     * <p>
     * #card#face#yuntalk#yuntel#yunMonitor#textNotice#richTextNotice#mediaAdvert
     *
     * @param deviceId
     * @param notice
     */
    @Override
    public boolean sendTextMessage(String deviceId, ProjectNotice notice) {
        ObjectMapper objectMapper = ObjectMapperUtil.instance();

        log.info("notice: {}", JSON.toJSONString(notice));
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        if (notice.getNoticeTitle().length() > 1024) {
            throw new RuntimeException("标题长度不应超过1024个字");
        } else if (notice.getContent().length() > 102400) {
            throw new RuntimeException("内容过长，不应超过102400个字");
        }

        NoticeMessageInfo infoObj = new NoticeMessageInfo(notice.getSeq(),
                MsgTypeEnum.getByContentType(notice.getContentType()), notice.getNoticeTitle(), notice.getContent());
        infoObj.setValidityDay(120);
        infoObj.setMsgType(null);
        infoObj.setMsgId(Integer.valueOf(ShortUUIDUtil.shortUUID()));
        infoObj.setMsgkind(DeviceTypeConstants.INDOOR_DEVICE.equals(deviceInfo.getDeviceType()) ? "1" : "2");
        infoObj.setTime(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))));

        AurineEdgeRespondDTO respondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer()).sendMessage(config, infoObj, deviceInfo.getThirdpartyCode());

        if (respondDTO.getErrorEnum() == AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            return true;

        } else {

            return false;
        }
        /*
        String deviceCapabilities = deviceInfo.getDeviceCapabilities();
        boolean send = StringUtil.isNotEmpty(deviceCapabilities) &&( (MsgTypeEnum.TEXT.contentType.equals(notice.getContentType()) && deviceCapabilities.contains("textNotice")) || (MsgTypeEnum.RICH_TEXT.contentType.equals(notice.getContentType()) && deviceCapabilities.contains("richTextNotice")) );
        if (send) {
            HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).sendMessage(config, infoObj, deviceInfo.getThirdpartyCode());
        } else {
            log.warn("设备不支持进行此类型的通知，设备ID：{} 设备能力集：{} 通知：{}", deviceInfo.getDeviceId(), deviceCapabilities, JSON.toJSONString(notice));
        }
*/

    }

    @Override
    public void cleanTextMessage(String deviceId) {
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, AurineEdgeConfigDTO.class);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);

        JSONObject paramJson = new JSONObject();
        paramJson.put("devId", deviceInfo.getThirdpartyCode());

        AurineEdgeRespondDTO AurineEdgeRespondDTO = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer())
                .commandsDown(config, deviceInfo.getThirdpartyCode(), AurineEdgeServiceEnum.DEVICE_CONTROL.code, AurineEdgeCommandConstant.INFO_CLEAN, paramJson, null);

//        AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(getVer()).cleanMessage(config, deviceInfo.getThirdpartyCode());
    }

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
     * 获取版本
     *
     * @return
     */
    public VersionEnum getVer() {
        return VersionEnum.V1;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    @Override
    public String getPlatform() {
        return PlatformEnum.AURINE_EDGE_MIDDLE.code;
    }

    /**
     * 拆分通行凭证列表为列表数组
     *
     * @param doorKeyVoList
     * @return
     */
    private List<DoorKeyObj>[] splitAurineEdgeCertList(List<DoorKeyObj> doorKeyVoList, AurineEdgeConfigDTO config) {

        int bufferSize = Integer.valueOf(config.getTransferBufferSize());//分页单页容量

        List<List<DoorKeyObj>> resultList = CollUtil.split(doorKeyVoList, bufferSize);
        return resultList.toArray(new List[resultList.size()]);
    }

    private List<DoorKeyObj>[] splitAurineEdgeFaceCertList(List<DoorKeyObj> doorKeyVoList, AurineEdgeConfigDTO config) {
        List<List<DoorKeyObj>> resultList = CollUtil.split(doorKeyVoList, 1);
        return resultList.toArray(new List[resultList.size()]);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        log.info("[冠林边缘网关] 发送信息到kafka：{}", message);
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }


    /**
     * 将卡片结果数据发送给统一接口服务，进行后续处理
     *
     * @param deviceInfo
     * @param certIdList
     * @param resultStatus
     */
    public void sendCardToMq(ProjectDeviceInfo deviceInfo, List<String> certIdList, String resultStatus) {


        if (CollUtil.isNotEmpty(certIdList)) {
            List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getDeviceId, deviceInfo.getDeviceId())
                    .in(ProjectRightDevice::getCertMediaId, certIdList));
            try {
                log.info("发送前查询project_right_device：{}", ObjectMapperUtil.instance().writeValueAsString(rightDeviceList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.value);
            responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS_LIST);
            responseOperateDTO.setDeviceInfo(deviceInfo);
            responseOperateDTO.setRightDeviceList(rightDeviceList);
            responseOperateDTO.setRespondStatus(resultStatus);

            this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);

        }

    }

}
