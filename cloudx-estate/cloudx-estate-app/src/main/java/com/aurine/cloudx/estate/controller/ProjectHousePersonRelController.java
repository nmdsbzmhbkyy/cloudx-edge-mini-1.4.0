package com.aurine.cloudx.estate.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.feign.RemoteHousePersonRelService;
import com.aurine.cloudx.estate.feign.RemoteImageService;
import com.aurine.cloudx.estate.feign.RemoteWebSocketService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 住户管理 (ProjectHousePersonRelController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 10:13
 */
@RestController
@RequestMapping("/house")
@Api(value = "house", tags = "住户管理")
public class ProjectHousePersonRelController {
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectHouseInfoService projectHouseInfoService;
    @Resource
    private RemoteHousePersonRelService remoteHousePersonRelService;
    @Resource
    private RemoteImageService remoteImageService;
    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private RemoteWebSocketService remoteWebSocketService;


    /**
     * 获取当前账号的小区房屋状态
     *
     * @return
     */
    @GetMapping("/status")
    @ApiOperation(value = "获取当前账号的小区房屋状态", notes = "获取当前账号的小区房屋状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<AppPersonInfoVo> getProjectHouseStatus() {
        List<ProjectHouseStatusVo> projectHouseStatusList = projectPersonInfoService.getListPerson();
        if (projectHouseStatusList.size() < 1) {
            return R.failed("您还未登记");
        }
        AppPersonInfoVo appPersonInfoVo = new AppPersonInfoVo();
        List<String> projectIdList = new ArrayList<>();
        for (ProjectHouseStatusVo projectHouseStatusVo : projectHouseStatusList) {
            if (!projectIdList.contains(projectHouseStatusVo.getProjectId())) {
                projectIdList.add(projectHouseStatusVo.getProjectId());
            }
            if (appPersonInfoVo.getAuditStatus() == null || !appPersonInfoVo.getAuditStatus().equals("2")) {
                appPersonInfoVo.setAuditStatus(projectHouseStatusVo.getAuditStatus());
            }
        }
        appPersonInfoVo.setProjectIdList(projectIdList);
        return R.ok(appPersonInfoVo);
    }

    /**
     * 业主分页查询关联的房屋信息
     *
     * @return
     */
    @ApiOperation(value = "获取当前小区业主的房屋列表（房屋）", notes = "获取当前小区业主的房屋列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
//            @ApiImplicitParam(name = "total", value = "总条数", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "房屋状态 不传为所有，1 审核中 2 通过 9 不通过", required = false, paramType = "query")
    })
    @GetMapping("/page")
//    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page, String status) {
    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Long size, Long current, String status) {

        AppPage page = new AppPage(current, size);

//        page.setTotal(total);

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.ok(page);
        }
        return R.ok(projectHousePersonRelService.filterPageById(page, projectPersonInfo.getPersonId(), status));
    }

    /**
     * 住户新增房屋申请
     *
     * @param projectHousePersonRel 住户
     * @return R
     */
    @ApiOperation(value = "住户新增房屋申请或者新增房屋成员申请（房屋）", notes = "住户新增房屋申请或者新增房屋成员申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("住户新增房屋申请")
    @PostMapping
    public R save(@RequestBody ProjectHousePersonRelRequestVo projectHousePersonRel) throws IOException {
        if (StringUtils.isEmpty(projectHousePersonRel.getTelephone())) {
            projectHousePersonRel.setTelephone(SecurityUtils.getUser().getUsername());
        }
        projectHousePersonRel.setOrigin(DataOriginEnum.APP.code);
        projectHousePersonRel.setOriginEx(DataOriginExEnum.YZ.code);
        projectHousePersonRel.setCheckInTime(LocalDateTime.now());
        ProjectPersonInfo personInfo = projectPersonInfoService.getPersonByOwner();
        if (StrUtil.isNotBlank(projectHousePersonRel.getCredentialPicFront()) && StrUtil.isNotBlank(projectHousePersonRel.getCredentialPicBack())) {
            projectHousePersonRel.setCredentialPicFront(remoteImageService.base64ToImage(projectHousePersonRel.getCredentialPicFront()));
            projectHousePersonRel.setCredentialPicBack(remoteImageService.base64ToImage(projectHousePersonRel.getCredentialPicBack()));
        }
        if (!ObjectUtil.isEmpty(personInfo)) {
            projectHousePersonRel.setRemark(personInfo.getPersonId());
        }
        R r = projectHousePersonRelService.request(projectHousePersonRel);
        if (r.getCode() == 0) {
            r.setData(true);
        } else {
            r.setData(false);
        }
        return r;
    }

    /**
     * 获取当前用户所有的房屋信息列表
     *
     * @return R
     */
    @ApiOperation(value = "获取当前用户所有的房屋信息列表（房屋）", notes = "获取当前用户所有的房屋信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/list")
    public R<List<ProjectUserHouseInfoVo>> getUserHouseInfo() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPersonInfoService.getUserHouseInfo(projectPersonInfo.getUserId()));
    }

    /**
     * 住户重新审核申请
     *
     * @param projectHousePersonRelRequestAgainVo 住户
     * @return R
     */
    @ApiOperation(value = "住户新增房屋重新申请（房屋）", notes = "住户新增房屋重新申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("住户重新审核申请")
    @PostMapping("/review-again")
    public R requestAgain(@RequestBody ProjectHousePersonRelRequestAgainVo projectHousePersonRelRequestAgainVo) {

        R r = projectHousePersonRelService.requestAgain(projectHousePersonRelRequestAgainVo);
        remoteWebSocketService.transferSocket(ProjectContextHolder.getProjectId().toString());
        if (r.getCode() == 0) {
            r.setData(true);
        } else {
            r.setData(false);
        }
        return r;
    }

    /**
     * 通过id迁出住户
     *
     * @param id
     * @return R
     */
    @ApiOperation(value = "通过房屋id迁出住户（房屋）", notes = "通过房屋id迁出住户")
    @SysLog("通过id迁出住户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path")
    })
    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable String id) {
        R r = remoteHousePersonRelService.removeById(id);
        if (r.getCode() == 0) {
            r.setData(true);
        } else {
            r.setData(false);
        }
        return r;
    }

    /**
     * 查询该房屋的所有住户 :TODO
     *
     * @param houseId id
     * @return
     * @throws
     */
    @ApiOperation(value = "通过房屋ID获取房屋的所有房屋成员（房屋）")
    @SysLog("房屋住户信息查看")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, paramType = "param")
    })
    @GetMapping("/person/list/{houseId}")
    public R<List<ProjectHouseResidentVo>> getHouseResident(@PathVariable("houseId") String houseId,
                                    @RequestParam(value = "phone", required = false) String phone) {
        return R.ok(projectHouseInfoService.getHouseResidentsWithoutStatus(houseId, phone));
    }

    /**
     * 房间内是否已经有业主
     *
     * @param houseId houseId
     * @return R
     */
    @ApiOperation(value = "根据房屋ID查询房屋内是否已经有业主（房屋）", notes = "根据房屋ID查询房屋内是否已经有业主")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path")
    })
    @GetMapping("/have-owner/{houseId}")
    public R<Boolean> haveOwner(@PathVariable("houseId") String houseId) {
        return R.ok(projectHousePersonRelService.haveOwner(houseId));
    }

    /**
     * 通过id查询更多信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过房屋id查询更多房屋相关信息（房屋）", notes = "通过房屋id查询更多房屋相关信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path")
    })
    @GetMapping("/more/{id}")
    public R<ProjectHousePersonRelVo> getMoreInfoById(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.getVoById(id));
    }

    @ApiOperation(value = "通过房屋id和人员id进行迁出（房屋）", notes = "通通过房屋id和人员id进行迁出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "houseId", value = "房屋id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path")
    })
    @DeleteMapping("/{houseId}/{personId}")
    public R<Boolean> removeById(@PathVariable("houseId") String houseId, @PathVariable("personId") String personId) {
        projectHousePersonRelService.remove(new QueryWrapper<ProjectHousePersonRel>().lambda()
                .eq(ProjectHousePersonRel::getHouseId, houseId)
                .eq(ProjectHousePersonRel::getPersonId, personId));
        return R.ok();
    }

    /**
     * 通过id查询住户房屋信息
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过房屋关系id查询身份认证详情(物业)", notes = "通过房屋关系id查询身份认证详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "房屋关系id", required = true, paramType = "path"),
    })
    @GetMapping("/staff/verify/{id}")
    public R<ProjectHousePersonRelVo> getById(@PathVariable("id") String id) {
        return R.ok(projectHousePersonRelService.getVoById(id));
    }

    /**
     * 身份认证分页
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "身份认证分页(物业)", notes = "身份认证分页(物业)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    @GetMapping("/staff/verify/page")
    public R<IPage<ProjectHousePersonRelRecordVo>> pageIdentity(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.pageIdentity(page, searchCondition));
    }


    @PutMapping("/staff/verify")
    @ApiOperation(value = "身份认证审核(物业)", notes = "身份认证审核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @SysLog("身份认证审核")
    public R verify(@RequestBody AppHousePersonVerifyVo housePersonVerifyVo) {
        ProjectHousePersonRelVo projectHousePersonRelVo = new ProjectHousePersonRelVo();
        BeanUtil.copyProperties(housePersonVerifyVo, projectHousePersonRelVo);
        return R.ok(remoteHousePersonRelService.verify(projectHousePersonRelVo));
    }

    /**
     * 小程序新增住户
     *
     * @param appHousePersonRelVo 住户
     * @return R
     */
    @ApiOperation(value = "物业新增住户（物业）", notes = "新增住户")
    @SysLog("app新增住户")
    @PostMapping("/staff")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<String> save(@RequestBody AppHousePersonRelVo appHousePersonRelVo) {
        ProjectHousePersonRelVo projectHousePersonRel = new ProjectHousePersonRelVo();
        BeanUtil.copyProperties(appHousePersonRelVo, projectHousePersonRel);
        /**
         * 初始化住户、员工权限凭证，避免首次访问无通行方案
         * 如果用户已有方案，获取方案id，如果没有，分配默认方案，并返回方案id
         */
        String personId = projectPersonInfoService.getPersonId(projectHousePersonRel);
        projectPersonDeviceService.initDefaultPassRightPlan(PersonTypeEnum.PROPRIETOR, personId);
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(personId);
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) &&
                DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                LocalDateTime.now().isBefore(personPlanRel.getExpTime())) {
            projectHousePersonRel.setPersonId(personId);
            projectHousePersonRel.setOrigin(DataOriginEnum.APP.code);
            projectHousePersonRel.setOriginEx(DataOriginExEnum.WY.code);
            projectHousePersonRel.setCheckInTime(LocalDateTime.now());
            projectHousePersonRel.setRentStartTime(LocalDateTime.now());
            projectHousePersonRel.setAuditStatus(AuditStatusEnum.pass.code);
            remoteHousePersonRelService.saveRel(projectHousePersonRel);
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            projectFaceResources.setPersonId(personId);
            projectFaceResources.setPersonType(PersonTypeEnum.PROPRIETOR.code);
            projectFaceResources.setOrigin(DataOriginEnum.APP.code);
            projectFaceResources.setPicUrl(projectHousePersonRel.getPicUrl());
            projectFaceResources.setStatus("1");
            return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
        }
        return R.failed("该住户通行权限已被禁用，请启用后再进行配置");
    }

    @ApiOperation(value = "物业登记的住户分页（物业）", notes = "物业登记的住户分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "dlStatus", value = "授权状态",required = false, paramType = "param")
    })
    @GetMapping("/staff/register/page")
    public R<Page<ProjectFaceResourceAppPageVo>> getByPersonId(Page page,
                                           @RequestParam(value = "dlStatus",required = false) String dlStatus) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        //查询房屋含人脸授权信息
        return R.ok(projectFaceResourcesService.pagePersonFace(page, dlStatus));
    }

    /**
     * 分页查询住户信息(物业)
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "分页查询住户信息(物业)", notes = "分页查询住户信息(物业)")
    @GetMapping("/person/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    public R<IPage<ProjectHousePersonRelRecordVo>> getProjectHousePersonRelPage(Page page, ProjectHousePersonRelSearchConditionVo searchCondition) {
        return R.ok(projectHousePersonRelService.findPage(page, searchCondition));
    }
}
