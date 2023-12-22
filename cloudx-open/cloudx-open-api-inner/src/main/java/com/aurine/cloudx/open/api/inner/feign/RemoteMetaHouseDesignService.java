package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectHouseDesign;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 户型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteMetaHouseDesign", value = "cloudx-open-biz")
public interface RemoteMetaHouseDesignService {

    /**
     * 新增户型
     *
     * @param model 户型信息
     * @return R 返回新增后的户型信息
     */
    @PostMapping("/v1/meta/house-design")
    R<ProjectHouseDesign> save(@RequestBody OpenApiModel<ProjectHouseDesign> model);

    /**
     * 修改户型
     *
     * @param model 户型信息
     * @return R 返回修改后的户型信息
     */
    @PutMapping("/v1/meta/house-design")
    R<ProjectHouseDesign> update(@RequestBody OpenApiModel<ProjectHouseDesign> model);

    /**
     * 通过id删除户型
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/meta/house-design/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}