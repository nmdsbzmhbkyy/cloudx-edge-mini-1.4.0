

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectBuildingBatchAddTemplateService;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectBuildingBatchAddTemplateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 楼栋模板
 *
 * @author 王伟
 * @date 2020-06-04 15:36:20
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseBuildingBatchTemplate")
@Api(value = "baseBuildingBatchTemplate", tags = "楼栋模板管理")
public class ProjectBuildingBatchAddTemplateController {

    private final ProjectBuildingBatchAddTemplateService projectBuildingBatchAddTemplateService;

    /**
     * 分页查询
     *
     * @param page            分页对象
     * @param searchCondition 楼栋模板
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectBuildingBatchAddTemplatePage(Page page, ProjectBuildingBatchAddTemplateSearchCondition searchCondition) {
        return R.ok(projectBuildingBatchAddTemplateService.findPage(page, searchCondition));
    }

    /**
     * 获取列表
     *
     * @return
     */
    @ApiOperation(value = "获取列表", notes = "获取列表")
    @GetMapping("/list")
    public R listBuildingBatchAddTemplatePage() {
        return R.ok(projectBuildingBatchAddTemplateService.list());
    }


    /**
     * 通过id查询楼栋模板
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectBuildingBatchAddTemplateService.getVo(id));
    }

    /**
     * 新增楼栋模板
     *
     * @param projectBuildingBatchAddTemplateVo 楼栋模板
     * @return R
     */
    @ApiOperation(value = "新增楼栋模板", notes = "新增楼栋模板")
    @SysLog("新增楼栋模板")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_buildingtemplate_add')")
    public R save(@RequestBody ProjectBuildingBatchAddTemplateVo projectBuildingBatchAddTemplateVo) {
        return R.ok(projectBuildingBatchAddTemplateService.save(projectBuildingBatchAddTemplateVo));
    }

    /**
     * 修改楼栋模板
     *
     * @param projectBuildingBatchAddTemplate 楼栋模板
     * @return R
     */
    @ApiOperation(value = "修改楼栋模板", notes = "修改楼栋模板")
    @SysLog("修改楼栋模板")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_buildingtemplate_edit')")
    public R updateById(@RequestBody ProjectBuildingBatchAddTemplateVo projectBuildingBatchAddTemplate) {
        return R.ok(projectBuildingBatchAddTemplateService.update(projectBuildingBatchAddTemplate));
    }

    /**
     * 通过id删除楼栋模板
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除楼栋模板", notes = "通过id删除楼栋模板")
    @SysLog("通过id删除楼栋模板")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_buildingtemplate_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(projectBuildingBatchAddTemplateService.deleteById(id));
    }

}
