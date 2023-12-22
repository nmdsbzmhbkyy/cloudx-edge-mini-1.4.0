package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PassPlanPolicyRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPassPlanPolicyRelService", value = "cloudx-open-biz")
public interface RemoteOpenPassPlanPolicyRelService {

    /**
     * 通过id查询通行方案策略关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回通行方案策略关系信息
     */
    @GetMapping("/v1/open/pass-plan-policy-rel/{id}")
    R<PassPlanPolicyRelVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询通行方案策略关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回通行方案策略关系分页数据
     */
    @GetMapping("/v1/open/pass-plan-policy-rel/page")
    R<Page<PassPlanPolicyRelVo>> page(@RequestBody OpenApiPageModel<PassPlanPolicyRelVo> pageModel);

    /**
     * 新增通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回新增后的通行方案策略关系
     */
    @PostMapping("/v1/open/pass-plan-policy-rel")
    R<PassPlanPolicyRelVo> save(@RequestBody OpenApiModel<PassPlanPolicyRelVo> model);

    /**
     * 修改通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回修改后的通行方案策略关系
     */
    @PutMapping("/v1/open/pass-plan-policy-rel")
    R<PassPlanPolicyRelVo> update(@RequestBody OpenApiModel<PassPlanPolicyRelVo> model);

    /**
     * 通过id删除通行方案策略关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/pass-plan-policy-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
