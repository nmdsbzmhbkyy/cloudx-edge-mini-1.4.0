

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectLabelConfig;
import com.aurine.cloudx.estate.service.ProjectLabelConfigService;
import com.aurine.cloudx.estate.vo.ProjectLabelConfigVo;
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
 * 标签管理
 *
 * @author 王伟
 * @date 2020-05-07 08:09:35
 */
@RestController
@AllArgsConstructor
@RequestMapping("/baseLabelConfig")
@Api(value = "baseLabelConfig", tags = "标签管理")
public class ProjectLabelConfigController {

    private final ProjectLabelConfigService projectLabelConfigService;

    /**
     * 分页查询
     *
     * @param page      分页对象
     * @param  projectLabelConfigVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getLabelConfigPage(Page page, ProjectLabelConfigVo projectLabelConfigVo ) {
        return R.ok(projectLabelConfigService.findPage(page, projectLabelConfigVo));
    }

    /**
     * 分页查询
     *
     * @return
     * @author: 王良俊
     */
    @ApiOperation(value = "查询所有的标签", notes = "查询所有的标签")
    @GetMapping("/allLabel")
    public R getAllLabel() {
        return R.ok(projectLabelConfigService.list());
    }


    /**
     * 通过id查询标签管理
     *
     * @param labelId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{labelId}")
    public R getById(@PathVariable("labelId") String labelId) {
        return R.ok(projectLabelConfigService.getById(labelId));
    }


    /**
     * 新增标签管理
     *
     * @param projectLabelConfig 标签管理
     * @return R
     * @Author: 王良俊
     */
    @ApiOperation(value = "新增标签管理", notes = "新增标签管理")
    @SysLog("新增标签管理")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_labelconfig_add')")
    public R save(@RequestBody ProjectLabelConfig projectLabelConfig) {
        return R.ok(projectLabelConfigService.saveLabel(projectLabelConfig));
    }

    /**
     * 修改标签管理
     *
     * @param projectLabelConfig 标签管理
     * @return R
     */
    @ApiOperation(value = "修改标签管理", notes = "修改标签管理")
    @SysLog("修改标签管理")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_labelconfig_edit')")
    public R updateById(@RequestBody ProjectLabelConfig projectLabelConfig) {
        projectLabelConfig.setOperator(null);
        ProjectLabelConfigVo projectLabelConfigVo = projectLabelConfigService.selectByLabelId(projectLabelConfig.getLabelId());
        if (!projectLabelConfigVo.getProjectId().equals(ProjectContextHolder.getProjectId())) {
            throw new RuntimeException("该标签不可修改");
        }
        return R.ok(projectLabelConfigService.updateById(projectLabelConfig));
    }

    /**
     * 通过id删除标签管理
     *
     * @param labelId id
     * @return R
     * @Author: 王良俊
     */
    @ApiOperation(value = "通过id删除标签管理", notes = "通过id删除标签管理")
    @SysLog("通过id删除标签管理")
    @DeleteMapping("/{labelId}")
    @PreAuthorize("@pms.hasPermission('estate_labelconfig_del')")
    public R removeById(@PathVariable String labelId) {
        ProjectLabelConfigVo projectLabelConfigVo = projectLabelConfigService.selectByLabelId(labelId);
        if (!projectLabelConfigVo.getProjectId().equals(ProjectContextHolder.getProjectId())) {
            throw new RuntimeException("该标签不可删除");
        }
        return R.ok(projectLabelConfigService.removeById(labelId));
    }

    @GetMapping("/getLabelByName")
    @ApiOperation(value = "通过标签名查询", notes = "通过标签名查询")
    public R getTemplateByTitle(String labelName) {
        if (ProjectContextHolder.getProjectId() == 1) {
            return R.ok(projectLabelConfigService.list(Wrappers.lambdaQuery(ProjectLabelConfig.class).eq(ProjectLabelConfig::getLabelName, labelName)));
        } else {
            return R.ok();
        }

    }
}
