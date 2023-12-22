package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPassPlanService", value = "cloudx-open-biz")
public interface RemoteOpenPassPlanService {

    /**
     * 通过id查询通行方案
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回通行方案信息
     */
    @GetMapping("/v1/open/pass-plan/{id}")
    R<PassPlanVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询通行方案
     *
     * @param pageModel 分页查询条件
     * @return R 返回通行方案分页数据
     */
    @GetMapping("/v1/open/pass-plan/page")
    R<Page<PassPlanVo>> page(@RequestBody OpenApiPageModel<PassPlanVo> pageModel);

    /**
     * 新增通行方案
     *
     * @param model 通行方案
     * @return R 返回新增后的通行方案
     */
    @PostMapping("/v1/open/pass-plan")
    R<PassPlanVo> save(@RequestBody OpenApiModel<PassPlanVo> model);

    /**
     * 修改通行方案
     *
     * @param model 通行方案
     * @return R 返回修改后的通行方案
     */
    @PutMapping("/v1/open/pass-plan")
    R<PassPlanVo> update(@RequestBody OpenApiModel<PassPlanVo> model);

    /**
     * 通过id删除通行方案
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/pass-plan/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
