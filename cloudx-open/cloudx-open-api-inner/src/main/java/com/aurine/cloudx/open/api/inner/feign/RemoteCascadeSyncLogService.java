package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 级联入云同步日志
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@FeignClient(contextId = "remoteCascadeSyncLog", value = "cloudx-open-biz")
public interface RemoteCascadeSyncLogService {

    /**
     * 新增级联入云同步日志
     *
     * @param model 级联入云同步日志
     * @return 返回新增后的级联入云同步日志
     */
    @PostMapping("v1/cascade/sync-log")
    R<EdgeSyncLog> save(@RequestBody OpenApiModel<EdgeSyncLog> model);

    /**
     * 修改级联入云同步日志
     *
     * @param model 级联入云同步日志
     * @return 返回修改后的级联入云同步日志
     */
    @PutMapping("v1/cascade/sync-log")
    R<EdgeSyncLog> update(@RequestBody OpenApiModel<EdgeSyncLog> model);
}
