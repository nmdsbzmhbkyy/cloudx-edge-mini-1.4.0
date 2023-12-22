package com.aurine.cloudx.estate.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.dto.ProjectHouseDTO;
import com.aurine.cloudx.estate.entity.ProjectPassPlan;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.feign.RemoteDeviceInfoService;
import com.aurine.cloudx.estate.feign.RemoteDevicePassService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.util.QrcodeUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Base64;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备管理(ProjectDeviceController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/8 11:08
 */
@RestController
@RequestMapping("/projectDevice")
@Api(value = "projectDevice", tags = "设备管理")
@Slf4j
public class ProjectDeviceController {

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private ProjectVisitorService projectVisitorService;

    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private RemoteDeviceInfoService remoteDeviceInfo;

    @Resource
    private ProjectPassPlanService projectPassPlanService;

    @Resource
    private RemoteDevicePassService remoteDevicePassService;


    private static final int IMG_SiZE = 800;

    private static final int VALID_TIME = 60;

    /**
     * 一键开门
     *
     * @return
     */
    @GetMapping("/openTheDoor/{id}")
    @ApiOperation(value = "一键开门(业主、物业)", notes = "一键开门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R openTheDoor(@PathVariable String id) {
        return R.ok(remoteDeviceInfo.open(id));
    }

    /**
     * 一键开门
     *
     * @return
     */
    @GetMapping("/door/open/{id}/{personType}/{personId}")
    @ApiOperation(value = "一键开门(业主、物业)", notes = "一键开门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R openTheDoor(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {
        return remoteDeviceInfo.open(id, personType, personId);
    }

    /**
     * 访客获取二维码
     *
     * @return
     */

    @GetMapping("/openByQRCode/{visitId}")
    @ApiOperation(value = "访客获取二维码(访客)", notes = "访客获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "visitId", value = "visitId", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<QRCodeVo> openByQRCode(@PathVariable("visitId") String visitId) {
        QRCodeVo qrCodeVo = new QRCodeVo();
        ProjectVisitorVo projectVisitorVo = projectVisitorService.getDataById(visitId);
        if (!AuditStatusEnum.pass.code.equals(projectVisitorVo.getAuditStatus())) {
            return R.failed("获取二维码失败，审核未通过");
        }
        qrCodeVo.setBeginTime(LocalDateTime.now());
        qrCodeVo.setEndTime(LocalDateTime.now().plusMinutes(60));
        ProjectDevicePassQRDTO projectDevicePassQRDTO = new ProjectDevicePassQRDTO();
        projectDevicePassQRDTO.setPersonId(projectVisitorVo.getVisitId());
        projectDevicePassQRDTO.setPersonName(projectVisitorVo.getVisitorName());
        projectDevicePassQRDTO.setProjectId(ProjectContextHolder.getProjectId());
        projectDevicePassQRDTO.setBeginTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) * 1000);
        projectDevicePassQRDTO.setEffectiveTime(60);
        projectDevicePassQRDTO.setTimes(999);
        projectDevicePassQRDTO.setPersonType(PersonTypeEnum.VISITOR);
        R result = remoteDevicePassService.getQRCode(projectDevicePassQRDTO);
        if (result.getCode() == 1) {
            return R.failed(result.getMsg());
        }
        String code = result.getData().toString();
        try {
            log.info("访客二维码code: " + code);
            QrConfig config = new QrConfig(IMG_SiZE, IMG_SiZE);
            config.setMargin(0);
            byte[] bytes = QrCodeUtil.generatePng(code, config);
            String base64Img = Base64.encodeBase64String(bytes);
            qrCodeVo.setImgBase64("data:image/png;base64," + base64Img);
        } catch (Exception e) {
            return R.failed("二维生成失败");
        }
        qrCodeVo.setBeginTime(DateUtil.parseLocalDateTime(projectVisitorVo.getPassBeginTime()));
        qrCodeVo.setEndTime(DateUtil.parseLocalDateTime(projectVisitorVo.getPassEndTime()));
        return R.ok(qrCodeVo);
    }

    /**
     * 业主获取二维码
     *
     * @return
     */

    @GetMapping("/openByQRCodeOnPerson")
    @ApiOperation(value = "业主获取二维码(业主)", notes = "业主获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<QRCodeVo> openByQRCodeOnPerson() {
        QRCodeVo qrCodeVo = new QRCodeVo();
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        qrCodeVo.setBeginTime(LocalDateTime.now());
        qrCodeVo.setEndTime(LocalDateTime.now().plusMinutes(1));
        ProjectDevicePassQRDTO projectDevicePassQRDTO = new ProjectDevicePassQRDTO();
        projectDevicePassQRDTO.setPersonId(projectPersonInfo.getPersonId());
        projectDevicePassQRDTO.setPersonName(projectPersonInfo.getPersonName());
        projectDevicePassQRDTO.setProjectId(ProjectContextHolder.getProjectId());
        projectDevicePassQRDTO.setBeginTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")) * 1000);
        projectDevicePassQRDTO.setEffectiveTime(1);
        projectDevicePassQRDTO.setPersonType(PersonTypeEnum.PROPRIETOR);
        R result = remoteDevicePassService.getQRCode(projectDevicePassQRDTO);
        if (result.getCode() == 1) {
            return R.failed(result.getMsg());
        }
        String code = result.getData().toString();
        try {
            log.info("业主二维码code: " + code);
            byte[] bytes = QrCodeUtil.generatePng(code, IMG_SiZE, IMG_SiZE);
            String base64Img = Base64.encodeBase64String(bytes);
            qrCodeVo.setImgBase64("data:image/png;base64," + base64Img);
        } catch (Exception e) {
            return R.failed("二维生成失败");
        }
        return R.ok(qrCodeVo);
    }

    private QRCodeVo getPersonQrCode(String personName) {
        QRCodeVo qrCodeVo = new QRCodeVo();
        int now = (int) (System.currentTimeMillis() / 1000);
        String code = QrcodeUtil.getInstance().getQrcode(personName, "S" + ProjectContextHolder.getProjectId() + "-00000000", now, now + VALID_TIME, 1, 1);
        byte[] bytes = QrCodeUtil.generatePng(code, IMG_SiZE, IMG_SiZE);
        String base64Img = Base64.encodeBase64String(bytes);
        qrCodeVo.setImgBase64("data:image/png;base64," + base64Img);
        return qrCodeVo;
    }

    /**
     * 物业获取二维码
     *
     * @return
     */


    @GetMapping("/openByQRCodeOnStaff")
    @ApiOperation(value = "物业获取二维码(物业)", notes = "物业获取二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<QRCodeVo> openByQRCodeOnStaff() {
        QRCodeVo qrCodeVo = new QRCodeVo();
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        int now = (int) (System.currentTimeMillis() / 1000);
        String code = QrcodeUtil.getInstance().getQrcode(projectStaffVo.getStaffName(),
                "S" + ProjectContextHolder.getProjectId() + "-00000000", now,
                now + VALID_TIME, 1, 2);
        qrCodeVo.setBeginTime(LocalDateTime.now());
        qrCodeVo.setEndTime(LocalDateTime.now().plusMinutes(1));
        try {
            log.info("物业二维码code: " + code);
            byte[] bytes = QrCodeUtil.generatePng(code, IMG_SiZE, IMG_SiZE);
            String base64Img = Base64.encodeBase64String(bytes);
            qrCodeVo.setImgBase64("data:image/png;base64," + base64Img);
        } catch (Exception e) {
            return R.failed("二维生成失败");
        }
        return R.ok(qrCodeVo);
    }

    /**
     * 业主获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/person/getDoorDevice")
    @ApiOperation(value = "业主获取可通行门禁设备列表(业主) ** 有bug **", notes = "业主获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getDoorDeviceByPerson() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) && (LocalDateTime.now().isAfter
                (personPlanRel.getEffTime()) && personPlanRel.getExpTime() == null ? true : LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.wechatGetlistDevice(projectPersonInfo.getPersonId(), personPlanRel.getPlanId()));
        }
        return R.ok(new ArrayList<ProjectPassDeviceVo>());
    }


    /**
     * 业主获取可通行门禁设备列表
     *
     * @return
     */


    /**
     * 业主分页获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/person/pageDoorDevice")
    @ApiOperation(value = "业主分页获取可通行门禁设备列表(业主)", notes = "业主分页获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectPassDeviceVo>> pageDoorDeviceByPerson(Page<ProjectPassDeviceVo> page) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && personPlanRel.getExpTime() == null ? true : LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.pageDeviceByPersonId(page, projectPersonInfo.getPersonId()));
        }
        return R.ok(page);
    }
    /**
     * 业主获取可通行门禁设备名称
     *
     * @return
     */

    @GetMapping("/name/list")
    @ApiOperation(value = "业主获取可通行门禁设备名称(一键开门)", notes = "业主获取可通行门禁设备名称")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<String>> getDeviceName() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && personPlanRel.getExpTime() == null ? true : LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            List<String> deviceName = new ArrayList<>();
            projectPersonDeviceService.wechatGetlistDevice(projectPersonInfo.getPersonId(), personPlanRel.getPlanId())
                    .stream().forEach(e -> {
                deviceName.add(e.getDeviceName());
            });
//            StrUtil.join(",", deviceName);
            return R.ok(deviceName);
        }
        return R.ok(new ArrayList<String>());
    }


    /**
     * 物业获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/staff/getDoorDevice")
    @ApiOperation(value = "物业获取可通行门禁设备列表(物业)", notes = "物业获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectPassDeviceVo>> getDoorDeviceByStaff() {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectStaffVo.getStaffId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && personPlanRel.getExpTime() == null ? true : LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.listDeviceByPersonIdAndPlanId(projectStaffVo.getStaffId(), personPlanRel.getPlanId()));
        }
        return R.ok(new ArrayList<>());
    }


    /**
     * 物业分页获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/staff/pageDoorDevice")
    @ApiOperation(value = "物业分页获取可通行门禁设备列表(物业)", notes = "物业分页获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectPassDeviceVo>> pageDoorDeviceByStaff(Page<ProjectPassDeviceVo> page) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
         ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectStaffVo.getStaffId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && personPlanRel.getExpTime() == null ? true : LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.pageDeviceByPersonId(page, projectStaffVo.getStaffId()));
        }
        return R.ok(page);
    }

    /**
     * 物业获取监控设备列表
     *
     * @return
     */

    @GetMapping("/staff/getVideoDevice")
    @ApiOperation(value = "物业获取监控设备列表(物业,业主)", notes = "物业获取监控设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getVideoDeviceByStaff(Page page) {

        ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo = new ProjectDeviceInfoPageFormVo();
        projectDeviceInfoPageFormVo.setDeviceTypeId(DeviceTypeConstants.MONITOR_DEVICE);
        return R.ok(projectDeviceInfoService.pageVo(page, projectDeviceInfoPageFormVo));
    }
}
