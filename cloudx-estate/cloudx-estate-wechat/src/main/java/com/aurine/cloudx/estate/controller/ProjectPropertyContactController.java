package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectPropertyContact;
import com.aurine.cloudx.estate.service.ProjectPropertyContactService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表控制层
 *
 * @author xull
 * @version 1.0.0
 * @date 2020-10-27 15:38:48
 */
@RestController
@RequestMapping("projectPropertyContact")
@Api(value = "projectPropertyContact", tags = "项目物业联系方式表")
public class ProjectPropertyContactController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPropertyContactService projectPropertyContactService;

    /**
     * 分页查询所有数据
     *
     * @param page                   分页对象
     * @param projectPropertyContact 查询实体
     * @return 所有数据
     */
    @GetMapping
    @ApiOperation(value = "分页查询", notes = "分页查询projectPropertyContact所有数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectPropertyContact>> selectAll(Page<ProjectPropertyContact> page, ProjectPropertyContact projectPropertyContact) {
        return success(this.projectPropertyContactService.page(page, new QueryWrapper<>(projectPropertyContact)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询projectPropertyContact单条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "主键id", paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectPropertyContact> selectOne(@PathVariable("id") Long id) {
        return success(this.projectPropertyContactService.getById(id));
    }

}