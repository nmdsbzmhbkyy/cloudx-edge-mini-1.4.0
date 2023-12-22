

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectLiftPlan;
import com.aurine.cloudx.estate.service.ProjectLiftPlanService;
import com.aurine.cloudx.estate.service.ProjectPersonLiftPlanRelService;
import com.aurine.cloudx.estate.vo.ProjectLiftPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>电梯通行方案视图控制器</p>
 *
 * @ClassName: ProjectLiftPlanController
 * @author: 陈喆 <chenz@aurine.cn>
 * @date: 2022/2/22 14:51
 * @Copyright:
 */
@RestController
@RequestMapping("/serviceLiftPlan")
@Api(value = "serviceLiftPlan", tags = "电梯通行方案管理")
public class ProjectLiftPlanController {
    @Resource
    private ProjectLiftPlanService projectLiftPlanService;
    @Resource
    private ProjectPersonLiftPlanRelService projectPersonLiftPlanRelService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param projectLiftPlan 通行方案
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectLiftPlanPage(Page page, ProjectLiftPlan projectLiftPlan) {
//        return R.ok(projectLiftPlanService.page(page, Wrappers.query(projectLiftPlan)));
        //如果当前项目没有数据，则创建数据
        if (projectLiftPlanService.count() == 0) {
            projectLiftPlanService.createByDefault(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        }
        return R.ok(projectLiftPlanService.page(page, new QueryWrapper<ProjectLiftPlan>().orderByAsc("createTime").like(StringUtils.isNotEmpty(projectLiftPlan.getPlanName()), "planName", projectLiftPlan.getPlanName())));
    }

    /**
     * 初始化通行方案
     *
     * @return
     */
    @ApiOperation(value = "初始化通行方案", notes = "初始化通行方案")
    @GetMapping("/init")
    public R initDefault() {
        if (projectLiftPlanService.count() == 0) {
            projectLiftPlanService.createByDefault(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        }
        return R.ok();
    }


    /**
     * 通过id查询通行方案
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectLiftPlanService.getVo(id));
    }

    /**
     * 新增通行方案
     *
     * @param projectLiftPlanVo 通行方案
     * @return R
     */
    @ApiOperation(value = "新增通行方案", notes = "新增通行方案")
    @SysLog("新增通行方案")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectliftpassplan_add')")
    public R save(@RequestBody ProjectLiftPlanVo projectLiftPlanVo) {
        projectLiftPlanVo.setIsDefault("0");
        return R.ok(projectLiftPlanService.save(projectLiftPlanVo));
    }

    /**
     * 修改通行方案
     *
     * @param projectLiftPlanVo 通行方案
     * @return R
     */
    @ApiOperation(value = "修改通行方案", notes = "修改通行方案")
    @SysLog("修改通行方案")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectliftpassplan_edit')")
    public R updateById(@RequestBody ProjectLiftPlanVo projectLiftPlanVo) {
        return R.ok(projectLiftPlanService.update(projectLiftPlanVo));
    }

    /**
     * 通过id删除通行方案
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除通行方案", notes = "通过id删除通行方案")
    @SysLog("通过id删除通行方案")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_projectliftpassplan_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectLiftPlanService.delete(id));
    }


    /**
     * 通过类型获取通行方案
     *
     * @param planObject planObject
     * @return R
     */
    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
    @GetMapping("/listByType/{planObject}")
    public R list(@PathVariable("planObject") String planObject) {
        return R.ok(projectLiftPlanService.listByType(planObject));
    }

    /**
     * 通过人员ID获取通行方案
     *
     * @param personId
     * @return R
     */
    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
    @GetMapping("/getIdByPersonId/{personId}")
    public R getIdByPersonId(@PathVariable("personId") String personId) {
        return R.ok(projectPersonLiftPlanRelService.getPlanIdByPersonId(personId));
    }

//    /**
//     * 通过通行方案ID获取可选设备状态列表
//     *
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
//    @GetMapping("/listDevice/{id}")
//    public R listDevice(@PathVariable("id") String id) {
//        return R.ok(this.projectLiftPlanService.listDeviceByPlanId(id, "", ""));
//    }

}
