package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 户型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenHouseDesignService", value = "cloudx-open-biz")
public interface RemoteOpenHouseDesignService {

    /**
     * 通过id查询户型
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回户型信息
     */
    @GetMapping("/v1/open/house-design/{id}")
    R<HouseDesignVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询户型
     *
     * @param pageModel 分页查询条件
     * @return R 返回户型分页数据
     */
    @GetMapping("/v1/open/house-design/page")
    R<Page<HouseDesignVo>> page(@RequestBody OpenApiPageModel<HouseDesignVo> pageModel);

    /**
     * 新增户型
     *
     * @param model 户型信息
     * @return R 返回新增后的户型信息
     */
    @PostMapping("/v1/open/house-design")
    R<HouseDesignVo> save(@RequestBody OpenApiModel<HouseDesignVo> model);

    /**
     * 修改户型
     *
     * @param model 户型信息
     * @return R 返回修改后的户型信息
     */
    @PutMapping("/v1/open/house-design")
    R<HouseDesignVo> update(@RequestBody OpenApiModel<HouseDesignVo> model);

    /**
     * 通过id删除户型
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/house-design/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
