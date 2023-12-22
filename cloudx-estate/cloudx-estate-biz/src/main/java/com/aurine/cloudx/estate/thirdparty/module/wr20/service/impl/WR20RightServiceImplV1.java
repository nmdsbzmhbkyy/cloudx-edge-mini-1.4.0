package com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiEventEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20DeviceRightObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20TenementObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.WR20WorkObj;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-04
 * @Copyright:
 */
@Service
@Slf4j
@RefreshScope
public class WR20RightServiceImplV1 implements WR20RightService {
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private ProjectStaffService projectStaffService;


    @Value("${server.base-uri}")
    private String baseUriPath;

    String wr20Id = "5ebb5524f2d7e4035a3327a7_7000ABF6-98D1-4B7E-91E3-EFB9981C8FEF";

    /**
     * 增加卡片
     *
     * @param card 卡片信息
     * @return
     */
    @Override
    public boolean addCard(int projectId, ProjectCard card) {
        //1.init
//
//        Map<String, Object> personMap = new HashMap<>();
//        ProjectPersonInfo person = projectPersonInfoService.getById(card.getPersonId());
//
//
//        String personCode = "";
//
//        //获取人员的所有房屋，并全部添加权限
//        List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda()
//                .eq(ProjectHousePersonRel::getPersonId, card.getPersonId())
//        );
//
//        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
//            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//
//                personCode = housePersonRel.getRelaCode();
//
//                if (StringUtils.isEmpty(personCode)) {
//                    break;// 住户无第三方信息
//                }
//
//                JSONArray params = new JSONArray();
//                JSONObject param = new JSONObject();
//                param.put("teneID", personCode);
//                param.put("cardNo", card.getCardNo());
//                params.add(param);
//                Map<String, Object> otherParams = new HashMap<>();
//                otherParams.put("orgUid", "");
//
//                HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                        "CardManager", "ADD", "", params, new JSONObject());
//
//
//            }
//        }


        return true;
    }

    /**
     * 删除卡片
     *
     * @param card 卡片信息
     * @return
     */
    @Override
    public boolean delCard(int projectId, ProjectCard card) {
        //1.init
//
//        Map<String, Object> personMap = new HashMap<>();
//        ProjectPersonInfo person = projectPersonInfoService.getById(card.getPersonId());
//
//
//        String personCode = "";
//
//        //获取人员的所有房屋，并全部添加权限
//        List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda()
//                .eq(ProjectHousePersonRel::getPersonId, card.getPersonId())
//        );
//
//        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
//            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//
//                personCode = housePersonRel.getRelaCode();
//
//                if (StringUtils.isEmpty(personCode)) {
//                    break;// 住户无第三方信息
//                }
//
//                JSONArray params = new JSONArray();
//                JSONObject param = new JSONObject();
//
//                param.put("teneID", personCode);
//                param.put("cardNo", card.getCardNo());
//                params.add(param);
//                HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                        "CardManager", "DELETE", "", params, new JSONObject());
//
//
//            }
//        }

        return false;
    }

    /**
     * 增加面部识别
     *
     * @param face 人脸识别
     * @return
     */
    @Override
    public boolean addFace(int projectId, ProjectFaceResources face) {
//        //1.init
//
//        Map<String, Object> personMap = new HashMap<>();
//        ProjectPersonInfo person = projectPersonInfoService.getById(face.getPersonId());
//        String personCode = "";
//
//        //获取人员的所有房屋，并全部添加权限
//        List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda()
//                .eq(ProjectHousePersonRel::getPersonId, face.getPersonId())
//        );
//
//        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
//            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//
//                personCode = housePersonRel.getRelaCode();
//
//                if (StringUtils.isEmpty(personCode)) {
//                    break;// 住户无第三方信息
//                }
//
//                JSONArray params = new JSONArray();
//                JSONObject param = new JSONObject();
//
//                param.put("teneId", personCode);
//                param.put("faceImage", baseUriPath + face.getPicUrl());
//                param.put("srcType", 2);
//
//                params.add(param);
//
//                Map<String, Object> otherParams = new HashMap<>();
//                otherParams.put("orgUid", "");
//                HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                        "FaceManager", "ADD", "", params, new JSONObject());
//
//
//            }
//        }


        return true;
    }

    /**
     * 删除全部面部识别
     *
     * @param face 人脸识别
     * @return
     */
    @Override
    public boolean delFace(int projectId, ProjectFaceResources face) {
        //1.init
//
//        Map<String, Object> personMap = new HashMap<>();
//        ProjectPersonInfo person = projectPersonInfoService.getById(face.getPersonId());
//        String personCode = "";
//
//        //获取人员的所有房屋，并全部添加权限
//        List<ProjectHousePersonRel> housePersonRelList = projectHousePersonRelService.list(new QueryWrapper<ProjectHousePersonRel>().lambda()
//                .eq(ProjectHousePersonRel::getPersonId, face.getPersonId())
//        );
//
//        if (CollectionUtil.isNotEmpty(housePersonRelList)) {
//            for (ProjectHousePersonRel housePersonRel : housePersonRelList) {
//
//                personCode = housePersonRel.getRelaCode();
//
//                if (StringUtils.isEmpty(personCode)) {
//                    break;// 住户无第三方信息
//                }
//
//                JSONArray params = new JSONArray();
//                JSONObject param = new JSONObject();
//
//                param.put("teneId", personCode);
//                param.put("srcType", 2);
//
//                params.add(param);
//                HuaweiRespondDTO huaweiRespondDTO = HuaweiRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1).objectManage(wr20Id,
//                        "FaceManager", "DELETE", "", params, new JSONObject());
//            }
//        }

        return true;
    }

    /**
     * 生成下载中的下发记录
     *
     * @return
     */
    @Override
    public void createDownloadingCert(int projectId, HuaweiEventEnum eventEnum, List<String> deviceCodeList, ProjectCard card, ProjectFaceResources face, boolean thirdCode) {

//        String cardNo = null;
//        String faceCode = null;
//        List<String> deviceThirdpartyCodeList = null;
//        ProjectDeviceInfo deviceInfo = null;
//        //根据网关，获取项目信息
//        ProjectContextHolder.setProjectId(projectId);
//        log.info("[WR20] 开始生成：{} 的 凭证 下载中 状态数据", projectId);
//
//        if (CollUtil.isNotEmpty(deviceCodeList)) {
//            //解析凭证类型，人脸或卡片
//            switch (eventEnum) {
//                //卡片下载中
//                case CERT_CARD_ADD_ING:
//                    cardNo = card.getCardNo();
//                    deviceThirdpartyCodeList = new ArrayList<>();
//
//                    for (String deviceCode : deviceCodeList) {
//                        if (thirdCode) {
//                            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(deviceCode);
//                        } else {
//                            deviceInfo = projectDeviceInfoProxyService.getVoById(deviceCode);
//                        }
//
//                        if (deviceInfo != null) {
//                            deviceThirdpartyCodeList.add(deviceInfo.getDeviceId());
//                        } else {
//                            log.error("[WR20] 回调添加卡片 下载中 状态失败，设备{} 系统中不存在", deviceCode);
//                        }
//                    }
//
//                    projectRightDeviceService.saveOrUpdateCard(cardNo, deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DOWNLOADING);
//                    log.info("[WR20] 卡片{} 状态设置为 下载中", cardNo);
//                    break;
//
//                case CERT_FACE_ADD_ING:
//                    deviceThirdpartyCodeList = new ArrayList<>();
//
//                    for (String deviceCode : deviceCodeList) {
//                        if (thirdCode) {
//                            deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(deviceCode);
//                        } else {
//                            deviceInfo = projectDeviceInfoProxyService.getVoById(deviceCode);
//                        }
//
//                        if (deviceInfo != null) {
//                            deviceThirdpartyCodeList.add(deviceInfo.getDeviceId());
//                        } else {
//                            log.error("[WR20] 回调添加人脸 下载中 状态失败，设备{} 系统中不存在", deviceCode);
//                        }
//                    }
//                    if (face != null) {
//                        projectRightDeviceService.saveOrUpdateFace(face.getFaceId(), deviceThirdpartyCodeList, PassRightCertDownloadStatusEnum.DOWNLOADING);
//                        log.info("[WR20] 面部：{} 状态设置为 下载中", faceCode);
//                    } else {
//                        log.error("[WR20] 面部添加失败，当前面部信息在系统中不存在，已忽略");
//                    }
//
//                    break;
//                default:
//                    break;
//            }
//        }


    }

    /**
     * 获取住户\员工的权限列表，返回第三方设备ID集合
     *
     * @param personId
     * @param personTypeEnum
     * @return
     */
    @Override
    public List<ProjectDeviceInfo> getPersonRightList(String personId, PersonTypeEnum personTypeEnum) {

        List<ProjectDeviceInfo> deviceInfoList = new ArrayList<>();
        Set<String> deviceCodeSet = new HashSet<>();

        if (personTypeEnum == PersonTypeEnum.PROPRIETOR) {
            log.info("[WR20] 查询住户 {} 在WR20中的授权信息", personId);

            //查询住户的所有房屋
            List<ProjectHousePersonRel> personRelList = projectHousePersonRelService.listHousePersonByPersonId(personId);

            if (CollUtil.isEmpty(personRelList)) {
                return deviceInfoList;
            }

            for (ProjectHousePersonRel housePersonRel : personRelList) {
                if (StringUtil.isEmpty(housePersonRel.getRelaCode())) {
                    continue;
                }

                //通过TeneId查询，并解析WR20住户权限，再有权限的设备中创建下载中的凭证记录 @since 2021-08-11 王伟
                JSONObject queryJson = new JSONObject();
                queryJson.put("TeneID", housePersonRel.getRelaCode());
                HuaweiRespondDTO respondDTO = wr20RemoteService.queryTenementSync(ProjectContextHolder.getProjectId(), queryJson);
                log.info("[WR20] 查询到住户 {} 权限 {}", housePersonRel.getRelaCode(), respondDTO.getBodyObj());

                CallBackData callBackData = respondDTO.getBodyObj().toJavaObject(CallBackData.class);
                if (callBackData == null) {
                    log.error("[WR20] 接收到事件信息格式错误:{}", respondDTO.getBodyObj());
                    throw new RuntimeException("接收到数据信息格式错误，请联系管理员");
                }


                List<WR20TenementObj> tenementObjList = callBackData.getOnNotifyData().getObjManagerData().getObjInfo().getJSONArray("list").toJavaList(WR20TenementObj.class);

                if (CollUtil.isNotEmpty(tenementObjList)) {
                    if (CollUtil.isNotEmpty(tenementObjList.get(0).getRights())) {
                        for (WR20DeviceRightObj wr20DeviceRightObj : tenementObjList.get(0).getRights()) {
                            deviceCodeSet.add(wr20DeviceRightObj.getDeviceNo());
                        }
                    }
                }

            }


        } else if (personTypeEnum == PersonTypeEnum.STAFF) {
            log.info("[WR20] 查询员工 {} 在WR20中的授权信息", personId);
            ProjectStaff staff = projectStaffService.getById(personId);
            if (staff == null) {
                log.error("[WR20] 员工获取权限失败，未查询到员工{}", personId);
                return deviceInfoList;
            }

            if (StringUtils.isEmpty(staff.getStaffCode())) {
                log.error("[WR20] 员工获取权限失败，未查询到员工{}", staff);
                return deviceInfoList;
            }

            //通过TeneId查询，并解析WR20住户权限，再有权限的设备中创建下载中的凭证记录 @since 2021-08-11 王伟
            JSONObject queryJson = new JSONObject();
            queryJson.put("GuardID", staff.getStaffCode());
            queryJson.put("PageIndex", 1);
            queryJson.put("CountPrePage", 10);
            HuaweiRespondDTO respondDTO = wr20RemoteService.queryWorkSync(ProjectContextHolder.getProjectId(), queryJson);
            log.info("[WR20] 查询到员工 {} 权限 {}", staff, respondDTO.getBodyObj());

            CallBackData callBackData = respondDTO.getBodyObj().toJavaObject(CallBackData.class);
            if (callBackData == null) {
                log.error("[WR20] 接收到事件信息格式错误:{}", respondDTO.getBodyObj());
                throw new RuntimeException("接收到数据信息格式错误，请联系管理员");
            }


            List<WR20WorkObj> workObjList = callBackData.getOnNotifyData().getObjManagerData().getObjInfo().getJSONArray("list").toJavaList(WR20WorkObj.class);

            if (CollUtil.isNotEmpty(workObjList)) {
                if (CollUtil.isNotEmpty(workObjList.get(0).getRights())) {
                    for (WR20DeviceRightObj wr20DeviceRightObj : workObjList.get(0).getRights()) {
                        deviceCodeSet.add(wr20DeviceRightObj.getDeviceNo());
                    }
                }
            }
        }

        if (CollUtil.isNotEmpty(deviceCodeSet)) {
            ProjectDeviceInfoProxyVo deviceInfo = null;
            for (String deviceCode : deviceCodeSet) {
                deviceInfo = projectDeviceInfoProxyService.getByThirdPartyNo(deviceCode);
                if (deviceInfo == null) {
                    log.error("[WR20] 获取人员权限，设备 {} 在系统中未找到，已忽略该权限", deviceCode);
                } else {
                    deviceInfoList.add(deviceInfo);
                }
            }
        }

        return deviceInfoList;
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
