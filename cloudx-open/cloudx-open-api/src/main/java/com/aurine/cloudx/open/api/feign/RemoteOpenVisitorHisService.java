package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.VisitorHisVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 来访记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenVisitorHisService", value = "cloudx-open-biz")
public interface RemoteOpenVisitorHisService {

    /**
     * 通过id查询来访记录
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回来访记录信息
     */
    @GetMapping("/v1/open/visitor-his/{id}")
    R<VisitorHisVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询来访记录
     *
     * @param pageModel 分页查询条件
     * @return R 返回来访记录分页数据
     */
    @GetMapping("/v1/open/visitor-his/page")
    R<Page<VisitorHisVo>> page(@RequestBody OpenApiPageModel<VisitorHisVo> pageModel);

    /**
     * 新增来访记录
     *
     * @param model 来访记录
     * @return R 返回新增后的来访记录
     */
    @PostMapping("/v1/open/visitor-his")
    R<VisitorHisVo> save(@RequestBody OpenApiModel<VisitorHisVo> model);

    /**
     * 修改来访记录
     *
     * @param model 来访记录
     * @return R 返回修改后的来访记录
     */
    @PutMapping("/v1/open/visitor-his")
    R<VisitorHisVo> update(@RequestBody OpenApiModel<VisitorHisVo> model);

    /**
     * 通过id删除来访记录
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/visitor-his/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
