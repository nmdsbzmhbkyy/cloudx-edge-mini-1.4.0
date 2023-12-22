package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.FrameInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenFrameInfoService", value = "cloudx-open-biz")
public interface RemoteOpenFrameInfoService {

    /**
     * 通过id查询框架信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回框架信息
     */
    @GetMapping("/v1/open/frame-info/{id}")
    R<FrameInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询框架信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回框架信息分页数据
     */
    @GetMapping("/v1/open/frame-info/page")
    R<Page<FrameInfoVo>> page(@RequestBody OpenApiPageModel<FrameInfoVo> pageModel);

    /**
     * 新增框架信息
     *
     * @param model 框架信息
     * @return R 返回新增后的框架信息
     */
    @PostMapping("/v1/open/frame-info")
    R<FrameInfoVo> save(@RequestBody OpenApiModel<FrameInfoVo> model);

    /**
     * 修改框架信息
     *
     * @param model 框架信息
     * @return R 返回修改后的框架信息
     */
    @PutMapping("/v1/open/frame-info")
    R<FrameInfoVo> update(@RequestBody OpenApiModel<FrameInfoVo> model);

    /**
     * 通过id删除框架信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/frame-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
