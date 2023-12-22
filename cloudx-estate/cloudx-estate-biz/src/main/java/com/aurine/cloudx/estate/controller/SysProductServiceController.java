package com.aurine.cloudx.estate.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.service.SysProductServiceService;
import com.aurine.cloudx.estate.service.SysServiceParamConfService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 以产品为维度进行平台设备参数配置项管理(SysProductService)表控制层
 *
 * @author 王良俊
 * @since 2020-12-15 10:24:58
 */
@RestController
@RequestMapping("SysProductService")
@Api(value = "SysProductService", tags = "以产品为维度进行平台设备参数配置项管理")
public class SysProductServiceController {
    /**
     * 服务对象
     */
    @Resource
    private SysProductServiceService sysProductServiceService;
    /**
     * 服务对象
     */
    @Resource
    private SysServiceParamConfService sysServiceParamConfService;
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    /**
     * 服务对象
     */
    @Resource
    private ProjectDeviceParamInfoService projectDeviceParamInfoService;

    /**
     * 分页查询所有数据
     *
     * @param page              分页对象
     * @param sysProductService 查询实体
     * @return 所有数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询sysProductParamCategory所有数据")
    public R selectAll(Page<com.aurine.cloudx.estate.entity.SysProductService> page, com.aurine.cloudx.estate.entity.SysProductService sysProductService) {
        return R.ok(this.sysProductServiceService.page(page, new QueryWrapper<>(sysProductService)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    @ApiOperation(value = "通过id查询", notes = "通过主键查询sysProductParamCategory单条数据")
    public R selectOne(@PathVariable Integer id) {
        return R.ok(this.sysProductServiceService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysProductService 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增sysProductParamCategory数据")
    public R insert(@RequestBody com.aurine.cloudx.estate.entity.SysProductService sysProductService) {
        return R.ok(this.sysProductServiceService.save(sysProductService));
    }

    /**
     * 修改数据
     *
     * @param sysProductService 实体对象
     * @return 修改结果
     */
    @PutMapping
    @ApiOperation(value = "修改数据", notes = "修改sysProductParamCategory数据")
    public R update(@RequestBody com.aurine.cloudx.estate.entity.SysProductService sysProductService) {
        return R.ok(this.sysProductServiceService.updateById(sysProductService));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    @ApiOperation(value = "删除数据", notes = "通过id删除sysProductParamCategory数据")
    public R delete(@RequestParam("idList") List<Integer> idList) {
        return R.ok(this.sysProductServiceService.removeByIds(idList));
    }

    /**
     * 根据产品ID获取到这个产品所有的表单
     ** json样例如下
     * <br>[
     * <br>    {
     * <br>        "serviceId": "NetworkObj",
     * <br>        "serviceName": "网络参数",
     * <br>        "formItems": [
     * <br>            {
     * <br>                "seq": null,
     * <br>                "serviceId": "NetworkObj",
     * <br>                "serviceName": "网络参数",
     * <br>                "paramId": null,
     * <br>                "paramName": "ip地址",
     * <br>                "isMandatory": null,
     * <br>                "columnType": null,
     * <br>                "valueRange": null,
     * <br>                "remark": null,
     * <br>                "tenant_id": null,
     * <br>                "createTime": null,
     * <br>                "updateTime": null
     * <br>            },
     * <br>            {
     * <br>                "seq": null,
     * <br>                "serviceId": "NetworkObj",
     * <br>                "serviceName": "网络参数",
     * <br>                "paramId": null,
     * <br>                "paramName": "mac地址",
     * <br>                "isMandatory": null,
     * <br>                "columnType": null,
     * <br>                "valueRange": null,
     * <br>                "remark": null,
     * <br>                "tenant_id": null,
     * <br>                "createTime": null,
     * <br>                "updateTime": null
     * <br>            }
     * <br>        ]
     * <br>    }
     * <br>]
     * @param productId 产品ID
     */
    @GetMapping("/getFormParamByProductId")
    @ApiOperation(value = "根据产品ID获取到这个产品所有的表单", notes = "根据产品ID获取到这个产品所有的表单")
    public R getFormParamByProductId(@RequestParam("productId") String productId, @RequestParam("deviceId") String deviceId) {
        List<SysProductService> serviceList = sysProductServiceService.list(new QueryWrapper<SysProductService>().lambda()
                .eq(SysProductService::getProductId, productId).notIn(SysProductService::getServiceId, DeviceParamEnum.getIsNotADeviceParameterServiceID()));
        if (CollUtil.isNotEmpty(serviceList)) {
            List<String> serviceIdList = serviceList.stream().map(SysProductService::getServiceId).collect(Collectors.toList());
            return R.ok(sysServiceParamConfService.getDeviceParamFormData(serviceIdList, deviceId));
        }
        return R.failed("本产品没有参数");
    }

    /**
     * 根据设备ID获取所属产品的所有表单
     * 返回的json格式如下
     *
     * json样例如下
     * <br>[
     * <br>    {
     * <br>        "serviceId": "NetworkObj",
     * <br>        "serviceName": "网络参数",
     * <br>        "formItems": [
     * <br>            {
     * <br>                "seq": null,
     * <br>                "serviceId": "NetworkObj",
     * <br>                "serviceName": "网络参数",
     * <br>                "paramId": null,
     * <br>                "paramName": "ip地址",
     * <br>                "isMandatory": null,
     * <br>                "columnType": null,
     * <br>                "valueRange": null,
     * <br>                "remark": null,
     * <br>                "tenant_id": null,
     * <br>                "createTime": null,
     * <br>                "updateTime": null
     * <br>            },
     * <br>            {
     * <br>                "seq": null,
     * <br>                "serviceId": "NetworkObj",
     * <br>                "serviceName": "网络参数",
     * <br>                "paramId": null,
     * <br>                "paramName": "mac地址",
     * <br>                "isMandatory": null,
     * <br>                "columnType": null,
     * <br>                "valueRange": null,
     * <br>                "remark": null,
     * <br>                "tenant_id": null,
     * <br>                "createTime": null,
     * <br>                "updateTime": null
     * <br>            }
     * <br>        ]
     * <br>    }
     * <br>]
     * @param deviceId 设备ID
     */
    @GetMapping("/getFormParamByDeviceId")
    @ApiOperation(value = "根据设备ID获取到这个产品所有的表单", notes = "根据设备ID获取到这个产品所有的表单")
    public R getFormParamByDeviceId(@RequestParam("deviceId") String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getOne(new QueryWrapper<ProjectDeviceInfo>().lambda()
                .eq(ProjectDeviceInfo::getDeviceId, deviceId));
        String productId = deviceInfo.getProductId();

        if (StrUtil.isNotEmpty(productId)) {
            List<SysProductService> serviceList = sysProductServiceService.list(new QueryWrapper<SysProductService>().lambda()
                    .eq(SysProductService::getProductId, productId).notIn(SysProductService::getServiceId, DeviceParamEnum.getIsNotADeviceParameterServiceID()));
            List<String> serviceIdList = serviceList.stream().map(SysProductService::getServiceId).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(serviceIdList)) {
                return R.ok(sysServiceParamConfService.getDeviceParamFormData(serviceIdList, deviceId));
            }
            return R.failed("当前设备没有参数");
        } else {
            return R.failed("设备没有关联产品");
        }
    }

    /**
    * <p>
    * 获取到设备状态页面展示项数据（决定了要显示出来设备在线状态外的哪些状态）
    * </p>
    *
    * @param
    * @return
    * @author: 王良俊
    */
    @GetMapping("/getDeviceStatusDisplayDataByDeviceId")
    @ApiOperation(value = "获取到设备状态页面展示项数据（决定了要显示出来设备在线状态外的哪些状态）", notes = "获取到设备状态页面展示项数据（决定了要显示出来设备在线状态外的哪些状态）")
    public R getDeviceStatusDisplayData(@RequestParam("deviceId") String deviceId) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(deviceId);
        if (deviceInfo != null) {
            int count = sysProductServiceService.count(new LambdaQueryWrapper<SysProductService>()
                    .eq(SysProductService::getProductId, deviceInfo.getProductId())
                    .eq(SysProductService::getServiceId, DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId));
            if (count != 0) {
                return R.ok(this.sysServiceParamConfService.list(new LambdaQueryWrapper<SysServiceParamConf>()
                        .eq(SysServiceParamConf::getServiceId, DeviceParamEnum.DEVICE_STATE_CHANGE.serviceId)));
            }
        }
        return R.ok(new ArrayList<>());
    }



}