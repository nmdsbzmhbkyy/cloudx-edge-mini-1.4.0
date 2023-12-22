package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AuditStatusEnum;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonPlanRel;
import com.aurine.cloudx.estate.feign.*;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.api.client.util.Base64;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.aurine.cloudx.estate.util.QrcodeUtil;

import javax.annotation.Resource;
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
@RequestMapping("/device")
@Api(value = "device", tags = "设备管理")
@Slf4j
public class ProjectDeviceController {

    @Resource
    private ProjectPersonDeviceService projectPersonDeviceService;
    @Resource
    private RemotePersonInfoService remotePersonInfoService;

    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;

    @Resource
    private RemoteVisitorService remoteVisitorService;

    @Resource
    private ProjectPersonPlanRelService projectPersonPlanRelService;

    @Resource
    private RemoteDeviceInfoService remoteDeviceInfo;

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    @Resource
    private ProjectStaffService projectStaffService;

    @Resource
    private RemoteDevicePassService remoteDevicePassService;

    @Resource
    private RemoteDdDeviceMapService remoteDdDeviceMapService;


    private static final int IMG_SiZE = 200;

    private static final int VALID_TIME = 60;

    /**
     * 访客获取二维码
     *
     * @return
     */

    @GetMapping("/qr-code/visitor/{visitId}")
    @ApiOperation(value = "获取访客二维码(访客邀约)", notes = "获取访客二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客ID", required = true, paramType = "path")
    })
    public R<QRCodeVo> openByQRCode(@PathVariable("visitId") String visitId) {
        QRCodeVo qrCodeVo = new QRCodeVo();

        R<ProjectVisitorVo> request = remoteVisitorService.getData(visitId);
        if (request.getCode() == 1 || ObjectUtil.isEmpty(request.getData())) {
            return R.failed("不存在该访客");
        }
        ProjectVisitorVo projectVisitorVo = request.getData();
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
            byte[] bytes = QrCodeUtil.generatePng(code, IMG_SiZE, IMG_SiZE);
            String base64Img = Base64.encodeBase64String(bytes);
            qrCodeVo.setImgBase64("data:image/png;base64," + base64Img);
        } catch (Exception e) {
            return R.failed("二维生成失败");
        }
        return R.ok(qrCodeVo);
    }

    /**
     * 访客获取通行权二维码字符串
     *
     * @return
     */

    @GetMapping("/qr-code-src/visitor/{visitId}")
    @ApiOperation(value = "访客获取通行权二维码字符串(访客邀约)", notes = "访客获取通行权二维码字符串")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "visitId", value = "访客ID", required = true, paramType = "path")
    })
    public R<AppQRCodeVo> openByQRCStr(@PathVariable("visitId") String visitId) {
        AppQRCodeVo qrCodeVo = new AppQRCodeVo();

        R<ProjectVisitorVo> request = remoteVisitorService.getData(visitId);
        if (request.getCode() == 1 || ObjectUtil.isEmpty(request.getData())) {
            return R.failed("不存在该访客");
        }
        ProjectVisitorVo projectVisitorVo = request.getData();
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
        qrCodeVo.setQRCodeStr(code);
        return R.ok(qrCodeVo);
    }

    /**
     * 获取监控设备列表
     *
     * @return
     */
    @GetMapping("/monitor/page")
    @ApiOperation(value = "获取监控设备列表（实时监控）", notes = "获取监控设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getVideoDeviceByStaff(
            @RequestParam(value = "size",required = false)Long size,
            @RequestParam(value = "current",required = false) Long current) {

        ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo = new ProjectDeviceInfoPageFormVo();
        projectDeviceInfoPageFormVo.setDeviceTypeId(DeviceTypeConstants.MONITOR_DEVICE);
        AppPage page = new AppPage(current, size);
        return R.ok(projectDeviceInfoService.pageVo(page, projectDeviceInfoPageFormVo));
    }

    /**
     * 业主分页获取可通行门禁设备列表
     *
     * @return
     */
    @GetMapping("/door/page")
    @ApiOperation(value = "业主获取可通行门禁设备列表（一键开门）", notes = "业主获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    public R<Page<ProjectPassDeviceVo>> pageDoorDeviceByPerson(
            @RequestParam(value = "size",required = false)Long size,
            @RequestParam(value = "current",required = false) Long current) {

//        R<ProjectPersonInfo> r = remotePersonInfoService.getOwner();
//        //判断业主访客是否存在并且申请已经通过
//        if (r.getCode()==1||ObjectUtil.isEmpty(r.getData())) {
//            return R.failed("您在该小区下还未登记");
//        }
//        ProjectPersonInfo projectPersonInfo = r.getData();

        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        AppPage page = new AppPage(current, size);
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectPersonInfo.getPersonId());
        if (ObjectUtil.isNotEmpty(personPlanRel) &&
                DataConstants.ROOT.equals(personPlanRel.getIsActive()) &&
                (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) &&
                        LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {


            return R.ok(projectPersonDeviceService.pageDeviceByPersonId(page, projectPersonInfo.getPersonId()));
        }
        return R.ok(page);
    }

    /**
     * 业主获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/door/list")
    @ApiOperation(value = "业主获取所有可通行门禁设备列表(一键开门)", notes = "业主获取所有可通行门禁设备列表")
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
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) && (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.wechatGetlistDevice(projectPersonInfo.getPersonId(), personPlanRel.getPlanId()));
        }
        return R.ok(new ArrayList<ProjectPassDeviceVo>());
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
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) && (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            List<String> deviceName = new ArrayList<>();
            projectPersonDeviceService.wechatGetlistDevice(projectPersonInfo.getPersonId(), personPlanRel.getPlanId())
            .stream().forEach(e-> {
                deviceName.add(e.getDeviceName());
            });
            return R.ok(deviceName);
        }
        return R.ok(new ArrayList<String>());
    }
    /**
     * 一键开门
     *
     * @return
     */
    @PutMapping("/door/open/{id}")
    @ApiOperation(value = "一键开门（一键开门）", notes = "一键开门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path")
    })
    public R<String> openTheDoor(@PathVariable("id") String id) {
        return remoteDeviceInfo.open(id);
    }
    /**
     * 一键开门 生成通行记录
     *
     * @return
     */
    @PutMapping("/door/open/{id}/{personType}/{personId}")
    @ApiOperation(value = "一键开门（一键开门）", notes = "一键开门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员ID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path")
    })
    public R<String> openTheDoor(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {
        return remoteDeviceInfo.open(id, personType, personId);
    }

    /**
     * 业主获取二维码
     *
     * @return
     */
    @GetMapping("/qr-code/person")
    @ApiOperation(value = "业主获取通行权限二维码(二维码开门)", notes = "业主获取通行权限二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<QRCodeVo> openByQRCodeOnPerson() {
        QRCodeVo qrCodeVo = new QRCodeVo();

        R<ProjectPersonInfo> r = remotePersonInfoService.getOwner();
        //判断业主访客是否存在并且申请已经通过
        if (r.getCode()==1||ObjectUtil.isEmpty(r.getData())) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonInfo projectPersonInfo = r.getData();
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
    /**
     * 业主获取二维码
     *
     * @return
     */
    @GetMapping("/qr-code-str/person")
    @ApiOperation(value = "业主获取通行权限二维码字符串(二维码开门)", notes = "业主获取通行权限二维码字符串")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<AppQRCodeVo> openByQRCodeStrOnPerson() {
        AppQRCodeVo qrCodeVo = new AppQRCodeVo();

        R<ProjectPersonInfo> r = remotePersonInfoService.getOwner();
        //判断业主访客是否存在并且申请已经通过
        if (r.getCode()==1||ObjectUtil.isEmpty(r.getData())) {
            return R.failed("您在该小区下还未登记");
        }
        ProjectPersonInfo projectPersonInfo = r.getData();
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
        qrCodeVo.setQRCodeStr(code);
        return R.ok(qrCodeVo);
    }
    private String getPersonQrCode(String personName) {
        int now = (int) (System.currentTimeMillis() / 1000);
        String code = QrcodeUtil.getInstance().getQrcode(personName, "S" + ProjectContextHolder.getProjectId()+"-00000000", now, now + VALID_TIME, 1, 1);
        return code;
    }
    /**
     * 物业分页获取可通行门禁设备列表
     *
     * @return
     */

    @GetMapping("/staff/door/page")
    @ApiOperation(value = "物业分页获取可通行门禁设备列表(物业)", notes = "物业分页获取可通行门禁设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    public R<Page<ProjectPassDeviceVo>> pageDoorDeviceByStaff(@RequestParam(value = "size",required = false)Long size,
                                                              @RequestParam(value = "current",required = false) Long current) {
        ProjectStaffVo projectStaffVo = projectStaffService.getStaffByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectStaffVo)) {
            return R.failed("您在该小区下还未登记");
        }
        AppPage page = new AppPage(current, size);
        ProjectPersonPlanRel personPlanRel = projectPersonPlanRelService.getPoByPersonId(projectStaffVo.getStaffId());
        //添加通行配置判断通行方法必须启用且未过期
        if (ObjectUtil.isNotEmpty(personPlanRel) && DataConstants.ROOT.equals(personPlanRel.getIsActive()) && (LocalDateTime.now().isAfter(personPlanRel.getEffTime()) && LocalDateTime.now().isBefore(personPlanRel.getExpTime()))) {
            return R.ok(projectPersonDeviceService.pageDeviceByPersonId(page, projectStaffVo.getStaffId()));
        }
        return R.ok(page);
    }
    /**
     * 一键开门 生成通行记录
     *
     * @return
     */
    @PutMapping("/staff/door/open/{id}/{personType}/{personId}")
    @ApiOperation(value = "一键开门（物业）", notes = "一键开门")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员ID", required = true, paramType = "path"),
    })
    public R<String> staffOpenTheDoor(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {
        return remoteDeviceInfo.open(id, personType, personId);
    }

    /**
     * 物业获取监控设备列表
     *
     * @return
     */

    @GetMapping("/staff/monitor/page")
    @ApiOperation(value = "物业获取监控设备列表(物业)", notes = "物业获取监控设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query")
    })
    public R<Page<ProjectDeviceInfoPageVo>> getVideoDeviceByStaff(Page page) {

        ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo = new ProjectDeviceInfoPageFormVo();
        projectDeviceInfoPageFormVo.setDeviceTypeId(DeviceTypeConstants.MONITOR_DEVICE);
        return R.ok(projectDeviceInfoService.pageVo(page, projectDeviceInfoPageFormVo));
    }

    /**
     * 获取咚咚设备列表
     *
     * @return
     */

    @GetMapping("/dd/list")
    @ApiOperation(value = "获取咚咚设备列表", notes = "获取咚咚设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceInfo>> getDdDeviceList() {

        return remoteDdDeviceMapService.getDdDeviceList();
    }
}
