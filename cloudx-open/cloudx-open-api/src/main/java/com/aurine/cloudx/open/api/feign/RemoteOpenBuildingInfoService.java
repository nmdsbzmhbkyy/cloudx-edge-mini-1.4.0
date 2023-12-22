package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.BuildingInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenBuildingInfoService", value = "cloudx-open-biz")
public interface RemoteOpenBuildingInfoService {

    /**
     * 通过id查询楼栋信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回楼栋信息
     */
    @GetMapping("/v1/open/building-info/{id}")
    R<BuildingInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询楼栋信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回楼栋信息分页数据
     */
    @GetMapping("/v1/open/building-info/page")
    R<Page<BuildingInfoVo>> page(@RequestBody OpenApiPageModel<BuildingInfoVo> pageModel);

    /**
     * 新增楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回新增后的楼栋信息
     */
    @PostMapping("/v1/open/building-info")
    R<BuildingInfoVo> save(@RequestBody OpenApiModel<BuildingInfoVo> model);

    /**
     * 修改楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回修改后的楼栋信息
     */
    @PutMapping("/v1/open/building-info")
    R<BuildingInfoVo> update(@RequestBody OpenApiModel<BuildingInfoVo> model);

    /**
     * 通过id删除楼栋信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/building-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
