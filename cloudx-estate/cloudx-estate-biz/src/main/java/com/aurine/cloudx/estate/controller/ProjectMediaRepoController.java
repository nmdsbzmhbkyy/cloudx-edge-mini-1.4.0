

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectMediaAdPlaylist;
import com.aurine.cloudx.estate.entity.ProjectMediaRepo;
import com.aurine.cloudx.estate.service.ProjectMediaAdPlaylistService;
import com.aurine.cloudx.estate.service.ProjectMediaRepoService;
import com.aurine.cloudx.estate.vo.ProjectMediaRepoVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * 项目媒体资源库
 *
 * @author xull@aurine.cn
 * @date 2020-06-04 11:35:57
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectMediaRepo")
@Api(value = "projectMediaRepo", tags = "项目媒体资源库管理")
public class ProjectMediaRepoController {

    private final ProjectMediaRepoService projectMediaRepoService;
    private final ProjectMediaAdPlaylistService projectMediaAdPlaylistService;

    /**
     * 分页查询
     *
     * @param page
     *         分页对象
     * @param projectMediaRepo
     *         项目媒体资源库
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectMediaRepoPage(Page page, ProjectMediaRepoVo projectMediaRepo) {
        return R.ok(projectMediaRepoService.getList(page, projectMediaRepo));
    }


    /**
     * 通过id查询项目媒体资源库
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectMediaRepoService.getById(id));
    }

    /**
     * 新增项目媒体资源库
     *
     * @param projectMediaRepo 项目媒体资源库
     * @return R
     */
    @ApiOperation(value = "新增项目媒体资源库", notes = "新增项目媒体资源库")
    @SysLog("新增项目媒体资源库")
    @PostMapping
    public R save(@RequestBody ProjectMediaRepo projectMediaRepo) {
        projectMediaRepoService.save(projectMediaRepo);
        return R.ok(projectMediaRepo.getRepoId());
    }

    /**
     * 修改项目媒体资源库
     *
     * @param projectMediaRepo
     *         项目媒体资源库
     *
     * @return R
     */
    @ApiOperation(value = "修改项目媒体资源库", notes = "修改项目媒体资源库")
    @SysLog("修改项目媒体资源库")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_mediarepo_edit')")
    public R updateById(@Valid @RequestBody ProjectMediaRepo projectMediaRepo) {
        return R.ok(projectMediaRepoService.updateById(projectMediaRepo));
    }

    /**
     * 通过id删除项目媒体资源库
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目媒体资源库", notes = "通过id删除项目媒体资源库")
    @SysLog("通过id删除项目媒体资源库")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_mediarepo_del')")
    public R removeById(@PathVariable String id) {
        Integer count = projectMediaAdPlaylistService.count(Wrappers.lambdaQuery(ProjectMediaAdPlaylist.class).eq(ProjectMediaAdPlaylist::getRepoId, id));
        if (count > 0) {
            return R.failed("资源已被占用,无法删除");
        }
        return R.ok(projectMediaRepoService.removeById(id));
    }

    /**
     * 新增出入口资源
     *
     *         新增出入口资源
     *
     * @return R
     */
    @ApiOperation(value = "新增资源", notes = "新增资源")
    @SysLog("新增资源")
    @PostMapping("/saveList")
    @PreAuthorize("@pms.hasPermission('estate_mediarepo_add')")
    public R saveList(@RequestBody List<ProjectMediaRepo> projectMediaRepo) {
        projectMediaRepoService.saveBatch(projectMediaRepo);
        return R.ok();
    }


}
