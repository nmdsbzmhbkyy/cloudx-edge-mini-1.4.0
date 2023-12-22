package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectPasswd;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaPasswordInfo", value = "cloudx-open-biz")
public interface RemoteMetaPasswordInfoService {

    /**
     * 新增密码信息
     *
     * @param model 密码信息
     * @return R 返回新增后的密码信息
     */
    @PostMapping("/v1/meta/password-info")
    R<ProjectPasswd> save(@RequestBody OpenApiModel<ProjectPasswd> model);

    /**
     * 修改密码信息
     *
     * @param model 密码信息
     * @return R 返回修改后的密码信息
     */
    @PutMapping("/v1/meta/password-info")
    R<ProjectPasswd> update(@RequestBody OpenApiModel<ProjectPasswd> model);

    /**
     * 通过id删除密码信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/password-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}