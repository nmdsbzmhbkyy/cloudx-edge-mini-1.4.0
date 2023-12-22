

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.service.ProjectPersonPlanRelService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 人员通行方案关系
 *
 * @author 王良俊
 * @date 2020-05-22 11:35:52
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectpersonplanrel")
@Api(value = "projectpersonplanrel", tags = "人员通行方案关系管理")
public class ProjectPersonPlanRelController {

    private final ProjectPersonPlanRelService projectPersonPlanRelService;

    /**
     * 分页查询
     *
     * @param page                 分页对象
     * @param projectPersonPlanRel 人员通行方案关系
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectPersonPlanRelPage(Page page, ProjectPersonPlanRel projectPersonPlanRel) {
        return R.ok(projectPersonPlanRelService.page(page, Wrappers.query(projectPersonPlanRel)));
    }


    /**
     * 通过id查询人员通行方案关系
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R<ProjectPersonPlanRel> getById(@PathVariable("seq") Integer seq) {
        return R.ok(projectPersonPlanRelService.getById(seq));
    }

    /**
     * 新增人员通行方案关系
     *
     * @param projectPersonPlanRel 人员通行方案关系
     * @return R
     */
    @ApiOperation(value = "新增人员通行方案关系", notes = "新增人员通行方案关系")
    @SysLog("新增人员通行方案关系")
    @PostMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectpersonplanrel_add')")
    public R save(@RequestBody ProjectPersonPlanRel projectPersonPlanRel) {
        return R.ok(projectPersonPlanRelService.save(projectPersonPlanRel));
    }

    /**
     * 修改人员通行方案关系
     *
     * @param projectPersonPlanRel 人员通行方案关系
     * @return R
     */
    @ApiOperation(value = "修改人员通行方案关系", notes = "修改人员通行方案关系")
    @SysLog("修改人员通行方案关系")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('estate_projectpersonplanrel_edit')")
    public R updateById(@RequestBody ProjectPersonPlanRel projectPersonPlanRel) {
        return R.ok(projectPersonPlanRelService.updateById(projectPersonPlanRel));
    }

    /**
     * 通过id删除人员通行方案关系
     *
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除人员通行方案关系", notes = "通过id删除人员通行方案关系")
    @SysLog("通过id删除人员通行方案关系")
    @DeleteMapping("/{seq}")
//    @PreAuthorize("@pms.hasPermission('estate_projectpersonplanrel_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectPersonPlanRelService.removeById(seq));
    }

}
