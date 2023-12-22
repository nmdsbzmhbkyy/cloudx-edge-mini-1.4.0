package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectHousePersonRelDto;
import com.aurine.cloudx.estate.service.OpenApiProjectHousePersonRelService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/06/27 14:43
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks:
 **/
@RestController
@RequestMapping("/open/household/inner")
@Api(value = "openApiProjectHousehold", tags = "openApi项目住户信息表管理", hidden = true)
public class OpenApiProjectHousePersonRelController {
    @Resource
    private OpenApiProjectHousePersonRelService projectHousePersonRelService;

    /**
     * 开放平台 复合接口
     * 涉及 新增住户信息，有人脸、卡时保存人脸、卡信息 下发用户绑定设备是保存设备权限
     *
     * @param projectHousePersonRelDto 项目住户信息表
     * @return R
     */
    @ApiOperation(value = "新增项目住户信息", notes = "新增项目住户信息")
    @SysLog("新增项目住户信息")
    @PostMapping
    @Inner
    public R<OpenApiProjectHousePersonRelDto> openApiInnerSave(@RequestBody OpenApiProjectHousePersonRelDto projectHousePersonRelDto) {

        try {
            return projectHousePersonRelService.saveHousehold(projectHousePersonRelDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 开放平台 复合接口
     * 涉及 新增住户信息，有人脸、卡时保存人脸、卡信息 下发用户绑定设备是保存设备权限
     *
     * @param projectHousePersonRelDto 项目住户信息表
     * @return R
     */
    @ApiOperation(value = "修改项目住户信息表", notes = "修改项目住户信息表")
    @SysLog("修改项目住户信息表")
    @PutMapping
    @Inner
    public R<OpenApiProjectHousePersonRelDto> openApiInnerUpdate(@RequestBody OpenApiProjectHousePersonRelDto projectHousePersonRelDto) {
        try {
            return projectHousePersonRelService.updateById(projectHousePersonRelDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 开放平台 复合接口
     * 涉及 删除住户信息，有人脸、卡时删除人脸、卡信息 下发用户绑定设备删除设备上人员信息
     *
     * @param realId 住户关系id
     * @return R
     */
    @ApiOperation(value = "删除项目住户信息", notes = "删除项目住户信息")
    @SysLog("删除项目住户信息")
    @DeleteMapping("/{realId}")
    @Inner
    public R<String> openApiInnerRemove(@PathVariable("realId") String realId) {
        try {
            return projectHousePersonRelService.removeHousePersonRel(realId);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

}
