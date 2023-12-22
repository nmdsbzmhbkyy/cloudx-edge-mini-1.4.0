package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.DeviceInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 设备信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenDeviceInfoService", value = "cloudx-open-biz")
public interface RemoteOpenDeviceInfoService {

    /**
     * 通过id查询设备信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回设备信息
     */
    @GetMapping("/v1/open/device-info/{id}")
    R<DeviceInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询设备信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回设备信息分页数据
     */
    @GetMapping("/v1/open/device-info/page")
    R<Page<DeviceInfoVo>> page(@RequestBody OpenApiPageModel<DeviceInfoVo> pageModel);

    /**
     * 新增设备信息
     *
     * @param model 设备信息
     * @return R 返回新增后的设备信息
     */
    @PostMapping("/v1/open/device-info")
    R<DeviceInfoVo> save(@RequestBody OpenApiModel<DeviceInfoVo> model);

    /**
     * 修改设备信息
     *
     * @param model 设备信息
     * @return R 返回修改后的设备信息
     */
    @PutMapping("/v1/open/device-info")
    R<DeviceInfoVo> update(@RequestBody OpenApiModel<DeviceInfoVo> model);

    /**
     * 通过id删除设备信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/device-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
