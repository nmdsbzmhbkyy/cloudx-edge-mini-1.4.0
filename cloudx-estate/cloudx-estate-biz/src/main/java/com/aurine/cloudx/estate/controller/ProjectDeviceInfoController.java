package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.DataConstants;
import com.aurine.cloudx.common.core.constant.DeviceTypeConstants;
import com.aurine.cloudx.common.core.constant.enums.DoorControllerEnum;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.core.util.TreeUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.DeviceCollectConstant;
import com.aurine.cloudx.estate.constant.DeviceInfoConstant;
import com.aurine.cloudx.estate.constant.enums.*;
import com.aurine.cloudx.estate.constant.enums.device.DeviceRegParamEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.interceptor.submit.IgnoreDuplicateSubmit;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.exception.entity.DeviceAbnormalHandleInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.param.entity.StreetLightDeviceStatus;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 设备信息表
 *
 * @author xull@aurine.cn
 * @date 2020-05-20 10:38:59
 */
@RestController
@RequestMapping("/projectDeviceInfo")
@Api(value = "projectDeviceInfo", tags = "设备信息表管理")
public class ProjectDeviceInfoController {
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectInfoService projectInfoService;
    @Resource
    private ProjectFrameInfoService projectFrameInfoService;
    @Resource
    private ProjectHousePersonRelService projectHousePersonRelService;
    @Resource
    private SysDeviceProductMapService sysDeviceProductMapService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;
    @Resource
    private ProjectDeviceParamHisService projectDeviceParamHisService;
    @Resource
    private ProjectEntityLevelCfgService projectEntityLevelCfgService;
    @Resource
    private ProjectDeviceCollectService projectDeviceCollectService;
    @Resource
    private ProjectPerimeterAlarmAreaService projectPerimeterAlarmAreaService;
    @Resource
    private ProjectDeviceAbnormalService projectDeviceAbnormalService;
    @Resource
    private ProjectDeviceGatherAlarmRuleService projectDeviceGatherAlarmRuleService;
    @Resource
    private ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    private OpenApiMessageService openApiMessageService;
    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;

    /**
     * 分页查询
     *
     * @param page                        分页对象
     * @param projectDeviceInfoPageFormVo 设备信息表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getProjectDeviceInfoPage(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        Page<ProjectDeviceInfoPageVo> projectDeviceInfoPageVoPage = projectDeviceInfoService.pageVo(page, projectDeviceInfoPageFormVo);
        return R.ok(projectDeviceInfoPageVoPage);
    }


    /**
     * 分页查询 分层控制器
     *
     * @param page                        分页对象
     * @param projectDeviceInfo 设备信息表
     * @return
     */
    /*@ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getLayerDevicePage(Page page, ProjectDeviceInfo projectDeviceInfo) {
        Page<ProjectDeviceInfoPageVo> projectDeviceInfoPageVoPage = projectDeviceInfoService.getLayerDevicePage(page, projectDeviceInfo);
        return R.ok(projectDeviceInfoPageVoPage);
    }*/

    /**
     * <p>根据设备ID获取所有与它存在三种参数冲突的设备</p>
     *
     * @param
     * @return
     * @author: 王良俊
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/getAbnormalList/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getProjectDeviceInfoPage(@PathVariable String deviceId) {
        List<ProjectDeviceInfoAbnormalVo> deviceInfoList = projectDeviceInfoService.listAbnormalDevice(deviceId);
        ProjectDeviceInfoAbnormalVo projectDeviceInfoAbnormalVo = deviceInfoList.get(0);
        String ipv4 = projectDeviceInfoAbnormalVo.getIpv4();
        String mac = projectDeviceInfoAbnormalVo.getMac();
        String deviceCode = projectDeviceInfoAbnormalVo.getDeviceCode();
        for (int i = 1; i < deviceInfoList.size(); i++) {
            ProjectDeviceInfoAbnormalVo item = deviceInfoList.get(i);
            StringBuilder abnormalDesc = new StringBuilder();
            if (StrUtil.isNotEmpty(item.getIpv4()) && item.getIpv4().equals(ipv4)) {
                if (abnormalDesc.length() != 0) {
                    abnormalDesc.append("，");
                }
                abnormalDesc.append("IPV4地址相同");
            }
            if (StrUtil.isNotEmpty(item.getMac()) && item.getMac().equals(mac)) {
                if (abnormalDesc.length() != 0) {
                    abnormalDesc.append("，");
                }
                abnormalDesc.append("MAC相同");
            }
            if (StrUtil.isNotEmpty(item.getDeviceCode()) && item.getDeviceCode().equals(deviceCode)) {
                if (abnormalDesc.length() != 0) {
                    abnormalDesc.append("，");
                }
                abnormalDesc.append("编号重复注册");
            }
            item.setAbnormalDesc(abnormalDesc.toString());
        }

        return R.ok(deviceInfoList);
    }


    /**
     * 根据产品ID获取到这个产品下所有的设备，根据设备ID排除该设备
     *
     * @param page                 分页对象
     * @param deviceInfoPageFormVo 设备查询对象
     * @return
     */
    @ApiOperation(value = "根据产品ID获取到这个产品下所有的设备", notes = "根据产品ID获取到这个产品下所有的设备")
    @GetMapping("/getDevicePageByProductId")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getDevicePageByProductId(Page page, ProjectDeviceInfoPageFormVo deviceInfoPageFormVo) {
        List<String> deviceIdList = new ArrayList<>();
        deviceIdList.add(deviceInfoPageFormVo.getDeviceId());
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new LambdaQueryWrapper<ProjectDeviceInfo>()
                .eq(ProjectDeviceInfo::getDeviceId, deviceInfoPageFormVo.getDeviceId())
                .select(ProjectDeviceInfo::getDeviceType));
        deviceInfoPageFormVo.setDeviceTypeId(deviceInfo.getDeviceType());
        deviceInfoPageFormVo.setStatus("1");
        deviceInfoPageFormVo.setExcludedDeviceIDList(deviceIdList);
        return R.ok(projectDeviceInfoService.pageVo(page, deviceInfoPageFormVo));
    }

    /**
     * 分页查询设备参数信息
     *
     * @param page                        分页对象
     * @param projectDeviceInfoPageFormVo 设备信息表
     * @return
     */
    @ApiOperation(value = "分页查询设备参数信息", notes = "分页查询设备参数信息")
    @GetMapping("/pageDeviceParam")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R pageDeviceParam(Page page, ProjectDeviceInfoPageFormVo projectDeviceInfoPageFormVo) {
        return R.ok(projectDeviceInfoService.pageDeviceParam(page, projectDeviceInfoPageFormVo));
    }

    /**
     * 通过id查询设备信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectDeviceInfoVo> getById(@PathVariable("id") String id) {
        return R.ok(projectDeviceInfoService.getProjectDeviceInfoById(id));
    }

    /**
     * <p>
     * 根据新设备SN和旧设备的ID进行设备替换
     * </p>
     *
     * @param deviceInfo 设备信息对象 -> 设备ID(所要替换的原设备ID)，设备SN(所要替换新设备的SN) 这两个是必填
     * @return R
     */
    @ApiOperation(value = "替换设备", notes = "替换设备")
    @SysLog("替换设备")
    @PostMapping("/replaceDevice")
    @PreAuthorize("@pms.hasPermission('estate_device_replace')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R replaceDevice(@RequestBody ProjectDeviceInfo deviceInfo) {

        if (StrUtil.isNotEmpty(deviceInfo.getSn().trim())) {
            int count = projectDeviceInfoService.countBySn(deviceInfo.getSn(), null);
            if (count != 0) {
                return R.failed("所要替换设备在系统中已存在");
            }
            String code = projectDeviceInfoService.replaceDevice(deviceInfo.getDeviceId(), deviceInfo.getSn()).code;
            return R.ok(code);
        } else {
            return R.failed("请输入sn");
        }
    }


    /**
     * 新增设备信息表
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "新增设备信息表", notes = "新增设备信息表")
    @SysLog("新增设备信息表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('estate_device_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R save(@RequestBody @Valid ProjectDeviceInfoVo projectDeviceInfo) {
        System.out.println(projectDeviceInfo);
        //TODO 新增时暂时设置状态为未激活状态
        projectDeviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
        //设置为手动接入
        projectDeviceInfo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);

        if (checkDevice(projectDeviceInfo)) {
            return R.failed("设备名称或SN已存在");
        }
        switch (projectDeviceInfo.getDeviceType()) {
            case DeviceTypeConstants.INDOOR_DEVICE:
                //TODO 室内机新增接口对接
                break;
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
                //TODO 梯口机新增接口对接
                break;
            case DeviceTypeConstants.GATE_DEVICE:
                //TODO 区口机新增接口对接
                break;
            case DeviceTypeConstants.CENTER_DEVICE:
                //TODO 中心机新增接口对接
                break;
            case DeviceTypeConstants.ENCODE_DEVICE:
                //TODO 编码设备新增接口对接
                break;
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备新增接口对接
                break;
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机新增接口对接
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表新增接口对接
                break;
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计新增接口对接
                break;
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感新增接口对接
                break;
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖新增接口对接
                break;
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯新增接口对接
                break;
            case DeviceTypeConstants.AI_BOX_DEVICE:
                //TODO AI盒子新增接口对接
                break;
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                //TODO 电梯分层控制器对接
                break;
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
                //TODO 电梯乘梯识别终端对接
                break;
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                //TODO 电梯状态检测器对接
                break;
         /*  // 20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE:
                //TODO 车道一体机接口对接
                break;*/
            case DeviceTypeConstants.ENVIRONMENTAL_MONITORING:
                //TODO 环境检测新增接口对接
                break;
            default:
                return R.failed("暂时不支持该类型的设备");
        }

        projectDeviceInfoService.saveDeviceVo(projectDeviceInfo);


        DeviceAbnormalHandleInfo handleInfo = new DeviceAbnormalHandleInfo();

        handleInfo.setThirdpartyCode(projectDeviceInfo.getThirdpartyCode());
        handleInfo.setDStatus(projectDeviceInfo.getStatus());
        handleInfo.setDeviceDesc(projectDeviceInfo.getDeviceName());
        handleInfo.setDeviceId(projectDeviceInfo.getDeviceId());
        handleInfo.setProjectId(ProjectContextHolder.getProjectId());
        handleInfo.setSn(projectDeviceInfo.getSn());
        handleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, projectDeviceInfo.getDeviceCode());
        handleInfo.addParam(DeviceRegParamEnum.IPV4, projectDeviceInfo.getIpv4());
        handleInfo.addParam(DeviceRegParamEnum.MAC, projectDeviceInfo.getMac());
        projectDeviceAbnormalService.checkAbnormal(handleInfo);

        /**
         * 报警主机添加设备调用同步接口
         */
//        if (projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.ALARM_HOST)) {
//            projectDeviceInfoService.reacquireDefenseArea(projectDeviceInfo);
//        }

        /**
         *
         * 区口机，梯口机 添加后，增量添加人员权限信息
         * 王伟 2020-06-19
         */
        try {
            projectDeviceInfoService.initDeviceParamData(projectDeviceInfo.getDeviceId());
            if (DeviceTypeConstants.SMART_STREET_LIGHT.equals(projectDeviceInfo.getDeviceType())) {
                // 路灯默认设置为关灯
                projectDeviceInfoService.streetLightControl(new StreetLightDeviceStatus("0", "50", projectDeviceInfo.getDeviceId()),
                        ProjectContextHolder.getProjectId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 这里如果获取参数有一次失败的则删除设备-获取设备参数不能放在事务会有问题
            projectDeviceInfoService.removeDevice(projectDeviceInfo.getDeviceId());
            return R.failed();
        }
        //保存设备参数
        return R.ok();
    }

    /**
     * 新增设备信息表-内部接口
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "新增设备信息表-内部接口", notes = "新增设备信息表-内部接口")
    @SysLog("新增设备信息表-内部接口")
    @PostMapping("/inner")
    @Inner
    //@PreAuthorize("@pms.hasPermission('estate_device_add')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerSave(@RequestBody @Valid ProjectDeviceInfoVo projectDeviceInfo) {
        System.out.println(projectDeviceInfo);
        //TODO 新增时暂时设置状态为未激活状态
        projectDeviceInfo.setStatus(DeviceInfoConstant.UNACTIVATED_STATUS);
        //设置为手动接入
        projectDeviceInfo.setAccessMethod(DeviceAccessMethodEnum.MANUAL.code);

        if (checkDevice(projectDeviceInfo)) {
            return R.failed("设备名称或SN已存在");
        }
        switch (projectDeviceInfo.getDeviceType()) {
            case DeviceTypeConstants.INDOOR_DEVICE:
                //TODO 室内机新增接口对接
                break;
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
                //TODO 梯口机新增接口对接
                break;
            case DeviceTypeConstants.GATE_DEVICE:
                //TODO 区口机新增接口对接
                break;
            case DeviceTypeConstants.CENTER_DEVICE:
                //TODO 中心机新增接口对接
                break;
            case DeviceTypeConstants.ENCODE_DEVICE:
                //TODO 编码设备新增接口对接
                break;
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备新增接口对接
                break;
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机新增接口对接
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表新增接口对接
                break;
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计新增接口对接
                break;
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感新增接口对接
                break;
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖新增接口对接
                break;
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯新增接口对接
                break;
            case DeviceTypeConstants.AI_BOX_DEVICE:
                //TODO AI盒子新增接口对接
                break;
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                //TODO 电梯分层控制器对接
                break;
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
                //TODO 电梯分层控制器对接
                break;
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                //TODO 电梯状态检测器对接
                break;
             /*  // 20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE:
                //TODO 车道一体机接口对接
                break;*/
            default:
                return R.failed("暂时不支持该类型的设备");
        }

        projectDeviceInfoService.saveDeviceVo(projectDeviceInfo);


        DeviceAbnormalHandleInfo handleInfo = new DeviceAbnormalHandleInfo();

        handleInfo.setThirdpartyCode(projectDeviceInfo.getThirdpartyCode());
        handleInfo.setDStatus(projectDeviceInfo.getStatus());
        handleInfo.setDeviceDesc(projectDeviceInfo.getDeviceName());
        handleInfo.setDeviceId(projectDeviceInfo.getDeviceId());
        handleInfo.setProjectId(ProjectContextHolder.getProjectId());
        handleInfo.setSn(projectDeviceInfo.getSn());
        handleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, projectDeviceInfo.getDeviceCode());
        handleInfo.addParam(DeviceRegParamEnum.IPV4, projectDeviceInfo.getIpv4());
        handleInfo.addParam(DeviceRegParamEnum.MAC, projectDeviceInfo.getMac());
        projectDeviceAbnormalService.checkAbnormal(handleInfo);

        /**
         * 报警主机添加设备调用同步接口
         */
//        if (projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.ALARM_HOST)) {
//            projectDeviceInfoService.reacquireDefenseArea(projectDeviceInfo);
//        }

        /**
         *
         * 区口机，梯口机 添加后，增量添加人员权限信息
         * 王伟 2020-06-19
         */
        try {
            projectDeviceInfoService.initDeviceParamData(projectDeviceInfo.getDeviceId());
            if (DeviceTypeConstants.SMART_STREET_LIGHT.equals(projectDeviceInfo.getDeviceType())) {
                // 路灯默认设置为关灯
                projectDeviceInfoService.streetLightControl(new StreetLightDeviceStatus("0", "50", projectDeviceInfo.getDeviceId()),
                        ProjectContextHolder.getProjectId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 这里如果获取参数有一次失败的则删除设备-获取设备参数不能放在事务会有问题
            projectDeviceInfoService.removeDevice(projectDeviceInfo.getDeviceId());
            return R.failed();
        }

        //保存设备参数
        return R.ok(projectDeviceInfo.getDeviceId());
    }


    /**
     * 修改设备信息表
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "修改设备信息表", notes = "修改设备信息表")
    @SysLog("修改设备信息表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('estate_device_edit')")
    public R updateById(@RequestBody @Valid ProjectDeviceInfoVo projectDeviceInfo) {
        if (checkDevice(projectDeviceInfo)) {
            return R.failed("设备名称或SN已存在");
        }
        switch (projectDeviceInfo.getDeviceType()) {

            case DeviceTypeConstants.INDOOR_DEVICE:
                //TODO 室内机更新接口对接
                break;
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
                //TODO 梯口机更新接口对接
                break;
            case DeviceTypeConstants.GATE_DEVICE:
                //TODO 区口机更新接口对接
                break;
            case DeviceTypeConstants.CENTER_DEVICE:
                //TODO 中心机更新接口对接
                break;
            case DeviceTypeConstants.ENCODE_DEVICE:
                //TODO 编码设备更新接口对接
                break;
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备更新接口对接
                break;
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机更新接口对接
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表更新接口对接
                break;
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计更新接口对接
                break;
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感更新接口对接
                break;
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖更新接口对接
                break;
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯更新接口对接
                break;
            case DeviceTypeConstants.AI_BOX_DEVICE:
                //TODO AI盒子新增接口对接
                break;
             /*  // 20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE:
                //TODO 车道一体机接口对接
                break;*/
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                //TODO 电梯分层控制器
                break;
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
                //TODO 乘梯识别终端
                break;
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                //TODO 电梯状态检测器
                break;
            default:
                return R.failed("暂时不支持该类型的设备");
        }
        projectDeviceInfoService.updateVo(projectDeviceInfo);

        DeviceAbnormalHandleInfo handleInfo = new DeviceAbnormalHandleInfo();

        handleInfo.setThirdpartyCode(projectDeviceInfo.getThirdpartyCode());
        handleInfo.setDStatus(projectDeviceInfo.getStatus());
        handleInfo.setDeviceDesc(projectDeviceInfo.getDeviceName());
        handleInfo.setDeviceId(projectDeviceInfo.getDeviceId());
        handleInfo.setProjectId(ProjectContextHolder.getProjectId());
        handleInfo.setSn(projectDeviceInfo.getSn());
        handleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, projectDeviceInfo.getDeviceCode());
        handleInfo.addParam(DeviceRegParamEnum.IPV4, projectDeviceInfo.getIpv4());
        handleInfo.addParam(DeviceRegParamEnum.MAC, projectDeviceInfo.getMac());
        projectDeviceAbnormalService.checkAbnormal(handleInfo);

        /**
         * AI盒子添加设备调用设置账号接口
         */
        if (projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.AI_BOX_DEVICE)) {
            projectDeviceInfoService.setAccount(projectDeviceInfo);
        }
        return R.ok();
    }

    /**
     * 修改设备信息表-内部接口
     *
     * @param projectDeviceInfo 设备信息表
     * @return R
     */
    @ApiOperation(value = "修改设备信息表-内部接口", notes = "修改设备信息表-内部接口")
    @SysLog("修改设备信息表-内部接口")
    @PutMapping("/inner")
    @Inner
    public R innerUpdateById(@RequestBody @Valid ProjectDeviceInfoVo projectDeviceInfo) {
        if (checkDevice(projectDeviceInfo)) {
            return R.failed("设备名称或SN已存在");
        }
        switch (projectDeviceInfo.getDeviceType()) {

            case DeviceTypeConstants.INDOOR_DEVICE:
                //TODO 室内机更新接口对接
                break;
            case DeviceTypeConstants.LADDER_WAY_DEVICE:
                //TODO 梯口机更新接口对接
                break;
            case DeviceTypeConstants.GATE_DEVICE:
                //TODO 区口机更新接口对接
                break;
            case DeviceTypeConstants.CENTER_DEVICE:
                //TODO 中心机更新接口对接
                break;
            case DeviceTypeConstants.ENCODE_DEVICE:
                //TODO 编码设备更新接口对接
                break;
            case DeviceTypeConstants.MONITOR_DEVICE:
                //TODO 监控设备更新接口对接
                break;
            case DeviceTypeConstants.ALARM_HOST:
                //TODO 报警主机更新接口对接
                break;
            case DeviceTypeConstants.SMART_WATER_METER:
                //TODO 智能水表更新接口对接
                break;
            case DeviceTypeConstants.LEVEL_GAUGE:
                //TODO 液位计更新接口对接
                break;
            case DeviceTypeConstants.SMOKE:
                //TODO 烟感更新接口对接
                break;
            case DeviceTypeConstants.SMART_MANHOLE_COVER:
                //TODO 智能井盖更新接口对接
                break;
            case DeviceTypeConstants.SMART_STREET_LIGHT:
                //TODO 智能路灯更新接口对接
                break;
            case DeviceTypeConstants.AI_BOX_DEVICE:
                //TODO AI盒子新增接口对接
                break;
              /*  // 20230704 V1.3.0.0（人行版）去除车场相关信息
            case DeviceTypeConstants.VEHICLE_BARRIER_DEVICE:
                //TODO 车道一体机接口对接
                break;*/
            case DeviceTypeConstants.ELEVATOR_LAYER_CONTROL_DEVICE:
                //TODO 电梯分层控制器
                break;
            case DeviceTypeConstants.ELEVATOR_RECOGNIZER_DEVICE:
                //TODO 乘梯识别终端
                break;
            case DeviceTypeConstants.ELEVATOR_STATE_DETECTOR_DEVICE:
                //TODO 电梯状态检测器
                break;
            default:
                return R.failed("暂时不支持该类型的设备");
        }
        projectDeviceInfoService.updateVo(projectDeviceInfo);

        DeviceAbnormalHandleInfo handleInfo = new DeviceAbnormalHandleInfo();

        handleInfo.setThirdpartyCode(projectDeviceInfo.getThirdpartyCode());
        handleInfo.setDStatus(projectDeviceInfo.getStatus());
        handleInfo.setDeviceDesc(projectDeviceInfo.getDeviceName());
        handleInfo.setDeviceId(projectDeviceInfo.getDeviceId());
        handleInfo.setProjectId(ProjectContextHolder.getProjectId());
        handleInfo.setSn(projectDeviceInfo.getSn());
        handleInfo.addParam(DeviceRegParamEnum.DEVICE_NO, projectDeviceInfo.getDeviceCode());
        handleInfo.addParam(DeviceRegParamEnum.IPV4, projectDeviceInfo.getIpv4());
        handleInfo.addParam(DeviceRegParamEnum.MAC, projectDeviceInfo.getMac());
        projectDeviceAbnormalService.checkAbnormal(handleInfo);

        /**
         * AI盒子添加设备调用设置账号接口
         */
        if (projectDeviceInfo.getDeviceType().equals(DeviceTypeConstants.AI_BOX_DEVICE)) {
            projectDeviceInfoService.setAccount(projectDeviceInfo);
        }
        return R.ok();
    }

    /**
     * 修改执行状态
     *
     * @param deviceId
     * @param name
     * @return
     */
    @ApiOperation(value = "修改执行状态", notes = "修改执行状态")
    @SysLog("修改执行状态")
    @PutMapping("/putByStatus/{deviceId}/{name}")
    public R updateByStatus(@PathVariable String deviceId, @PathVariable String name) {

        return R.ok(projectDeviceInfoService.updateByStatus(deviceId, name));

    }

    /**
     * <p>
     * 用于设置多台设备(这些设备的产品ID必须是同一个)
     * </p>
     *
     * @param deviceParamInfoVo deviceInfoList 设备信息对象集合（设备ID和设备第三方编码必须有数据）
     * @param deviceParamInfoVo paramJson      所要设置的参数json数据
     * @return 是否设置成功
     */
    @ApiOperation(value = "设置设备额外参数（多台设备）", notes = "设置设备额外参数（多台设备）")
    @SysLog("设置设备额外参数")
    @PutMapping("/setDevicesParam")
    public R setDevicesParam(@RequestBody DeviceParamInfoVo deviceParamInfoVo) {
        if (CollUtil.isNotEmpty(deviceParamInfoVo.getDeviceIdList())) {
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                    .eq(ProjectDeviceInfo::getDeviceId, deviceParamInfoVo.getDeviceIdList().get(0)));
            try {
                ObjectNode paramNode = (ObjectNode) new ObjectMapper().readTree(deviceParamInfoVo.getParamJson());
                DevicesResultVo devicesResultVo = projectDeviceInfoService.setDevicesParam(paramNode,
                        deviceParamInfoVo.getDeviceIdList(), deviceParamInfoVo.getServiceIdList());
                List<String> failedDeviceIdList = devicesResultVo.getFailedDeviceIdList();

                if (CollUtil.isNotEmpty(failedDeviceIdList)) {
                    List<ProjectDeviceInfo> failedDeviceList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda().in(ProjectDeviceInfo::getDeviceId, failedDeviceIdList));
                    projectDeviceParamHisService.addFailedParamHis(failedDeviceList, ProjectContextHolder.getProjectId());
                } else {
                    List<String> deviceIdList = deviceParamInfoVo.getDeviceIdList();
                    deviceIdList.removeAll(failedDeviceIdList);
                    if (CollUtil.isNotEmpty(deviceIdList)) {
                        // 这里判断是否是重配
                        if (deviceParamInfoVo.isReset()) {
                            projectDeviceParamHisService.updateSuccessParamHis(deviceIdList, ProjectContextHolder.getProjectId());
                        } else {
                            List<ProjectDeviceInfo> successDeviceInfoList = projectDeviceInfoService
                                    .list(new QueryWrapper<ProjectDeviceInfo>().lambda()
                                            .in(ProjectDeviceInfo::getDeviceId, deviceIdList));
                            projectDeviceParamHisService.addSuccessParamHis(successDeviceInfoList, ProjectContextHolder.getProjectId());
                        }
                    }
                }
                return R.ok(devicesResultVo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException("数据格式错误");
            }
        }
        throw new RuntimeException("未选择任何设备");
    }

    /**
     * <p>
     * 设置单台车场设备
     * </p>
     *
     * @param deviceParamInfoVo deviceCode 设备的第三方编码<br>
     *                          deviceParamInfoVo deviceId   设备的设备ID<br>
     *                          deviceParamInfoVo paramJson  所要设置的参数json数据<br>
     * @return 是否设置成功
     */

    @ApiOperation(value = "设置设备额外参数（单台）", notes = "设置设备额外参数（单台）")
    @SysLog("设置设备额外参数")
    @PutMapping("/setDeviceParam")
    public R setDeviceParam(@RequestBody DeviceParamInfoVo deviceParamInfoVo) {
        List<String> changeServiceIdList = deviceParamInfoVo.getChangeServiceIdList();
        if (CollUtil.isEmpty(changeServiceIdList)) {
            // 这里由于并没有修改任何参数所以直接算作参数设置成功
            // 这里如果是重配还要更新记录
            if (deviceParamInfoVo.isReset()) {
                List<String> deviceIdList = new ArrayList<>();
                deviceIdList.add(deviceParamInfoVo.getDeviceId());
                projectDeviceParamHisService.updateSuccessParamHis(deviceIdList, ProjectContextHolder.getProjectId());
            }
            return R.ok("参数设置成功");
        }
        List<String> totalServiceIdList = deviceParamInfoVo.getServiceIdList();
        List<ProjectDeviceInfo> deviceList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>()
                .lambda().eq(ProjectDeviceInfo::getDeviceId, deviceParamInfoVo.getDeviceId()));
        if (CollUtil.isNotEmpty(deviceList)) {
            if (!"1".equals(deviceList.get(0).getStatus())) {
                return R.failed("设备当前不是在线状态无法进行参数设置");
            }
        }
        try {
            ObjectNode paramNode = (ObjectNode) new ObjectMapper().readTree(deviceParamInfoVo.getParamJson());
            totalServiceIdList.removeAll(changeServiceIdList);
            // 去除没有修改过的参数本次不进行设置
            paramNode.remove(totalServiceIdList);
            JsonNode deviceNoObj = paramNode.findPath("DeviceNoObj");
            if (!deviceNoObj.isMissingNode()) {
                int roomNoLen = Integer.parseInt(deviceNoObj.findPath("roomNoLen").asText());
                int stairNoLen = Integer.parseInt(deviceNoObj.findPath("stairNoLen").asText());
                if (roomNoLen + stairNoLen > 17) {
                    return R.failed("参数设置失败：梯口号长度 + 房号长度 不能大于17");
                }
            }
            List<ProjectDeviceParamSetResultVo> resultVoList =
                    projectDeviceInfoService.setDeviceParam(paramNode, deviceParamInfoVo.getDeviceId());
            boolean isFailed = projectDeviceInfoService.handleDeviceParamSetResult(resultVoList, deviceParamInfoVo.isReset());
            if (isFailed) {
                return R.failed("设备参数设置失败");
            }
            return R.ok("参数设置成功");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("数据格式错误");
        }
    }

//   // 20230704 V1.3.0.0（人行版）去除车场相关信息
//    /**
//     * <p>
//     * 用于设置单台车场设备(这些设备的产品ID必须是同一个)
//     * </p>
//     *
//     * @param paramInfoVo 参数数据vo对象
//     * @return
//     * @author: 王良俊
//     */
//    @ApiOperation(value = "设置车场设备参数（单台）", notes = "设置车场设备参数（单台）")
//    @SysLog("设置设备额外参数")
//    @PutMapping("/setParkingDeviceParam")
//    public R setParkingDeviceParam(@RequestBody ParkingParamInfoVo paramInfoVo) {
//        return projectDeviceInfoService.setParkingDeviceParam(paramInfoVo);
//    }

//    // 20230704 V1.3.0.0（人行版）去除车场相关信息
//    /**
//     * <p>
//     * 用于设置多台车场设备(这些设备的产品ID必须是同一个)
//     * </p>
//     *
//     * @param paramInfoVo 参数数据vo对象
//     * @return 是否设置成功
//     */
//    @ApiOperation(value = "设置车场设备参数（多台设备）", notes = "设置车场设备参数（多台设备）")
//    @SysLog("设置车场设备参数")
//    @PutMapping("/setParkingDevicesParam")
//    public R<DevicesResultVo> setParkingDevicesParam(@RequestBody ParkingParamInfoVo paramInfoVo) {
//        return projectDeviceInfoService.setParkingDevicesParam(paramInfoVo);
//    }

    /**
     * <p>
     * 根据设备ID判断设备是否存在
     * </p>
     *
     * @param deviceId 设备ID
     * @author: 王良俊
     */
    @ApiOperation(value = "根据设备ID判断设备是否存在", notes = "根据设备ID判断设备是否存在")
    @SysLog("根据设备ID判断设备是否存在")
    @GetMapping("/checkDeviceIsExist")
    public R checkDeviceIsExist(@RequestParam("deviceId") String deviceId) {
        int deviceNum = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda().eq(ProjectDeviceInfo::getDeviceId, deviceId));
        return R.ok(deviceNum != 0);
    }

    /**
     * <p>
     * 根据设备ID获取设备基本信息包括能力集
     * </p>
     *
     * @param deviceId 设备的设备ID
     * @return 是否设置成功
     */
    @ApiOperation(value = "根据设备ID获取设备基本信息包括能力集（单台）", notes = "根据设备ID获取设备基本信息包括能力集（单台）")
    @SysLog("根据设备ID获取设备基本信息包括能力集")
    @GetMapping("/getDeviceInfo")
    public R getDeviceInfo(@RequestParam("deviceId") String deviceId) {
        ProjectDeviceProductInfoVo deviceProductInfoVo = new ProjectDeviceProductInfoVo();
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));
        SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new QueryWrapper<SysDeviceProductMap>()
                .lambda().eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
        if (StrUtil.isNotEmpty(deviceInfo.getDeviceRegionId())) {
            ProjectDeviceRegion deviceRegion = projectDeviceRegionService.getOne(new QueryWrapper<ProjectDeviceRegion>().lambda()
                    .eq(ProjectDeviceRegion::getRegionId, deviceInfo.getDeviceRegionId()));
            if (deviceRegion != null) {
                deviceProductInfoVo.setRegionName(deviceRegion.getRegionName());
            }

        }
        // 这里先后顺序不能搞反否则设备的项目ID会被productMap的覆盖掉
        if (productMap != null) {
            BeanUtil.copyProperties(productMap, deviceProductInfoVo);
        }
        BeanUtil.copyProperties(deviceInfo, deviceProductInfoVo);

        return R.ok(deviceProductInfoVo);

    }


    private boolean checkDevice(ProjectDeviceInfo projectDeviceInfo) {
        Integer countDevice = 0;
        if (StringUtils.equalsAny(projectDeviceInfo.getDeviceType(), DeviceTypeConstants.INDOOR_DEVICE, DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE)) {
            if (StringUtils.isBlank(projectDeviceInfo.getDeviceType())) {
                return true;
            }
            DoorControllerEnum doorController = DoorControllerEnum.getByModel(projectDeviceInfo.getBrand());

            if (Objects.isNull(doorController) && StringUtils.isNotEmpty(projectDeviceInfo.getSn())) {//SN空值时不校验
                //校验sn
                countDevice = projectDeviceInfoService.countBySn(projectDeviceInfo.getSn(), projectDeviceInfo.getDeviceId());
                if (countDevice != null && countDevice > 0) {
                    return true;
                }
            }

        }
        //校验设备名称
        if (projectDeviceInfo.getDeviceId() != null && !"".equals(projectDeviceInfo.getDeviceId())) {
            countDevice = projectDeviceInfoService.count(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .notIn(ProjectDeviceInfo::getDeviceId, projectDeviceInfo.getDeviceId()).and(wrapper -> {
                        wrapper.eq(ProjectDeviceInfo::getDeviceName, StringUtil.trim(projectDeviceInfo.getDeviceName()));
                    })
            );
        } else {
            countDevice = projectDeviceInfoService.count(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
                    .and(wrapper -> {
                        wrapper.eq(ProjectDeviceInfo::getDeviceName, StringUtil.trim(projectDeviceInfo.getDeviceName()));
                    }));
        }
        if (countDevice != null && countDevice > 0) {
            return true;
        }
        //20230426 适配门控制器 将校验放置前端做弹窗提示
//        if (StrUtil.isNotEmpty(projectDeviceInfo.getThirdpartyCode())) {
//            //校验设备编码
//            if (projectDeviceInfo.getDeviceId() != null && !"".equals(projectDeviceInfo.getDeviceId())) {
//                countDevice = projectDeviceInfoService.count(Wrappers.lambdaQuery(ProjectDeviceInfo.class)
//                        .notIn(ProjectDeviceInfo::getDeviceId, projectDeviceInfo.getDeviceId()).and(wrapper -> {
//                            wrapper.eq(ProjectDeviceInfo::getThirdpartyCode, StringUtil.trim(projectDeviceInfo.getThirdpartyCode()));
//                        })
//                );
//            } else {
//                countDevice = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda()
//                        .eq(ProjectDeviceInfo::getThirdpartyCode, StringUtil.trim(projectDeviceInfo.getThirdpartyCode())));
//            }
//        }
        if (countDevice != null && countDevice > 0) {
            return true;
        }
        if (DeviceTypeConstants.ENVIRONMENTAL_MONITORING.equals(projectDeviceInfo.getDeviceType())) {
            //修改
            if (projectDeviceInfo.getDeviceId() != null && !"".equals(projectDeviceInfo.getDeviceId())) {
                countDevice = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda()
                        .ne(ProjectDeviceInfo::getDeviceId, projectDeviceInfo.getDeviceId())
                        .eq(ProjectDeviceInfo::getDeviceCode, StringUtil.trim(projectDeviceInfo.getDeviceCode())));
            } else {
                countDevice = projectDeviceInfoService.count(new QueryWrapper<ProjectDeviceInfo>().lambda()
                        .eq(ProjectDeviceInfo::getDeviceCode, StringUtil.trim(projectDeviceInfo.getDeviceCode())));
            }
            if (countDevice != null && countDevice > 0) {
                return true;
            }
        }
        return false;

    }

    /**
     * 通过id删除设备信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息表", notes = "通过id删除设备信息表")
    @SysLog("通过id删除设备信息表")
    @DeleteMapping("/{id}")
//    @PreAuthorize("@pms.hasPermission('estate_device_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable String id) {
        int count = edgeCloudRequestService.count(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectId, ProjectContextHolder.getProjectId())
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if (count > 0) {
            OpenApiEntity openApiEntity = new OpenApiEntity();
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
            openApiEntity.setCommandType(OpenApiCommandTypeEnum.DELETE_DEVICE.name);
            openApiEntity.setServiceName(OpenApiServiceNameEnum.DEVICE_INFO.name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", id);
            openApiEntity.setData(jsonObject);
            openApiMessageService.sendOpenApiMessage(openApiEntity);
        }
        if (projectDeviceInfoService.removeDevice(id)) {
            return R.ok("设备删除成功");
        }
        return R.failed("设备删除失败");
    }

    /**
     * 通过id删除设备信息表-内部接口
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息表-内部接口", notes = "通过id删除设备信息表-内部接口")
    @SysLog("通过id删除设备信息表-内部接口")
    @DeleteMapping("/inner/{id}")
    @Inner
//    @PreAuthorize("@pms.hasPermission('estate_device_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerRemoveById(@PathVariable String id) {
        int count = edgeCloudRequestService.count(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectId, ProjectContextHolder.getProjectId())
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if (count > 0) {
            OpenApiEntity openApiEntity = new OpenApiEntity();
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
            openApiEntity.setCommandType(OpenApiCommandTypeEnum.DELETE_DEVICE.name);
            openApiEntity.setServiceName(OpenApiServiceNameEnum.DEVICE_INFO.name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid", id);
            openApiEntity.setData(jsonObject);
            openApiMessageService.sendOpenApiMessage(openApiEntity);
        }
        if (projectDeviceInfoService.removeDevice(id)) {
            return R.ok("设备删除成功");
        }
        return R.failed("设备删除失败");
    }

    /**
     * 通过id批量删除设备信息表
     *
     * @param ids ids
     * @return R
     */
    @ApiOperation(value = "通过id删除设备信息表", notes = "通过id删除设备信息表")
    @SysLog("通过id删除设备信息表")
    @DeleteMapping("/removeAll")
    @PreAuthorize("@pms.hasPermission('estate_device_del')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@RequestBody List<String> ids) {

        if (ids != null && ids.size() > 0) {

            return R.ok(projectDeviceInfoService.removeAll(ids));
        } else {
            return R.failed("请选择设备");
        }
    }


    /**
     * 通过id查询设备信息表
     *
     * @param type 设备类型id
     * @return R
     */
    @ApiOperation(value = "根据类型查询设备数量统计", notes = "根据类型查询设备数量统计")
    @GetMapping("/count/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "设备类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<DeviceCountInfoVo> selectCount(@PathVariable(value = "type") String type, @RequestParam(value = "deviceRegionId", required = false) String deviceRegionId) {

        LambdaQueryWrapper<ProjectDeviceInfo> totalQueryWrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
                .eq(ProjectDeviceInfo::getDeviceType, type);
        LambdaQueryWrapper<ProjectDeviceInfo> onlineQueryWrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
                .eq(ProjectDeviceInfo::getDeviceType, type).in(ProjectDeviceInfo::getStatus, DeviceStatusEnum.ONLINE.code);
        LambdaQueryWrapper<ProjectDeviceInfo> outlineQueryWrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
                .eq(ProjectDeviceInfo::getDeviceType, type).in(ProjectDeviceInfo::getStatus, DeviceStatusEnum.OFFLINE.code);
        LambdaQueryWrapper<ProjectDeviceInfo> inactiveNumQueryWrapper = Wrappers.<ProjectDeviceInfo>lambdaQuery()
                .eq(ProjectDeviceInfo::getDeviceType, type).in(ProjectDeviceInfo::getStatus, DeviceStatusEnum.DEACTIVE.code);
        Integer abnormalCount = projectDeviceInfoService.getAbnormalCount(type, deviceRegionId);


        if (StringUtils.isNotBlank(deviceRegionId)) {
            totalQueryWrapper.eq(ProjectDeviceInfo::getDeviceRegionId, deviceRegionId);
            onlineQueryWrapper.eq(ProjectDeviceInfo::getDeviceRegionId, deviceRegionId);
            outlineQueryWrapper.eq(ProjectDeviceInfo::getDeviceRegionId, deviceRegionId);
            inactiveNumQueryWrapper.eq(ProjectDeviceInfo::getDeviceRegionId, deviceRegionId);
        }
        DeviceCountInfoVo deviceCountInfoVo = new DeviceCountInfoVo();
        Integer totalCount = projectDeviceInfoService.count(totalQueryWrapper);
        Integer onlineCount = projectDeviceInfoService.count(onlineQueryWrapper);
        Integer outlineCount = projectDeviceInfoService.count(outlineQueryWrapper);
        Integer inactiveCount = projectDeviceInfoService.count(inactiveNumQueryWrapper);
        deviceCountInfoVo.setOnlineCount(onlineCount);
        deviceCountInfoVo.setOutlineCount(outlineCount);
        deviceCountInfoVo.setTotalCount(totalCount);
        deviceCountInfoVo.setInactiveCount(inactiveCount);
        deviceCountInfoVo.setAbnormalCount(abnormalCount);
        return R.ok(deviceCountInfoVo);
    }

    /**
     * 根据产品id查询设备总数
     *
     * @param productId 产品id
     * @return R
     */
    @ApiOperation(value = "根据产品id查询设备总数", notes = "根据产品id查询设备总数")
    @GetMapping("/countByProductId/{productId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "产品id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R selectCount(@PathVariable(value = "productId") String productId) {
        return R.ok(projectDeviceInfoService.countByProductId(productId));
    }

    @ApiOperation("根据类型获取门禁设备列表")
    @PostMapping("/getDeviceByType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceInfoThirPartyVo>> getByType(@RequestBody ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        List<ProjectDeviceInfo> deviceList = projectDeviceInfoService.findByType(projectDeviceInfoFormVo);
        List<ProjectDeviceInfoThirPartyVo> deviceThirPartyList = new ArrayList<>();
        for (ProjectDeviceInfo device : deviceList) {
            ProjectDeviceInfoThirPartyVo deviceInfoThirPartyVo = new ProjectDeviceInfoThirPartyVo();
            BeanUtil.copyProperties(device, deviceInfoThirPartyVo);
//            SysThirdPartyInterfaceConfig thirdParty = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(device.getDeviceId());
//            if (thirdParty.getName().equals(THIRD_PARTY_NAME)) {
            deviceInfoThirPartyVo.setIsResetDevice(1);
//            }
            deviceThirPartyList.add(deviceInfoThirPartyVo);
        }
        return R.ok(deviceThirPartyList);
    }

    @ApiOperation("根据类型获取分页门禁设备列表")
    @PostMapping("/getDevicePageByType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getPageByType(Page page, @RequestBody ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        Page<ProjectDeviceInfoThirPartyVo> deviceList = projectDeviceInfoService.findPageByType(page, projectDeviceInfoFormVo);
        return R.ok(deviceList);
    }

    @ApiOperation("根据设备类型ID获取到物联网设备（带设备参数信息）")
    @PostMapping("/getIotDeviceByType")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectIotDeviceInfoVo>> getIotDeviceByType(@RequestBody ProjectDeviceInfoFormVo projectDeviceInfoFormVo) {
        return R.ok(projectDeviceInfoService.findIotByType(projectDeviceInfoFormVo));
    }


    @ApiOperation("开门")
    @SysLog("开门")
    @GetMapping("/open/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R open(@PathVariable("id") String id) {
        DeviceFactoryProducer.getFactory(id).getPassWayDeviceService().openDoor(id);
        return R.ok();
    }

    @ApiOperation("用户开门")
    @SysLog("用户开门")
    @GetMapping("/open-by-person/{id}/{personType}/{personId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员的UUID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R openByPerson(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {

        DeviceFactoryProducer.getFactory(id).getPassWayDeviceService().openDoor(id, personId, personType);
        return R.ok();
    }

    @ApiOperation("用户开门")
    @SysLog("用户开门")
    @GetMapping("/inner/open-by-person/{id}/{personType}/{personId}")
    @Inner
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personType", value = "用户类型,1=住户，2=员工，3=访客", required = true, paramType = "path"),
            @ApiImplicitParam(name = "personId", value = "人员的UUID", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R innerOpenByPerson(@PathVariable("id") String id, @PathVariable("personType") String personType, @PathVariable("personId") String personId) {

        DeviceFactoryProducer.getFactory(id).getPassWayDeviceService().openDoor(id, personId, personType);
        return R.ok();
    }

    @ApiOperation("重启")
    @SysLog("重启")
    @GetMapping("/restart/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R restart(@PathVariable("id") String id) {
        DeviceFactoryProducer.getFactory(id).getDeviceService().reboot(id);
        return R.ok();
    }

    @ApiOperation("恢复出厂")
    @SysLog("恢复出厂")
    @GetMapping("/reset/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R reset(@PathVariable("id") String id) {
        return R.ok(DeviceFactoryProducer.getFactory(id).getDeviceService().reset(id));
    }

    @ApiOperation("设置管理员密码")
    @SysLog("设置管理员密码")
    @PostMapping("/setAdminPwd")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R setAdminPwd(@RequestBody DeviceAdminPassword adminPassword) {
        if (StrUtil.isNotEmpty(adminPassword.getPassword())) {
            return R.ok(DeviceFactoryProducer.getFactory(adminPassword.getDeviceId()).getDeviceService().setPwd(adminPassword.getDeviceId(), adminPassword.getPassword()));
        }
        return R.failed("密码设置失败");
    }

    @ApiOperation("清空人脸")
    @SysLog("清空人脸")
    @GetMapping("/cleanFace/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R cleanFace(@PathVariable("id") String id) {
        //TODO:清空人脸
        return R.ok();
    }

    @ApiOperation("清空门卡")
    @SysLog("清空门卡")
    @GetMapping("/cleanCard/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R cleanCard(@PathVariable("id") String id) {
        //TODO:清空门卡
        return R.ok();
    }

    @ApiOperation("清空指纹")
    @SysLog("清空指纹")
    @GetMapping("/cleanFingerprint/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R cleanFingerprint(@PathVariable("id") String id) {
        //TODO:清空指纹
        return R.ok();
    }

    @ApiOperation("下载人脸")
    @SysLog("下载人脸")
    @GetMapping("/loadFace/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R loadFace(@PathVariable("id") String id) {
        //TODO:下载人脸
        return R.ok();
    }

    @ApiOperation("下载门卡")
    @SysLog("下载门卡")
    @GetMapping("/loadCard/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R loadCard(@PathVariable("id") String id) {
        //TODO:下载门卡
        return R.ok();
    }

    @ApiOperation("下载指纹")
    @SysLog("下载指纹")
    @GetMapping("/loadFingerprint/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R loadFingerprint(@PathVariable("id") String id) {
        //TODO:下载指纹
        return R.ok();
    }

    /**
     * 获取室内终端树形图
     *
     * @return
     */
    @ApiOperation("获取室内终端树形图")
    @GetMapping("/indoorTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSelectTreeVo>> findIndoorTree() {
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置设备树形图列表 xull@aurine.cn 2020/5/25 10:57
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVoList = new ArrayList<>();
        //如果项目查询为空直接返回空值
        if (projectInfo == null) {
            return R.ok(projectDeviceSelectTreeVoList);
        }
        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
        projectDeviceInfoFormVo.setTypes(ListUtil.toList("1"));
        //获取室内终端列表 xull@aurine.cn 2020/5/25 11:03
//        List<ProjectDeviceInfo> projectDeviceInfos = projectDeviceInfoService.findByType(projectDeviceInfoFormVo);


        //获取楼栋单元房间列表 xull@aurine.cn 2020/5/25 11:03
        List<ProjectFrameInfo> projectBuildingInfos = projectFrameInfoService.list(
                Wrappers.lambdaQuery(ProjectFrameInfo.class).le(ProjectFrameInfo::getLevel, 3).gt(ProjectFrameInfo::getLevel, 1));
        List<String> houseIds = projectHousePersonRelService.list().stream().filter(e -> e.getAuditStatus().equals(AuditStatusEnum.pass.code)).map(e -> {
            return e.getHouseId();
        }).collect(Collectors.toList());
        if (houseIds != null) {
            List<ProjectFrameInfo> projectHouseInfos = projectFrameInfoService.listByIds(houseIds);
            projectBuildingInfos.addAll(projectHouseInfos);
        }
        //设置项目信息 xull@aurine.cn 2020/5/25 10:42
        ProjectDeviceSelectTreeVo projectInfoVo = new ProjectDeviceSelectTreeVo();
        projectInfoVo.setName(projectInfo.getProjectName());
        projectInfoVo.setId(projectInfo.getProjectId().toString());
        //设置根节点 xull@aurine.cn 2020/5/25 11:47
        projectInfoVo.setParentId(DataConstants.ROOT);
        projectInfoVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);


        //添加项目节点
        projectDeviceSelectTreeVoList.add(projectInfoVo);
        //添加楼栋单元房间节点
        projectDeviceSelectTreeVoList.addAll(getBuildTree(projectBuildingInfos));
        //添加室内终端节点
        /*        projectDeviceSelectTreeVoList.addAll(getIndoorDeviceTree(projectDeviceInfos));*/
        //构造结构树
        return R.ok(TreeUtil.build(projectDeviceSelectTreeVoList, DataConstants.ROOT, e -> {
            if (DeviceInfoConstant.IS_DEVICE.equals(e.getType())) {
                return true;
            } else {
                return false;
            }
        }));
    }

    @ApiOperation("获取室内房屋树形图")
    @GetMapping("/findIndoorHouseTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSelectTreeVo>> findIndoorHouseTree() {
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置设备树形图列表 xull@aurine.cn 2020/5/25 10:57
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVoList = new ArrayList<>();
        //如果项目查询为空直接返回空值
        if (projectInfo == null) {
            return R.ok(projectDeviceSelectTreeVoList);
        }
        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
        projectDeviceInfoFormVo.setTypes(ListUtil.toList("1"));
        //获取室内终端列表 xull@aurine.cn 2020/5/25 11:03
//        List<ProjectDeviceInfo> projectDeviceInfos = projectDeviceInfoService.findByType(projectDeviceInfoFormVo);


        //获取楼栋单元房间列表 xull@aurine.cn 2020/5/25 11:03
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVos = projectFrameInfoService.getAllFrameInfosOnPerson();
        //设置项目信息 xull@aurine.cn 2020/5/25 10:42
        ProjectDeviceSelectTreeVo projectInfoVo = new ProjectDeviceSelectTreeVo();
        projectInfoVo.setName(projectInfo.getProjectName());
        projectInfoVo.setId(projectInfo.getProjectId().toString());
        //设置根节点 xull@aurine.cn 2020/5/25 11:47
        projectInfoVo.setParentId(DataConstants.ROOT);
        projectInfoVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);


        //添加项目节点
        projectDeviceSelectTreeVoList.add(projectInfoVo);
        //添加楼栋单元房间节点
        projectDeviceSelectTreeVoList.addAll(projectDeviceSelectTreeVos);
        //添加室内终端节点
        /*        projectDeviceSelectTreeVoList.addAll(getIndoorDeviceTree(projectDeviceInfos));*/
        //构造结构树
        return R.ok(TreeUtil.build(projectDeviceSelectTreeVoList, DataConstants.ROOT, e -> {
            if (DeviceInfoConstant.IS_DEVICE.equals(e.getType())) {
                return true;
            } else {
                return false;
            }
        }));
    }

    @ApiOperation("获取选中的房屋数量")
    @GetMapping("/getCountByIds")
    public R<Integer> getCountIndoorByIds(@RequestParam("ids") List<String> ids) {

        if (ids == null || ids.size() == 0) {
            return R.ok(0);
        }
        return R.ok(projectFrameInfoService.getCountIndoorByIds(ids));
    }

    @ApiOperation("获取室内终端树形子节点")
    @GetMapping("/indoorTreeByPid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSelectTreeVo>> findIndoorTreeByPid(@RequestParam(value = "pid") String pid) {
        //设置设备树形图列表 xull@aurine.cn 2020/5/25 10:57
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVoList = new ArrayList<>();
        if (StringUtils.equals(DataConstants.ROOT, pid)) {

            ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
            //设置项目信息 xull@aurine.cn 2020/5/25 10:42
            ProjectDeviceSelectTreeVo projectInfoVo = new ProjectDeviceSelectTreeVo();
            projectInfoVo.setName(projectInfo.getProjectName());
            projectInfoVo.setId(projectInfo.getProjectId().toString());
            //设置根节点 xull@aurine.cn 2020/5/25 11:47
            projectInfoVo.setParentId(DataConstants.ROOT);
            projectInfoVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);
            //添加项目节点
            projectDeviceSelectTreeVoList.add(projectInfoVo);
            return R.ok(projectDeviceSelectTreeVoList);
        } else {
            return R.ok(projectFrameInfoService.getFrameInfosOnPerson(pid));

        }
    }


    /**
     * 将楼栋单元房间转为结构树列表
     *
     * @param projectBuildingInfos
     * @return
     */
    private List<ProjectDeviceSelectTreeVo> getBuildTree(List<ProjectFrameInfo> projectBuildingInfos) {
        List<ProjectDeviceSelectTreeVo> treeList = projectBuildingInfos.stream()
                .filter(frameInfo -> !frameInfo.getEntityId().equals(frameInfo.getPuid()))
                .map(frameInfo -> {
                    ProjectDeviceSelectTreeVo node = new ProjectDeviceSelectTreeVo();
                    node.setId(frameInfo.getEntityId());
                    //如果是是楼栋则设置父级为项目id xull@aurine.cn 2020/5/25 11:36
                    if (DataConstants.TRUE.equals(frameInfo.getIsBuilding())) {
                        node.setParentId(ProjectContextHolder.getProjectId().toString());
                    } else {
                        node.setParentId(frameInfo.getPuid());
                    }
                    node.setName(frameInfo.getEntityName());
                    if ("1".equals(frameInfo.getIsHouse())) {
                        node.setType(DeviceInfoConstant.IS_DEVICE);
                    } else {
                        node.setType(DeviceInfoConstant.IS_NOT_DEVICE);
                    }
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }

    /**
     * 将室内设备列表转化为结构树列表
     *
     * @param projectDeviceInfos
     * @return
     */
    private List<ProjectDeviceSelectTreeVo> getIndoorDeviceTree(List<ProjectDeviceInfo> projectDeviceInfos) {
        List<ProjectDeviceSelectTreeVo> treeList = projectDeviceInfos.stream()
                .map(deviceInfo -> {
                    ProjectDeviceSelectTreeVo node = new ProjectDeviceSelectTreeVo();
                    node.setId(deviceInfo.getDeviceId());
                    //设置父级节点为房间id xull@aurine.cn 2020/5/25 11:51
                    node.setParentId(deviceInfo.getHouseId());
                    node.setName(deviceInfo.getDeviceName());
                    node.setType(DeviceInfoConstant.IS_DEVICE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }

    /**
     * 将梯口,区口设备列表转化为结构树列表
     *
     * @param projectDeviceInfos
     * @return
     */
    private List<ProjectDeviceSelectTreeVo> getDoorDeviceTree(List<ProjectDeviceInfo> projectDeviceInfos) {
        List<ProjectDeviceSelectTreeVo> treeList = projectDeviceInfos.stream()
                .map(deviceInfo -> {
                    ProjectDeviceSelectTreeVo node = new ProjectDeviceSelectTreeVo();
                    node.setId(deviceInfo.getDeviceId());
                    if (DeviceTypeConstants.GATE_DEVICE.equals(deviceInfo.getDeviceType())) {
                        //如果是区口设备设置父级节点为 区口 xull@aurine.cn 2020/5/25 13:23
                        node.setParentId(DeviceInfoConstant.TREE_GATE_ID);
                    } else {
                        node.setParentId(deviceInfo.getUnitId());
                    }
                    node.setName(deviceInfo.getDeviceName());
                    node.setType(DeviceInfoConstant.IS_DEVICE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }


    /**
     * 获取梯口区口终端树形图
     *
     * @return
     */
    @ApiOperation("获取梯口区口终端树形图")
    @GetMapping("/doorTree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceSelectTreeVo>> findDoorTree(@RequestParam(value = "isRich", required = false) Boolean isRich) {
        ProjectInfo projectInfo = projectInfoService.getById(ProjectContextHolder.getProjectId());
        //设置设备树形图列表 xull@aurine.cn 2020/5/25 10:57
        List<ProjectDeviceSelectTreeVo> projectDeviceSelectTreeVoList = new ArrayList<>();
        //如果项目查询为空直接返回空值
        if (projectInfo == null) {
            return R.ok(projectDeviceSelectTreeVoList);
        }

        ProjectDeviceInfoFormVo projectDeviceInfoFormVo = new ProjectDeviceInfoFormVo();
        projectDeviceInfoFormVo.setTypes(ListUtil.toList(DeviceTypeConstants.LADDER_WAY_DEVICE, DeviceTypeConstants.GATE_DEVICE));
        //获取梯口区口终端列表 xull@aurine.cn 2020/5/25 11:03
        List<ProjectDeviceInfo> projectDeviceInfos = null;

        if (isRich != null && isRich) {
            //查询支持富文本的门禁设备
            projectDeviceInfos = projectDeviceInfoService.findRichByType(projectDeviceInfoFormVo);
        } else {
            //
            projectDeviceInfos = projectDeviceInfoService.findByType(projectDeviceInfoFormVo);
        }


        //获取楼栋单元列表 xull@aurine.cn 2020/5/25 11:03
        List<ProjectFrameInfo> projectBuildingInfos = projectFrameInfoService.list(
                Wrappers.lambdaQuery(ProjectFrameInfo.class)
                        .le(ProjectFrameInfo::getLevel, 3)
                        .gt(ProjectFrameInfo::getLevel, 1));


        //设置项目信息 xull@aurine.cn 2020/5/25 10:42
        ProjectDeviceSelectTreeVo projectInfoVo = new ProjectDeviceSelectTreeVo();
        projectInfoVo.setName(projectInfo.getProjectName());
        projectInfoVo.setId(projectInfo.getProjectId().toString());
        projectInfoVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);
        //设置根节点 xull@aurine.cn 2020/5/25 11:47
        projectInfoVo.setParentId(DataConstants.ROOT);


        //设置区口终端分类信息 xull@aurine.cn 2020/5/25 10:42
        ProjectDeviceSelectTreeVo projectInfoGateVo = new ProjectDeviceSelectTreeVo();
        projectInfoGateVo.setName(projectInfo.getProjectName());
        projectInfoGateVo.setId(DeviceInfoConstant.TREE_GATE_ID);
        projectInfoGateVo.setName(DeviceInfoConstant.TREE_GATE_NAME);
        projectInfoGateVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);
        projectInfoGateVo.setParentId(projectInfo.getProjectId().toString());

        //设置梯口终端分类信息 xull@aurine.cn 2020/5/25 10:42
        ProjectDeviceSelectTreeVo projectInfoBuildVo = new ProjectDeviceSelectTreeVo();
        projectInfoBuildVo.setName("");
        projectInfoBuildVo.setId(DeviceInfoConstant.TREE_BUILD_ID);
        projectInfoBuildVo.setName(DeviceInfoConstant.TREE_BUILD_NAME);
        projectInfoBuildVo.setType(DeviceInfoConstant.IS_NOT_DEVICE);
        projectInfoBuildVo.setParentId(projectInfo.getProjectId().toString());


        //添加项目节点
        projectDeviceSelectTreeVoList.add(projectInfoVo);
        //添加区口模块分类
        projectDeviceSelectTreeVoList.add(projectInfoGateVo);
        //添加梯口模块分类
        projectDeviceSelectTreeVoList.add(projectInfoBuildVo);
        //添加楼栋单元房间节点
        projectDeviceSelectTreeVoList.addAll(getTypeBuildTree(projectBuildingInfos));
        //添加室内终端节点
        projectDeviceSelectTreeVoList.addAll(getDoorDeviceTree(projectDeviceInfos));
        //构造结构树
        return R.ok(TreeUtil.build(projectDeviceSelectTreeVoList, DataConstants.ROOT, e -> {
                    if (DeviceInfoConstant.IS_DEVICE.equals(e.getType())) {
                        return true;
                    } else {
                        return false;
                    }
                }
        ));
    }

    /**
     * 获取梯口设备楼栋单元结构树
     *
     * @param projectBuildingInfos
     * @return
     */
    private List<ProjectDeviceSelectTreeVo> getTypeBuildTree(List<ProjectFrameInfo> projectBuildingInfos) {
        List<ProjectDeviceSelectTreeVo> treeList = projectBuildingInfos.stream()
                .filter(frameInfo -> !frameInfo.getEntityId().equals(frameInfo.getPuid()))
                .map(frameInfo -> {
                    ProjectDeviceSelectTreeVo node = new ProjectDeviceSelectTreeVo();
                    node.setId(frameInfo.getEntityId());
                    //如果是是楼栋则设置父级为梯口终端类型id xull@aurine.cn 2020/5/25 11:36
                    if (DataConstants.TRUE.equals(frameInfo.getIsBuilding())) {
                        node.setParentId(DeviceInfoConstant.TREE_BUILD_ID);
                    } else {
                        node.setParentId(frameInfo.getPuid());
                    }
                    node.setName(frameInfo.getEntityName());
                    node.setType(DeviceInfoConstant.IS_NOT_DEVICE);
                    return node;
                }).collect(Collectors.toList());
        return treeList;
    }

    @ApiModelProperty(value = "导入设备数据", notes = "导入设备数据")
    @PostMapping("/excel/verifyCode/{type}")
    @SysLog("导入设备数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "设备类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R verifyCodeWithExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file) {
        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(type, "0");

        if (deviceExcelEnum == null) {
            return R.failed("不存在该类型的文件");
        }
        try {
            return R.ok(projectDeviceInfoService.verifyCodeWithExcel(file, deviceExcelEnum));
        } catch (IOException e) {
            e.printStackTrace();
            return R.failed("未获取到导入文件");
        }
    }

    @ApiModelProperty(value = "导入设备数据", notes = "导入设备数据")
    @PostMapping("/importExcel/{type}")
    @SysLog("导入设备数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "设备类型", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R importExcel(@PathVariable("type") String type, @RequestParam("file") MultipartFile file, @RequestParam(value = "isCover", required = false) boolean isCover) {
        //获取公安对接接口判断是否启用
        List<ProjectDeviceCollectListVo> ProjectDeviceCollectListVos = projectDeviceCollectService
                .getDeviceCollectListVo(ProjectContextHolder.getProjectId(), DeviceCollectTypeEnum.POLICE.code, DeviceCollectConstant.POLICE_PARAM_NAME);
        String policeStatus = "0";
        if (ProjectDeviceCollectListVos != null && ProjectDeviceCollectListVos.size() > 0) {
            policeStatus = ProjectDeviceCollectListVos.get(0).getAttrValue();
            if (StringUtils.isBlank(policeStatus)) {
                policeStatus = "0";
            }
        }
        DeviceExcelEnum deviceExcelEnum = DeviceExcelEnum.getEnum(type, policeStatus);

        if (deviceExcelEnum == null) {
            return R.failed("不存在该类型的文件");
        }
        try {
            projectDeviceInfoService.importExcel(file, deviceExcelEnum, ProjectContextHolder.getProjectId(), isCover);
        } catch (IOException e) {
            e.printStackTrace();
            return R.failed("未获取到导入文件");
        }
        return R.ok();
    }

    @GetMapping("/errorExcel/{projectId}/{batchId}")
    @ApiModelProperty(value = "获取导入失败列表", notes = "获取导入失败列表")
    @Inner(false)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "异常设备excel表名", required = true, paramType = "path"),
            @ApiImplicitParam(name = "batchId", value = "导入批次ID（设备导入日志表ID）", required = true, paramType = "path"),
    })
    public void errorExcel(@PathVariable("projectId") Integer projectId, @PathVariable("batchId") String batchId, HttpServletResponse httpServletResponse) throws IOException {
        projectDeviceInfoService.errorExcel(projectId, batchId, httpServletResponse);
    }

    @GetMapping("/modelExcel/{projectId}/{code}")
    @ApiModelProperty(value = "获取导入模板", notes = "获取导入模板")
    @Inner(false)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "项目id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "code", value = "设备类型", required = true, paramType = "path"),
    })
    public void modelExcel(@PathVariable("code") String code, @PathVariable("projectId") Integer projectId, HttpServletResponse httpServletResponse) throws IOException {
        projectDeviceInfoService.modelExcel(code, httpServletResponse, projectId);
    }

    @ApiOperation(value = "获取到这个巡点拥有的设备列表", notes = "获取到这个巡点拥有的设备列表")
    @GetMapping("/listDeviceByPointId/{pointId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listDeviceByPointId(@PathVariable("pointId") String pointId) {
        return R.ok(projectDeviceInfoService.listDeviceByPointId(pointId));
    }

    /**
     * 获取视频直播流的地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取视频直播流的地址", notes = "获取视频直播流的地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getLiveUrl/{deviceId}")
    public R<String> getLiveUrl(@PathVariable("deviceId") String deviceId) {
        return projectDeviceInfoService.getLiveUrl(deviceId);
    }

    /**
     * rtsp转码流
     *
     * @param rtsp
     * @return
     */
    @GetMapping("/rtspToUrl")
    public R<JSONObject> rtspToUrl(@RequestParam("rtsp") String rtsp) {
        return projectDeviceInfoService.rtspToUrl(rtsp);
    }

    /**
     * 获取视频录播流的地址
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取视频录播流的地址", notes = "获取视频录播流的地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceId", value = "设备id", paramType = "path", required = true),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "path", required = true),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "path", required = true),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @GetMapping("/getVideoUrl/{deviceId}/{startTime}/{endTime}")
    public R<String> getVideoUrl(@PathVariable("deviceId") String deviceId, @PathVariable("startTime") Long startTime, @PathVariable("endTime") Long endTime) {
        return R.ok(projectDeviceInfoService.getVideoUrl(deviceId, startTime, endTime));
    }

    @ApiOperation(value = "通过设备类型和项目id获取到设备信息", notes = "通过设备类型和项目id获取到设备信息")
    @GetMapping("/listDeviceByType/{projectId}/{type}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listDeviceByType(@PathVariable("projectId") Integer projectId, @PathVariable("type") String type) {
        return R.ok(projectDeviceInfoService.listDeviceByType(projectId, type));
    }

    /**
     * 判断设备名称在当前项目中是否唯一
     *
     * @param deviceName
     * @return
     */
    @ApiOperation(value = "判断设备名称在当前项目中是否唯一", notes = "判断设备名称在当前项目中是否唯一")
    @PostMapping("/uniqueDeviceNameByProject")
    @IgnoreDuplicateSubmit
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "deviceName", value = "设备名称", required = true, paramType = "path"),
    })
    public R uniqueDeviceNameByProject(String deviceName, String deviceId) {
        return R.ok(projectDeviceInfoService.uniqueDeviceNameByProject(deviceName, deviceId));
    }

    /**
     * 判断设备sn在当前项目中是否唯一
     *
     * @param sn
     * @return
     */
    @ApiOperation(value = "判断设备sn在当前项目中是否唯一", notes = "判断设备sn在当前项目中是否唯一")
    @PostMapping("/uniqueSnByProject")
    @IgnoreDuplicateSubmit
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "sn", value = "设备sn", required = true, paramType = "path"),
    })
    public R uniqueDeviceCodeByProject(String sn, String deviceId) {
        return R.ok(projectDeviceInfoService.countBySn(sn, deviceId) <= 1);
    }

    /**
     * 获取咚咚设备列表
     *
     * @return
     */
    @ApiOperation(value = "获取咚咚设备列表", notes = "获取咚咚设备列表")
    @GetMapping("/dd/list")
    @IgnoreDuplicateSubmit
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceInfo>> getDdDeviceList() {
        return R.ok(projectDeviceInfoService.getDdDeviceList());
    }

    @ApiOperation(value = "设备关联", notes = "设备关联")
    @SysLog("设备关联")
    @PutMapping("/putDeviceRelevance")
    public R putDeviceRelevance(@RequestBody ProjectDeviceMonitorRelVo projectDeviceMonitorRelVo) {
        return R.ok(projectDeviceInfoService.putDeviceRelevance(projectDeviceMonitorRelVo));
    }

    @ApiOperation(value = "查询关联的设备", notes = "查询关联的设备")
    @SysLog("查询关联的设备")
    @GetMapping("/getMonitoring")
    public R getMonitoring(@RequestParam("deviceId") String deviceId) {
        return R.ok(projectDeviceInfoService.getMonitoring(deviceId));
    }

    @ApiOperation(value = "查询设备品牌型号", notes = "查询设备品牌型号")
    @SysLog("查询设备品牌型号")
    @GetMapping("/getDeviceBrand")
    public R getDeviceBrand(String deviceTypeId) {
        return R.ok(projectDeviceInfoService.getDeviceBrand(deviceTypeId));
    }


    @ApiOperation(value = "智能路灯添加控制器IMEL", notes = "智能路灯添加控制器IMEL")
    @SysLog("智能路灯添加控制器IMEL")
    @PostMapping("/saveLightingIMEL")
    public R saveLightingIMEL(@RequestBody ProjectDeviceLightIMELVo projectDeviceLightIMELVo) {
        return R.ok(projectDeviceInfoService.saveLightingIMEL(projectDeviceLightIMELVo));
    }

    @ApiOperation(value = "查询控制器IMEL", notes = "查询控制器IMEL")
    @SysLog("查询控制器IMEL")
    @GetMapping("/getControllerIMEL")
    public R getControllerIMEL(String deviceType) {
        return R.ok(projectDeviceInfoService.getControllerIMEL(deviceType));
    }

    @ApiOperation(value = "路灯控制", notes = "路灯控制")
    @SysLog("路灯开关控制")
    @PostMapping("/streetLightControl")
    public R streetLightControl(@RequestBody StreetLightDeviceStatus deviceStatus) {
        return R.ok(projectDeviceInfoService.streetLightControl(deviceStatus, ProjectContextHolder.getProjectId()));
    }

    @ApiOperation(value = "根据设备ID查询所属模块", notes = "根据设备ID查询所属模块")
    @SysLog("根据设备ID查询所属模块")
    @GetMapping("/getModule/{deviceId}")
    public R getModule(@PathVariable String deviceId) {
        return R.ok(projectDeviceInfoService.getModule(deviceId));
    }

    @ApiOperation(value = "AI盒子设备关联摄像头", notes = "AI盒子设备关联摄像头")
    @SysLog("AI盒子设备关联摄像头")
    @GetMapping("/relateChildDev/{id}")
    public R relateChildDev(@PathVariable String id) {
        return R.ok(projectDeviceInfoService.relateChildDev(id));
    }

    @ApiOperation(value = "AI盒子设备获取摄像头", notes = "AI盒子设备获取摄像头")
    @SysLog("AI盒子设备获取摄像头")
    @GetMapping("/getChildDev/{id}")
    public R getChildDev(@PathVariable String id, @RequestParam(required = false) String deviceAlias, @RequestParam(required = false) String ipv4) {
        return R.ok(projectDeviceInfoService.getChildDev(id, deviceAlias, ipv4));
    }


    @ApiOperation(value = "AI盒子摄像头获取报警规则", notes = "AI盒子摄像头获取报警规则")
    @SysLog("AI盒子摄像头获取报警规则")
    @GetMapping("/getGatherAlarmRule/{id}")
    public R getGatherAlarmRule(@PathVariable String id) {
        return R.ok(projectDeviceGatherAlarmRuleService.getProjectDeviceGatherAlarmRuleVoByDeviceId(id));
    }

    @ApiOperation(value = "AI盒子摄像头设置报警规则", notes = "AI盒子摄像头设置报警规则")
    @SysLog("AI盒子摄像头设置报警规则")
    @PostMapping("/setGatherAlarmRule/{id}")
    public R setGatherAlarmRule(@PathVariable String id, @RequestBody ProjectDeviceGatherAlarmRuleVo ruleVo) {
        return R.ok(projectDeviceInfoService.setGatherAlarmRule(id, ruleVo));
    }

    @ApiOperation(value = "修改设备描述", notes = "修改设备描述")
    @SysLog("修改设备描述")
    @PutMapping("/setDeviceAlias/{id}")
    public R setDeviceAlias(@PathVariable String id, @RequestParam String deviceAlias) {
        return R.ok(projectDeviceInfoService.setDeviceAlias(id, deviceAlias));
    }

    @ApiOperation("获取电梯设备列表，包含对应楼栋的楼层数据")
    @GetMapping("/getLiftsWithFloor")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<List<ProjectDeviceLiftVo>> getLiftsWithFloor(@RequestParam(required = false) String liftPlanId, @RequestParam(required = false) String personId, @RequestParam(required = false) String personType) {
        return R.ok(projectDeviceInfoService.getLiftsWithFloor(liftPlanId, personType, personId));
    }

    @ApiOperation(value = "梯控楼层操作", notes = "梯控楼层操作")
    @SysLog("梯控楼层操作")
    @PostMapping("/floorOperate/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "设备id", required = true, paramType = "path"),
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R floorOperate(@PathVariable String id, @RequestBody String[] floor) {
        DeviceFactoryProducer.getFactory(id).getDeviceService().operateFloor(id, floor);
        return R.ok();
    }

    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出excel", notes = "导出excel")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public void exportExcel(@RequestBody Object type, HttpServletResponse httpServletResponse) {
        projectDeviceInfoService.exportExcel((String) type, httpServletResponse);
    }

    @ApiOperation(value = "查询设备免安装配置参数", notes = "查询设备免安装配置参数")
    @SysLog("查询设备免安装配置参数")
    @GetMapping("/network/config")
    @Inner(false)
    public R getNetworkConfig(@RequestParam String mac) {
        return R.ok(projectDeviceInfoService.getDeviceNetWorkInfo(mac));
    }

    @ApiOperation(value = "更新设备配置状态", notes = "更新设备配置状态")
    @SysLog("更新设备配置状态")
    @PutMapping("/configStatus")
    @Inner(false)
    public R updateConfigured(@RequestParam String mac) {
        return R.ok(projectDeviceInfoService.updateConfigured(mac));
    }

    /**
     * 返回有报警信息的设备
     *
     * @return 同类型字典
     */
    @GetMapping("/alarmDeviceType")
    public R alarmDeviceType() {
        return R.ok(projectDeviceInfoService.AlarmDeviceTypeList());
    }

/* //20230704 V1.3.0.0（人行版）去除车场相关信息
    @ApiOperation(value = "获取车道一体机列表", notes = "获取车道一体机列表")
    @GetMapping("/listVehicleBarrierDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R listVehicleBarrierDevice(VehicleBarrierDeviceQuery query) {
        return R.ok(projectDeviceInfoService.listVehicleBarrierDevice(query));
    }

    @ApiOperation(value = "新增车道一体机设备", notes = "新增车道一体机设备")
    @PostMapping("/saveVehicleBarrierDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R saveVehicleBarrierDevice(@RequestBody ProjectDeviceInfo projectDeviceInfo) {
        return projectDeviceInfoService.saveVehicleBarrierDevice(projectDeviceInfo);
    }*/

    @ApiOperation(value = "获取设备信息", notes = "获取设备信息")
    @GetMapping("/inner/getDeviceInfoByDeviceId/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Inner(value = false)
    public R<ProjectDeviceInfo> getDeviceInfoByDeviceId(@PathVariable("deviceId") String deviceId) {
        return R.ok(projectDeviceInfoProxyService.getVoById(deviceId));
    }

    @ApiOperation(value = "获取设备信息", notes = "获取设备信息")
    @PostMapping("/inner/listDeviceByIds")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Inner(value = false)
    public R<List<ProjectDeviceInfo>> listDeviceByIds(@RequestBody List<String> deviceId) {
        return R.ok(projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, deviceId)));
    }

    @ApiOperation(value = "通过第三方id获取设备信息", notes = "通过第三方id获取设备信息")
    @GetMapping("/inner/getDeviceInfoByThirdpartyCode/{thirdpartyCode}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "type", value = "设备类型", paramType = "path", required = true),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    @Inner
    public R<ProjectDeviceInfo> getDeviceInfoByThirdpartyCode(@PathVariable("thirdpartyCode") String thirdpartyCode) {
        return R.ok(projectDeviceInfoProxyService.getByThirdPartyCode(thirdpartyCode));
    }

    @ApiOperation("门常开设置")
    @SysLog("门常开设置")
    @PostMapping("/openAlways")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Boolean> openAlways(@RequestBody DeviceOpenAlwaysVo deviceOpenAlwaysVo) {
        return R.ok(DeviceFactoryProducer.getFactory(deviceOpenAlwaysVo.getDeviceId()).getDeviceService().openAlways(deviceOpenAlwaysVo.getDeviceId(), deviceOpenAlwaysVo.getDoorAction()));
    }

}
