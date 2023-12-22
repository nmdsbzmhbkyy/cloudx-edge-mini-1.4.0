package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.config.WsAddrConfigurationProperties;
import com.aurine.cloudx.estate.service.ProjectAttrService;
import com.aurine.cloudx.estate.vo.ProjectAttrVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProjectAttrController)
 * 属性拓展
 *
 * @author xull
 * @since 2020/7/6 10:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectAttr")
@Api(value = "projectAttr", tags = "属性配置表管理")
public class ProjectAttrController {
    ProjectAttrService projectAttrService;

    @Resource
    private WsAddrConfigurationProperties wsAddrConfigurationProperties;

    /**
     * 分页查询
     *
     * @param page          分页对象
     * @param projectAttrVo 拓展属性配置表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectAttrVo>> getProjectAttrPage(Page page, ProjectAttrVo projectAttrVo) {
        return R.ok(projectAttrService.page(page,projectAttrVo));
    }

    /**
     * 新增拓展属性配置表
     *
     * @param projectAttrVo 拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "新增拓展属性配置表", notes = "新增拓展属性配置表")
    @SysLog("新增拓展属性配置表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_attrconf_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R addProjectAttr(@RequestBody ProjectAttrVo projectAttrVo) {
        return R.ok(projectAttrService.add(projectAttrVo));
    }

    /**
     * 更新拓展属性配置表
     *
     * @param projectAttrVo 拓展属性配置表
     * @return R
     */
    @ApiOperation(value = "更新拓展属性配置表", notes = "更新拓展属性配置表")
    @SysLog("更新拓展属性配置表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_attrconf_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R editProjectAttr(@RequestBody ProjectAttrVo projectAttrVo) {
        return R.ok(projectAttrService.update(projectAttrVo));
    }

    /**
     * 删除拓展属性配置表
     *
     * @param style  类型
     * @param attrId 属性id
     * @return R
     */
    @ApiOperation(value = "删除拓展属性配置表", notes = "删除拓展属性配置表")
    @SysLog("删除拓展属性配置表")
    @DeleteMapping("/{style}/{attrId}")
    @PreAuthorize("@pms.hasPermission('estate_attrconf_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "style", value = "类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "attrId", value = "属性id", required = true, paramType = "path")
    })
    public R remove(@PathVariable("style") String style,@PathVariable("attrId") String attrId) {
        return R.ok(projectAttrService.remove(style, attrId));
    }
    @ApiOperation(value = "获取websocket地址", notes = "获取websocket地址")
    @GetMapping("/getWsAddr")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    public R getWebsocketAddr() {
        return R.ok(wsAddrConfigurationProperties);
    }

}
