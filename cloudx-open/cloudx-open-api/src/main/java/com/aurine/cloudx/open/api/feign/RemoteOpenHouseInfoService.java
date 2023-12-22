package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HouseInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenHouseInfoService", value = "cloudx-open-biz")
public interface RemoteOpenHouseInfoService {

    /**
     * 通过id查询房屋信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回房屋信息
     */
    @GetMapping("/v1/open/house-info/{id}")
    R<HouseInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询房屋信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回房屋信息分页数据
     */
    @GetMapping("/v1/open/house-info/page")
    R<Page<HouseInfoVo>> page(@RequestBody OpenApiPageModel<HouseInfoVo> pageModel);

    /**
     * 新增房屋信息
     *
     * @param model 房屋信息
     * @return R 返回新增后的房屋信息
     */
    @PostMapping("/v1/open/house-info")
    R<HouseInfoVo> save(@RequestBody OpenApiModel<HouseInfoVo> model);

    /**
     * 修改房屋信息
     *
     * @param model 房屋信息
     * @return R 返回修改后的房屋信息
     */
    @PutMapping("/v1/open/house-info")
    R<HouseInfoVo> update(@RequestBody OpenApiModel<HouseInfoVo> model);

    /**
     * 通过id删除房屋信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/house-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
