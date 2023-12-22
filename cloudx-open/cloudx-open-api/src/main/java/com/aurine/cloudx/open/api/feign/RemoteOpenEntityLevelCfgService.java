package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.EntityLevelCfgVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 组团配置管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenEntityLevelCfgService", value = "cloudx-open-biz")
public interface RemoteOpenEntityLevelCfgService {

    /**
     * 通过id查询组团配置
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回组团配置信息
     */
    @GetMapping("/v1/open/entity-level-cfg/{id}")
    R<EntityLevelCfgVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询组团配置
     *
     * @param pageModel 分页查询条件
     * @return R 返回组团配置分页数据
     */
    @GetMapping("/v1/open/entity-level-cfg/page")
    R<Page<EntityLevelCfgVo>> page(@RequestBody OpenApiPageModel<EntityLevelCfgVo> pageModel);

    /**
     * 新增组团配置
     * 注：组团配置可能没有新增功能，该接口只是统一定义后的结果
     *
     * @param model 组团配置
     * @return R 返回新增后的组团配置
     */
    @PostMapping("/v1/open/entity-level-cfg")
    R<EntityLevelCfgVo> save(@RequestBody OpenApiModel<EntityLevelCfgVo> model);

    /**
     * 修改组团配置
     *
     * @param model 组团配置
     * @return R 返回修改后的组团配置
     */
    @PutMapping("/v1/open/entity-level-cfg")
    R<EntityLevelCfgVo> update(@RequestBody OpenApiModel<EntityLevelCfgVo> model);

    /**
     * 通过id删除组团配置
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/entity-level-cfg/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
