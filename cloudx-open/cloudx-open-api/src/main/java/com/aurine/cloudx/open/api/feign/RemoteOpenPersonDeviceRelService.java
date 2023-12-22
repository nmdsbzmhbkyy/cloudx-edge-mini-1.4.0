package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonDeviceRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPersonDeviceRelService", value = "cloudx-open-biz")
public interface RemoteOpenPersonDeviceRelService {

    /**
     * 通过id查询人员设备权限关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回人员设备权限关系信息
     */
    @GetMapping("/v1/open/person-device-rel/{id}")
    R<PersonDeviceRelVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询人员设备权限关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回人员设备权限关系分页数据
     */
    @GetMapping("/v1/open/person-device-rel/page")
    R<Page<PersonDeviceRelVo>> page(@RequestBody OpenApiPageModel<PersonDeviceRelVo> pageModel);

    /**
     * 新增人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回新增后的人员设备权限关系
     */
    @PostMapping("/v1/open/person-device-rel")
    R<PersonDeviceRelVo> save(@RequestBody OpenApiModel<PersonDeviceRelVo> model);

    /**
     * 修改人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回修改后的人员设备权限关系
     */
    @PutMapping("/v1/open/person-device-rel")
    R<PersonDeviceRelVo> update(@RequestBody OpenApiModel<PersonDeviceRelVo> model);

    /**
     * 通过id删除人员设备权限关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/person-device-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
