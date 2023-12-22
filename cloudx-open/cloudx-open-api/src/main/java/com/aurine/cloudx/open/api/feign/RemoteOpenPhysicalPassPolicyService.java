package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPhysicalPassPolicyService", value = "cloudx-open-biz")
public interface RemoteOpenPhysicalPassPolicyService {

    /**
     * 通过id查询物理策略
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回物理策略信息
     */
    @GetMapping("/v1/open/physical-pass-policy/{id}")
    R<PhysicalPassPolicyVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询物理策略
     *
     * @param pageModel 分页查询条件
     * @return R 返回物理策略分页数据
     */
    @GetMapping("/v1/open/physical-pass-policy/page")
    R<Page<PhysicalPassPolicyVo>> page(@RequestBody OpenApiPageModel<PhysicalPassPolicyVo> pageModel);

    /**
     * 新增物理策略
     *
     * @param model 物理策略
     * @return R 返回新增后的物理策略
     */
    @PostMapping("/v1/open/physical-pass-policy")
    R<PhysicalPassPolicyVo> save(@RequestBody OpenApiModel<PhysicalPassPolicyVo> model);

    /**
     * 修改物理策略
     *
     * @param model 物理策略
     * @return R 返回修改后的物理策略
     */
    @PutMapping("/v1/open/physical-pass-policy")
    R<PhysicalPassPolicyVo> update(@RequestBody OpenApiModel<PhysicalPassPolicyVo> model);

    /**
     * 通过id删除物理策略
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/physical-pass-policy/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
