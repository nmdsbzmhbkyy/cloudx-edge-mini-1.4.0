package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.common.log.annotation.ProjSysLog;
import com.aurine.cloudx.estate.dto.ProjectStaffDTO;
import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.entity.SysProjectDept;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.service.adapter.AbstractProjectStaffService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@RestController
@RequestMapping("/projectStaff")
@Api(value = "projectStaff", tags = "项目员工信息表管理")
public class ProjectStaffController {
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private AbstractProjectStaffService abstractWebProjectStaffService;
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private SysProjectDeptService sysProjectDeptService;
    @Resource
    private ProjectShiftPlanStaffService projectShiftPlanStaffService;
    @Resource
    private ProjectPatrolPersonService projectPatrolPersonService;

    // 巡更人员
    @Resource
    private ProjectPatrolPersonPointService projectPatrolPersonPointService;
    // 报修投诉
    @Resource
    private ProjectComplaintRecordService projectComplaintRecordService;
    // 报事报修
    @Resource
    private ProjectRepairRecordService projectRepairRecordService;
    // 报事报修
    @Resource
    private ProjectInspectTaskStaffService projectInspectTaskStaffService;

    @Resource
    private ImgConvertUtil imgConvertUtil;


    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param projectStaff 项目员工信息表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ProjSysLog("用户操作日志管理 - 分页查询")
    @GetMapping("/page")
    public R getProjectStaffPage(Page page, ProjectStaff projectStaff) {
        QueryWrapper<ProjectStaff> queryWrapper = new QueryWrapper<ProjectStaff>();
        queryWrapper.like(StringUtils.isNotEmpty(projectStaff.getStaffName()), "staffName", projectStaff.getStaffName());
        queryWrapper.eq(projectStaff.getDepartmentId() != null, "departmentId", projectStaff.getDepartmentId());
        queryWrapper.eq(StringUtils.isNotEmpty(projectStaff.getStaffPost()), "staffPost", projectStaff.getStaffPost());
        queryWrapper.eq(StringUtils.isNotEmpty(projectStaff.getGrade()), "grade", projectStaff.getGrade());
        queryWrapper.eq("projectId", ProjectContextHolder.getProjectId());
        queryWrapper.orderByDesc("createTime");

        return R.ok(projectStaffService.page(page, queryWrapper));
    }


    /**
     * 通过id查询项目员工信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectStaffService.getStaffAttrById(id));
    }

    /**
     * 获取当前员工的信息
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getStaff")
    public R getByUserId() {
        return R.ok(projectStaffService.getStaffByOwner());
    }

    /**
     * 新增项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "新增项目员工信息表", notes = "新增项目员工信息表")
    @SysLog("新增项目员工信息表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectstaff_add')")
    public R save(@RequestBody ProjectStaffDTO projectStaff) {
        //添加员工后继续赋权, 需要返回员工ID - by 王伟 20200611
        String uid = UUID.randomUUID().toString().replace("-", "");
        projectStaff.setStaffId(uid);
        projectStaff.setProjectId(ProjectContextHolder.getProjectId());
        boolean resultFlag = abstractWebProjectStaffService.saveStaff(projectStaff);
        if (resultFlag) {
            return R.ok(uid);
        } else {
            return R.failed("员工添加失败");
        }
    }

    /**
     * 修改项目员工信息表
     *
     * @param projectStaff 项目员工信息表
     * @return R
     */
    @ApiOperation(value = "修改项目员工信息表", notes = "修改项目员工信息表")
    @SysLog("修改项目员工信息表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectstaff_edit')")
    public R updateById(@RequestBody ProjectStaffDTO projectStaff) {
        projectStaff.setProjectId(ProjectContextHolder.getProjectId());
        return R.ok(abstractWebProjectStaffService.editStaff(projectStaff));
    }

    /**
     * 通过id删除项目员工信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目员工信息表", notes = "通过id删除项目员工信息表")
    @SysLog("通过id删除项目员工信息表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_projectstaff_del')")
    public R removeById(@PathVariable("id") String id) {
        return R.ok(abstractWebProjectStaffService.removeStaff(id));
    }

    /**
     * 通过userId删除员工
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "通过userId删除项目员工信息表", notes = "通过userId删除项目员工信息表")
    @SysLog("通过userId删除项目员工信息表")
    @DeleteMapping("/removeByUserId/{userId}/{projectId}")
    //@PreAuthorize("@pms.hasPermission('estate_projectstaff_del')")
    public R removeByUserId(@PathVariable("userId") Integer userId, @PathVariable("projectId") Integer projectId) {
        return R.ok(projectStaffService.removeByUserId(userId, projectId));
    }

    /**
     * 通过手机号获取员工
     *
     * @param mobile
     * @return
     */
    @ApiOperation(value = "通过手机号获取员工", notes = "通过手机号获取员工")
    @GetMapping("/checkMobile/{mobile}")
    public R getProjectStaffPage(@PathVariable("mobile") String mobile) {
        if (StringUtils.isNotEmpty(mobile)) {
            QueryWrapper<ProjectStaff> queryWrapper = new QueryWrapper<ProjectStaff>();
            queryWrapper.eq("mobile", mobile);
            queryWrapper.eq("projectId", ProjectContextHolder.getProjectId());
            return R.ok(projectStaffService.getOne(queryWrapper));
        } else {
            return R.ok();
        }
    }

    @ApiOperation("获取员工树形图")
    @GetMapping("/staffTree")
    public R findStaffTree() {
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置员工树形图列表
        List<ProjectStaffSelectTreeVo> projectStaffSelectTreeVoList = new ArrayList<>();
        if (projectInfo == null) {
            return R.ok(projectStaffSelectTreeVoList);
        }
        //获取当前项目下的部门列表
        List<SysProjectDept> sysProjectDepts = sysProjectDeptService.list(Wrappers.lambdaQuery(SysProjectDept.class)
                .eq(SysProjectDept::getProjectId, ProjectContextHolder.getProjectId()));
        //获取当前项目下所有员工列表
        List<ProjectStaff> projectStaffs = projectStaffService.list(Wrappers.lambdaQuery(ProjectStaff.class)
                .eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId()));
        ProjectStaffSelectTreeVo projectStaffSelectTreeVo = new ProjectStaffSelectTreeVo();
        projectStaffSelectTreeVo.setName(projectInfo.getProjectName());
        projectStaffSelectTreeVo.setId(String.valueOf(projectInfo.getProjectId()));
        projectStaffSelectTreeVo.setParentId(DataConstants.ROOT);
        projectStaffSelectTreeVo.setType(DataConstants.FALSE);
        //添加项目节点
        projectStaffSelectTreeVoList.add(projectStaffSelectTreeVo);
        //添加当前项目下部门节点
        projectStaffSelectTreeVoList.addAll(getDeptTree(sysProjectDepts));
        //添加当前项目下的员工节点
        projectStaffSelectTreeVoList.addAll(getStaffTree(projectStaffs));
        return R.ok(TreeUtil.build(projectStaffSelectTreeVoList, DataConstants.ROOT));
    }

    @ApiOperation("获取员工树形图")
    @GetMapping("/staffTreeByPlanId/{planId}")
    public R findStaffTreeByPlanId(@PathVariable("planId") String planId) {
        List<String> staffIds = projectShiftPlanStaffService.getByPlanId(planId);
        //当前排班计划下的员工列表
        List<ProjectStaff> projectStaffs = projectStaffService.listByIds(staffIds);
        Set<Integer> deptIds = new HashSet<>();
        projectStaffs.forEach(e -> {
            deptIds.add(e.getDepartmentId());
        });
        //当前排班计划下员工的部门列表
        List<SysProjectDept> sysProjectDepts = sysProjectDeptService.listByIds(deptIds);
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置员工树形图列表
        List<ProjectStaffSelectTreeVo> projectStaffSelectTreeVoList = new ArrayList<>();
        if (projectInfo == null) {
            return R.ok(projectStaffSelectTreeVoList);
        }
        ProjectStaffSelectTreeVo projectStaffSelectTreeVo = new ProjectStaffSelectTreeVo();
        projectStaffSelectTreeVo.setName(projectInfo.getProjectName());
        projectStaffSelectTreeVo.setId(String.valueOf(projectInfo.getProjectId()));
        projectStaffSelectTreeVo.setParentId(DataConstants.ROOT);
        projectStaffSelectTreeVo.setType(DataConstants.FALSE);
        //添加项目节点
        projectStaffSelectTreeVoList.add(projectStaffSelectTreeVo);
        //添加当前项目下部门节点
        projectStaffSelectTreeVoList.addAll(getDeptTree(sysProjectDepts));
        //添加当前项目下的员工节点
        projectStaffSelectTreeVoList.addAll(getStaffTree(projectStaffs));
        return R.ok(TreeUtil.build(projectStaffSelectTreeVoList, DataConstants.ROOT));
    }

    @ApiOperation("获取员工树形图")
    @GetMapping("/staffTreeByPatrolId/{patrolId}")
    public R findStaffTreeByPatrolId(@PathVariable("patrolId") String patrolId) {
        List<ProjectPatrolPerson> patrolPersonList = projectPatrolPersonService.listStaffInPlan(patrolId);
        List<String> staffIds = patrolPersonList.stream().map(ProjectPatrolPerson::getStaffId).collect(Collectors.toList());
        if (CollUtil.isEmpty(staffIds)) {
            return R.ok();
        }
        //当前排班计划下的员工列表
        List<ProjectStaff> projectStaffs = projectStaffService.listByIds(staffIds);
        Set<Integer> deptIds = new HashSet<>();
        projectStaffs.forEach(e -> {
            deptIds.add(e.getDepartmentId());
        });
        //当前排班计划下员工的部门列表
        List<SysProjectDept> sysProjectDepts = sysProjectDeptService.listByIds(deptIds);
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置员工树形图列表
        List<ProjectStaffSelectTreeVo> projectStaffSelectTreeVoList = new ArrayList<>();
        if (projectInfo == null) {
            return R.ok(projectStaffSelectTreeVoList);
        }
        ProjectStaffSelectTreeVo projectStaffSelectTreeVo = new ProjectStaffSelectTreeVo();
        projectStaffSelectTreeVo.setName(projectInfo.getProjectName());
        projectStaffSelectTreeVo.setId(String.valueOf(projectInfo.getProjectId()));
        projectStaffSelectTreeVo.setParentId(DataConstants.ROOT);
        projectStaffSelectTreeVo.setType(DataConstants.FALSE);
        //添加项目节点
        projectStaffSelectTreeVoList.add(projectStaffSelectTreeVo);
        //添加当前项目下部门节点
        projectStaffSelectTreeVoList.addAll(getDeptTree(sysProjectDepts));
        //添加当前项目下的员工节点
        projectStaffSelectTreeVoList.addAll(getStaffTree(projectStaffs));
        return R.ok(TreeUtil.build(projectStaffSelectTreeVoList, DataConstants.ROOT));
    }

    /**
     * <p>
     * 获取这个区域管辖的员工
     * </p>
     */
    @ApiOperation("获取这个区域管辖的员工")
    @GetMapping("/getStaffIdListByRegionId")
    public R getStaffIdListByRegionId(@RequestParam("regionId") String regionId) {
        return R.ok(projectStaffService.getStaffIdByRegionId(regionId));
    }

    /**
     * <p>
     * 根据员工ID获取到这个员工的工作情况
     * </p>
     *
     * @param dateStr 所要查询的日期 为空默认查询全部
     * @param staffId 所要查询的员工的ID
     * @author: 王良俊
     */
    @ApiOperation("根据员工ID获取到这个员工的工作情况")
    @GetMapping("/getStaffWorkByStaffId")
    public R getWorkingConditionByStaffId(@RequestParam("staffId") String staffId, @RequestParam("date") String dateStr) {
        LocalDate date = null;
        if (StrUtil.isNotEmpty(dateStr)) {
            try {
                String[] dateTimeArr = dateStr.split("-");
                date = LocalDate.of(Integer.parseInt(dateTimeArr[0]), Integer.parseInt(dateTimeArr[1]), 1);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("日期格式错误");
            }

        }
        List<ProjectStaffWorkVo> staffWorkVoList = new ArrayList<>();

        staffWorkVoList.add(new ProjectStaffWorkVo(
                "报事投诉",
                projectComplaintRecordService.countAllByStaffId(staffId, date),
                projectComplaintRecordService.countUnCompleteByStaffId(staffId, date),
                projectComplaintRecordService.countCompleteByStaffId(staffId, date)
        ));
        staffWorkVoList.add(new ProjectStaffWorkVo(
                "报修服务",
                projectRepairRecordService.countAllByStaffId(staffId, date),
                projectRepairRecordService.countUnCompleteByStaffId(staffId, date),
                projectRepairRecordService.countCompleteByStaffId(staffId, date)
        ));
        staffWorkVoList.add(new ProjectStaffWorkVo(
                "巡更",
                projectPatrolPersonPointService.countAllByStaffId(staffId, date),
                projectPatrolPersonPointService.countUnCompleteByStaffId(staffId, date),
                projectPatrolPersonPointService.countCompleteByStaffId(staffId, date)
        ));
        staffWorkVoList.add(new ProjectStaffWorkVo(
                "巡检",
                projectInspectTaskStaffService.countAllByStaffId(staffId, date),
                projectInspectTaskStaffService.countUnCompleteByStaffId(staffId, date),
                projectInspectTaskStaffService.countCompleteByStaffId(staffId, date)
        ));
        return R.ok(staffWorkVoList);
    }

    /**
     * <p>
     * 只获取未管辖区域的员工
     * </p>
     */
    @ApiOperation("获取带管辖区域相关信息的员工树形图")
    @GetMapping("/staffTreeByRegion")
    public R findStaffTreeUnJurisdiction(@RequestParam("regionId") String regionId) {
        List<StaffRegionVo> staffRegionVoList = projectStaffService.getManagerRegionStaffList();
        if (CollUtil.isEmpty(staffRegionVoList)) {
            return R.ok();
        }

        List<ProjectStaff> staffList = new ArrayList<>();
        staffRegionVoList.forEach(staffRegionVo -> {
            ProjectStaff staff = new ProjectStaff();
            staff.setStaffName(staffRegionVo.getStaffName() + "-" + staffRegionVo.getRegionNum());
            staff.setDepartmentId(staffRegionVo.getDepartmentId());
            staff.setStaffId(staffRegionVo.getStaffId());
            staffList.add(staff);
        });

        Set<Integer> deptIds = staffRegionVoList.stream().map(StaffRegionVo::getDepartmentId).collect(Collectors.toSet());

        List<SysProjectDept> sysProjectDepts = sysProjectDeptService.listByIds(deptIds);
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置员工树形图列表
        List<ProjectStaffSelectTreeVo> projectStaffSelectTreeVoList = new ArrayList<>();
        if (projectInfo == null) {
            return R.ok(projectStaffSelectTreeVoList);
        }
        ProjectStaffSelectTreeVo projectStaffSelectTreeVo = new ProjectStaffSelectTreeVo();
        projectStaffSelectTreeVo.setName(projectInfo.getProjectName());
        projectStaffSelectTreeVo.setId(String.valueOf(projectInfo.getProjectId()));
        projectStaffSelectTreeVo.setParentId(DataConstants.ROOT);
        projectStaffSelectTreeVo.setType(DataConstants.FALSE);
        //添加项目节点
        projectStaffSelectTreeVoList.add(projectStaffSelectTreeVo);
        //添加当前项目下部门节点
        projectStaffSelectTreeVoList.addAll(getDeptTree(sysProjectDepts));
        //添加当前项目下的员工节点
        projectStaffSelectTreeVoList.addAll(getStaffTree(staffList));
        return R.ok(TreeUtil.build(projectStaffSelectTreeVoList, DataConstants.ROOT));
    }


    private List<ProjectStaffSelectTreeVo> getDeptTree(List<SysProjectDept> sysProjectDepts) {
        List<ProjectStaffSelectTreeVo> treeList = sysProjectDepts.stream()
                .map(e -> {
                    ProjectStaffSelectTreeVo node = new ProjectStaffSelectTreeVo();
                    node.setParentId(String.valueOf(ProjectContextHolder.getProjectId()));
                    node.setName(e.getDeptName());
                    node.setId(String.valueOf(e.getDeptId()));
                    node.setType(DataConstants.FALSE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }

    private List<ProjectStaffSelectTreeVo> getStaffTree(List<ProjectStaff> projectStaffs) {
        List<ProjectStaffSelectTreeVo> treeList = projectStaffs.stream()
                .map(e -> {
                    ProjectStaffSelectTreeVo node = new ProjectStaffSelectTreeVo();
                    node.setParentId(String.valueOf(e.getDepartmentId()));
                    node.setName(e.getStaffName());
                    node.setId(e.getStaffId());
                    node.setType(DataConstants.TRUE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }

    @GetMapping("/get-owner")
    public R<ProjectStaffVo> getOwner() {
        return R.ok(projectStaffService.getStaffByOwner());
    }

    /**
     * <p>
     * 获取某个项目的员工
     * </p>
     */
    @ApiOperation("获取某个项目的员工")
    @GetMapping("/inner/list/{projectId}")
    @Inner(value = false)
    public R innerListByProjectId(@PathVariable Integer projectId) {
        return R.ok(projectStaffService.getStaffIdByProjectId(projectId));
    }





    @PostMapping("/saveEmployeeAvatar")
    public R<AppFaceHeadPortraitVo> saveEmployeeAvatar(@RequestBody AppFaceHeadPortraitVo appFaceHeadPortraitVo) {

        if (appFaceHeadPortraitVo.getPicBase64().isEmpty()){
            return R.failed("图片路径为空");
        }
        try {
        String url = imgConvertUtil.base64ToMinio(appFaceHeadPortraitVo.getPicBase64());
        appFaceHeadPortraitVo.setHeadPortraitUrl(url);
        projectStaffService.updateEmployeeAvatar(appFaceHeadPortraitVo);
        }catch (Exception ex){
             ex.printStackTrace();;
            return R.failed("图片保存或上传失败");
        }
         return R.ok(appFaceHeadPortraitVo);
    }

    /**
     * app调用修改员工手机号
     *
     * @param
     * @return R
     */
    @ApiOperation(value = "app调用修改员工手机号", notes = "修改项目员工信息表")
    @SysLog("app调用修改员工手机号")
    @PutMapping("/updatePhoneById/{phone}")
    public R<Boolean> updatePhoneById(@PathVariable("phone") String phone) {
        Integer userId = SecurityUtils.getUser().getId();

        return R.ok(projectStaffService.updatePhoneById(userId,phone));
    }

    /**
     * 开通账号
     *
     * @return
     */
    @ApiOperation(value = "开通账号", notes = "开通账号")
    @SysLog("开通账号")
    @PostMapping("/createAnAccount")
    public R<Boolean> createAnAccount(@RequestBody ProjectStaffDTO projectStaffDTO){
        boolean result = projectStaffService.createAnAccount(projectStaffDTO);
        if(result){
            return R.ok(Boolean.TRUE);
        }else{
            return R.failed(Boolean.FALSE,"开通账号失败");
        }
    }
}
