

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.ProjectInfoConstant;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.entity.ProjectParkingPlace;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 * 项目
 *
 * @author xull@aurine.cn
 * @date 2020-05-06 19:14:05
 */
@RestController
@RequestMapping("/projectInfo")
@Api(value = "projectInfo", tags = "项目管理")
public class ProjectInfoController {
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private ProjectParkEntranceHisService projectParkEntranceHisService;
    @Resource
    private ProjectVisitorHisService projectVisitorHisService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectEntranceAlarmEventService projectEntranceAlarmEventService;
    @Resource
    private ProjectParkingPlaceService projectParkingPlaceService;
    @Resource
    private ProjectParkBillingInfoService projectParkBillingInfoService;
    @Resource
    private ProjectEntranceEventService projectEntranceEventService;

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param projectInfo 项目
     * @return 放回项目分页信息
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<com.aurine.cloudx.estate.vo.ProjectInfoPageVo>> getProjectInfoPage(Page page, ProjectInfo projectInfo) {
        projectInfo.setProjectGroupId(ProjectContextHolder.getProjectId());
        return R.ok(projectInfoService.pageProject(page, projectInfo));
    }

    /**
     * 根据角色分页查询
     *
     * @param page                     分页对象
     * @param projectInfoByAdminFormVo 项目
     * @return 返回项目分页信息
     */
    @ApiOperation(value = "根据角色分页查询", notes = "根据角色分页查询")
    @GetMapping("/pageByAdmin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<com.aurine.cloudx.estate.vo.ProjectInfoPageVo>> pageByAdmin(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        return R.ok(projectInfoService.pageByAdmin(page, projectInfoByAdminFormVo));
    }

    /**
     * 根据角色分页查询
     *
     * @param page                     分页对象
     * @param projectInfoByAdminFormVo 项目
     * @return 返回项目分页信息
     */
    @ApiOperation(value = "根据角色分页查询", notes = "根据角色分页查询")
    @GetMapping("/pageByVisible")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectInfoPageVo>> pageByVisible(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        return R.ok(projectInfoService.pageByVisible(page, projectInfoByAdminFormVo));
    }

    /**
     * 分页查询项目配置
     *
     * @param page                     分页对象
     * @param projectInfoByAdminFormVo 项目
     * @return 返回项目分页信息
     */
    @ApiOperation(value = "根据角色分页查询", notes = "根据角色分页查询")
    @GetMapping("/pageByConfig")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectInfoPageVo>> pageByConfig(Page page, ProjectInfoByAdminFormVo projectInfoByAdminFormVo) {
        return R.ok(projectInfoService.pageByConfig(page, projectInfoByAdminFormVo));
    }

    /**
     * 通过id查询项目
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectInfo> getById(@PathVariable("id") Integer id) {
        return R.ok(projectInfoService.getProjectInfoVoById(id));
    }

    /**
     * 新增项目
     *
     * @param projectInfoFormVo 项目
     * @return R
     */
    @ApiOperation(value = "新增项目", notes = "新增项目")
    @SysLog("新增项目")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectinfo_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Integer> save(@RequestBody ProjectInfoFormVo projectInfoFormVo) {
        return R.ok(projectInfoService.saveReturnId(projectInfoFormVo));
    }

    /**
     * 修改项目
     *
     * @param projectInfo 项目
     * @return R
     */
    @ApiOperation(value = "修改项目", notes = "修改项目")
    @SysLog("修改项目")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectinfo_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> updateById(@RequestBody ProjectInfoFormVo projectInfo) {

        String auditStatus = projectInfo.getAuditStatus();
        //审核判断
        if (ProjectInfoConstant.PASSED.equals(auditStatus)) {
            R.failed("该项目已通过，无法修改");
        }
        return R.ok(projectInfoService.updateProjectAndUser(projectInfo));
    }

    /**
     * 通过id删除项目
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目", notes = "通过id删除项目")
    @SysLog("通过id删除项目")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_projectinfo_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> removeById(@PathVariable Integer id) {

        ProjectInfo projectInfo = projectInfoService.getById(id);

        String auditStatus = projectInfo.getAuditStatus();
        if (ProjectInfoConstant.PASSED.equals(auditStatus)) {
            R.failed("该项目已通过，无法删除");
        }
        return R.ok(projectInfoService.removeById(id));
    }

    /**
     * 项目审批
     *
     * @param projectInfoApprovalVo 项目审核查询表单
     * @return R
     */
    @ApiOperation(value = "项目审批", notes = "项目审批")
    @SysLog("项目项目审批")
    @PutMapping("/pass")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R pass(@RequestBody ProjectInfoApprovalVo projectInfoApprovalVo) {
        projectInfoService.passProject(projectInfoApprovalVo);
        /*ProjectInfo projectInfo = projectInfoService.getById(projectInfoApprovalVo.getProjectId());
        if (projectInfoApprovalVo.getPass()) {
            projectInfo.setAuditStatus(ProjectInfoConstant.PASSED);
        } else {
            projectInfo.setAuditStatus(ProjectInfoConstant.FAIL);
        }
        //项目过期时间未填则为永久有效
        if (ObjectUtil.isEmpty(projectInfoApprovalVo.getExpTime())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.parse("2199-12-30 23:59:59",df);
            projectInfo.setExpTime(date);
        } else {
            projectInfo.setExpTime(DateUtil.parseLocalDateTime(projectInfoApprovalVo.getExpTime().toString() + " 23:59:59"));
        }
        projectInfo.setAuditReason(projectInfoApprovalVo.getAuditReason());
        projectInfoService.passByVo(projectInfoApprovalVo);
        projectInfoService.updateById(projectInfo);*/

        return R.ok();
    }


    /**
     * 分页查询当前项目组下的次级项目
     *
     * @param page        分页对象
     * @param projectInfo 项目
     * @return 放回项目分页信息
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/pageOnSelf")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R pageOnSelf(Page<ProjectInfo> page, ProjectInfo projectInfo) {
        LambdaQueryWrapper<ProjectInfo> wrapper = Wrappers.<ProjectInfo>lambdaQuery()
                .eq(ProjectInfo::getProjectGroupId, projectInfo.getProjectGroupId());
        //判断项目名称传参是否为空,为空不进行like操作
        if (projectInfo.getProjectName() != null && !"".equals(projectInfo.getProjectName())) {
            wrapper.like(ProjectInfo::getProjectName, projectInfo.getProjectName());
        }
        return R.ok(projectInfoService.page(page, wrapper));
    }

    @ApiModelProperty(value = "设置项目过期时间", notes = "设置项目过期时间")
    @PostMapping("/setTime")
    @SysLog("设置项目过期时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R setTime(@RequestBody ProjectInfoTimeFormVo projectInfoTimeFormVo) {

        String replace = projectInfoTimeFormVo.getExpTime()+" 23:59:59";
        return R.ok(projectInfoService.update(Wrappers.lambdaUpdate(ProjectInfo.class).eq(ProjectInfo::getProjectId, projectInfoTimeFormVo.getProjectId()).set(ProjectInfo::getExpTime,replace)));
    }

    @ApiModelProperty("更新项目配置状态")
    @PutMapping("/updateType/{id}")
    @SysLog("更新项目配置状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateType(@PathVariable("id") Integer id) {
        return R.ok(projectInfoService.pass(id));
    }

    @ApiModelProperty("获取项目面板统计数据")
    @GetMapping("/dashCount")
    @SysLog("获取项目统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R dashCount() {
        Map<String, Object> dashDataMap = new HashMap<>();
        dashDataMap.put("TEMP_CAR", projectParkEntranceHisService.countTemperCarNumber());//临时停车数
        dashDataMap.put("CURR_VISITOR", projectVisitorHisService.countCurrVisitor());//当前访客(按访问预约区间)
        dashDataMap.put("MAX_VISITOR_30DAYS", projectVisitorHisService.count30DayMostestVisitorAndDate());//30天内最大访客(按实际访问时间)
        dashDataMap.put("ERROR_DEVICE", projectDeviceInfoService.countOfflineDevice());//设备异常数
        dashDataMap.put("ALARM", projectEntranceAlarmEventService.countCurrDay());//获取当天警报数量 0-24小时

        return R.ok(dashDataMap);
    }

    @ApiModelProperty("获取项目通行统计数据")
    @GetMapping("/passCount")
    @SysLog("获取通行统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R passCount() {
        Map<String, Object> dashDataMap = new HashMap<>();

        dashDataMap.put("PROPRIETOR", projectEntranceEventService.countOneDayByPersonType(PersonTypeEnum.PROPRIETOR));//住户
        dashDataMap.put("VISITOR", projectEntranceEventService.countOneDayByPersonType(PersonTypeEnum.VISITOR));//访客
        dashDataMap.put("STAFF", projectEntranceEventService.countOneDayByPersonType(PersonTypeEnum.STAFF));//员工
        dashDataMap.put("OTHER", projectEntranceEventService.countOneDayByPersonType(null));//其他

        return R.ok(dashDataMap);
    }

    @ApiModelProperty("获取项目下所有车场统计数据")
    @GetMapping("/parkCount")
    @SysLog("获取项目车场统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R dashParkCount() {
        Map<String, Object> dashDataMap = new HashMap<>();
        dashDataMap.put("ALL_CAR", projectParkEntranceHisService.countEnterCarNumber());//停车数
        dashDataMap.put("ALL_PLACE", projectParkingPlaceService.count());//项目下的总车位数

        dashDataMap.put("CURR_MONTH_PAY", projectParkBillingInfoService.currMonthPay());//当前月总收入
        return R.ok(dashDataMap);
    }
    @ApiModelProperty("初次入云以云端数据为准，要删除边缘侧数据")
    @DeleteMapping("/delAllObj/{projectId}")
    @SysLog("初次入云以云端数据为准，要删除边缘侧数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R delAllObj(@PathVariable("projectId") Integer projectId) {
        return projectInfoService.delAllObj(projectId);
    }

    @ApiModelProperty("同步")
    @PostMapping("/sync")
    @SysLog("同步")
    public R sync(Integer projectId) {
        return projectInfoService.sync(projectId);
    }

    @ApiModelProperty("获取项目UUID")
    @PostMapping("/getProjectUUID/{projectId}")
    @SysLog("获取项目UUID")
    @Inner
    public R getProjectUUID(@PathVariable("projectId") Integer projectId) {
        return R.ok(projectInfoService.getProjectUUID(projectId));
    }

    @ApiModelProperty("初始化minio桶")
    @PostMapping("/initMinio")
    @SysLog("初始化minio桶")
    @Inner(false)
    public R initMinio() {
        return projectInfoService.initMinio();
    }
}
