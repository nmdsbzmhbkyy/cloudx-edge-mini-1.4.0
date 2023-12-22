package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * open平台-人员标签管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@FeignClient(contextId = "remoteMetaLabelConfig", value = "cloudx-open-biz")
public interface RemoteMetaLabelConfigService {

    /**
     * 新增人员标签
     *
     * @param model 人员标签
     * @return R 返回新增后的人员标签
     */
    @PostMapping("/v1/meta/label-config")
    R<ProjectLabelConfig> save(@RequestBody OpenApiModel<ProjectLabelConfig> model);

    /**
     * 修改人员标签
     *
     * @param model 人员标签
     * @return R 返回修改后的人员标签
     */
    @PutMapping("/v1/meta/label-config")
    R<ProjectLabelConfig> update(@RequestBody OpenApiModel<ProjectLabelConfig> model);

    /**
     * 删除人员标签
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/label-config/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

}
