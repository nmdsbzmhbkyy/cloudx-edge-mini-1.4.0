package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectNoticeObject;
import com.aurine.cloudx.estate.service.ProjectNoticeObjectService;
import com.aurine.cloudx.estate.vo.ProjectNoticeObjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 项目信息发布对象配置表(ProjectNoticeObject)表控制层
 *
 * @author xull
 * @since 2021-02-07 17:15:31
 */
@RestController
@RequestMapping("project-notice-object")
@Api(value = "projectNoticeObject", tags = "项目信息发布对象配置表")
public class ProjectNoticeObjectController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNoticeObjectService projectNoticeObjectService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询projectNoticeObject所有数据")
    public R selectAll(Page<ProjectNoticeObjectVo> page,@RequestParam String noticeId
            , @RequestParam(value = "buildName", required = false) String buildName
            , @RequestParam(value = "unitName", required = false) String unitName
            , @RequestParam(value = "houseName", required = false) String houseName) {
        return R.ok(this.projectNoticeObjectService.pageNoticeObject(page,noticeId, buildName, unitName, houseName));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectNoticeObject单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectNoticeObjectService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectNoticeObject 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectNoticeObject数据")
    public R insert(@RequestBody ProjectNoticeObject projectNoticeObject) {
        return R.ok(this.projectNoticeObjectService.save(projectNoticeObject));
    }

    /**
     * 修改数据
     *
     * @param projectNoticeObject 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改projectNoticeObject数据")
    public R update(@RequestBody ProjectNoticeObject projectNoticeObject) {
        return R.ok(this.projectNoticeObjectService.updateById(projectNoticeObject));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除projectNoticeObject数据")
    public R delete(@RequestParam("idList") List<String> idList) {
        return R.ok(this.projectNoticeObjectService.removeByIds(idList));
    }
}
