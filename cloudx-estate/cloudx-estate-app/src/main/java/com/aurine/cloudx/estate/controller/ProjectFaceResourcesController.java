package com.aurine.cloudx.estate.controller;


import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectFaceResources;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.AppFaceGetVo;
import com.aurine.cloudx.estate.vo.AppFaceUploadVo;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import com.aurine.cloudx.estate.util.ImgConvertUtil;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;


/**
 * 人脸接口(ProjectFaceResourcesController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/4 11:56
 */

@RestController
@RequestMapping("/face")
@Api(value = "projectFaceResources", tags = "人脸接口")
@Slf4j
public class ProjectFaceResourcesController {
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;
    @Resource
    private ImgConvertUtil imgConvertUtil;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    /**
     * 通过人员类型查询小区人脸库，用于小区辖区内的人脸识别设备下载
     *
     * @return R
     */
    @GetMapping("/person-type/{personType}")
    @ApiOperation(value = "查询当前小区业主的人脸信息与状态（门禁人脸）", notes = "当用户不存在人脸，返回haveFace = 0，其他时候 = 1\n" +
            "     * 当用户正在下发人脸，状态为 0 全部下载失败 更换面部图片重试\n" +
            "     * 当用户存在多张人脸，状态为 1 授权成功 存在微信上传的人脸时，返回微信上传的人脸  \n" +
            "     * 当用户正在下发人脸, 状态为 2 部分授权成功 部分还在下发中(授权中)\" +\n" +
            "     * 当用户正在下发人脸，状态为 3 超过1天还未全部下载成功 （下载超时 请联系物业）\n" +
            "     * 当用户正在下发人脸, 状态为 4 部分授权成功 部分授权失败（授权失败，请联系物业）" +
            "     * 当用户正在下发人脸, 状态为 5 项目无设备或住户无设备人脸无状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "personType", required = true, value = "人员类型：1、住户，2、员工、3：访客", paramType = "path")
    })
    public R<AppFaceGetVo> getByPersonId(@PathVariable("personType") String personType) throws IOException {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        ProjectFaceResourcesAppVo faceResourcesAppVo = projectFaceResourcesService.getByPersonIdForApp(projectPersonInfo.getPersonId(), personType);
        faceResourcesAppVo.setIsActive("4");
        if (ObjectUtil.isNotEmpty(personPlanRel)) {
            if (DataConstants.FALSE.equals(personPlanRel.getIsActive())) {
                // 通行权限禁用 0
                faceResourcesAppVo.setDlStatus("5");
                faceResourcesAppVo.setIsActive(DataConstants.FALSE);
            } else if (LocalDateTime.now().isAfter(personPlanRel.getExpTime())) {
                // 通行权限过期 2
                faceResourcesAppVo.setIsActive("2");
                faceResourcesAppVo.setDlStatus("5");
            } else if (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) &&
                    LocalDateTime.now().isBefore(personPlanRel.getExpTime())) {
                Page<ProjectPassDeviceVo> passDeviceVos = projectPersonDeviceService.pageDeviceByPersonId(new Page(), projectPersonInfo.getPersonId());
                if (passDeviceVos.getTotal() == 0) {
                    // 通行权限无设备
                    faceResourcesAppVo.setIsActive("3");
                    faceResourcesAppVo.setDlStatus("5");
                } else {
                    faceResourcesAppVo.setIsActive(DataConstants.TRUE);
                }
            }
        }
        Integer deviceCount = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.LADDER_WAY_DEVICE)
                .or().eq(ProjectDeviceInfo::getDeviceType, DeviceTypeConstants.GATE_DEVICE)
                .eq(ProjectDeviceInfo::getStatus, "1"));
        faceResourcesAppVo.setIsHaveDevice(deviceCount > 0 ? "1" : "0");
        if ("0".equals(faceResourcesAppVo.getIsHaveDevice())) {
            faceResourcesAppVo.setDlStatus("5");
        }
        AppFaceGetVo appVo = new AppFaceGetVo();
        BeanUtils.copyProperties(faceResourcesAppVo, appVo);
        appVo.setPicBase64(imgConvertUtil.urlToBase64(faceResourcesAppVo.getPicUrl()));
        appVo.setPersonType(personType);
        R r = R.ok(appVo);
        log.info("App {} Data {} : {}", "Get", "Face", r);
        return r;
    }

    /**
     * 业主上传门禁人脸
     * 如果用户已存在其他微信上传人脸，该接口启动替换流程
     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程
     * 如果用户无人脸，该接口启动正常添加人脸流程
     *
     * @return R
     */
    @ApiOperation(value = "业主上传门禁人脸（门禁人脸）", notes = "     * 如果用户已存在其他微信上传人脸，该接口启动替换流程\n" +
            "     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程\n" +
            "     * 如果用户无人脸，该接口启动正常添加人脸流程")
    @SysLog("微信小程序上传人脸")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "picUrl", value = "人脸图片地址", required = true, paramType = "query")
//            @ApiImplicitParam(name = "picBase64", value = "人脸图片Base64", required = true, paramType = "body")
    })
    @PostMapping
    public R<String> saveFace(@RequestBody AppFaceUploadVo appFaceUploadVo) throws IOException {
        //personId、picUrl、personType为必填

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        appFaceUploadVo.setPersonId(projectPersonInfo.getPersonId());
//        appVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        appFaceUploadVo.setOrigin(DataOriginEnum.APP.code);
        appFaceUploadVo.setStatus("1");

        /**
         * 初始化住户、员工权限凭证，避免首次访问无通行方案
         * 如果用户已有方案，获取方案id，如果没有，分配默认方案，并返回方案id
         */
        projectPersonDeviceService.initDefaultPassRightPlan(PersonTypeEnum.PROPRIETOR, projectPersonInfo.getPersonId());
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) &&
                DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                LocalDateTime.now().isBefore(personPlanRel.getExpTime())) {
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            BeanUtils.copyProperties(appFaceUploadVo, projectFaceResources);

            projectFaceResources.setPicUrl(imgConvertUtil.base64ToMinio(appFaceUploadVo.getPicBase64()));
            projectFaceResources.setPersonType("1");
            return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
        }
        return R.failed("权限已被禁止或无门禁设备，无法上传");
    }

    /**
     * 业主上传门禁人脸
     * 如果用户已存在其他微信上传人脸，该接口启动替换流程
     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程
     * 如果用户无人脸，该接口启动正常添加人脸流程
     *
     * @return R
     */
    @ApiOperation(value = "物业上传门禁人脸（物业）", notes = "     * 如果用户已存在其他微信上传人脸，该接口启动替换流程\n" +
            "     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程\n" +
            "     * 如果用户无人脸，该接口启动正常添加人脸流程")
    @SysLog("微信小程序上传人脸")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
    })
    @PostMapping("/staff")
    public R<String> saveStaffFace(@RequestBody AppFaceUploadVo appFaceUploadVo) throws IOException {
        //personId、picUrl、personType为必填

        appFaceUploadVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        appFaceUploadVo.setOrigin(DataOriginEnum.APP.code);
        appFaceUploadVo.setStatus("1");

        /**
         * 初始化住户、员工权限凭证，避免首次访问无通行方案
         * 如果用户已有方案，获取方案id，如果没有，分配默认方案，并返回方案id
         */
        projectPersonDeviceService.initDefaultPassRightPlan(PersonTypeEnum.PROPRIETOR, appFaceUploadVo.getPersonId());
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(appFaceUploadVo.getPersonId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) &&
                DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                LocalDateTime.now().isBefore(personPlanRel.getExpTime())) {
            ProjectFaceResources projectFaceResources = new ProjectFaceResources();
            BeanUtils.copyProperties(appFaceUploadVo, projectFaceResources);

            projectFaceResources.setPicUrl(imgConvertUtil.base64ToMinio(appFaceUploadVo.getPicBase64()));
            return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
        }
        return R.failed("权限已被禁止或无门禁设备，无法上传");
    }

}
