package com.aurine.cloudx.estate.controller;


import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.SysRegion;
import com.aurine.cloudx.estate.feign.RemoteWechatProjectInfoService;
import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.SysRegionService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aurine.cloudx.estate.util.QrcodeUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/project")
@Api(value = "project", tags = "小区管理")
public class ProjectInfoController {


    @Resource
    private ProjectInfoService projectInfoService;

    @Resource
    private SysRegionService sysRegionService;

    @Resource
    private RemoteWechatProjectInfoService remoteWechatProjectInfoService;

    /**
     * 业主分页查询所有小区
     *
     * @param size
     * @param current
     * @param name
     * @param provinceCode
     * @param cityCode
     * @param streetCode
     * @return
     */
    @ApiOperation(value = "查询小区列表(小区)", notes = "查询小区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "小区名称", paramType = "query"),
            @ApiImplicitParam(name = "provinceCode", value = "市级编码", paramType = "query"),
            @ApiImplicitParam(name = "cityCode", value = "县(区)编码", paramType = "query"),
            @ApiImplicitParam(name = "streetCode", value = "街道编码", paramType = "query")
    })
    @GetMapping("/page/all")
    public R<Page<ProjectInfoSimplePageVo>> pageAllByPerson(Long size,
                                                            Long current,
                                                            String name,
                                                            String provinceCode,
                                                            String cityCode,
                                                            String streetCode) {
        ProjectAddressParamVo addressParam = new ProjectAddressParamVo();
        addressParam.setName(name);
        addressParam.setProvinceCode(provinceCode);
        addressParam.setCityCode(cityCode);
        addressParam.setStreetCode(streetCode);
        addressParam.setUserId(SecurityUtils.getUser().getId());
        addressParam.setType(PersonTypeEnum.PROPRIETOR.code);

        AppPage page = new AppPage(current, size);

        return R.ok(projectInfoService.pageAll(page, addressParam));
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
                .map(projectInfo -> {
                    if (StrUtil.isBlank(projectInfo.getCityCode())) {
                        projectInfo.setCityCode(projectInfo.getProvinceCode());
                    }
                    return projectInfo;
                })
                .filter(QrcodeUtil.distinctByKey(b -> b.getCityCode()))
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

    /**
     * 分页查询当前员工负责的小区列表
     *
     * @return 放回小区分页信息
     */
    @ApiOperation(value = "获取当前员工负责的小区列表(物业)", notes = "获取当前员工负责的小区列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "projectId", value = "小区ID", required = false, paramType = "query")
    })
    @GetMapping("/staff/page")
    public R<Page<ProjectInfoPageVo>> pageProjectByStaff(@RequestParam(value = "size", required = false) Long size,
                                                         @RequestParam(value = "current", required = false) Long current,
                                                         @RequestParam(value = "projectId", required = false) String  projectId) {
        Map<String, Object> query = new HashMap<>();
        query.put("size", size);
        query.put("current", current);
        query.put("projectId", projectId);
        return remoteWechatProjectInfoService.pageProjectByStaff(query);
    }
}
