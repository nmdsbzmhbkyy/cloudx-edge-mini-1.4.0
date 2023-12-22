package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectFrameInfo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaFrameInfo", value = "cloudx-open-biz")
public interface RemoteMetaFrameInfoService {

    /**
     * 新增框架信息
     *
     * @param model 框架信息
     * @return R 返回新增后的框架信息
     */
    @PostMapping("/v1/meta/frame-info")
    R<ProjectFrameInfo> save(@RequestBody OpenApiModel<ProjectFrameInfo> model);

    /**
     * 修改框架信息
     *
     * @param model 框架信息
     * @return R 返回修改后的框架信息
     */
    @PutMapping("/v1/meta/frame-info")
    R<ProjectFrameInfo> update(@RequestBody OpenApiModel<ProjectFrameInfo> model);

    /**
     * 通过id删除框架信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/frame-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}