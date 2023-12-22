package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.entity.ProjectPropertyContact;
import com.aurine.cloudx.estate.service.ProjectPropertyContactService;
import com.aurine.cloudx.estate.vo.AppPage;
import com.aurine.cloudx.estate.vo.AppPropertyContactVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目物业联系方式表(ProjectPropertyContact)表控制层
 *
 * @author xull
 * @version 1.0.0
 * @date 2020-10-27 15:38:48
 */
@RestController
@RequestMapping("/property-contact")
@Api(value = "property-contact", tags = "项目物业联系方式表")
public class ProjectPropertyContactController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private ProjectPropertyContactService projectPropertyContactService;

    /**
     * 分页查询所有数据
     * @param size
     * @param current
     * @param name
     * @param contactPhone
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取物业联系方式列表（联系物业）", notes = "获取物业联系方式列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "名字", paramType = "query"),
            @ApiImplicitParam(name = "contactPhone", value = "电话", paramType = "query")
    })
    public R<Page<AppPropertyContactVo>> selectAll(Long size, Long current, String name, String contactPhone) {
        AppPage page = new AppPage(current, size);

        ProjectPropertyContact projectPropertyContact = new ProjectPropertyContact();

        if (name != null) {
            projectPropertyContact.setName(name);
        }

        if (contactPhone != null) {
            projectPropertyContact.setContactPhone(contactPhone);
        }

        Page<ProjectPropertyContact> propertyContactVoPage = projectPropertyContactService.page(page, new QueryWrapper<>(projectPropertyContact));
        Page<AppPropertyContactVo> apppropertyContactVoPage =  new Page<>();
        BeanUtil.copyProperties(propertyContactVoPage, apppropertyContactVoPage);
        List<AppPropertyContactVo> appPropertyContactVoList = propertyContactVoPage.getRecords().stream()
                .map(e -> {
                    AppPropertyContactVo propertyContact = new AppPropertyContactVo();
                    propertyContact.setName(e.getName());
                    propertyContact.setContactPhone(e.getContactPhone());
                    return propertyContact;
                }).collect(Collectors.toList());
        apppropertyContactVoPage.setRecords(appPropertyContactVoList);
        return R.ok(apppropertyContactVoPage);
    }
}