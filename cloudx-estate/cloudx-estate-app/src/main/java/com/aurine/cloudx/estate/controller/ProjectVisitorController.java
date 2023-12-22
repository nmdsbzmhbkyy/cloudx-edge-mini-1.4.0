

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.estate.constant.ProjectConfigConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.RemoteVisitorService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;


/**
 * 访客
 * (ProjectRepairRecordController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 14:40
 */
@RestController
@RequestMapping("/visitor")
@Api(value = "visitor", tags = "访客管理")
@Slf4j
public class ProjectVisitorController {
    @Resource
    private ProjectVisitorService projectVisitorService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private RemoteVisitorService remoteVisitorService;
    @Resource
    private ImgConvertUtil imgConvertUtil;

    @Resource
    private ProjectStaffService projectStaffService;

    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取访客申请记录列表（来访审核）", notes = "获取访客申请记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "status", value = "审核状态 不传为所有，0: 待抢单 1: 待完成 2: 已完成 其他值参考字典类型 complaint_status", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
//            @ApiImplicitParam(name = "total", value = "总条数", paramType = "query")
    })

    @GetMapping("/apply/page")
//    public R<Page<ProjectVisitorRecordVo>> fetchListByPerson(Page page, @RequestParam(value = "status", required = false) String status) {
    public R<Page<ProjectVisitorRecordVo>> fetchListByPerson(Long current, Long size, @RequestParam(value = "status", required = false) String status) {

        AppPage page = new AppPage(current, size);

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectVisitorService.getPageByPerson(page, projectPersonInfo.getPersonId(), status));
    }

    /**
     * <p>
     * 拒绝访客申请
     * </p>
     *
     * @return
     * @throws
     */
    @PutMapping("/reject-audit")
    @ApiOperation(value = "拒绝访客申请（来访审核）", notes = "拒绝访客申请")
    @SysLog("拒绝访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R<String> rejectAudit(@RequestBody AppVisitorRejectAuditFormVo visitorRejectAuditFormVo) {
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        projectVisitorVo.setVisitId(visitorRejectAuditFormVo.getVisitId());
        projectVisitorVo.setRejectReason(visitorRejectAuditFormVo.getRejectReason());
        return remoteVisitorService.rejectAudit(projectVisitorVo);
    }

    /**
     * <p>
     * 业主审核通过
     * </p>
     *
     * @return
     * @throws
     */
    @PutMapping("/pass-audit")
    @ApiOperation(value = "同意访客申请（来访审核）", notes = "同意访客申请")
    @SysLog("业主审核通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<String> passAuditByPerson(@RequestBody AppPassAuditByPersonVo passAuditByPersonVo) {

        ProjectConfig config = projectConfigService.getConfig();
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        projectVisitorVo.setVisitId(passAuditByPersonVo.getVisitId());
        if (ObjectUtil.isNotEmpty(config) && ProjectConfigConstant.SYSTEM_IDENTITY.equals(config.getVisitorAudit())) {
            //如果是系统审核直接通过
            return remoteVisitorService.passAudit(projectVisitorVo);
        } else {
            return remoteVisitorService.homeowersPassAudit(projectVisitorVo);
        }
    }

    /**
     * <p>
     * 获取申请数据
     * </p>
     *
     * @param visitId 访客申请id
     * @return
     * @throws
     */
    @GetMapping("/{visitId}")
    @ApiOperation(value = "获取访客申请数据明细或者访客邀约申请明细（访客邀约）", notes = "获取访客申请数据明细或者访客邀约申请明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客id", required = true, paramType = "path")
    })
    public R<ProjectVisitorVo> getData(@PathVariable("visitId") String visitId) {
        return R.ok(projectVisitorService.getDataById(visitId));
    }

    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param
     * @return
     */
    @ApiOperation(value = "获取邀约访客申请记录列表（访客邀约）", notes = "获取邀约访客申请记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "status", value = "审核状态 不传为所有，0: 待抢单 1: 待完成 2: 已完成 其他值参考字典类型 complaint_status", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
//            @ApiImplicitParam(name = "total", value = "总条数", paramType = "query")
    })
    @GetMapping("/invitation/page")
    public R<Page<ProjectVisitorRecordVo>> fetchListByOperator(Long current, Long size, @RequestParam(value = "status", required = false) String status) {
        AppPage page = new AppPage(current, size);

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectVisitorService.getPageByCreate(page, projectPersonInfo.getPersonId(), status));
    }

    /**
     * 住户邀请 访客邀约登记
     *
     * @param projectVisitorRequestFormVo 访客vo对象
     * @return R
     */
    @ApiOperation(value = "访客邀约登记（访客邀约）", notes = "访客邀约登记")
    @SysLog("住户邀请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/register")
    public R<String> registerByOwner(@RequestBody ProjectVisitorRequestFormVo projectVisitorRequestFormVo) throws IOException {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        BeanUtil.copyProperties(projectVisitorRequestFormVo, projectVisitorVo);
        projectVisitorVo.setVisitPersonId(projectPersonInfo.getPersonId());
        projectVisitorVo.setAuditStatus(AuditStatusEnum.inAudit.code);

        String[] deviceIds = projectPersonDeviceService.listDeviceByPersonId(projectPersonInfo.getPersonId()).stream().map(r -> r.getDeviceId()).toArray(String[]::new);
        //设置人脸
        if (DataConstants.TRUE.equals(projectVisitorRequestFormVo.getIsFace()) && StringUtils.isNotBlank(projectVisitorRequestFormVo.getFaceUrl())) {
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            projectFaceResources.setPicUrl(imgConvertUtil.base64ToMinio(projectVisitorRequestFormVo.getFaceUrl()));
            projectFaceResources.setPersonType(PersonTypeEnum.VISITOR.code);
            projectVisitorVo.setFaceList(Lists.newArrayList(projectFaceResources));
        } else if (DataConstants.TRUE.equals(projectVisitorRequestFormVo.getIsFace()) && StringUtils.isBlank(projectVisitorRequestFormVo.getFaceUrl())) {
            return R.failed("请添加人脸图片");
        }
        projectVisitorVo.setDeviceIdArray(deviceIds);
        projectVisitorVo.setPersonCount(1);
        projectVisitorVo.setRegisterType(RegisterTypeEnum.residentApplication.code);
        // 来源于业主
        projectVisitorVo.setOriginEx(DataOriginExEnum.YZ.code);
        // 来源于app
        projectVisitorVo.setOrigin(OriginTypeEnum.APP.code);


        //访客邀约默认设置被访人房屋为当前名下能检索到的第一个房屋
        List<ProjectHousePersonRel> visitorHisList = projectHousePersonRelService.list(Wrappers.lambdaQuery(ProjectHousePersonRel.class)
                .eq(ProjectHousePersonRel::getAuditStatus, AuditStatusEnum.pass.code)
                .eq(ProjectHousePersonRel::getPersonId, projectPersonInfo.getPersonId())
                .orderByAsc(ProjectHousePersonRel::getHouseholdType, ProjectHousePersonRel::getCreateTime));
        if (visitorHisList.size() > 0) {
            projectVisitorVo.setVisitHouseId(visitorHisList.get(0).getHouseId());
        }
        return remoteVisitorService.registerVo(projectVisitorVo);
    }

    /**
     * <p>
     * 物业审核通过
     * </p>
     *
     * @return
     * @throws
     */
    @ApiOperation(value = "物业审核通过(物业)", notes = "物业审核通过")
    @SysLog("物业审核通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客申请历史记录id", required = true, paramType = "path"),
    })
    @PutMapping("/staff/pass/{visitId}")
    public R<String> passAuditByStaff(@PathVariable("visitId") String visitId) {
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        projectVisitorVo.setVisitId(visitId);
        projectVisitorVo.setAuditStatus(AuditStatusEnum.pass.code);
        R passAudit = remoteVisitorService.passAudit(projectVisitorVo);
        if (passAudit.getCode() == 0) {
            return R.ok("审核通过");
        } else {
            return R.failed("操作失败");
        }
    }

    /**
     * <p>
     * 拒绝访客申请
     * </p>
     *
     * @return
     * @throws
     */
    @ApiOperation(value = "拒绝访客申请(物业)", notes = "拒绝访客申请")
    @SysLog("拒绝访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客申请历史记录id", required = true, paramType = "body"),
            @ApiImplicitParam(name = "rejectReason", value = "拒绝原因", required = true, paramType = "body")
    })
    @PutMapping("/staff/reject")
    public R rejectAudit(@RequestBody AppVisitorRejectVo appVisitorRejectVo) {
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        projectVisitorVo.setVisitId(appVisitorRejectVo.getVisitId());
        projectVisitorVo.setRejectReason(appVisitorRejectVo.getRejectReason());
        return remoteVisitorService.rejectAudit(projectVisitorVo);
    }
    /**
     * 分页查询(物业查询访客申请记录)
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "物业查询访客申请记录( 物业)", notes = "分页查询(物业查询访客申请记录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
    })

    @GetMapping("/staff/page")
    public R<Page<ProjectVisitorRecordVo>> fetchList(Page page, ProjectVisitorSearchConditionVo searchCondition) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            //不是该小区员工提示异常
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectVisitorService.getPage(page, searchCondition));
    }

}
