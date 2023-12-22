package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaExecuteSql", value = "cloudx-open-biz")
public interface RemoteMetaExecuteSqlService {

    /**
     * 执行新增sql
     *
     * @param model 报警事件
     * @return R 返回新增后的报警事件
     */
    @PostMapping("/v1/meta/executeSql")
    R<Boolean> save(@RequestBody OpenApiModel model);

    /**
     * 执行更新sql
     *
     * @param model 报警事件
     * @return R 返回修改后的报警事件
     */
    @PutMapping("/v1/meta/executeSql")
    R<Boolean> update(@RequestBody OpenApiModel model);

    /**
     * 执行删除sql
     * @param model
     * @return
     */
    @DeleteMapping("/v1/meta/executeSql")
    R<Boolean> delete( @RequestBody OpenApiModel model);
}