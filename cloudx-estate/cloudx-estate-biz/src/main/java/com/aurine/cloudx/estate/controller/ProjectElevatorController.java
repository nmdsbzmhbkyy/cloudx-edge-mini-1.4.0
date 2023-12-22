package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.IntoCloudStatusEnum;
import com.aurine.cloudx.estate.entity.EdgeCloudRequest;
import com.aurine.cloudx.estate.entity.OpenApiEntity;
import com.aurine.cloudx.estate.openapi.enums.OpenApiCommandTypeEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.openapi.service.OpenApiMessageService;
import com.aurine.cloudx.estate.service.EdgeCloudRequestService;
import com.aurine.cloudx.estate.service.ProjectElevatorService;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>电梯</p>
 *
 * @author 王良俊
 * @date 2022/2/11
 */
@RestController
@RequestMapping("/projectElevator")
public class ProjectElevatorController {

    @Resource
    ProjectElevatorService projectElevatorService;

    @Resource
    private EdgeCloudRequestService edgeCloudRequestService;

    @Resource
    private OpenApiMessageService openApiMessageService;

    /**
     * 分页查询 电梯
     *
     * @param page               分页对象
     * @param elevatorPageFormVo 电梯查询条件
     */
    @ApiOperation(value = "分页查询 电梯", notes = "分页查询 电梯")
    @GetMapping("/pageElevator")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getElevatorPage(Page page, ElevatorPageFormVo elevatorPageFormVo) {
        return R.ok(projectElevatorService.pageElevator(page, elevatorPageFormVo));
    }

    /**
     * 分页查询 电梯
     *
     * @param page   分页对象
     * @param formVo 电梯查询条件
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/pageLayerControlDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectLayerDeviceInfoVo>> pageLayerControllerDevice(Page page, ProjectDeviceInfoPageFormVo formVo) {
        return R.ok(projectElevatorService.pageLayerDevice(page, formVo.getUnitId(), formVo));
    }

    /**
     * 分页查询 电梯
     *
     * @param page   分页对象
     * @param formVo 电梯查询条件
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/pageRecognizerControlDevice")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<Page<ProjectDeviceInfoVo>> pageRecognizerControlDevice(Page page, ProjectDeviceInfoPageFormVo formVo) {
        return R.ok(projectElevatorService.pageRecognizerControlDevice(page, formVo.getUnitId(), formVo));
    }


    /**
     * 创建 电梯
     *
     * @param formVo 电梯对象
     */
    @ApiOperation(value = "创建电梯", notes = "创建电梯")
    @PostMapping("/addElevator")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R addElevator(@RequestBody ProjectElevatorFormVo formVo) {
        return projectElevatorService.creatElevator(formVo);
    }


    /**
     * 获取电梯信息
     *
     * @param deviceId 电梯ID
     */
    @ApiOperation(value = "创建电梯", notes = "创建电梯")
    @GetMapping("/getById/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getById(@PathVariable("deviceId") String deviceId) {
        return R.ok(projectElevatorService.getElevatorById(deviceId));
    }

    /**
     * 删除电梯
     *
     * @param deviceId 电梯ID
     */
    @ApiOperation(value = "删除电梯", notes = "删除电梯")
    @GetMapping("/removeById/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R removeById(@PathVariable("deviceId") String deviceId) {
        int count = edgeCloudRequestService.count(Wrappers.lambdaQuery(EdgeCloudRequest.class)
                .eq(EdgeCloudRequest::getProjectId, ProjectContextHolder.getProjectId())
                .in(EdgeCloudRequest::getCloudStatus, IntoCloudStatusEnum.INTO_CLOUD.code, IntoCloudStatusEnum.UNBINDING.code));
        if(count > 0){
            OpenApiEntity openApiEntity = new OpenApiEntity();
            openApiEntity.setServiceType(OpenPushSubscribeCallbackTypeEnum.COMMAND.name);
            openApiEntity.setCommandType(OpenApiCommandTypeEnum.DELETE_DEVICE.name);
            openApiEntity.setServiceName(OpenApiServiceNameEnum.DEVICE_INFO.name);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uuid",deviceId);
            openApiEntity.setData(jsonObject);
            openApiMessageService.sendOpenApiMessage(openApiEntity);
        }
        return R.ok(projectElevatorService.removeElevatorById(deviceId));
    }

    /**
     * 修改电梯信息
     *
     * @param data 要修改的电梯数据（带电梯ID）
     */
    @ApiOperation(value = "更新电梯信息", notes = "更新电梯信息")
    @PostMapping("/updateElevatorById")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R updateElevatorById(@RequestBody ProjectElevatorFormVo data) {
        return projectElevatorService.updateElevatorById(data);
    }


    /**
     * 获取分层控制器设备列表
     *
     * @param deviceId 电梯ID
     */
    @ApiOperation(value = "根据电梯ID，获取分层控制器设备列表", notes = "根据电梯ID，获取分层控制器设备列表")
    @GetMapping("/getLayerControlDeviceByElevatorId/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getLayerControlDeviceByElevatorId(@PathVariable("deviceId") String deviceId) {
        return R.ok(projectElevatorService.getLayerControlDeviceByElevatorId(deviceId));
    }

    /**
     * 获取乘梯识别终端列表
     *
     * @param deviceId 电梯ID
     */
    @ApiOperation(value = "根据电梯ID，获取乘梯识别终端列表", notes = "根据电梯ID，获取乘梯识别终端列表")
    @GetMapping("/getRecognizerDeviceByElevatorId/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getRecognizerDeviceByElevatorId(@PathVariable("deviceId") String deviceId) {
        return R.ok(projectElevatorService.getRecognizerDeviceByElevatorId(deviceId));
    }

    /**
     * 获取电梯的分层控制器配置
     *
     * @param deviceId 电梯ID
     */
    @ApiOperation(value = "根据电梯ID，获取电梯的分层控制器配置", notes = "根据电梯ID，获取电梯的分层控制器配置")
    @GetMapping("/getRecognizerDeviceParamByElevatorId/{deviceId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R getRecognizerDeviceParamByElevatorId(@PathVariable("deviceId") String deviceId) {
        return R.ok(projectElevatorService.getRecognizerDeviceParamByElevatorId(deviceId));
    }

}
