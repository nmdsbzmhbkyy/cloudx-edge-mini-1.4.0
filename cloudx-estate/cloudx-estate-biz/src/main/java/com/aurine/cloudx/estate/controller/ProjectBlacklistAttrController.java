package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectBlacklistAttr;
import com.aurine.cloudx.estate.entity.ProjectCard;
import com.aurine.cloudx.estate.service.ProjectBlacklistAttrService;
import com.aurine.cloudx.estate.vo.BlacklistAttrSearchCondition;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 黑名单属性(project_blacklist_attr)表控制层
 *
 * @author 顾文豪
 * @since 2023-11-13 9:52:48
 */
@RestController
@RequestMapping("projectBlacklistAttr")
@Api(value = "projectBlacklistAttr", tags = "黑名单属性")
public class ProjectBlacklistAttrController {
    @Resource
    private ProjectBlacklistAttrService projectBlacklistAttrService;

    /**
     * 分页查询
     *
     * @param page   分页对象
     * @param query  查询条件
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "projectBlacklistAttr", value = "分页查询条件", required = false, paramType = "query")
    })
    @GetMapping("/page")
    public R fetchList(Page page, BlacklistAttrSearchCondition query) {
        return R.ok(projectBlacklistAttrService.fetchList(page, query));
    }
}
