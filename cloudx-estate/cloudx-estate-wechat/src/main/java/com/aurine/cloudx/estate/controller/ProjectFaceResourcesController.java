package com.aurine.cloudx.estate.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.estate.constant.enums.DataOriginEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.feign.RemoteFaceResourcesService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectFaceResourceAppPageVo;
import com.aurine.cloudx.estate.vo.ProjectFaceResourcesAppVo;
import com.aurine.cloudx.estate.vo.ProjectPassDeviceVo;
import com.aurine.cloudx.estate.vo.ProjectStaffVo;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 人脸接口(ProjectFaceResourcesController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/4 11:56
 */

@RestController
@RequestMapping("/projectFaceResources")
@Api(value = "projectFaceResources", tags = "人脸接口")
@Slf4j
public class ProjectFaceResourcesController {
    @Resource
    private ProjectFaceResourcesService projectFaceResourcesService;
    @Resource
    private ProjectStaffService projectStaffService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private RemoteFaceResourcesService remoteFaceResourcesService;
    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    /**
     * 通过id查询小区人脸库，用于小区辖区内的人脸识别设备下载
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过人脸id获取人脸信息(业主、物业)", notes = "通过人脸id获取人脸信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "人脸id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(projectFaceResourcesService.getById(id));
    }

    /**
     * 通过id查询小区人脸库，用于小区辖区内的人脸识别设备下载
     *
     * @return R
     */
    @ApiOperation(value = "查询小区下一个人员的人脸信息与状态(业主、物业)", notes = "当用户不存在人脸，返回haveFace = 0，其他时候 = 1\n" +
            "     * 当用户正在下发人脸，状态为 0 全部下载失败 更换面部图片重试\n" +
            "     * 当用户存在多张人脸，状态为 1 授权成功 存在微信上传的人脸时，返回微信上传的人脸  \n" +
            "     * 当用户正在下发人脸, 状态为 2 部分授权成功 部分还在下发中(授权中)\" +\n" +
            "     * 当用户正在下发人脸，状态为 3 超过1天还未全部下载成功 （下载超时 请联系物业）\n" +
            "     * 当用户正在下发人脸, 状态为 4 部分授权成功 部分授权失败 部分下发中（授权失败，请联系物业）" +
            "     * 当用户正在下发人脸, 状态为 5 项目无设备或住户无设备人脸无状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "人脸id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
            @ApiImplicitParam(name = "personType", required = true, value = "人员类型：1、住户，2、员工、3：访客", paramType = "path"),
    })
    @GetMapping("/face/{personType}")
    public R getByPersonId(@PathVariable("personType") String personType) {
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
                faceResourcesAppVo.setDlStatus("5");
                faceResourcesAppVo.setIsActive("2");
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
        //检查是否全部都下载成功（）
        List<ProjectRightDevice> rightDeviceList = projectRightDeviceService.list(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getCertMediaId, faceResourcesAppVo.getFaceId()));
        boolean isSuccess = checkIsSuccess(rightDeviceList);
        if (isSuccess) {
            faceResourcesAppVo.setDlStatus(DataConstants.TRUE);
        }
        R r = R.ok(faceResourcesAppVo);
        log.info("WeChat {} Data {} : {}", "Get", "Face", r);
        return r;
    }

    private boolean checkIsSuccess(List<ProjectRightDevice> rightDeviceList) {
        if (CollUtil.isNotEmpty(rightDeviceList)) {
            int countSuccess = 0, countTotal = rightDeviceList.size();
            for (ProjectRightDevice rightDevice : rightDeviceList) {
                if (rightDevice.getDlStatus().equals(PassRightCertDownloadStatusEnum.SUCCESS.code)) {
                    countSuccess++;
                }
            }
            if (countSuccess >= countTotal) {
                return true;
            }
        }
        return false;
    }


    /**
     * 业主上传门禁人脸
     * 如果用户已存在其他微信上传人脸，该接口启动替换流程
     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程
     * 如果用户无人脸，该接口启动正常添加人脸流程
     *
     * @return R
     */
    @ApiOperation(value = "上传门禁人脸(业主)", notes = "     * 如果用户已存在其他微信上传人脸，该接口启动替换流程\n" +
            "     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程\n" +
            "     * 如果用户无人脸，该接口启动正常添加人脸流程")
    @SysLog("微信小程序上传人脸")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "picUrl", value = "人脸图片地址", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/face")
    public R saveFace(@RequestBody ProjectFaceResourcesAppVo appVo) {


        //personId、picUrl、personType为必填

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        appVo.setPersonId(projectPersonInfo.getPersonId());
        appVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        appVo.setOrigin(DataOriginEnum.WECHAT.code);
        appVo.setStatus("1");

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
            BeanUtils.copyProperties(appVo, projectFaceResources);
            return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
        }
        return R.failed("权限已被禁止，无法上传");
    }

    /**
     * 物业上传门禁人脸
     * 如果用户已存在其他微信上传人脸，该接口启动替换流程
     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程
     * 如果用户无人脸，该接口启动正常添加人脸流程
     *
     * @return R
     */
    @ApiOperation(value = "上传门禁人脸(业主)", notes = "     * 如果用户已存在其他微信上传人脸，该接口启动替换流程\n" +
            "     * 如果用户上传人脸未成功，该接口启动未成功替换人脸流程\n" +
            "     * 如果用户无人脸，该接口启动正常添加人脸流程")
    @SysLog("微信小程序上传人脸")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "picUrl", value = "人脸图片地址", required = true, paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @PostMapping("/faceByStaff")
    public R saveFaceByStaff(@RequestBody ProjectFaceResourcesAppVo appVo) {


        //personId、picUrl、personType为必填

//        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
//        //判断业主访客是否存在并且申请已经通过
//        if (ObjectUtil.isEmpty(projectPersonInfo)) {
//            return R.failed("您在该小区下还未登记");
//        }
//        appVo.setPersonId(projectPersonInfo.getPersonId());
        appVo.setPersonType(PersonTypeEnum.PROPRIETOR.code);
        appVo.setOrigin(DataOriginEnum.WECHAT.code);
        appVo.setStatus("1");

        /**
         * 初始化住户、员工权限凭证，避免首次访问无通行方案
         * 如果用户已有方案，获取方案id，如果没有，分配默认方案，并返回方案id
         */
        projectPersonDeviceService.initDefaultPassRightPlan(PersonTypeEnum.PROPRIETOR, appVo.getPersonId());

        ProjectFaceResources projectFaceResources = new ProjectFaceResources();
        BeanUtils.copyProperties(appVo, projectFaceResources);
        return remoteFaceResourcesService.saveFaceByApp(projectFaceResources);
    }

    @ApiOperation(value = "查询人脸授权分页列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dlStatus", value = "授权状态", paramType = ""),
            @ApiImplicitParam(name = "id", value = "人脸id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header"),
    })
    @GetMapping("/face/page")
    public R<Page<ProjectFaceResourceAppPageVo>> getByPersonId(Page page, @RequestParam("dlStatus") String dlStatus) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        Page<ProjectFaceResourceAppPageVo> faceResourceAppPageVoPage = projectFaceResourcesService.pagePersonFace(page, dlStatus);
        //查询房屋含人脸授权信息
        return R.ok(faceResourceAppPageVoPage);
    }


}
