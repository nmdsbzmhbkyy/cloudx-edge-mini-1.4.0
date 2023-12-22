package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.VisitorInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenVisitorInfoService", value = "cloudx-open-biz")
public interface RemoteOpenVisitorInfoService {

    /**
     * 通过id查询访客信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回访客信息
     */
    @GetMapping("/v1/open/visitor-info/{id}")
    R<VisitorInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询访客信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回访客信息分页数据
     */
    @GetMapping("/v1/open/visitor-info/page")
    R<Page<VisitorInfoVo>> page(@RequestBody OpenApiPageModel<VisitorInfoVo> pageModel);

    /**
     * 新增访客信息
     *
     * @param model 访客信息
     * @return R 返回新增后的访客信息
     */
    @PostMapping("/v1/open/visitor-info")
    R<VisitorInfoVo> save(@RequestBody OpenApiModel<VisitorInfoVo> model);

    /**
     * 修改访客信息
     *
     * @param model 访客信息
     * @return R 返回修改后的访客信息
     */
    @PutMapping("/v1/open/visitor-info")
    R<VisitorInfoVo> update(@RequestBody OpenApiModel<VisitorInfoVo> model);

    /**
     * 通过id删除访客信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/visitor-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
