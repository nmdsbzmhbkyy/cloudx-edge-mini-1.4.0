package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingInfo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 缴费记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaParkBillingInfo", value = "cloudx-open-biz")
public interface RemoteMetaParkBillingInfoService {

    /**
     * 新增缴费记录
     *
     * @param model 缴费记录
     * @return R 返回新增后的缴费记录
     */
    @PostMapping("/v1/meta/park-billing-info")
    R<ProjectParkBillingInfo> save(@RequestBody OpenApiModel<ProjectParkBillingInfo> model);

    /**
     * 修改缴费记录
     *
     * @param model 缴费记录
     * @return R 返回修改后的缴费记录
     */
    @PutMapping("/v1/meta/park-billing-info")
    R<ProjectParkBillingInfo> update(@RequestBody OpenApiModel<ProjectParkBillingInfo> model);

    /**
     * 通过id删除缴费记录
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/park-billing-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}