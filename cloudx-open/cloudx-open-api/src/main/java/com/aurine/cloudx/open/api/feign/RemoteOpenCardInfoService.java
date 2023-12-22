package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.CardInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenCardInfoService", value = "cloudx-open-biz")
public interface RemoteOpenCardInfoService {

    /**
     * 通过id查询卡信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回卡信息
     */
    @GetMapping("/v1/open/card-info/{id}")
    R<CardInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询卡信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回卡信息分页数据
     */
    @GetMapping("/v1/open/card-info/page")
    R<Page<CardInfoVo>> page(@RequestBody OpenApiPageModel<CardInfoVo> pageModel);

    /**
     * 新增卡信息
     *
     * @param model 卡信息
     * @return R 返回新增后的卡信息
     */
    @PostMapping("/v1/open/card-info")
    R<CardInfoVo> save(@RequestBody OpenApiModel<CardInfoVo> model);

    /**
     * 修改卡信息
     *
     * @param model 卡信息
     * @return R 返回修改后的卡信息
     */
    @PutMapping("/v1/open/card-info")
    R<CardInfoVo> update(@RequestBody OpenApiModel<CardInfoVo> model);

    /**
     * 通过id删除卡信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/card-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
