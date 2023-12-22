package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HousePersonInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 住户信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenHousePersonInfoService", value = "cloudx-open-biz")
public interface RemoteOpenHousePersonInfoService {

    /**
     * 通过id查询住户信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回住户信息
     */
    @GetMapping("/v1/open/house-person-info/{id}")
    R<HousePersonInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询住户信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回住户信息分页数据
     */
    @GetMapping("/v1/open/house-person-info/page")
    R<Page<HousePersonInfoVo>> page(@RequestBody OpenApiPageModel<HousePersonInfoVo> pageModel);

    /**
     * 新增住户信息
     *
     * @param model 住户信息
     * @return R 返回新增后的住户信息
     */
    @PostMapping("/v1/open/house-person-info")
    R<HousePersonInfoVo> save(@RequestBody OpenApiModel<HousePersonInfoVo> model);

    /**
     * 修改住户信息
     *
     * @param model 住户信息
     * @return R 返回修改后的住户信息
     */
    @PutMapping("/v1/open/house-person-info")
    R<HousePersonInfoVo> update(@RequestBody OpenApiModel<HousePersonInfoVo> model);

    /**
     * 通过id删除住户信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/house-person-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
