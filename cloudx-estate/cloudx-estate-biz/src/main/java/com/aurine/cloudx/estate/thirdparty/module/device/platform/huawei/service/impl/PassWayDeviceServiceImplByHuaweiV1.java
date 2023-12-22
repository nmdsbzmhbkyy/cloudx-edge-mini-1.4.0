package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.EventDeviceGatePassConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.EventDeviceGatePassDTO;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PassWayDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config.HuaweiConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.core.util.HuaweiUtil;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.*;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.door.DoorKeyObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.message.InfoObj;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums.MsgTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.remote.factory.HuaweiRemoteDeviceOperateServiceFactory;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.ShortUUIDUtil;
import com.aurine.cloudx.estate.util.delay.DelayTaskUtil;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.zn.SetSupperPasscodeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 华为中台 V1 版本 对接业务实现
 *
 * @ClassName:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-31 13:44
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class PassWayDeviceServiceImplByHuaweiV1 implements PassWayDeviceService {
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
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


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
//        //华为中台的开门业务处理
//        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
//        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
//
//        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getDeviceCode())) {
//            //调用对接业务
//            HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.
//                    getInstance(getVer()).commandsDown(config, deviceInfo.getDeviceCode(), HuaweiServiceEnum.DEVICE_CONTROL.code, HuaweiCommandConstant.OPEN_DOOR, null, null);
//            return huaweiRespondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS;
//        } else {
//            throw new RuntimeException("设备 " + deviceId + " 缺失对接参数");
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
        //华为中台的开门业务处理
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);

        if (deviceInfo != null && StringUtil.isNotEmpty(deviceInfo.getThirdpartyCode())) {
            //调用对接业务
            HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.
                    getInstance(getVer()).commandsDown(config, deviceInfo.getThirdpartyCode(), HuaweiServiceEnum.DEVICE_CONTROL.code, HuaweiCommandConstant.OPEN_DOOR, null, null);

//            return huaweiRespondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS;
            /**
             * 远程开门发送指令后，就记录开门者信息
             * @since:2021-02-22
             */
            if (huaweiRespondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {

                EventDeviceGatePassDTO eventDeviceGatePassDTO = new EventDeviceGatePassDTO();

//                eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON);
                eventDeviceGatePassDTO.setCompany(PlatformEnum.AURINE_MIDDLE.name());
                eventDeviceGatePassDTO.setCertMediaType(null);
                eventDeviceGatePassDTO.setDesc("使用远程开门");
                eventDeviceGatePassDTO.setEventTime(LocalDateTime.now());
                eventDeviceGatePassDTO.setThirdPartyCode(deviceInfo.getThirdpartyCode());
                if (StringUtils.isNotEmpty(personId) && StringUtils.isNotEmpty(personType)) {
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE_WITH_PERSON);
                    eventDeviceGatePassDTO.setPersonCode(personId);
                    eventDeviceGatePassDTO.setPersonType(personType);
                } else {
                    eventDeviceGatePassDTO.setAction(EventDeviceGatePassConstant.ACTION_REMOTE);
                }

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
        if (CollUtil.isEmpty(certList)) {
            return true;
        }

        //初始化
        ProjectDeviceInfoProxyVo deviceInfo;
        //获取对接配置
        HuaweiConfigDTO config;

        List<ProjectFingerprints> fingerList = new ArrayList<>();
        List<ProjectFaceResources> faceList = new ArrayList<>();
        List<ProjectPasswd> passwordList = new ArrayList<>();
        List<ProjectCard> cardList = new ArrayList<>();

        List<String> fingerIdsList = new ArrayList<>();
        List<String> faceIdList = new ArrayList<>();
        List<String> passwordIdList = new ArrayList<>();
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
        Map<HuaweiServiceEnum, List<DoorKeyObj>[]> doorKeyVoListMatrix = null;
        List<DoorKeyObj>[] doorKeyVoArrayList = null;
        List<String> doorKeyIdList = null;
        List<DoorKeyObj> doorKeyVoList;
        DoorKeyObj doorKeyVo;

        //將列表按照设备ID的不同，进行拆分
        Set<String> deviceIdSet = certList.stream().map(e -> e.getDeviceId()).collect(Collectors.toSet());


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
                log.error("[华为中台] 设备{} 无第三方id，不执行下发操作.   {}", deviceInfo.getDeviceName(), deviceInfo);
                continue;
            }

            config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceType(deviceInfo.getDeviceType(), deviceInfo.getProjectId(), deviceInfo.getTenantId(), HuaweiConfigDTO.class);

            doorKeyVoListMatrix = new HashMap<>();

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
                doorKeyVoList = new ArrayList<>();

                for (ProjectCard card : cardList) {
                    doorKeyVo = new DoorKeyObj();
                    doorKeyVo.setPassNo(card.getCardNo());
                    doorKeyVo.setKeyid(card.getCardId());
                    doorKeyVoList.add(doorKeyVo);
                }

                //矩阵列
//                doorKeyVoListMatrix.add(splitHuaweiCertList(doorKeyVoList, config));
                doorKeyVoListMatrix.put(HuaweiServiceEnum.CERT_CARD, splitHuaweiCertList(doorKeyVoList, config));
            }

            //密码
            if (CollUtil.isNotEmpty(passwordList)) {
                doorKeyVoList = new ArrayList<>();

                for (ProjectPasswd password : passwordList) {
                    doorKeyVo = new DoorKeyObj();
                    doorKeyVo.setPassNo(password.getPasswd());
                    doorKeyVo.setKeyid(password.getPassId());
//                    doorKeyVo.setUid(password.getPassId());
                    doorKeyVoList.add(doorKeyVo);
                }

                //矩阵列
//                doorKeyVoListMatrix.add(splitHuaweiCertList(doorKeyVoList, config));
                doorKeyVoListMatrix.put(HuaweiServiceEnum.CERT_PASSWORD, splitHuaweiCertList(doorKeyVoList, config));
            }

            //面部
            if (CollUtil.isNotEmpty(faceList)) {
                doorKeyVoList = new ArrayList<>();
                String houseCode = "";
                Date date = new Date();
                for (ProjectFaceResources face : faceList) {
                    List<String> houseCodeList = projectRightDeviceService.getHouseCode(face.getFaceId());
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

                    // 这里截取到尽可能长的uuid
//                    String uuid = face.getFaceId()
//                            .substring(0, 32 - (config.getFaceUuidPre().length() - 4 + houseCode.length() + 10));
                    String passNo;
                    if (StringUtil.isEmpty(face.getFaceCode())) {
//                        String uuid = String.valueOf(face.getSeq());
                        String uuid = ShortUUIDUtil.shortUUID();
                        passNo = String.format(config.getFaceUuidPre(), houseCode, uuid);
                    } else {
                        passNo = face.getFaceCode();
                    }

                    doorKeyVo.setPassNo(passNo);
                    doorKeyVo.setKeyid(face.getFaceId());
                    doorKeyVo.setExturl(baseUriPath + face.getPicUrl());
                    doorKeyVoList.add(doorKeyVo);

                }

                //矩阵列
//                doorKeyVoListMatrix.add(splitHuaweiCertList(doorKeyVoList, config));
                doorKeyVoListMatrix.put(HuaweiServiceEnum.CERT_FACE, splitHuaweiCertList(doorKeyVoList, config));
            }

            //迭代矩阵
            /***********按通行凭证类型 以及 分页进行分组***********/

            //卡片分页处理（同步）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(HuaweiServiceEnum.CERT_CARD);
            if (ArrayUtil.isNotEmpty(doorKeyVoArrayList)) {
                for (int i = 0; i < doorKeyVoArrayList.length; i++) {

                    //生成回传数据，处理传输数据
                    doorKeyIdList = doorKeyVoArrayList[i].stream().map(e -> e.getKeyid()).collect(Collectors.toList());
                    doorKeyVoArrayList[i].forEach(e -> e.setKeyid(null));

                    if ("add".equals(type)) {
                        //新增下发
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        this.sendCertAsync(config, deviceInfo, HuaweiServiceEnum.CERT_CARD.code, HuaweiActionConstant.ADD, paramsJsonArray, doorKeyIdList);
                    } else {
                        //删除
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        this.sendCertAsync(config, deviceInfo, HuaweiServiceEnum.CERT_CARD.code, HuaweiActionConstant.DELETE, paramsJsonArray, doorKeyIdList);
                    }

                }
            }

            //人脸分页处理（异步）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(HuaweiServiceEnum.CERT_FACE);
            if (ArrayUtil.isNotEmpty(doorKeyVoArrayList)) {
                for (int i = 0; i < doorKeyVoArrayList.length; i++) {

                    //生成回传数据，处理传输数据
                    doorKeyIdList = doorKeyVoArrayList[i].stream().map(e -> e.getKeyid()).collect(Collectors.toList());
//                    doorKeyVoArrayList[i].forEach(e -> e.setKeyid(null));
                    if ("add".equals(type)) {
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        this.sendCertAsync(config, deviceInfo, HuaweiServiceEnum.CERT_FACE.code, HuaweiActionConstant.ADD, paramsJsonArray, doorKeyIdList);
                    } else {
                        //生成请求
                        paramsJsonArray = JSONArray.parseArray(JSONArray.toJSONString(doorKeyVoArrayList[i]));
                        this.sendCertAsync(config, deviceInfo, HuaweiServiceEnum.CERT_FACE.code, HuaweiActionConstant.DELETE, paramsJsonArray, doorKeyIdList);

                    }
                }
            }


           /* //密码分页处理（未对接）
            doorKeyVoArrayList = doorKeyVoListMatrix.get(HuaweiServiceEnum.CERT_PASSWORD);
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
     * <p>
     * 异步方式发送命令下发请求（因为可能会一直在重新下发命令浪费过多时间）
     * </p>
     *
     * @param action          下发行为：新增、删除
     * @param certType        介质类型：人脸、门禁卡
     * @param doorKeyIdList   本次下发介质ID列表
     * @param paramsJsonArray 本次下发json数组（原来的接口都是这么调用的直接套过来了）
     * @author: 王良俊
     */
    private void sendCertAsync(HuaweiConfigDTO config, ProjectDeviceInfo deviceInfo, String certType,
                               String action, JSONArray paramsJsonArray, List<String> doorKeyIdList) {

        threadPoolTaskExecutor.execute(() -> {
            this.sendCert(config, deviceInfo, certType, action, paramsJsonArray, doorKeyIdList);
        });
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
    private void sendCert(HuaweiConfigDTO config, ProjectDeviceInfo deviceInfo, String certType, String action, JSONArray paramsJsonArray, List<String> doorKeyIdList) {

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer())
                .objectManage(config, deviceInfo.getThirdpartyCode(), certType, action, "DoorKeyObj", paramsJsonArray, null);
        HuaweiFaceResultCodeEnum certStatusByResult = HuaweiFaceResultCodeEnum.getCertStatusByResult(respondDTO.getErrorEnum().code);
        log.info("[华为中台] {}介质id：{}；介质下发或删除结果：{}；设备端同步返回结果code：{}；", certType, paramsJsonArray.toString(), certStatusByResult.desc, certStatusByResult.resultCode);
        if (PassRightCertDownloadStatusEnum.RE_DOWNLOAD.code.equals(certStatusByResult.cetDownloadStatus)) {
            if (HuaweiFaceResultCodeEnum.RESTART_IN_THE_MIDDLE_OF_THE_NIGHT.resultCode.equals(certStatusByResult.resultCode)) {
                // 这里使用时间轮进行延时重下发
                TaskUtil.hashedWheelTimer.newTimeout(timeout -> {
                    this.sendCert(config, deviceInfo, certType, action, paramsJsonArray, doorKeyIdList);
                }, 5, TimeUnit.MINUTES);
            }
        } else {
            // 这里判断有问题，这里已经可以确定人脸下发失败了无需等待设备回调
            // 人脸是否下发成功需要等设备回调
//            if (!HuaweiServiceEnum.CERT_FACE.code.equals(certType)) {
            this.sendCardToMq(deviceInfo, doorKeyIdList, certStatusByResult.cetDownloadStatus);
//            }
        }
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
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);

        if (HuaweiServiceEnum.getByCloudCode(certmediaTypeEnum.code) == null) {
            throw new RuntimeException("不支持清空该类型凭证");
        }

        HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).objectManage(config, deviceInfo.getThirdpartyCode(), HuaweiServiceEnum.getByCloudCode(certmediaTypeEnum.code).code, HuaweiActionConstant.CLEAN, "DoorKeyObj", new JSONArray(), null);
        if (respondDTO.getErrorEnum() == HuaweiErrorEnum.SUCCESS) {
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

        log.info("notice: {}", JSON.toJSONString(notice));
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        String deviceCapabilities = deviceInfo.getDeviceCapabilities();
        boolean send = StringUtil.isNotEmpty(deviceCapabilities) && ((MsgTypeEnum.TEXT.contentType.equals(notice.getContentType()) && deviceCapabilities.contains("textNotice")) || (MsgTypeEnum.RICH_TEXT.contentType.equals(notice.getContentType()) && deviceCapabilities.contains("richTextNotice")));
        if (send) {
            if (notice.getNoticeTitle().length() > 1024) {
                throw new RuntimeException("标题长度不应超过1024个字");
            } else if (notice.getContent().length() > 102400) {
                throw new RuntimeException("内容过长，不应超过102400个字");
            }
            InfoObj infoObj = new InfoObj(notice.getSeq(), MsgTypeEnum.getByContentType(notice.getContentType()), notice.getNoticeTitle(), notice.getContent());
            infoObj.setValidityDay(365 * 6);
            HuaweiRespondDTO respondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).sendMessage(config, infoObj, deviceInfo.getThirdpartyCode());
            return HuaweiErrorEnum.SUCCESS.equals(respondDTO.getErrorEnum());
        } else {
            log.warn("设备不支持进行此类型的通知，设备ID：{} 设备能力集：{} 通知：{}", deviceInfo.getDeviceId(), deviceCapabilities, JSON.toJSONString(notice));
        }
        return false;
    }

    @Override
    public void cleanTextMessage(String deviceId) {
        HuaweiConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(deviceId, HuaweiConfigDTO.class);
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getVoById(deviceId);
        HuaweiRemoteDeviceOperateServiceFactory.getInstance(getVer()).cleanMessage(config, deviceInfo.getThirdpartyCode());

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
        return PlatformEnum.HUAWEI_MIDDLE.code;
    }

    /**
     * 拆分通行凭证列表为列表数组
     *
     * @param doorKeyVoList
     * @return
     */
    private List<DoorKeyObj>[] splitHuaweiCertList(List<DoorKeyObj> doorKeyVoList, HuaweiConfigDTO config) {

        int bufferSize = Integer.valueOf(config.getTransferBufferSize());//分页单页容量

        List<List<DoorKeyObj>> resultList = CollUtil.split(doorKeyVoList, bufferSize);
        return resultList.toArray(new List[resultList.size()]);
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        log.info("[华为中台] 发送信息到kafka：{}", message);
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }


    /**
     * 将卡片结果数据发送给统一接口服务，进行后续处理
     *
     * @param deviceInfo
     * @param certIdList
     * @param resultStatus
     */
    private void sendCardToMq(ProjectDeviceInfo deviceInfo, List<String> certIdList, String resultStatus) {


        if (CollUtil.isNotEmpty(certIdList)) {
            List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                    .eq(ProjectRightDevice::getDeviceId, deviceInfo.getDeviceId())
                    .in(ProjectRightDevice::getCertMediaId, certIdList));

            ResponseOperateDTO responseOperateDTO = new ResponseOperateDTO();
            responseOperateDTO.setCompany(PlatformEnum.HUAWEI_MIDDLE.value);
            responseOperateDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_CERT_STATUS_LIST);
            responseOperateDTO.setDeviceInfo(deviceInfo);
            responseOperateDTO.setRightDeviceList(rightDeviceList);
            responseOperateDTO.setRespondStatus(resultStatus);

            this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, responseOperateDTO);

        }

    }

}
