package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 开关闸记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaOpenLaneHis", value = "cloudx-open-biz")
public interface RemoteMetaOpenLaneHisService {

    /**
     * 新增开关闸记录
     *
     * @param model 开关闸记录
     * @return R 返回新增后的开关闸记录
     */
    @PostMapping("/v1/meta/open-lane-his")
    R<ProjectOpenLaneHis> save(@RequestBody OpenApiModel<ProjectOpenLaneHis> model);

    /**
     * 修改开关闸记录
     *
     * @param model 开关闸记录
     * @return R 返回修改后的开关闸记录
     */
    @PutMapping("/v1/meta/open-lane-his")
    R<ProjectOpenLaneHis> update(@RequestBody OpenApiModel<ProjectOpenLaneHis> model);

    /**
     * 通过id删除开关闸记录
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/open-lane-his/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}