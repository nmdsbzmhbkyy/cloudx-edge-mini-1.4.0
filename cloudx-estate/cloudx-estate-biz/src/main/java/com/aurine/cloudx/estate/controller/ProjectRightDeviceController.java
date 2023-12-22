

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.constant.enums.CertmediaTypeEnum;
import com.aurine.cloudx.estate.constant.enums.PassRightCertDownloadStatusEnum;
import com.aurine.cloudx.estate.entity.ProjectRightDevice;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.PersonRightStatusSearchCondition;
import com.aurine.cloudx.estate.vo.ProjectCertStateVo;
import com.aurine.cloudx.estate.vo.ProjectRightDeviceOptsAccessSearchConditionVo;
import com.aurine.cloudx.estate.vo.RightStatusSearchCondition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 权限设备关系表，记录权限（认证介质）的下发状态
 *
 * @author 王良俊
 * @date 2020-05-21 09:52:28
 */
@RestController
@RequestMapping("/projectRightDevice")
@Api(value = "projectRightDevice", tags = "权限设备关系表，记录权限（认证介质）的下发状态管理")
public class ProjectRightDeviceController {
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;



    /**
     * 分页查询
     *
     * @param page               分页对象
     * @param projectRightDevice 权限设备关系表，记录权限（认证介质）的下发状态
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList(Page page, ProjectRightDevice projectRightDevice) {
        return R.ok(projectRightDeviceService.page(page, Wrappers.query(projectRightDevice)));
    }

    /**
     * 获取所有设备列表
     *
     * @param deviceId 设备id
     * @param query    查询条件
     * @param page     分页
     * @return
     */
    @ApiOperation(value = "获取所有设备列表", notes = "获取所有设备列表")
    @GetMapping("/pageByDeviceId")
    public R pageByDeviceId(Page page, ProjectRightDeviceOptsAccessSearchConditionVo query, String deviceId) {
        return R.ok(projectRightDeviceService.pageByDeviceId(page, query, deviceId));
    }

    /**
     * 获取所有人员授权情况数据
     *
     * @param query    查询条件
     * @param page     分页
     * @return
     */
    @ApiOperation(value = "获取所有人员授权介质下发情况数据", notes = "获取所有人员授权介质下发情况数据")
    @GetMapping("/pagePersonRightStatus")
    public R pageByDeviceId(Page page, PersonRightStatusSearchCondition query) {
        return R.ok(projectRightDeviceService.pagePersonRightStatus(page, query));
    }

    /**
     * 根据人员ID获取该人员介质下发情况
     *
     * @param query    查询条件
     * @param page     分页
     */
    @ApiOperation(value = "根据人员ID获取该人员介质下发情况", notes = "根据人员ID获取该人员介质下发情况")
    @GetMapping("/pageRightStatus")
    public R pageRightStatus(Page page, RightStatusSearchCondition query) {
        return R.ok(projectRightDeviceService.pageRightStatus(page, query));
    }

    /**
     * 统计下发异常的人员数量
     *
     */
    @ApiOperation(value = "统计下发异常的人员数量", notes = "统计下发异常的人员数量")
    @GetMapping("/countFailedPersonNum")
    public R countFailedPersonNum() {
        List<String> failedCode = new ArrayList<>();
        failedCode.add(PassRightCertDownloadStatusEnum.FAIL.code);
        failedCode.add(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
        return R.ok(projectRightDeviceService.countFailedPersonNUm());
    }


    /**
     * 根据人员ID获取这个人下发失败的介质数
     *
     * @param personId  人员ID
     */
    @ApiOperation(value = "根据人员ID获取这个人下发失败的介质数", notes = "根据人员ID获取这个人下发失败的介质数")
    @GetMapping("/countFailedNumByPersonId")
    public R countFailedNumByPersonId(String personId) {
        List<String> failedCodeList = new ArrayList<>();
        failedCodeList.add(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code);
        failedCodeList.add(PassRightCertDownloadStatusEnum.FAIL.code);
        return R.ok(projectRightDeviceService.count(new QueryWrapper<ProjectRightDevice>().lambda()
                .eq(ProjectRightDevice::getPersonId, personId)
                .in(ProjectRightDevice::getDlStatus, failedCodeList)));
    }

    /**
     * 根据人员ID重新下发失败凭证
     *
     * @param personId  人员ID
     */
    @ApiOperation(value = "根据人员ID重新下发失败凭证", notes = "根据人员ID重新下发失败凭证")
    @GetMapping("/resendFailedByPersonId")
    public R resendFailedByPersonId(String personId) {
        return R.ok(projectRightDeviceService.resendFailedByPersonId(personId));
    }

    /**
     * 重新下发
     *
     * @param rightDeviceIdList 设备权限ID列表
     * @return
     */
    @ApiOperation(value = "重新下发", notes = "重新下发")
    @PostMapping("/resendBatch")
    public R resendBatch(@RequestBody List<String> rightDeviceIdList) {
        return R.ok(projectRightDeviceService.resendBatch(rightDeviceIdList));
    }

    /**
     * 通过id查询权限设备关系表，记录权限（认证介质）的下发状态
     *
     * @param seq 自增序列id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(projectRightDeviceService.getById(seq));
    }

    /**
     * 新增权限设备关系表，记录权限（认证介质）的下发状态
     *
     * @param projectRightDevice 权限设备关系表，记录权限（认证介质）的下发状态
     * @return R
     */
    @ApiOperation(value = "新增权限设备关系表，记录权限（认证介质）的下发状态", notes = "新增权限设备关系表，记录权限（认证介质）的下发状态")
    @SysLog("新增权限设备关系表，记录权限（认证介质）的下发状态")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_projectrightdevice_add')")
    public R save(@RequestBody ProjectRightDevice projectRightDevice) {
        return R.ok(projectRightDeviceService.save(projectRightDevice));
    }

    /**
     * 修改权限设备关系表，记录权限（认证介质）的下发状态
     *
     * @param projectRightDevice 权限设备关系表，记录权限（认证介质）的下发状态
     * @return R
     */
    @ApiOperation(value = "修改权限设备关系表，记录权限（认证介质）的下发状态", notes = "修改权限设备关系表，记录权限（认证介质）的下发状态")
    @SysLog("修改权限设备关系表，记录权限（认证介质）的下发状态")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_projectrightdevice_edit')")
    public R updateById(@RequestBody ProjectRightDevice projectRightDevice) {
        return R.ok(projectRightDeviceService.updateById(projectRightDevice));
    }

    /**
     * 通过id删除权限设备关系表，记录权限（认证介质）的下发状态
     *
     * @param seq 自增序列id
     * @return R
     */
    @ApiOperation(value = "通过id删除权限设备关系表，记录权限（认证介质）的下发状态", notes = "通过id删除权限设备关系表，记录权限（认证介质）的下发状态")
    @SysLog("通过id删除权限设备关系表，记录权限（认证介质）的下发状态")
    @DeleteMapping("/{seq}")
    @PreAuthorize("@pms.hasPermission('estate_projectrightdevice_del')")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(projectRightDeviceService.removeById(seq));
    }

    /**
     * 根据介质类型重新下载该设备上这个类型的介质
     *
     * @param deviceId 要操作的设备ID
     * @param certType 介质类型
     * @return R
     */
    @ApiOperation(value = "根据介质类型重新下载该设备上这个类型的介质", notes = "根据介质类型重新下载该设备上这个类型的介质")
    @SysLog("根据介质类型重新下载该设备上这个类型的介质")
    @GetMapping("/reDownloadCertByType/{deviceId}/{certType}")
    public R reDownloadCertByType(@PathVariable String deviceId, @PathVariable String certType) {
        return R.ok(projectRightDeviceService.redownloadCertByType(deviceId, certType));
    }


    /**
     * 根据介质类型清空该设备上这个类型的介质
     *
     * @param deviceId 要操作的设备ID
     * @param certType 介质类型
     * @return R
     */
    @ApiOperation(value = "根据介质类型清空该设备上这个类型的介质", notes = "根据介质类型清空该设备上这个类型的介质")
    @SysLog("根据介质类型清空该设备上这个类型的介质")
    @GetMapping("/clearCertByType/{deviceId}/{certType}")
    public R clearCertByType(@PathVariable String deviceId, @PathVariable String certType) {
        boolean clearResult = false;
        switch (certType) {
            case "3":
                clearResult = DeviceFactoryProducer.getFactory(deviceId).getPassWayDeviceService().clearCerts(deviceId, CertmediaTypeEnum.Card);
                break;
            case "2":
                clearResult = DeviceFactoryProducer.getFactory(deviceId).getPassWayDeviceService().clearCerts(deviceId, CertmediaTypeEnum.Face);
                break;
            case "4":
                clearResult = DeviceFactoryProducer.getFactory(deviceId).getPassWayDeviceService().clearCerts(deviceId, CertmediaTypeEnum.Password);
                break;
            default:
        }

        return R.ok(clearResult);
    }





    /**
     * 按设备id查询资质下载状态
     * @param deviceId
     * @return
     */
    @GetMapping("/countBydlStatus/{deviceId}")
    R<Integer[]> countByDeviceId(@PathVariable("deviceId") String deviceId) {
        Integer[] ai = {0,0,0,0};
        List<ProjectCertStateVo> projectCertStateVoList = projectRightDeviceService.countDeviceId(deviceId);
        ai[0] = projectCertStateVoList.stream()
                .filter(e -> e.getState().equals(PassRightCertDownloadStatusEnum.SUCCESS.code))
                .map(ProjectCertStateVo::getNum)
                .collect(Collectors.toList()).stream().findFirst().orElse(0);
        ai[1] = projectCertStateVoList.stream()
                .filter(e -> e.getState().equals(PassRightCertDownloadStatusEnum.FAIL.code))
                .map(ProjectCertStateVo::getNum)
                .collect(Collectors.toList()).stream().findFirst().orElse(0);
        ai[2] = projectCertStateVoList.stream()
                .filter(e -> e.getState().equals(PassRightCertDownloadStatusEnum.DOWNLOADING.code))
                .map(ProjectCertStateVo::getNum)
                .collect(Collectors.toList()).stream().findFirst().orElse(0);
        ai[3] = projectCertStateVoList.stream()
                .filter(e -> e.getState().equals(PassRightCertDownloadStatusEnum.OUT_OF_MEMORY.code))
                .map(ProjectCertStateVo::getNum)
                .collect(Collectors.toList()).stream().findFirst().orElse(0);
        return R.ok(ai);
    }


    /**
     * 按设备id统计失败数据数量
     * @param deviceIdList 设备权限ID列表
     * @return
     */
    @PostMapping("/countFailNums")
    @ResponseBody
     R<Integer> countFailNums(@RequestBody List<String> deviceIdList) {
        return R.ok(projectRightDeviceService.countFailNumbs(deviceIdList));
    }

    /**
     * 根据deviceId重新下载失败凭证
     * @param deviceIdList 设备权限ID列表
     * @return
     */
    @PostMapping("/resentFailCert")
    public R resendFailCert(@RequestBody List<String> deviceIdList) {
        return R.ok(projectRightDeviceService.resendFailCert(deviceIdList));
    }

    /**
     * 根据deviceId重新下载失败凭证
     * @param deviceIdList 设备权限ID列表
     * @return
     */
    @PostMapping("/inner/resentFailCert")
    @Inner
    public R innerResentFailCert(@RequestBody List<String> deviceIdList) {
        return R.ok(projectRightDeviceService.resendFailCert(deviceIdList));
    }

    /**
     * 重新下发所有下发失败的介质
     */
    @GetMapping("/resendAllFailed")
    public R resendAllFailed() {
        return R.ok(projectRightDeviceService.resendAllFailed());
    }


}
