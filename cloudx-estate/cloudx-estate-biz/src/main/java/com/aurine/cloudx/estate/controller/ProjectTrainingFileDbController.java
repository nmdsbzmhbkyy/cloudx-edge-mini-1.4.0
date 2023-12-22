package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.vo.ProjectTrainingFormVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectTrainingFileDb;
import com.aurine.cloudx.estate.service.ProjectTrainingFileDbService;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * 培训资料库(ProjectTrainingFileDb)表控制层
 *
 * @author makejava
 * @since 2021-01-12 10:26:08
 */
@RestController
@RequestMapping("projectTrainingFileDb")
@Api(value = "projectTrainingFileDb", tags = "培训资料库")
public class ProjectTrainingFileDbController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectTrainingFileDbService projectTrainingFileDbService;

    /**
     * 查询文件夹
     *
     * @param projectTrainingFileDb
     * @return
     */
    @GetMapping("/selectDir")
    @ApiOperation(value = "查询文件夹", notes = "查询文件夹")
    public R selectDir(ProjectTrainingFileDb projectTrainingFileDb) {
        return R.ok(this.projectTrainingFileDbService.selectDir(projectTrainingFileDb));
    }

    /**
     * 分页查询文件
     *
     * @param page                  分页对象
     * @param projectTrainingFileDb 查询实体
     * @return 所有数据
     */
    @GetMapping("/selectFile")
    @ApiOperation(value = "分页查询文件", notes = "分页查询文件")
    public R selectFile(Page<ProjectTrainingFileDb> page, ProjectTrainingFileDb projectTrainingFileDb) {
        return R.ok(this.projectTrainingFileDbService.selectFile(page, projectTrainingFileDb));
    }

    /**
     * 根据文件id批量查询
     *
     * @param projectTrainingFormVo
     * @return 所有数据
     */
    @GetMapping("/selectFileByIds")
    @ApiOperation(value = "根据文件id批量查询", notes = "根据文件id批量查询")
    public R selectFileByIds(ProjectTrainingFormVo projectTrainingFormVo) {
        return R.ok(this.projectTrainingFileDbService.listByIds(projectTrainingFormVo.getFileIds()));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectTrainingFileDb单条数据")
    public R selectOne(@PathVariable String id) {
        return R.ok(this.projectTrainingFileDbService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param projectTrainingFileDb 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectTrainingFileDb数据")
    public R insert(@RequestBody ProjectTrainingFileDb projectTrainingFileDb) {
        int count = projectTrainingFileDbService.count(Wrappers.lambdaQuery(ProjectTrainingFileDb.class)
                .eq(ProjectTrainingFileDb::getFileName, projectTrainingFileDb.getFileName())
                .eq(ProjectTrainingFileDb::getIsDir, projectTrainingFileDb.getIsDir()));
        if (count != 0) {
            if ("1".equals(projectTrainingFileDb.getIsDir())) {
                throw new RuntimeException("该文件夹已存在");
            }
            if ("0".equals(projectTrainingFileDb.getIsDir())) {
                throw new RuntimeException("该文件已存在");
            }
        }
        String uid = UUID.randomUUID().toString().replaceAll("-", "");
        projectTrainingFileDb.setFileId(uid);
        return R.ok(this.projectTrainingFileDbService.save(projectTrainingFileDb));
    }

    /**
     * 修改数据
     *
     * @param projectTrainingFileDb 实体对象
     * @return 新增结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改数据")
    public R update(@RequestBody ProjectTrainingFileDb projectTrainingFileDb) {
        int count = projectTrainingFileDbService.count(Wrappers.lambdaQuery(ProjectTrainingFileDb.class)
                .eq(ProjectTrainingFileDb::getFileName, projectTrainingFileDb.getFileName())
                .eq(ProjectTrainingFileDb::getIsDir, projectTrainingFileDb.getIsDir())
                .ne(ProjectTrainingFileDb::getFileId, projectTrainingFileDb.getFileId()));
        if (count != 0) {
            throw new RuntimeException("该文件已存在");
        }
        return R.ok(this.projectTrainingFileDbService.updateById(projectTrainingFileDb));
    }

    /**
     * 删除数据
     *
     * @param fileId 主键结合
     * @return 删除结果
     */
    @DeleteMapping("{fileId}")
    @ApiOperation(value = "删除数据", notes = "通过id删除projectTrainingFileDb数据")
    public R delete(@PathVariable("fileId") String fileId) {
        return R.ok(this.projectTrainingFileDbService.deleteFile(fileId));
    }
}