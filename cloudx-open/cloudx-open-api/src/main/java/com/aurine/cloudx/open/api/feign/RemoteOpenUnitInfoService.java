package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.UnitInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 单元信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenUnitInfoService", value = "cloudx-open-biz")
public interface RemoteOpenUnitInfoService {

    /**
     * 通过id查询单元信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回单元信息
     */
    @GetMapping("/v1/open/unit-info/{id}")
    R<UnitInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询单元信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回单元信息分页数据
     */
    @GetMapping("/v1/open/unit-info/page")
    R<Page<UnitInfoVo>> page(@RequestBody OpenApiPageModel<UnitInfoVo> pageModel);

    /**
     * 新增单元信息
     *
     * @param model 单元信息
     * @return R 返回新增后的单元信息
     */
    @PostMapping("/v1/open/unit-info")
    R<UnitInfoVo> save(@RequestBody OpenApiModel<UnitInfoVo> model);

    /**
     * 修改单元信息
     *
     * @param model 单元信息
     * @return R 返回修改后的单元信息
     */
    @PutMapping("/v1/open/unit-info")
    R<UnitInfoVo> update(@RequestBody OpenApiModel<UnitInfoVo> model);

    /**
     * 通过id删除单元信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/unit-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
