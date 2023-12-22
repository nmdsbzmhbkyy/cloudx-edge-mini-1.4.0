package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.estate.entity.SysCompany;
import com.aurine.cloudx.estate.service.SysCompanyService;
import com.aurine.cloudx.estate.vo.SysCompanyFormVo;
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

import java.util.List;


/**
 * 集团管理
 *
 * @author xull@aurine.cn
 * @date 2020-04-29 16:23:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysCompany")
@Api(value = "sysCompany", tags = "集团管理管理")
public class SysCompanyController {

    private final SysCompanyService sysCompanyService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param sysCompany 集团管理
     * @return R
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R< Page<SysCompany>> getCompanyPage(Page page, SysCompany sysCompany) {
        return R.ok(sysCompanyService.pageCompany(page, sysCompany));
    }


    /**
     * 通过id查询集团管理
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "集团id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<SysCompany> getById(@PathVariable("id") Integer id) {
        return R.ok(sysCompanyService.getById(id));
    }

    /**
     * 新增集团管理
     *
     * @param sysCompany 集团管理
     * @return R
     */
    @ApiOperation(value = "新增集团管理", notes = "新增集团管理")
    @SysLog("新增集团管理")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_company_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody SysCompanyFormVo sysCompany) {
        return R.ok(sysCompanyService.saveReturnId(sysCompany));
    }

    /**
     * 修改集团管理
     *
     * @param sysCompanyFormVo 集团管理
     * @return R
     */
    @ApiOperation(value = "修改集团管理", notes = "修改集团管理")
    @SysLog("修改集团管理")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_company_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody SysCompanyFormVo sysCompanyFormVo) {
        return R.ok(sysCompanyService.updateCompanyAndUser(sysCompanyFormVo));
    }


    /**
     * 通过id删除集团管理
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除集团管理", notes = "通过id删除集团管理")
    @SysLog("通过id删除集团管理")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_company_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "集团id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysCompanyService.removeById(id));
    }

    /**
     * 通过项目id或项目组id查询集团信息
     *
     * @param id
     *         id
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/findByGroupOrProjectId/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "项目id或项目组id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<SysCompany>> findByGroupOrProjectId(@PathVariable Integer id) {
        return R.ok(sysCompanyService.findByGroupOrProjectId(id));
    }

}
