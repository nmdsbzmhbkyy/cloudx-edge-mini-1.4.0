package com.aurine.cloudx.estate.controller;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.config.PermissionConfigure;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.SysRegion;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.DistinctUtil;
import com.aurine.cloudx.estate.util.QrcodeUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Lists;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (ProjectInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/8/31 16:51
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectInfo")
@Api(value = "projectInfo", tags = "小区管理")
public class ProjectInfoController {


    @Resource
    ProjectInfoService projectInfoService;

    @Resource
    ProjectStaffService projectStaffService;

    @Resource
    ProjectPersonInfoService projectPersonInfoService;

    @Resource
    ProjectVisitorService projectVisitorService;

    @Autowired
    PermissionConfigure permissionConfigure;

    @Resource
    private SysRegionService sysRegionService;

    /**
     * 分页查询当前员工负责的小区列表
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "分页查询当前员工负责的小区列表(物业)", notes = "分页查询当前员工负责的小区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "projectId", value = "小区ID", required = false, paramType = "param")
    })
    @GetMapping("/page/byStaff")
    public R<Page<ProjectInfoPageVo>> pageProjectByStaff(Page page,
                             @RequestParam(value = "projectId", required = false) String  projectId) {

        return R.ok(projectInfoService.pageProjectByStaff(page, projectId));
    }

    /**
     * 分页查询当前业主绑定的小区列表
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "分页查询当前业主绑定的小区列表(业主)", notes = "分页查询当前业主绑定的小区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/page/byPerson")
    public R<Page<ProjectInfoPageVo>> pageProjectByPerson(Page page) {
        return R.ok(projectInfoService.pageProjectByPerson(page));
    }

    /**
     * 员工分页查询所有小区
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "员工分页查询所有小区(物业)", notes = "员工分页查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/page/all/staff")
    public R<Page<ProjectInfoSimplePageVo>> pageAllByStaff(Page page, ProjectAddressParamVo addressParam) {

        addressParam.setUserId(SecurityUtils.getUser().getId());
        addressParam.setType(PersonTypeEnum.STAFF.code);
        return R.ok(projectInfoService.pageAll(page, addressParam));
    }

    /**
     * 员工查询所有小区
     *
     * @return 放回小区信息
     */
    @ApiOperation(value = "员工查询所有小区(物业)", notes = "员工查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/list/all/staff")
    public R<List<ProjectInfoSimplePageVo>> listAllByStaff(ProjectAddressParamVo addressParam) {
        addressParam.setUserId(SecurityUtils.getUser().getId());
        addressParam.setType( PersonTypeEnum.STAFF.code);
        return R.ok(projectInfoService.listAll(addressParam ));
    }

    /**
     * 业主分页查询所有小区
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "业主分页查询所有小区(业主)", notes = "业主分页查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/page/all/person")
    public R<Page<ProjectInfoSimplePageVo>> pageAllByPerson(Page page, ProjectAddressParamVo addressParam) {
        addressParam.setUserId(SecurityUtils.getUser().getId());
        addressParam.setType(PersonTypeEnum.PROPRIETOR.code);
        return R.ok(projectInfoService.pageAll(page, addressParam));
    }

    /**
     * 业主查询所有小区
     *
     * @return 放回小区信息
     */
    @ApiOperation(value = "业主查询所有小区(业主)", notes = "业主查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/list/all/person")
    public R<List<ProjectInfoSimplePageVo>> listAllByPerson(ProjectAddressParamVo addressParam) {
        addressParam.setType(PersonTypeEnum.PROPRIETOR.code);
        addressParam.setUserId(SecurityUtils.getUser().getId());
        return R.ok(projectInfoService.listAll(addressParam));
    }

    /**
     * 访客分页查询所有小区
     *
     * @param page 分页查询对象
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "访客分页查询所有小区(访客)", notes = "访客分页查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/page/all/visitor")
    public R<Page<ProjectInfoSimplePageVo>> pageAllByVisitor(Page page, ProjectAddressParamVo addressParam) {
        addressParam.setType(PersonTypeEnum.VISITOR.code);
        addressParam.setUserId(SecurityUtils.getUser().getId());
        return R.ok(projectInfoService.pageAll(page, addressParam));
    }

    /**
     * 访客查询所有小区
     *
     * @return 放回小区信息
     */
    @ApiOperation(value = "访客查询所有小区(访客)", notes = "访客查询所有小区")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/list/all/visitor")
    public R<List<ProjectInfoSimplePageVo>> listAllByVisitor(ProjectAddressParamVo addressParam) {
        addressParam.setUserId(SecurityUtils.getUser().getId());
        addressParam.setType(PersonTypeEnum.VISITOR.code);
        return R.ok(projectInfoService.listAll(addressParam));
    }

    /**
     * 根据id获取小区信息
     *
     * @return 放回小区信息
     */
    @ApiOperation(value = "根据id获取小区信息(业主、物业、访客)", notes = "根据id获取小区信息")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "PROJECT-ID", value = "小区id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping
    public R<ProjectInfoPageVo> nearest() {
        return R.ok(projectInfoService.getProjectInfoVoById(ProjectContextHolder.getProjectId()));
    }

    /**
     * 获取微信端小区权限列表
     *
     * @return 权限列表
     */
    @ApiOperation(value = "获取微信端小区权限列表(业主、物业、访客)", notes = "获取微信端小区权限列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型（1：员工2：业主3：访客）", required = true, paramType = "path"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    @GetMapping("/permission/{type}")
    public R<List<String>> permission(@PathVariable("type") String type) {

        if (ObjectUtil.isNotEmpty(permissionConfigure) && ObjectUtil.isNotEmpty(permissionConfigure.getDefaultPermissions())) {

            List<String> permissions = new ArrayList<>();
            for (PermissionProperty p : permissionConfigure.getDefaultPermissions()) {
                if (p.getType().equals(type)) {
                    permissions = p.getPermissions();
                }
            }
            List<String> excludePermissions = new ArrayList<>();
            Integer projectId = ProjectContextHolder.getProjectId();
            for (PermissionProperty p : permissionConfigure.getExcludePermissions()) {
                if (p.getType().equals(type) && p.getProjectId().equals(projectId)) {
                    excludePermissions = p.getPermissions();
                }
            }
            permissions.removeAll(excludePermissions);
            return R.ok(permissions);
        }
        return R.ok(Lists.newArrayList());
    }
    /**
     * 业主查询已开通的城市列表
     *
     * @return 放回小区信息
     */
    @ApiOperation(value = "查询已开通的城市列表(小区)", notes = "查询已开通的城市列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "name", value = "小区名称", paramType = "query"),
            @ApiImplicitParam(name = "provinceCode", value = "市级编码", paramType = "query"),
            @ApiImplicitParam(name = "cityCode", value = "县(区)编码", paramType = "query"),
            @ApiImplicitParam(name = "streetCode", value = "街道编码", paramType = "query"),
            @ApiImplicitParam(name = "cityName", value = "城市名称", paramType = "query")
    })
    @GetMapping("/city/list")
    public R<List<ProjectCityInfoVo>> listAllByPerson(@RequestParam(value = "name", required = false) String name,
                                                      @RequestParam(value = "provinceCode", required = false) String provinceCode,
                                                      @RequestParam(value = "cityCode", required = false) String cityCode,
                                                      @RequestParam(value = "streetCode", required = false) String streetCode,
                                                      @RequestParam(value = "cityName", required = false) String cityName) {
        ProjectAddressParamVo addressParam = new ProjectAddressParamVo();
        addressParam.setName(name);
        addressParam.setProvinceCode(provinceCode);
        addressParam.setCityCode(cityCode);
        addressParam.setStreetCode(streetCode);
        addressParam.setType(PersonTypeEnum.PROPRIETOR.code);
        addressParam.setUserId(SecurityUtils.getUser().getId());

        List<SysRegion> provinceList = sysRegionService.list(new QueryWrapper<SysRegion>().lambda()
                .eq(SysRegion::getRLevel, "1"));
        List<SysRegion> cityList = sysRegionService.list(new QueryWrapper<SysRegion>().lambda()
                .eq(SysRegion::getRLevel, "2"));
        List<ProjectInfoSimplePageVo>  projectInfoSimplePageVos  = projectInfoService.listAll(addressParam);
        List<ProjectCityInfoVo> cityInfoVos = new ArrayList<>();
        projectInfoSimplePageVos.stream()
                .filter(s -> StringUtil.isNotBlank(s.getCityCode()))
                .map(projectInfo -> {
                    if (StrUtil.isBlank(projectInfo.getCityCode())) {
                        projectInfo.setCityCode(projectInfo.getProvinceCode());
                    }
                    return projectInfo;
                })
                .filter(DistinctUtil.distinctByKey(b -> b.getCityCode()))
                .forEach(e -> {
                    ProjectCityInfoVo cityInfoVo = new ProjectCityInfoVo();
                    cityInfoVo.setCityCode(e.getCityCode());
                    cityInfoVo.setProvinceCode(e.getProvinceCode());
                    for (SysRegion sysRegion : provinceList) {
                        if (e.getProvinceCode().startsWith(sysRegion.getUid())) {
                            cityInfoVo.setProvinceName(sysRegion.getRegionName());
                            break;
                        }
                    }
                    for (SysRegion sysRegion : cityList) {
                        if (e.getProvinceCode().equals(e.getCityCode())) {
                            cityInfoVo.setCityName(cityInfoVo.getProvinceName());
                            break;
                        }
                        if (sysRegion.getUid().startsWith(e.getCityCode())) {
                            cityInfoVo.setCityName(sysRegion.getRegionName());
                            break;
                        }
                    }
                    cityInfoVos.add(cityInfoVo);
                });
        return R.ok(cityInfoVos.stream()
                .filter(e ->StrUtil.isBlank(cityName) || StrUtil.contains(e.getCityName(), cityName))
                .collect(Collectors.toList()));
    }
}
