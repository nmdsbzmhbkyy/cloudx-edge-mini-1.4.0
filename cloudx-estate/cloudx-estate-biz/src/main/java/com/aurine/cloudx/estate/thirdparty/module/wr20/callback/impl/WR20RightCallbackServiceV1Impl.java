package com.aurine.cloudx.estate.thirdparty.module.wr20.callback.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightTokenStateEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20RightCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20CertDownLoadObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-31 14:35
 * @Copyright:
 */
@Service
@Slf4j
public class WR20RightCallbackServiceV1Impl implements WR20RightCallbackService {

    @Resource
    private ProjectDockModuleConfService projectDockModuleConfService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private WR20RightService wr20RightService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;

    /**
     * 变更通行凭证状态，如果原通行凭证不存在，则直接创建一条凭证记录
     *
     * @param jsonObject
     * @param gatewayId
     */
    @Override
    public void changeCertStateByWR20(JSONObject jsonObject, String gatewayId, String eventCode) {
        String cardNo = null;
        String faceCode = null;
        ProjectFaceResources face = null;
        List<String> deviceThirdpartyCodeList = null;
        jsonObject.toJSONString();
        WR20CertDownLoadObj downLoadObj = null;
        ProjectDeviceInfo deviceInfo = null;
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        log.info("[WR20] 开始处理项目：{} 的 凭证状态数据 {}", wr20Config.getProjectId(), jsonObject);

//        WR20CertDownLoadObj certDownLoadObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20CertDownLoadObj.class);
        JSONObject data = jsonObject.getJSONObject("data");
        HuaweiEventEnum eventEnum = HuaweiEventEnum.getByCode(eventCode);
        //如果存在则变更状态，不存在则创建对应的记录。如果是删除成功，则直接删除记录

        //解析凭证类型，人脸或卡片


        switch (eventEnum) {
            //卡片下载中
            case CERT_CARD_ADD_ING:
                cardNo = data.getString("cardID");
                deviceThirdpartyCodeList = new ArrayList<>();

                for (JSONObject obj : data.getJSONArray("rights").toJavaList(JSONObject.class)) {
                    deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(obj.getString("deviceID"));
                    if (deviceInfo != null) {
                        deviceThirdpartyCodeList.add(deviceInfo.getDeviceId());
                    } else {
                        log.error("[WR20] 回调添加卡片 下载中 状态失败，设备{} 系统中不存在", obj.getString("deviceID"));
                    }
                }

                projectRightDeviceService.saveOrUpdateCard(cardNo, deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DOWNLOADING);
                log.info("[WR20] 卡片{} 状态设置为 下载中", cardNo);
                break;
            case CERT_CARD_DEL_ING:
                //直接删除该卡片
                cardNo = data.getString("cardID");
                projectRightDeviceService.saveOrUpdateCard(cardNo, deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DELETING);
                log.info("[WR20] 卡片{} 状态设置为 删除中", cardNo);

                ProjectCard card = projectCardService.getByCardNo(cardNo, ProjectContextHolder.getProjectId());
                if (card != null) {
                    projectRightDeviceService.removeCert(card.getCardId());

                    card.setPersonId("");
                    card.setStatus(PassRightTokenStateEnum.UNUSE.code);
                    card.setPersonType("");
                    card.setPersonId("");
                    projectCardService.updateById(card);

                    log.info("[WR20] 卡片{} 已删除", cardNo);
                } else {
                    log.info("[WR20] 卡片{} 删除失败，未找到该卡片", cardNo);
                }

                break;
            case CERT_FACE_ADD_ING:
                log.info("[WR20] 面部：{} 状态设置为 下载中,元数据：{}", faceCode, data);
                faceCode = data.getString("faceID");
                face = projectFaceResourcesService.getByCode(faceCode, ProjectContextHolder.getProjectId());


                deviceThirdpartyCodeList = new ArrayList<>();

                for (JSONObject obj : data.getJSONArray("rights").toJavaList(JSONObject.class)) {
                    deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(obj.getString("deviceID"));
                    if (deviceInfo != null) {
                        deviceThirdpartyCodeList.add(deviceInfo.getDeviceId());
                    } else {
                        log.error("[WR20] 回调添加人脸 下载中 状态失败，设备{} 系统中不存在", obj.getString("deviceID"));
                    }
                }
                if (face != null) {
                    log.info("[WR20] 面部：{} 状态设置为 下载中,在设备列表：{}", faceCode, deviceThirdpartyCodeList);
                    projectRightDeviceService.saveOrUpdateFace(face.getFaceId(), deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DOWNLOADING);
                    log.info("[WR20] 面部：{} 状态设置为 下载中", faceCode);
                } else {
                    log.error("[WR20] 面部添加失败，当前面部信息在系统中不存在，已忽略:{}", jsonObject);
                }

                break;
            case CERT_FACE_DEL_ING:
                //直接删除人脸
                faceCode = data.getString("faceID");
                face = projectFaceResourcesService.getByCode(faceCode, ProjectContextHolder.getProjectId());
                if (face != null) {
                    projectRightDeviceService.saveOrUpdateFace(face.getFaceId(), deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DELETING);
                    log.info("[WR20] 面部：{} 状态设置为 删除中", faceCode);

                    projectRightDeviceService.removeCert(face.getFaceId());
                    projectFaceResourcesService.removeById(face);
                    log.info("[WR20] 面部：{} 已删除", faceCode);
                } else {
                    log.error("[WR20] 面部删除失败，当前面部信息在系统中不存在，已忽略:{}", jsonObject);
                }
                break;
            case CERT_DEL_DONE:
                downLoadObj = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), WR20CertDownLoadObj.class);
                downLoadObj.getIdentification();
                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(downLoadObj.getDevice());
                if (deviceInfo != null) {
                    List<String> deviceIdList = new ArrayList<>();
                    deviceIdList.add(deviceInfo.getDeviceId());
                    changeCertStatus(downLoadObj.getType(), downLoadObj.getIdentification(), deviceIdList, null);
                    log.info("[WR20] 凭证 {} 在设备 {} 已成功删除:{}", downLoadObj.getIdentification(), deviceInfo.getDeviceName(), jsonObject);
                } else {
                    log.info("[WR20] 凭证 {} 删除失败:未找到ThirdPartyNo={} 的设备", downLoadObj.getIdentification(), downLoadObj.getDevice());
                }
                break;
            case CERT_FAIL:

                downLoadObj = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), WR20CertDownLoadObj.class);
                downLoadObj.getIdentification();
                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(downLoadObj.getDevice());
                if (deviceInfo != null) {
                    List<String> deviceIdList = new ArrayList<>();
                    deviceIdList.add(deviceInfo.getDeviceId());
                    changeCertStatus(downLoadObj.getType(), downLoadObj.getIdentification(), deviceIdList, PassRightCertDownloadStatusEnum.FAIL);
                    log.info("[WR20] 凭证 {} 在设备 {} 下载失败:{}", downLoadObj.getIdentification(), deviceInfo.getDeviceName(), jsonObject);
                } else {
                    log.info("[WR20] 凭证 {} 下载失败 状态变更错误:未找到ThirdPartyNo={} 的设备", downLoadObj.getIdentification(), downLoadObj.getDevice());
                }

                break;
            case CERT_ADD_FULL:
                downLoadObj = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), WR20CertDownLoadObj.class);
                downLoadObj.getIdentification();

                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(downLoadObj.getDevice());
                if (deviceInfo != null) {
                    List<String> deviceIdList = new ArrayList<>();
                    deviceIdList.add(deviceInfo.getDeviceId());
                    changeCertStatus(downLoadObj.getType(), downLoadObj.getIdentification(), deviceIdList, PassRightCertDownloadStatusEnum.OUT_OF_MEMORY);
                    log.info("[WR20] 凭证 {} 在设备 {} 下载失败 设备已满:{}", downLoadObj.getIdentification(), deviceInfo.getDeviceName(), jsonObject);
                } else {
                    log.info("[WR20] 凭证 {} 下载失败 设备已满 状态变更错误:未找到ThirdPartyNo={} 的设备", downLoadObj.getIdentification(), downLoadObj.getDevice());
                }
                break;
            case CERT_ADD_SUCCESS:
                downLoadObj = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), WR20CertDownLoadObj.class);
                downLoadObj.getIdentification();

                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(downLoadObj.getDevice());
                if (deviceInfo != null) {
                    List<String> deviceIdList = new ArrayList<>();
                    deviceIdList.add(deviceInfo.getDeviceId());
                    changeCertStatus(downLoadObj.getType(), downLoadObj.getIdentification(), deviceIdList, PassRightCertDownloadStatusEnum.SUCCESS);
                    log.info("[WR20] 凭证 {} 在设备 {} 下载成功:{}", downLoadObj.getIdentification(), deviceInfo.getDeviceName(), jsonObject);
                } else {
                    log.info("[WR20] 凭证 {} 下载成功 状态变更错误:未找到ThirdPartyNo={} 的设备", downLoadObj.getIdentification(), downLoadObj.getDevice());
                }

                break;
            case CERT_FREEZE_SUCCESS:
                downLoadObj = JSONObject.parseObject(jsonObject.getJSONObject("data").toJSONString(), WR20CertDownLoadObj.class);
                downLoadObj.getIdentification();

                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(downLoadObj.getDevice());
                if (deviceInfo != null) {
                    List<String> deviceIdList = new ArrayList<>();
                    deviceIdList.add(deviceInfo.getDeviceId());
                    changeCertStatus(downLoadObj.getType(), downLoadObj.getIdentification(), deviceIdList, PassRightCertDownloadStatusEnum.FREEZE);
                    log.info("[WR20] 凭证 {} 在设备 {} 停用成功:{}", downLoadObj.getIdentification(), deviceInfo.getDeviceName(), jsonObject);
                } else {
                    log.info("[WR20] 凭证 {} 停用成功 状态变更错误:未找到ThirdPartyNo={} 的设备", downLoadObj.getIdentification(), downLoadObj.getDevice());
                }
                break;

            default:


                break;
        }


//        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
//        if (housePersonRel != null) {
//            housePersonRel.setRelaCode(jsonObject.getString("ID"));
//            projectHousePersonRelService.updateById(housePersonRel);
//            log.info("[WR20] 添加住户 回调完成，住户CODE:{} 住户ID:{}", housePersonRel.getRelaCode(), housePersonRel.getRelaId());
//        }
    }

    /**
     * 变更人脸或卡片下发状态
     * 枚举为空时，表示删除成功，删除记录
     *
     * @param certType
     * @param identification
     * @param passRightCertDownloadStatusEnum
     */
    private void changeCertStatus(String certType, String identification, List<String> deviceIdList, PassRightCertDownloadStatusEnum passRightCertDownloadStatusEnum) {


        if (StringUtils.equals(certType, "0")) {//人脸
            ProjectFaceResources face = projectFaceResourcesService.getByCode(identification, ProjectContextHolder.getProjectId());
            if (face != null) {
                if (passRightCertDownloadStatusEnum == null) {//删除成功

                    if(CollUtil.isNotEmpty(deviceIdList)){
                        projectRightDeviceService.remove(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, face.getFaceId()).in(ProjectRightDevice::getDeviceId,deviceIdList));
                    }else{
                        projectRightDeviceService.removeCert(face.getFaceId());
                    }

                } else {//其他

                    //如果是人脸下载成功，开始基于小程序的删除旧人脸流程
                    /**
                     * @since 2021-08-14
                     * @author: 王伟
                     * 下载成功，且来源为APP或微信，调用接口检查是否全部成功，并执行相关业务：全部成功且存在旧的来自同一个平台的非WEB源面部信息，则删除旧的面部信息
                     */

                    //检查是否为来自微信端数据
                    projectFaceResourcesService.removeOldAppFace(face.getFaceId());
                    log.info("[WR20] 异步响应处理旧面部信息流程完成");

                    projectRightDeviceService.saveOrUpdateFace(face.getFaceId(), deviceIdList, passRightCertDownloadStatusEnum);
                }
            } else {
                if (passRightCertDownloadStatusEnum == null) {//删除成功
                    log.error("[WR20] 当前面部信息在系统中不存在，可能已被删除，根据faceCode删除人脸下发状态");
                    projectRightDeviceService.removeCertByCode(identification);
                } else {//其他
                    log.error("[WR20] 当前面部信息在系统中不存在，操作被忽略");
                }


            }

        } else if (StringUtils.equals(certType, "1")) {//卡片
            if (passRightCertDownloadStatusEnum == null) {//删除成功
                ProjectCard card = projectCardService.getByCardNo(identification, ProjectContextHolder.getProjectId());
                if (card != null) {
                    if(CollUtil.isNotEmpty(deviceIdList)){
                        projectRightDeviceService.remove(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, card.getCardId()).in(ProjectRightDevice::getDeviceId,deviceIdList));
                    }else {
                        projectRightDeviceService.removeCert(card.getCardId());
                    }

                }
            } else {//其他
                projectRightDeviceService.saveOrUpdateCard(identification, deviceIdList, passRightCertDownloadStatusEnum);
            }
        }
    }

    /**
     * 新增人脸
     *
     * @param jsonObject
     * @param gatewayId
     * @param uid
     * @return
     */
    @Override
    public void addFace(JSONObject jsonObject, String gatewayId, String uid) {
        /**
         * objManagerData":{"action":"ADD","objInfo":{"result":102,"message":"此图片无法识别到人脸信息"},"objName":"FaceInfo","serviceId":"FaceManager"},"productId":"2"},"resource":"device.objmanager","event":"report","subscriptionId":"47"}
         */
        ProjectFaceResources face = projectFaceResourcesService.getById(uid);
        String faceId = jsonObject.getString("FaceID");

//        JSONObject objInfo = jsonObject.getJSONObject("objInfo");

//        String result = objInfo.getString("result");
//        String messgae = objInfo.getString("message");

        if (StringUtils.isNotEmpty(faceId)) {
            face.setFaceCode(faceId);
            projectFaceResourcesService.updateById(face);
//            projectHousePersonRelService.updateById(housePersonRel);
//            log.info("[WR20] 添加员工 回调完成，住户CODE:{} 员工ID:{}", housePersonRel.getRelaCode(), housePersonRel.getRelaId());
            log.info("[WR20]面部信息添加成功,FaceCode:{}, 将FaceCode更新到设备维护状态中...", faceId, jsonObject);

            /**
             * 获取到面部第三方ID后，设置到下发状态数据中
             */
            List<ProjectRightDevice> faceRightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, uid));
            if (CollUtil.isNotEmpty(faceRightDeviceList)) {
                for (ProjectRightDevice faceRightDevice : faceRightDeviceList) {
                    faceRightDevice.setCertMediaCode(faceId);
                }
            }
            projectRightDeviceService.updateBatchById(faceRightDeviceList);

            log.info("[WR20] 添加人脸成功 开始获取权限信息 面部信息：{}", face);
            List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(face.getPersonId(), PersonTypeEnum.getEnum(face.getPersonType()));
            String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
            projectPersonDeviceService.savePersonDevice(face.getPersonId(), PersonTypeEnum.getEnum(face.getPersonType()), deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            log.info("[WR20] 添加人脸成功 回调完成， 变更住户授权信息：{}",deviceIdList);

        } else {
            log.error("[WR20] 添加面部失败：{}", jsonObject);
        }
    }

    /**
     * 新增卡片
     *
     * @param jsonObject
     * @param gatewayId
     * @param uid
     * @return
     */
    @Override
    public void addCard(JSONObject jsonObject, String gatewayId, String uid) {
        ProjectCard card = projectCardService.getById(uid);

        log.info("[WR20] 添加卡片成功 开始获取权限信息 面部信息：{}", card);
        List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(card.getPersonId(), PersonTypeEnum.getEnum(card.getPersonType()));
        String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
        projectPersonDeviceService.savePersonDevice(card.getPersonId(), PersonTypeEnum.getEnum(card.getPersonType()), deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

//
//        if (card != null) {
//            card.setCardCode(jsonObject.getString("ID"));
////            projectHousePersonRelService.updateById(housePersonRel);
////            log.info("[WR20] 添加员工 回调完成，住户CODE:{} 员工ID:{}", housePersonRel.getRelaCode(), housePersonRel.getRelaId());
//        }
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

}
