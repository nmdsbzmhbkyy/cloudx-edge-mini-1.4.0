package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPersonLiftRel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人员电梯权限关系
 *
 * @author : zy
 * @date : 2022-07-18 09:46:54
 */

@FeignClient(contextId = "remoteMetaPersonLiftRel", value = "cloudx-open-biz")
public interface RemoteMetaPersonLiftRelService {


    /**
     * 新增人员电梯权限关系
     *
     * @param model 人员电梯权限关系
     * @return R 返回新增后的人员电梯权限关系
     */
    @PostMapping("/v1/meta/person-lift-rel")
     R<ProjectPersonLiftRel> save(@RequestBody OpenApiModel<ProjectPersonLiftRel> model);

    /**
     * 修改人员电梯权限关系
     *
     * @param model 人员电梯权限关系
     * @return R 返回修改后的人员电梯权限关系
     */

    @PutMapping("/v1/meta/person-lift-rel")
     R<ProjectPersonLiftRel> update( @RequestBody OpenApiModel<ProjectPersonLiftRel> model);

    /**
     * 通过id删除人员电梯权限关系
     *
     * @param id
     * @return
     */
    @DeleteMapping("/v1/meta/person-lift-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}