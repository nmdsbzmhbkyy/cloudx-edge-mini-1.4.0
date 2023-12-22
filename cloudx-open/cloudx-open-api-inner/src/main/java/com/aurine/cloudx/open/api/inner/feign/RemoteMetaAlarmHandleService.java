package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectAlarmHandle;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaAlarmHandle", value = "cloudx-open-biz")
public interface RemoteMetaAlarmHandleService {

    /**
     * 新增报警处理
     *
     * @param model 报警处理
     * @return R 返回新增后的报警处理
     */
    @PostMapping("/v1/meta/alarm-handle")
    R<ProjectAlarmHandle> save(@RequestBody OpenApiModel<ProjectAlarmHandle> model);

    /**
     * 修改报警处理
     *
     * @param model 报警处理
     * @return R 返回修改后的报警处理
     */
    @PutMapping("/v1/meta/alarm-handle")
    R<ProjectAlarmHandle> update(@RequestBody OpenApiModel<ProjectAlarmHandle> model);

    /**
     * 通过id删除报警处理
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/alarm-handle/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}