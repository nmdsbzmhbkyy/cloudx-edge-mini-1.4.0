

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.feign.RemoteVisitorService;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.constant.ProjectConfigConstant;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.DataOriginExEnum;
import com.aurine.cloudx.estate.constant.enums.OriginTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.constant.enums.RegisterTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectConfig;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectHousePersonRel;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectVisitor;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectHousePersonRelService;
import com.aurine.cloudx.estate.service.ProjectPersonDeviceService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.service.ProjectVisitorService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoFormVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorRecordVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorRequestFormVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectVisitorVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
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
@RequestMapping("/serviceVisitor")
@Api(value = "serviceVisitor", tags = "访客管理")
@Slf4j
public class ProjectVisitorController {
    @Resource
    private ProjectVisitorService projectVisitorService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectConfigService projectConfigService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private RemoteVisitorService remoteVisitorService;
    @Resource
    private NoticeUtil noticeUtil;

    /**
     * 分页查询(物业查询访客申请记录)
     *
     * @param page            分页对象
     * @param searchCondition 查询条件
     * @return
     */
    @ApiOperation(value = "物业查询访客申请记录( 物业)", notes = "分页查询(物业查询访客申请记录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/page")
    public R<Page<ProjectVisitorRecordVo>> fetchList(Page page, ProjectVisitorSearchConditionVo searchCondition) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            //不是该小区员工提示异常
            return R.failed("您在该小区下还未登记");
        }

        return R.ok(projectVisitorService.getPage(page, searchCondition));
    }

    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = " 业主查询访客申请记录(业主)", notes = "分页查询(业主查询访客申请记录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "审核状态", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/pageByPerson")
    public R<Page<ProjectVisitorRecordVo>> fetchListByPerson(Page page, @RequestParam(value = "status", required = false) String status) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectVisitorService.getPageByPerson(page, projectPersonInfo.getPersonId(), status));
    }


    /**
     * 分页查询(业主查询访客申请记录)
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = " 业主查询邀约访客申请记录(业主)", notes = "分页查询(业主查询邀约访客申请记录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "审核状态", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })

    @GetMapping("/pageByOperator")
    public R<Page<ProjectVisitorRecordVo>> fetchListByOperator(Page page, @RequestParam(value = "status", required = false) String status) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectVisitorService.getPageByCreate(page, projectPersonInfo.getPersonId(), status));
    }

    /**
     * 分页查询(访客查询申请记录)
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "访客查询申请记录(访客)", notes = "分页查询(访客查询申请记录)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "审核状态(0:待住户审核1:待业主审核2:已通过:审核失败)", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/pageByVisitor")
    public R<Page<ProjectVisitorRecordVo>> fetchListByVisitor(Page page, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "date", required = false) String date) {

        return R.ok(projectVisitorService.getPageByVisitor(page, SecurityUtils.getUser().getId(), status, date));
    }


    /**
     * 住户邀请
     *
     * @param projectVisitorRequestFormVo 访客vo对象
     * @return R
     */
    @ApiOperation(value = "住户邀请(业主)", notes = "住户邀请")
    @SysLog("住户邀请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/registerByOwner")
    public R registerByOwner(@RequestBody ProjectVisitorRequestFormVo projectVisitorRequestFormVo) {
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
            projectFaceResources.setPicUrl(projectVisitorRequestFormVo.getFaceUrl());
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
        // 来源于微信端
        projectVisitorVo.setOrigin(OriginTypeEnum.WECHAT.code);


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
     * 访客申请
     *
     * @param projectVisitorRequestFormVo 访客vo对象
     * @return R
     */
    @ApiOperation(value = "访客申请(访客)", notes = "访客申请")
    @SysLog("提交访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/registerByVisitor")
    public R registerByVisitor(@RequestBody ProjectVisitorRequestFormVo projectVisitorRequestFormVo) {
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        BeanUtil.copyProperties(projectVisitorRequestFormVo, projectVisitorVo);
        String[] deviceIds;
        if (StringUtils.isNotBlank(projectVisitorRequestFormVo.getVisitHouseId())) {
            ProjectHousePersonRel projectHousePersonRel = projectHousePersonRelService.getOne(new QueryWrapper<ProjectHousePersonRel>().lambda()
                    .eq(ProjectHousePersonRel::getHouseId, projectVisitorRequestFormVo.getVisitHouseId())
                    .eq(ProjectHousePersonRel::getPersonId, projectVisitorRequestFormVo.getVisitPersonId()));
            if (ObjectUtil.isNotEmpty(projectHousePersonRel)) {
                //如果是访问业主则设置业主审核，且赋予业主通行权限
                projectVisitorVo.setAuditStatus(AuditStatusEnum.waitAudit.code);
                deviceIds = projectPersonDeviceService.listDeviceByPersonId(projectVisitorRequestFormVo.getVisitPersonId()).stream().map(r -> r.getDeviceId()).toArray(String[]::new);
            } else {
                return R.failed("该住户不存在");
            }
        } else {
            //如果是其他则交由物业审核，且赋予区口访问权限
            projectVisitorVo.setAuditStatus(AuditStatusEnum.inAudit.code);
            ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
            projectDeviceInfoFormVo.setTypes(Lists.newArrayList(DeviceTypeEnum.GATE_DEVICE.getCode()));
            deviceIds = projectDeviceInfoService.findByType(projectDeviceInfoFormVo).stream().map(r -> r.getDeviceId()).toArray(String[]::new);
        }
        //设置人脸
        if (DataConstants.TRUE.equals(projectVisitorRequestFormVo.getIsFace()) && StringUtils.isNotBlank(projectVisitorRequestFormVo.getFaceUrl())) {
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            projectFaceResources.setPicUrl(projectVisitorRequestFormVo.getFaceUrl());
            projectFaceResources.setPersonType(PersonTypeEnum.VISITOR.code);
            projectVisitorVo.setFaceList(Lists.newArrayList(projectFaceResources));
        } else if (DataConstants.TRUE.equals(projectVisitorRequestFormVo.getIsFace()) && StringUtils.isBlank(projectVisitorRequestFormVo.getFaceUrl())) {
            return R.failed("请添加人脸图片");
        }

        projectVisitorVo.setPicUrl(projectVisitorRequestFormVo.getFaceUrl());

        projectVisitorVo.setDeviceIdArray(deviceIds);
        projectVisitorVo.setPersonCount(1);
        projectVisitorVo.setRegisterType(RegisterTypeEnum.independentApplication.code);

        /*projectVisitorVo.setMobileNo(SecurityUtils.getUser().getUsername());*/

        //设置来源
        projectVisitorVo.setOrigin(DataOriginEnum.WECHAT.code);
        projectVisitorVo.setOriginEx(DataOriginExEnum.FK.code);
        //如果是访问具体人员则需住户审核 否则物业审核
        if (StringUtil.isNotBlank(projectVisitorVo.getVisitPersonId())) {
            projectVisitorVo.setAuditStatus(AuditStatusEnum.waitAudit.code);
        } else {
            projectVisitorVo.setAuditStatus(AuditStatusEnum.inAudit.code);
        }

        R r = remoteVisitorService.registerVo(projectVisitorVo);
        if (r.getCode() == 0) {

/*
            try {
                //来访申请通知业主
                noticeUtil.send(false, "来访申请", "您有新的来访申请，请尽快审核", projectVisitorRequestFormVo.getVisitPersonId());
            } catch (Exception e) {
                log.warn("消息发送异常", e);
                return R.failed(e.getMessage());
            }
*/

            return R.ok("操作成功");
        }
        return R.failed("操作失败");
    }

    /**
     * <p>
     * 拒绝访客申请
     * </p>
     *
     * @param rejectReason 拒绝原因
     * @param visitId      访客申请历史记录id
     * @return
     * @throws
     */
    @ApiOperation(value = "拒绝访客申请(业主、物业)", notes = "拒绝访客申请")
    @SysLog("拒绝访客申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitId", value = "访客申请历史记录id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "rejectReason", value = "拒绝原因", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/rejectAudit")
    public R rejectAudit(@RequestParam String visitId, @RequestParam String rejectReason) {
        ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
        projectVisitorVo.setVisitId(visitId);
        projectVisitorVo.setRejectReason(rejectReason);


        return remoteVisitorService.rejectAudit(projectVisitorVo);
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
    @ApiOperation(value = "获取申请数据明细(业主、物业、访客)", notes = "获取申请数据明细")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitId", value = "访客id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getData/{visitId}")
    public R<ProjectVisitorVo> getData(@PathVariable("visitId") String visitId) {
        return R.ok(projectVisitorService.getDataById(visitId));
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
            @ApiImplicitParam(name = "visitId", value = "访客申请历史记录id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/passAuditByStaff/{visitId}")
    public R passAuditByStaff(@PathVariable("visitId") String visitId) {
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
     * 业主审核通过
     * </p>
     *
     * @return
     * @throws
     */
    @ApiOperation(value = "业主审核通过(业主)", notes = "业主审核通过")
    @SysLog("业主审核通过")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客申请历史记录id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/passAuditByPerson/{visitId}")
    public R passAuditByPerson(@PathVariable("visitId") String visitId) {

        ProjectConfig config = projectConfigService.getConfig();
        boolean passAudit;

        if (ObjectUtil.isNotEmpty(config) && ProjectConfigConstant.SYSTEM_IDENTITY.equals(config.getVisitorAudit())) {
            //如果是系统审核直接通过
            ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
            projectVisitorVo.setVisitId(visitId);
            projectVisitorVo.setAuditStatus(AuditStatusEnum.pass.code);
            return remoteVisitorService.passAudit(projectVisitorVo);
        } else {
            ProjectVisitorVo projectVisitorVo = new ProjectVisitorVo();
            projectVisitorVo.setVisitId(visitId);

            return remoteVisitorService.homeowersPassAudit(projectVisitorVo);
        }

    }

    /**
     * <p>
     * 获取访客数据
     * </p>
     *
     * @return
     * @throws
     */
    @ApiOperation(value = "获取访客数据(访客)", notes = "获取访客数据")
    @SysLog("使用手机号获取访客数据")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getByMobileNo")
    public R getByMobileNo() {
        List<ProjectVisitor> list = projectVisitorService.list(new QueryWrapper<ProjectVisitor>().lambda().eq(ProjectVisitor::getMobileNo, SecurityUtils.getUser().getUsername()));
        if (CollUtil.isNotEmpty(list)) {
            return R.ok(list.get(0));
        } else {
            return R.ok();
        }
    }


}
