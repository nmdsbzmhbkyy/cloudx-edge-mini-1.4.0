package com.aurine.cloudx.estate.thirdparty.module.wr20.callback.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.SysProjectDeptConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.business.entity.constant.ThirdPartyBusinessPlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.callback.WR20PersonCallbackService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.constant.WR20PersonTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto.*;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20CredentialTypeEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums.WR20GenderEnum;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20PersonService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.WR20RightService;
import com.aurine.cloudx.estate.thirdparty.module.wr20.service.impl.WR20RemoteService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.aurine.cloudx.estate.vo.ProjectDockModuleConfWR20Vo;
import com.aurine.cloudx.estate.vo.ProjectHousePersonRelVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.admin.api.feign.RemoteRoleService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-07
 * @Copyright:
 */
@Service
@Slf4j
public class WR20PersonCallbackServiceV1Impl implements WR20PersonCallbackService {
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectDockModuleConfService projectDockModuleConfService;
    @Resource
    private ProjectHousePersonRelService webProjectHousePersonRelService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectPassPlanService projectPassPlanService;
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private ProjectCardService projectCardService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private RemoteRoleService remoteRoleService;
    @Resource
    private SysProjectDeptService sysProjectDeptService;
    @Resource
    private ImgConvertUtil imgConvertUtil;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private WR20RightService wr20RightService;
    @Resource
    private WR20RemoteService wr20RemoteService;
    @Resource
    private WR20PersonService wr20PersonService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;

    @Value("${server.base-uri}")
    private String baseUriPath;


    /**
     * 新增住户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void addPerson(JSONObject jsonObject, String gatewayId, String uid) {
        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
        if (housePersonRel != null) {
            housePersonRel.setRelaCode(jsonObject.getString("ID"));
            housePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            projectHousePersonRelService.updateById(housePersonRel);
            log.info("[WR20] 添加住户 回调完成，住户CODE:{} 住户ID:{}", housePersonRel.getRelaCode(), housePersonRel.getRelaId());

            //添加住户成功后，获取该住户的最老的一套房屋数据，并调用checkIn接口，实现WR20权限变更
            ProjectHousePersonRel firstHousePersonRel = wr20PersonService.getFitstHouseRel(housePersonRel.getPersonId());
            if (firstHousePersonRel != null && !StringUtils.equals(housePersonRel.getRelaCode(), firstHousePersonRel.getRelaCode())) {
                ProjectHouseInfo houseInfo = projectHouseInfoService.getById(housePersonRel.getHouseId());
                //该房屋为多套房
                log.info("[WR20] 添加住户 发送一人多方添加请求 以转移凭证授权 原住户code:{} 添加房屋code:{}", firstHousePersonRel.getRelaCode(), houseInfo.getHouseCode());
                JSONObject paramObj = new JSONObject();
                paramObj.put("teneId", firstHousePersonRel.getRelaCode());
                paramObj.put("thirdId", firstHousePersonRel.getPersonId());
                paramObj.put("frameNo", houseInfo.getHouseCode());

                wr20RemoteService.checkin(ProjectContextHolder.getProjectId(), paramObj, housePersonRel.getRelaId());
//                wr20PersonService.checkIn(firstHousePersonRel);
            } else {
                //住户只有一间房
                //添加住户成功后，查询住户名下所有房产的授权
                log.info("[WR20] 添加住户 回调完成，住户CODE:{} 当前住户只有一间房, 获取该住户全部权限", housePersonRel.getRelaCode());
                List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
                String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
                projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            }

//            //添加住户成功后，查询住户名下所有房产的授权
//            log.info("[WR20] 添加住户 回调完成，住户CODE:{} 获取该住户全部权限", housePersonRel.getRelaCode());
//            List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
//            String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
//            projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


//            //变更授权该住户名下所有凭证,等待接口方案确认
//            log.info("[WR20] 添加住户 回调完成，住户CODE:{} 变更该住户权限", housePersonRel.getRelaCode());
//            //人脸推送到当前住户下
//            List<ProjectFaceResources> faceResourcesList = projectFaceResourcesService.listByPersonId(housePersonRel.getPersonId());
//            if (CollUtil.isNotEmpty(faceResourcesList)) {
//                for (ProjectFaceResources faceResources : faceResourcesList) {
//
//                    WR20FaceManageObj faceManage = new WR20FaceManageObj();
//                    faceManage.setTeneId(housePersonRel.getRelaCode());
//                    faceManage.setSrcType("2");
//                    faceManage.setFaceImage(baseUriPath + faceResources.getPicUrl());
//                    faceManage.setPersonType(WR20PersonTypeEnum.getByCloudCode(faceResources.getPersonType()).code);
//                    wr20RemoteService.addFace(ProjectContextHolder.getProjectId(), JSONObject.parseObject(JSONObject.toJSONString(faceManage)), faceResources.getFaceId());
//
//                }
//            }


            //卡片重新获取权限


        }
    }

    /**
     * 新增一人多房住户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void checkInPerson(JSONObject jsonObject, String gatewayId, String uid) {

        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
        if (housePersonRel != null) {
            log.info("[WR20] 添加多房住户 回调完成，查询住户授权，住户CODE:{} 住户ID:{}", housePersonRel.getRelaCode(), housePersonRel.getRelaId());

            //添加住户成功后，查询住户名下所有房产的授权
            log.info("[WR20] 添加住户 回调完成，住户CODE:{} 获取该住户全部权限", housePersonRel.getRelaCode());
            List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
            String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
            projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    /**
     * 删除一人多房住户
     *
     * @param jsonObject
     * @return
     */
    @Override
    public void checkOutPerson(JSONObject jsonObject, String gatewayId, String uid) {

//
//        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
//        if (housePersonRel != null) {
        log.info("[WR20] 迁出多房住户 回调完成，转移住户凭证");

        //迁出住户成功后，获取到住户最老的一套房，并将凭证更新为该房绑定
        ProjectHousePersonRel housePersonRel = wr20PersonService.getFitstHouseRel(uid);
        List<ProjectFaceResources> faceList = projectFaceResourcesService.listByPersonId(uid);
        List<ProjectCard> cardList = projectCardService.listByPersonId(uid);

        if (housePersonRel != null) {
            if (CollUtil.isNotEmpty(faceList)) {
//            faceList.stream().map(e->e.)
            }

            if (CollUtil.isNotEmpty(cardList)) {

            }

            log.info("[WR20] 迁出多房住户 回调完成，重新获取该住户全部权限 新凭证归属的住户CODE为:{}", housePersonRel.getRelaCode());
            List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
            String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
            projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            //
        } else {
            //当前住户已经被清空，删除对应人脸和卡片


        }


        //
//        log.info("[WR20] 添加住户 回调完成，住户CODE:{} 获取该住户全部权限", housePersonRel.getRelaCode());
//        List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR);
//        String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
//        projectPersonDeviceService.savePersonDevice(housePersonRel.getPersonId(), PersonTypeEnum.PROPRIETOR, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        }
    }


    /**
     * 新增员工
     *
     * @param jsonObject
     * @param gatewayId
     * @param uid
     * @return
     */
    @Override
    public void addStaff(JSONObject jsonObject, String gatewayId, String uid) {
        ProjectStaff staff = projectStaffService.getById(uid);
//        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
        if (staff != null) {
            staff.setStaffCode(jsonObject.getString("guardID"));
            projectStaffService.updateById(staff);
            log.info("[WR20] 添加员工 回调完成，员工CODE:{} 员工:{}", staff.getStaffCode(), staff);
        }


        log.info("[WR20] 添加员工 回调完成，员工CODE:{} 获取该员工全部权限", staff.getStaffCode());
        List<ProjectDeviceInfo> rightDeviceList = wr20RightService.getPersonRightList(staff.getStaffId(), PersonTypeEnum.STAFF);
        String[] deviceIdList = rightDeviceList.stream().map(e -> e.getDeviceId()).collect(Collectors.toList()).toArray(new String[rightDeviceList.size()]);
        projectPersonDeviceService.savePersonDevice(staff.getStaffId(), PersonTypeEnum.STAFF, deviceIdList, LocalDateTime.now(), LocalDateTime.parse("2199-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        //变更授权该住户名下所有凭证,等待接口方案确认
        log.info("[WR20] 添加员工 回调完成，员工CODE:{} 变更该员工权限", staff.getStaffCode());
    }

    /**
     * 添加访客
     *
     * @param jsonObject
     * @param gatewayId
     * @param uid
     */
    @Override
    public void addVisitor(JSONObject jsonObject, String gatewayId, String uid) {
        ProjectVisitorHis visitorHis = projectVisitorHisService.getById(uid);
//        ProjectHousePersonRel housePersonRel = projectHousePersonRelService.getById(uid);
        if (visitorHis != null) {
            visitorHis.setVisitCode(jsonObject.getString("VIPK"));
            projectVisitorHisService.updateById(visitorHis);
            //回调设置访客人脸ID
            log.info("[WR20] 添加访客 开始设置面部第三方ID，访客CODE:{}  {}", visitorHis.getVisitCode(), visitorHis);
            List<ProjectFaceResources> faceResourcesList = projectFaceResourcesService.list(new QueryWrapper<ProjectFaceResources>().lambda().eq(ProjectFaceResources::getPersonId, visitorHis.getVisitId()));

            if (CollUtil.isNotEmpty(faceResourcesList)) {
                if (faceResourcesList.size() >= 2) {
                    log.error("[WR20] 添加访客 业务异常，当前访客无面部凭证存在多条，默认获取第一个面部凭证进行设置。访客CODE:{}  {}", visitorHis.getVisitCode(), faceResourcesList.get(0));
                }

                ProjectFaceResources face = faceResourcesList.get(0);
                face.setFaceCode(jsonObject.getString("FaceID"));
                projectFaceResourcesService.updateById(face);

                log.info("[WR20] 添加访客 设置面部第三方ID为{}。访客CODE:{}  {}", jsonObject.getString("FaceID"), visitorHis.getVisitCode(), visitorHis);
            } else {
                log.info("[WR20] 添加访客 开始设置面部第三方ID，当前访客无面部凭证。访客CODE:{}  {}", visitorHis.getVisitCode(), visitorHis);
            }

            log.info("[WR20] 添加访客 回调完成，访客CODE:{}  {}", visitorHis.getVisitCode(), visitorHis);
        }
    }

    /**
     * WR20侧 新增住户 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void addPersonByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始添加项目：{} 的 住户数据 {}", wr20Config.getProjectId(), jsonObject);


        //处理数据
        WR20TenementObj tenementObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20TenementObj.class);
        List<WR20TenementObj> tenementObjList = new ArrayList<>();
        tenementObjList.add(tenementObj);
        this.syncTentmentList(tenementObjList, wr20Config.getProjectId());

        log.info("[WR20] 添加住户流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * WR20侧 更新住户 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void updatePersonByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始更新项目：{} 的 住户数据 {}", wr20Config.getProjectId(), jsonObject);

        //处理数据
        WR20TenementObj tenementObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20TenementObj.class);
        List<WR20TenementObj> tenementObjList = new ArrayList<>();
        tenementObjList.add(tenementObj);
        this.syncTentmentList(tenementObjList, wr20Config.getProjectId());

        log.info("[WR20] 更新住户流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * WR20侧 删除住户 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void delPersonByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始删除项目：{} 的 住户数据 {}", wr20Config.getProjectId(), jsonObject);


        //处理数据
        WR20TenementObj tenementObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20TenementObj.class);
        webProjectHousePersonRelService.remove(new QueryWrapper<ProjectHousePersonRel>().lambda().eq(ProjectHousePersonRel::getRelaCode, tenementObj.getID()));

        log.info("[WR20] 删除住户流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * WR20侧 新增员工 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void addStaffByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始添加项目：{} 的 员工数据 {}", wr20Config.getProjectId(), jsonObject);

        //处理数据
        WR20WorkObj workObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20WorkObj.class);
        List<WR20WorkObj> workObjList = new ArrayList<>();
        workObjList.add(workObj);
        this.syncWorkerList(workObjList, wr20Config.getProjectId());

        log.info("[WR20] 添加员工流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * WR20侧 更新员工 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void updateStaffByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始更新项目：{} 的 员工数据 {}", wr20Config.getProjectId(), jsonObject);


        //处理数据
        WR20WorkObj workObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20WorkObj.class);
        List<WR20WorkObj> workObjList = new ArrayList<>();
        workObjList.add(workObj);
        this.syncWorkerList(workObjList, wr20Config.getProjectId());

        log.info("[WR20] 更新员工流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * WR20侧 删除员工 回调
     *
     * @param jsonObject
     * @param gatewayId
     * @return
     */
    @Override
    public void delStaffByWR20(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始删除项目：{} 的 员工数据 {}", wr20Config.getProjectId(), jsonObject);

        //处理数据
        WR20WorkObj workObj = JSONObject.parseObject(jsonObject.toJSONString(), WR20WorkObj.class);
        projectStaffService.remove(new QueryWrapper<ProjectStaff>().lambda().eq(ProjectStaff::getStaffCode, workObj.getGuardID()));

        log.info("[WR20] 删除员工流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * 同步住户
     *
     * @param jsonObject
     * @param gatewayId
     */
    @Override
    public void syncTenement(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始同步项目：{} 的 住户数据 {}", wr20Config.getProjectId(), jsonObject);

        //清空系统默认方案
        projectPassPlanService.clearDefaultPlan();

        //处理数据
        List<WR20TenementObj> tenementObjList = jsonObject.getJSONArray("list").toJavaList(WR20TenementObj.class);
        this.syncTentmentList(tenementObjList, wr20Config.getProjectId());

        log.info("[WR20] 同步住户流程结束{}", gatewayId);
        ProjectContextHolder.clear();
    }

    /**
     * 同步员工
     *
     * @param jsonObject
     * @param gatewayId
     */
    @Override
    public void syncStaff(JSONObject jsonObject, String gatewayId) {
        //根据网关，获取项目信息
        ProjectDockModuleConfWR20Vo wr20Config = projectDockModuleConfService.getConfigByThirdCode(ThirdPartyBusinessPlatformEnum.WR20.code, gatewayId, ProjectDockModuleConfWR20Vo.class);
        ProjectContextHolder.setProjectId(wr20Config.getProjectId());
        TenantContextHolder.setTenantId(1);
        log.info("[WR20] 开始同步项目：{} 的 员工数据 {}", wr20Config.getProjectId(), jsonObject);

        //授权

        //清空系统默认方案
        projectPassPlanService.clearDefaultPlan();

        //处理数据
        List<WR20WorkObj> workerObjList = jsonObject.getJSONArray("list").toJavaList(WR20WorkObj.class);
        this.syncWorkerList(workerObjList, wr20Config.getProjectId());

        log.info("[WR20] 同步员工流程结束{}", gatewayId);
        ProjectContextHolder.clear();
        TenantContextHolder.clear();
    }


    /**
     * sync worker list
     *
     * @param workObjList
     * @para projectId
     */
    private void syncWorkerList(List<WR20WorkObj> workObjList, int projectId) {
        ProjectStaffDTO staffDto = null;
        PersonTypeEnum personTypeEnum = PersonTypeEnum.STAFF;
        for (WR20WorkObj wr20WorkerObj : workObjList) {

            staffDto = new ProjectStaffDTO();
            staffDto.setStaffCode(String.valueOf(wr20WorkerObj.getGuardID()));//第三方ID
            staffDto.setStaffName(wr20WorkerObj.getName());//姓名
            staffDto.setMobile(wr20WorkerObj.getTelephone());//手机号

            if (StringUtils.isEmpty(staffDto.getMobile())) {
                log.error("[WR20] 同步员工错误 员工 {} 无手机号,已跳过", wr20WorkerObj.getName());
                continue;
            }

            staffDto.setSex(WR20GenderEnum.getByWR20Code(wr20WorkerObj.getGender()).cloudCode);//性别
            staffDto.setCredentialType(WR20CredentialTypeEnum.getByWR20Code(wr20WorkerObj.getCredentialType()).cloudCode);//证件类型
            staffDto.setCredentialNo(wr20WorkerObj.getCredentialID());//证件号

            //对员工赋予默认的角色、部门
            SysProjectDept sysProjectDept = sysProjectDeptService.getOne(Wrappers.lambdaQuery(SysProjectDept.class)
                    .eq(SysProjectDept::getProjectId, projectId)
                    .eq(SysProjectDept::getDeptName, SysProjectDeptConstant.OTHER_DEPT_NAME));

            R<List<SysRole>> r = remoteRoleService.getByDeptIdWithForm(projectId, SecurityConstants.FROM_IN);
            List<SysRole> roleList = r.getData().stream().filter(e -> e.getRoleName().indexOf("管理员") == -1).collect(Collectors.toList());
            if (CollUtil.isEmpty(roleList)) {
                log.error("[WR20] 同步员工出错，未找到有效角色，已跳过该权限同步");
                continue;
            }

            staffDto.setOldRoleId(roleList.get(0).getRoleId());
            staffDto.setRoleId(roleList.get(0).getRoleId());
            staffDto.setNewRoleId(roleList.get(0).getRoleId());
            staffDto.setDepartmentId(sysProjectDept.getDeptId());
            staffDto.setProjectId(projectId);

            //保存员工
            String personId = projectStaffService.saveOrUpdateStaffByStaffCode(staffDto);

            this.syncDeviceAndRights(personTypeEnum, wr20WorkerObj.getLimitDateTime(), personId, wr20WorkerObj.getRights(), wr20WorkerObj.getCards(), wr20WorkerObj.getFaces());

            log.info("[WR20] 同步单个员工完成：{}", wr20WorkerObj.getName());
        }
    }

    /**
     * sync tenement list
     *
     * @param tenementObjList
     * @para projectId
     */
    private void syncTentmentList(List<WR20TenementObj> tenementObjList, int projectId) {
        ProjectHousePersonRelVo housePersonRelVo = null;
        PersonTypeEnum personTypeEnum = PersonTypeEnum.PROPRIETOR;
        for (WR20TenementObj wr20TenementObj : tenementObjList) {

            housePersonRelVo = new ProjectHousePersonRelVo();
            housePersonRelVo.setRelaCode(String.valueOf(wr20TenementObj.getID()));//第三方ID
            housePersonRelVo.setTelephone(wr20TenementObj.getTelephone());//手机号

//            if (StringUtils.isEmpty(housePersonRelVo.getTelephone())) {
//                log.error("[WR20] 同步住户错误 住户 {} 无手机号,已跳过", wr20TenementObj.getName());
//                continue;
//            }

            housePersonRelVo.setPersonName(wr20TenementObj.getName());//姓名
            housePersonRelVo.setGender(WR20GenderEnum.getByWR20Code(wr20TenementObj.getGender()).cloudCode);//性别
            housePersonRelVo.setCredentialType(WR20CredentialTypeEnum.getByWR20Code(wr20TenementObj.getCredentialType()).cloudCode);//证件类型
            housePersonRelVo.setCredentialNo(wr20TenementObj.getCredentialID());//证件号

            //同步住户统一设置为 家属 其他
            wr20TenementObj.getMemberType();
            housePersonRelVo.setHouseholdType(HouseHoldTypeEnum.FAMILY.code);
            housePersonRelVo.setMemberType(HouseMemberTypeEnum.OTHER.code);


            //转存住户头像
            wr20TenementObj.getFaceImage();//Base64 转存到MinIO  //TODO:转存面部图片 王伟 -> 2021-01-30

            //通过框架号获取住户所在房间
            ProjectFrameInfo frameInfo = projectFrameInfoService.getByFrameNo(wr20TenementObj.getFrameNo());
            if (frameInfo == null) {
                log.error("[WR20] 住户框架不存在,已跳过该住户: {}", wr20TenementObj);
                continue; //数据异常，直接跳过存储当前住户
            }
            housePersonRelVo.setHouseId(frameInfo.getEntityId());

            //保存住户
            String personId = webProjectHousePersonRelService.saveOrUpdateRelByWR20(housePersonRelVo);
            this.syncDeviceAndRights(personTypeEnum, wr20TenementObj.getLimitDateTime(), personId, wr20TenementObj.getRights(), wr20TenementObj.getCards(), wr20TenementObj.getFaces());

            log.info("[WR20] 单个住户完成,开始回传WR20 PersonID：{}", wr20TenementObj.getName());
            wr20TenementObj.setThirdId(personId);
            wr20RemoteService.updateHousePersonRel(ProjectContextHolder.getProjectId(),JSONObject.parseObject(JSONObject.toJSONString(wr20TenementObj)),personId);
        }
    }

    /**
     * 同步设备、权限、凭证
     */
    private void syncDeviceAndRights(PersonTypeEnum personTypeEnum, String limitDateTime, String personId, List<WR20DeviceRightObj> rightList, List<String> cardNoList, List<WR20FaceObj> faceList) {
        //分配默认方案 与 该人员的过期时间、激活情况
        ProjectPassPlan plan = projectPassPlanService.getDefaultPlan(personTypeEnum);
        ProjectPersonPlanRel personPlanRel = new ProjectPersonPlanRel();
        personPlanRel.setIsActive("1");
        if (StringUtils.isNotEmpty(limitDateTime)) {
            personPlanRel.setExpTime(DateUtil.parseLocalDateTime(limitDateTime));  //配置过期时间
        } else {
            personPlanRel.setExpTime(DateUtil.parseLocalDateTime("2199-01-01 00:00:00"));  //配置过期时间
        }
        personPlanRel.setPlanId(plan.getPlanId());
        personPlanRel.setPersonId(personId);
        personPlanRel.setPersonType(personTypeEnum.code);
        personPlanRel.setEffTime(DateUtil.toLocalDateTime(new Date()));
        projectPersonPlanRelService.saveOrUpdateByPersonId(personPlanRel);

        //获取人员基础信息
        String personName = "";
        String phone = "";

        if (personTypeEnum == PersonTypeEnum.PROPRIETOR) {
            //住户
            ProjectPersonInfo personInfo = projectPersonInfoService.getById(personId);
            if (personInfo != null) {
                personName = personInfo.getPersonName();
                phone = personInfo.getTelephone();
            }
        } else if (personTypeEnum == PersonTypeEnum.STAFF) {
            //员工
            ProjectStaff staff = projectStaffService.getById(personId);
            if (staff != null) {
                personName = staff.getStaffName();
                phone = staff.getMobile();
            }
        } else {
            //访客
//            projectVisitorHisService.get
        }

        //配置人员-设备 权限
//        List<WR20DeviceRightObj> rightList = wr20TenementObj.getRights();
        ProjectPersonDevice projectPersonDevice = null;
        List<ProjectPersonDevice> personDeviceList = new ArrayList<>();

        if (CollUtil.isNotEmpty(rightList)) {
            for (WR20DeviceRightObj rightObj : rightList) {
                projectPersonDevice = new ProjectPersonDevice();
                ProjectDeviceInfoProxyVo deviceInfoProxyVo = projectDeviceInfoProxyService.getByThirdPartyNo(rightObj.getDeviceNo());
                if (deviceInfoProxyVo == null) {
                    log.error("[WR20] 同步权限出错，未找到设备ThirdPartyNo为 {} 的设备，已跳过该权限同步", rightObj.getDeviceNo());
                    continue;
                }

                projectPersonDevice.setDeviceId(deviceInfoProxyVo.getDeviceId());
                projectPersonDevice.setPersonId(personId);
                projectPersonDevice.setIsActive("1");
                projectPersonDevice.setPersonType(personTypeEnum.code);
                projectPersonDevice.setPlanId(null);
                projectPersonDevice.setEffTime(DateUtil.toLocalDateTime(new Date()));
                projectPersonDevice.setExpTime(personPlanRel.getExpTime());
                projectPersonDevice.setStatus("1");

                personDeviceList.add(projectPersonDevice);
            }
            projectPersonDeviceService.deleteByPersonId(personId);//先删除该人员的所有权限
            projectPersonDeviceService.saveBatch(personDeviceList);

        }


        List<ProjectCard> oriCardList = projectCardService.listByPersonId(personId);
        List<ProjectCard> delCardList = null;
        List<ProjectFaceResources> oriFaceList = projectFaceResourcesService.listByPersonId(personId);
        List<ProjectFaceResources> delFaceList = null;


        //保存卡片，如果卡片已经被使用则抛出异常，进行忽略拦截。
//        List<String> cardNoList = wr20TenementObj.getCards();
        List<String[]> cardIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(cardNoList)) {
            //添加或修改卡片
            ProjectCard card = null;
            for (String cardNo : cardNoList) {
                card = new ProjectCard();
                card.setCardId(UUID.randomUUID().toString().replace("-", ""));
                card.setPersonId(personId);
                card.setPersonType(personTypeEnum.code);
                card.setStatus(PassRightTokenStateEnum.USED.code);
                card.setCardNo(cardNo);

                try {
                    ProjectCard resultCard = projectCardService.saveOrUpdateCard(card);
                    cardIdList.add(new String[]{resultCard.getCardId(), cardNo});
                } catch (RuntimeException e) {
                    //
                    log.error("[WR20] 同步住户卡片出错 卡号:{} {}, 已跳过该卡片", cardNo, e.getMessage());
                }
            }

            //需要删除的卡片
            if (CollUtil.isNotEmpty(oriCardList)) {
                delCardList = oriCardList.stream().filter(e -> cardNoList.contains(e.getCardNo()) == false).collect(Collectors.toList());
            }

        } else {
            delCardList = oriCardList;
        }

        //删除卡
        if (CollUtil.isNotEmpty(delCardList)) {
            for (ProjectCard card : delCardList) {
                projectCardService.delCard(card.getCardId());
                projectRightDeviceService.removeCert(card.getCardId());
            }
        }


        //保存人脸，如果已存在则获取已存在人脸id
//        List<WR20FaceObj> faceList = wr20TenementObj.getFaces();
        List<String[]> faceIdList = new ArrayList<>();
        if (CollUtil.isNotEmpty(faceList)) {
            //添加或修改人脸
            ProjectFaceResources face = null;
            for (WR20FaceObj faceObj : faceList) {
                face = new ProjectFaceResources();
                face.setFaceCode(faceObj.getID());
                face.setOrigin(OriginTypeEnum.WEB.code);
                face.setPersonType(personTypeEnum.code);
                face.setPersonId(personId);
                face.setPicUrl(imgConvertUtil.convertToLocalUrl(faceObj.getUrl()));

                String faceId = projectFaceResourcesService.saveByThirdCode(face);
                faceIdList.add(new String[]{faceId, face.getPicUrl(), face.getFaceCode()});
            }


            //需要删除的人脸
            if (CollUtil.isNotEmpty(oriFaceList)) {
                delFaceList = oriFaceList.stream().filter(e -> e.getFaceCode() != null && faceList.stream().map(f -> f.getID()).collect(Collectors.toList()).contains(e.getFaceCode()) == false).collect(Collectors.toList());
            }
        } else {
            delFaceList = oriFaceList;
        }
        if (CollUtil.isNotEmpty(delFaceList)) {
            //删除当前人脸所有下载状态
            for (ProjectFaceResources face : delFaceList) {
                projectRightDeviceService.removeCert(face.getFaceId());
                projectFaceResourcesService.removeById(face.getFaceId());
            }
//            webProjectFaceResourcesService.removeByIds(delFaceList);
        }


        //保存凭证在设备上的状态，通过来的数据为 下载完成。如果凭证已存在，设置为下载完成。如果凭证不存在，进行添加

        if (CollUtil.isNotEmpty(personDeviceList)) {

            //每台设备设置凭证
            for (ProjectPersonDevice personDevice : personDeviceList) {

                //每个卡片凭证
                if (CollUtil.isNotEmpty(cardIdList)) {
                    for (String[] cardId : cardIdList) {
                        saveOrUpdateRightDevice(personDevice, cardId[0], cardId[1], null, personName, phone, personTypeEnum, CertmediaTypeEnum.Card);
                    }

                }

                //每个人脸凭证
                if (CollUtil.isNotEmpty(faceIdList)) {
                    for (String[] faceId : faceIdList) {
                        saveOrUpdateRightDevice(personDevice, faceId[0], faceId[1], faceId[2], personName, phone, personTypeEnum, CertmediaTypeEnum.Face);
                    }
                }

            }
        }
    }

    /**
     * 创建或者更新下载状态
     *
     * @param personDevice
     * @param certId
     * @param certmediaTypeEnum
     */
    private void saveOrUpdateRightDevice(ProjectPersonDevice personDevice, String certId, String certInfo, String certCode, String personName, String phone, PersonTypeEnum personTypeEnum, CertmediaTypeEnum certmediaTypeEnum) {
        ProjectRightDevice rightDevice = new ProjectRightDevice();
        rightDevice.setPersonId(personDevice.getPersonId());
        rightDevice.setDlStatus(PassRightCertDownloadStatusEnum.SUCCESS.code);
        rightDevice.setCertMediaId(certId);
        rightDevice.setCertMedia(certmediaTypeEnum.code);
        rightDevice.setDeviceId(personDevice.getDeviceId());
        rightDevice.setMobileNo(phone);
        rightDevice.setPersonName(personName);
        rightDevice.setPersonType(personTypeEnum.code);
        rightDevice.setCertMediaCode(certCode);

        rightDevice.setUid(UUID.randomUUID().toString().replaceAll("-", ""));
        rightDevice.setCertMediaInfo(certInfo);


        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda().eq(ProjectRightDevice::getCertMediaId, certId).eq(ProjectRightDevice::getDeviceId, personDevice.getDeviceId()));

        if (CollUtil.isNotEmpty(rightDeviceList)) {
            ProjectRightDevice rightDevicePo = rightDeviceList.get(0);
            rightDevicePo.setDlStatus(PassRightCertDownloadStatusEnum.SUCCESS.code);
            projectRightDeviceService.updateById(rightDevicePo);
        } else {
            projectRightDeviceService.save(rightDevice);
        }
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
