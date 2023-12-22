

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.service.ProjectPassPlanService;
import com.aurine.cloudx.estate.vo.ProjectPassPlanVo;
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
 * <p>通行方案视图控制器</p>
 *
 * @ClassName: ProjectPassPlanController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 14:16
 * @Copyright:
 */
@RestController
@RequestMapping("/servicePassPlan")
@Api(value = "servicePassPlan", tags = "通行方案管理")
public class ProjectPassPlanController {
    @Resource
    private ProjectPassPlanService projectPassPlanService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param projectPassPlan 通行方案
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectPassPlanPage(Page page, ProjectPassPlan projectPassPlan) {
//        return R.ok(projectPassPlanService.page(page, Wrappers.query(projectPassPlan)));
        //如果当前项目没有数据，则创建数据
//        if (projectPassPlanService.count() == 0) {
//            projectPassPlanService.createByDefault(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
//        }
        return R.ok(projectPassPlanService.page(page, new QueryWrapper<ProjectPassPlan>().orderByAsc("createTime").like(StringUtils.isNotEmpty(projectPassPlan.getPlanName()), "planName", projectPassPlan.getPlanName())));
    }

    /**
     * 初始化通行方案
     *
     * @return
     */
    @ApiOperation(value = "初始化通行方案", notes = "初始化通行方案")
    @GetMapping("/init")
    public R initDefault() {
        if (projectPassPlanService.count() == 0) {
            projectPassPlanService.createByDefault(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
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
        return R.ok(projectPassPlanService.getVo(id));
    }

    /**
     * 新增通行方案
     *
     * @param projectPassPlanVo 通行方案
     * @return R
     */
    @ApiOperation(value = "新增通行方案", notes = "新增通行方案")
    @SysLog("新增通行方案")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectpassplan_add')")
    public R save(@RequestBody ProjectPassPlanVo projectPassPlanVo) {
        projectPassPlanVo.setIsDefault("0");
        return R.ok(projectPassPlanService.save(projectPassPlanVo));
    }

    /**
     * 修改通行方案
     *
     * @param projectPassPlanVo 通行方案
     * @return R
     */
    @ApiOperation(value = "修改通行方案", notes = "修改通行方案")
    @SysLog("修改通行方案")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectpassplan_edit')")
    public R updateById(@RequestBody ProjectPassPlanVo projectPassPlanVo) {
        return R.ok(projectPassPlanService.update(projectPassPlanVo));
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
    @PreAuthorize("@pms.hasPermission('estate_projectpassplan_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectPassPlanService.delete(id));
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
        return R.ok(projectPassPlanService.listByType(planObject));
    }

    /**
     * 通过通行方案ID获取可选设备状态列表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过类型获取通行方案", notes = "通过类型获取通行方案")
    @GetMapping("/listDevice/{id}")
    public R listDevice(@PathVariable("id") String id) {
        return R.ok(this.projectPassPlanService.listDeviceByPlanId(id, "", ""));
    }

}
