package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectSnapRecord;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 抓拍记录
 *
 * @author : lxl
 * @date : 2022 10 20 14:34
 */

@FeignClient(contextId = "remoteMetaSnapRecord", value = "cloudx-open-biz")
public interface RemoteMetaSnapRecordService {

    /**
     * 新增
     */
    @PostMapping("/v1/meta/snap-record")
    R<ProjectSnapRecord> save(@RequestBody OpenApiModel<ProjectSnapRecord> model);

    /**
     * 修改
     */
    @PutMapping("/v1/meta/snap-record")
    R<ProjectSnapRecord> update(@RequestBody OpenApiModel<ProjectSnapRecord> model);

    /**
     * 删除
     */
    @DeleteMapping("/v1/meta/snap-record/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

}