package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.estate.service.ProjectShiftPlanService;
import com.aurine.cloudx.estate.service.ProjectShiftPlanStaffService;
import com.aurine.cloudx.estate.service.ProjectStaffShiftDetailService;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanFromVo;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanPageVo;
import com.aurine.cloudx.estate.vo.ProjectStaffShiftDetailPageVo;
import com.aurine.cloudx.estate.vo.ProjectStaffShiftDetailUpdateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 排班计划(ProjectShiftPlan)表控制层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 10:48:33
 */
@RestController
@RequestMapping("projectShiftPlan")
@Api(value = "projectShiftPlan", tags = "排班计划")
public class ProjectShiftPlanController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectShiftPlanService projectShiftPlanService;

    @Resource
    ProjectShiftPlanStaffService projectShiftPlanStaffService;

    @Resource
    ProjectStaffShiftDetailService projectStaffShiftDetailService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectShiftPlanPageVo 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询排班计划所有数据")
    public R selectAll(Page<ProjectShiftPlanPageVo> page, ProjectShiftPlanPageVo projectShiftPlanPageVo) {
        if (projectShiftPlanPageVo.getStartTimeString() != null && !"".equals(projectShiftPlanPageVo.getStartTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectShiftPlanPageVo.setStartTime(LocalDate.parse(projectShiftPlanPageVo.getStartTimeString(), fmt));
        }
        if (projectShiftPlanPageVo.getEndTimeString() != null && !"".equals(projectShiftPlanPageVo.getEndTimeString())) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            projectShiftPlanPageVo.setEndTime(LocalDate.parse(projectShiftPlanPageVo.getEndTimeString(), fmt));
        }
        return R.ok(this.projectShiftPlanService.pageShiftPlan(page, projectShiftPlanPageVo));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param planId 主键
     * @return 单条数据
     */
    @GetMapping("/{planId}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询排班计划单条数据")
    public R selectOne(@PathVariable("planId") String planId) {
        return R.ok(this.projectShiftPlanService.getByPlanId(planId));
    }

    /**
     * 新增数据
     *
     * @param projectShiftPlanFromVo 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增排班计划数据")
    public R insert(@RequestBody ProjectShiftPlanFromVo projectShiftPlanFromVo) {
        return R.ok(this.projectShiftPlanService.saveShiftPlan(projectShiftPlanFromVo));
    }

    /**
     * 修改数据
     *
     * @param projectShiftPlanFromVo 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改排班计划数据")
    public R update(@RequestBody ProjectShiftPlanFromVo projectShiftPlanFromVo) {
        return R.ok(projectShiftPlanService.updateShiftPlan(projectShiftPlanFromVo));
    }

    /**
     * 通过id删除排班计划
     *
     * @param planId id
     * @return R
     */
    @ApiOperation(value = "通过id删除排班计划", notes = "通过id删除排班计划")
    @SysLog("通过id删除排班计划")
    @DeleteMapping("/{planId}")
    //@PreAuthorize("@pms.hasPermission('')")
    public R removeById(@PathVariable("planId") String planId) {
        return R.ok(projectShiftPlanService.deleteByPlanId(planId));
    }

    /**
     * 根据排班名称查询单条数据
     *
     * @param planName
     * @param planId
     * @return
     */
    @GetMapping("/getByPlanName")
    @ApiOperation(value = "通过排班名称查询单条数据", notes = "通过排班名称查询单条数据")
    public R getByPlanName(@RequestParam(value = "planName", required = false) String planName, @RequestParam(value = "planId", required = false) String planId) {
        return R.ok(this.projectShiftPlanService.getByPlanName(planName, planId));
    }

    /**
     * 根据排班id查询员工列表
     *
     * @param planId
     * @param planId
     * @return
     */
    @GetMapping("/getStaffByPlanId/{planId}")
    @ApiOperation(value = "根据排班id查询员工列表", notes = "根据排班id查询员工列表")
    public R getByPlanId(@PathVariable String planId) {
        return R.ok(projectShiftPlanStaffService.getStaffListByPlanId(planId));
    }

    /**
     * 查询今年的节假日安排
     *
     * @return
     */
    @GetMapping("/listVacations")
    @ApiOperation(value = "查询今年的节假日安排", notes = "查询今年的节假日安排")
    public R listVacations() {
        return R.ok(projectShiftPlanService.listVacations());
    }

    /**
     * 分页查询排班计划详情
     * @param page
     * @param projectStaffShiftDetailPageVo
     * @return
     */
    @GetMapping("/pageShiftDetail")
    public R pageShiftDetail(Page<ProjectStaffShiftDetailPageVo> page, ProjectStaffShiftDetailPageVo projectStaffShiftDetailPageVo) {
        return R.ok(projectStaffShiftDetailService.pageShiftDetail(page, projectStaffShiftDetailPageVo));
    }

    /**
     * 更新排班计划详情
     * @param projectStaffShiftDetailUpdateVo
     * @return
     */
    @PutMapping("/updateShiftDetail")
    public R updateShiftDetail(@RequestBody ProjectStaffShiftDetailUpdateVo projectStaffShiftDetailUpdateVo) {
        return R.ok(projectStaffShiftDetailService.updateShiftDetail(projectStaffShiftDetailUpdateVo));
    }
}