
package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceSubsystem;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceSubsystemService;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoFormVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceSubsystemTreeVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
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
import java.util.List;


/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:39:47
 */
@RestController
@RequestMapping("/projectDeviceSubSystem")
@Api(value = "projectDeviceSubSystem", tags = "设备信息表管理")
public class ProjectDeviceSubsystemController {
    @Resource
    private ProjectDeviceSubsystemService projectDeviceSubsystemService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 分页查询
     *
     * @param page
     *         分页对象
     * @param projectDeviceSubsystem
     *         设备信息表
     *
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<IPage<ProjectDeviceSubsystem>> getProjectDeviceSubsystemPage(Page<ProjectDeviceSubsystem> page, ProjectDeviceSubsystem projectDeviceSubsystem) {

        return R.ok(projectDeviceSubsystemService.page(page, Wrappers.query(projectDeviceSubsystem)));
    }


    /**
     * 通过id查询设备信息表
     *
     * @param id
     *         id
     *
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备子系统id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectDeviceSubsystem> getById(@PathVariable("id") String id) {
        return R.ok(projectDeviceSubsystemService.getById(id));
    }

    /**
     * 新增设备信息表
     *
     * @param projectDeviceSubsystem
     *         设备信息表
     *
     * @return R
     */
    @ApiOperation(value = "新增设备信息表", notes = "新增设备信息表")
    @SysLog("新增设备信息表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_subsystem_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody ProjectDeviceSubsystem projectDeviceSubsystem) {
        List<ProjectDeviceSubsystem> list = projectDeviceSubsystemService.list(new LambdaQueryWrapper<ProjectDeviceSubsystem>().eq(ProjectDeviceSubsystem::getSubsystemName, projectDeviceSubsystem.getSubsystemName()));
        if(CollectionUtil.isNotEmpty(list)) {
            return R.failed("子系统名称不能重复");
        }
        projectDeviceSubsystemService.save(projectDeviceSubsystem);
        return R.ok(projectDeviceSubsystem.getSubsystemId());
    }

    /**
     * 修改设备信息表
     *
     * @param projectDeviceSubsystem
     *         设备信息表
     *
     * @return R
     */
    @ApiOperation(value = "修改设备信息表", notes = "修改设备信息表")
    @SysLog("修改设备信息表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_subsystem_edit')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateById(@RequestBody ProjectDeviceSubsystem projectDeviceSubsystem) {
        List<ProjectDeviceSubsystem> list = projectDeviceSubsystemService.list(new LambdaQueryWrapper<ProjectDeviceSubsystem>().eq(ProjectDeviceSubsystem::getSubsystemName, projectDeviceSubsystem.getSubsystemName())
                .ne(ProjectDeviceSubsystem::getSubsystemId,projectDeviceSubsystem.getSubsystemId()));
        if(CollectionUtil.isNotEmpty(list)) {
            return R.failed("子系统名称不能重复");
        }
        return R.ok(projectDeviceSubsystemService.updateById(projectDeviceSubsystem));
    }

    /**
     * 通过id删除设备信息表
     *
     * @param id
     *         id
     *
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息表", notes = "通过id删除设备信息表")
    @SysLog("通过id删除设备信息表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('estate_subsystem_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备子系统id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        ProjectDeviceSubsystem projectDeviceSubsystem = projectDeviceSubsystemService.getById(id);
        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
        projectDeviceInfoFormVo.setTypes(Lists.newArrayList(projectDeviceSubsystem.getSubsystemCode()));
        int count = projectDeviceSubsystemService.count(new LambdaQueryWrapper<ProjectDeviceSubsystem>().eq(ProjectDeviceSubsystem::getPid, id));
        if(count>0) {
            return R.failed("包含下级子系统,删除失败");
        }

        return R.ok(projectDeviceSubsystemService.removeById(id));
    }

    /**
     * 获取子系统树
     *
     * @return
     */
    @ApiOperation("获取子系统树")
    @GetMapping("/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSubsystemTreeVo> > findTree() {
        List<ProjectDeviceSubsystemTreeVo> tree = projectDeviceSubsystemService.findTree();

        return R.ok(updateTree(tree, ""));
    }

    private List<ProjectDeviceSubsystemTreeVo> updateTree(List<ProjectDeviceSubsystemTreeVo> treeVos, String name) {

        treeVos.forEach(treeVo -> {
            if (treeVo.getChildren() != null && treeVo.getChildren().size() > 0) {
                String nameString;
                if (name != null && !"".equals(name)) {
                    nameString = name + "-" + treeVo.getName();
                } else {
                    nameString = treeVo.getName();
                }
                updateTree(treeVo.getChildren(), nameString);
            }
            String nameString;
            if (name != null && !"".equals(name)) {
                nameString = name + "-" + treeVo.getName();
            } else {
                nameString = treeVo.getName();
            }
            treeVo.setTreeName(nameString);
        });
        return treeVos;
    }

    /**
     * 列表查询
     *
     * @param projectDeviceSubsystem
     *         设备信息表
     *
     * @return R
     */
    @ApiOperation(value = "子系统列表查询", notes = "子系统列表查询")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSubsystem>> list(ProjectDeviceSubsystem projectDeviceSubsystem) {
        return R.ok(projectDeviceSubsystemService.list(Wrappers.query(projectDeviceSubsystem)));
    }


}
