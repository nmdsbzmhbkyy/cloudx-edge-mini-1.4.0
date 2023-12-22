package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectFaceResourcesDto;
import com.aurine.cloudx.estate.service.OpenApiProjectFaceResourcesService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/05/17 10:54
 * @Package: com.aurine.openv2.controller
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@RestController
@RequestMapping("/open/face/inner")
@Api(value = "face", tags = "人脸信息管理", hidden = true)
public class OpenApiProjectFaceResourcesController {

    @Resource
    private OpenApiProjectFaceResourcesService openApiProjectFaceResourcesService;

    /**
     * 新增员工人脸信息
     *
     * @param projectFaceResourcesDto 员工人脸信息参数
     * @return R
     */
    @ApiOperation(value = "新增员工人脸信息", notes = "指定员工Id/手机号、人脸照片，更新员工人脸信息，不存在人脸信息则新增，返回新增人脸信息")
    @SysLog("新增员工人脸资源信息")
    @PostMapping(value = "/staff")
    @Inner
    public R<OpenApiProjectFaceResourcesDto> saveOrUpdateStaffFaceResource(@RequestBody OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        projectFaceResourcesDto.setPersonType(PersonTypeEnum.STAFF.code);

        try {
            return openApiProjectFaceResourcesService.saveFaceInfo(projectFaceResourcesDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 删除员工人脸信息
     *
     * @param projectFaceResourcesDto 员工人脸信息参数
     * @return R
     */
    @ApiOperation(value = "删除员工人脸信息", notes = "指定员工Id/手机号，删除员工人脸信息，返回员工Id/手机号")
    @SysLog("删除员工人脸信息资源信息")
    @DeleteMapping(value = "/staff")
    @Inner
    public R<String> removeStaffFaceResource(@RequestBody OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        projectFaceResourcesDto.setPersonType(PersonTypeEnum.STAFF.code);

        try {
            return openApiProjectFaceResourcesService.removeFaceInfo(projectFaceResourcesDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }


    /**
     * 新增住户人脸信息
     *
     * @param projectFaceResourcesDto 住户人脸信息参数
     * @return R
     */
    @ApiOperation(value = "新增住户人脸信息", notes = "指定住户Id/手机号、人脸照片，更新住户人脸信息，不存在人脸信息则新增，返回新增人脸信息")
    @SysLog("新增住户人脸资源信息")
    @PostMapping(value = "/household")
    @Inner
    public R<OpenApiProjectFaceResourcesDto> saveOrUpdateProprietorFaceResource(@RequestBody OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        projectFaceResourcesDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        try {
            return openApiProjectFaceResourcesService.saveFaceInfo(projectFaceResourcesDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 删除住户人脸信息
     *
     * @param projectFaceResourcesDto 住户人脸信息参数
     * @return R
     */
    @ApiOperation(value = "删除住户人脸信息", notes = "指定住户Id/手机号，删除住户人脸信息，返回住户Id/手机号")
    @SysLog("删除住户人脸信息资源信息")
    @DeleteMapping(value = "/household")
    @Inner
    public R<String> removeProprietorFaceResource(@RequestBody OpenApiProjectFaceResourcesDto projectFaceResourcesDto) {
        projectFaceResourcesDto.setPersonType(PersonTypeEnum.PROPRIETOR.code);

        try {
            return openApiProjectFaceResourcesService.removeFaceInfo(projectFaceResourcesDto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
