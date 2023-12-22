

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.SysCompany;
import com.aurine.cloudx.estate.entity.SysGroup;
import com.aurine.cloudx.estate.service.SysCompanyService;
import com.aurine.cloudx.estate.service.SysGroupService;
import com.aurine.cloudx.estate.vo.SysGroupFormVo;
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
 * 项目组
 *
 * @author xull@aurine.cn
 * @date 2020-04-30 16:04:44
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysGroup")
@Api(value = "sysGroup", tags = "项目组管理")
public class SysGroupController {

    private final SysGroupService sysGroupService;
    private final SysCompanyService sysCompanyService;

    /**
     * 分页查询
     *
     * @param page     分页对象
     * @param sysGroup 项目组
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<SysGroup>> getGroupPage(Page page, SysGroup sysGroup) {
        sysGroup.setParentId(ProjectContextHolder.getProjectId());
        return R.ok(sysGroupService.pageGroup(page, sysGroup));
    }


    /**
     * 通过id查询项目组
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 项目组id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getById(@PathVariable("id") Integer id) {
        SysGroup sysGroup = sysGroupService.getById(id);
        return R.ok(sysGroup);
    }

    /**
     * 新增项目组
     *
     * @param sysGroupFormVo 项目组
     * @return R
     */
    @ApiOperation(value = "新增项目组", notes = "新增项目组")
    @SysLog("新增项目组")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_group_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody SysGroupFormVo sysGroupFormVo) {
        return R.ok(sysGroupService.saveReturnId(sysGroupFormVo));
    }


    /**
     * 修改项目组
     *
     * @param sysGroupFormVo 项目组
     * @return R
     */
    @ApiOperation(value = "修改项目组", notes = "修改项目组")
    @SysLog("修改项目组")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_group_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody SysGroupFormVo sysGroupFormVo) {
        return R.ok(sysGroupService.updateGroupAndUser(sysGroupFormVo));
    }

    /**
     * 通过id删除项目组
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除项目组", notes = "通过id删除项目组")
    @SysLog("通过id删除项目组")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_group_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 项目组id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysGroupService.removeById(id));
    }

    /**
     * 通过集团id次级项目组
     *
     * @param id 集团id
     * @return R
     */
    @ApiOperation(value = "通过集团id次级项目组", notes = "通过集团id次级项目组")
    @GetMapping("/findByCompanyId/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 集团id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<Integer>> findByCompanyId(@PathVariable("id") Integer id) {
        return R.ok(sysGroupService.findByCompanyId(id));
    }

    /**
     * 通过id查询所有项目组及子项目组的id
     *
     * @param id 项目组id
     * @return R
     */
    @ApiOperation(value = "通过id查询所有项目组及子项目组id", notes = "通过id查询所有项目组及子项目组id")
    @GetMapping("/findAllById/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 项目组id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R findAllById(@PathVariable("id") Integer id) {
        return R.ok(sysGroupService.findAllById(id));
    }

    /**
     * 通过id查询所有项目组及子项目组
     *
     * @param id 项目组id
     * @return R
     */
    @ApiOperation(value = "通过id查询所有项目组及子项目组", notes = "通过id查询所有项目组及子项目组")
    @GetMapping("/findById/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 集团id 项目组id 或 项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R findById(@PathVariable("id") Integer id) {
        return R.ok(sysGroupService.findById(id));
    }

    /**
     * 获取当前集团底下项目组树形结构列表
     *
     * @param id 集团id 项目组id 或 项目id
     * @return 项目组树形结构列表(含集团)
     */
    @ApiOperation(value = "获取当前集团底下项目组树形结构列表")
    @GetMapping("/tree/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = " 集团id 项目组id 或 项目id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R tree(@PathVariable Integer id) {
        List<SysCompany> sysCompanyList = sysCompanyService.findByGroupOrProjectId(id);
        if (sysCompanyList.size() > 0) {
            return R.ok(sysGroupService.selectTree(sysCompanyList.get(0).getCompanyId()));
        } else {
            return R.ok();
        }
    }

}
