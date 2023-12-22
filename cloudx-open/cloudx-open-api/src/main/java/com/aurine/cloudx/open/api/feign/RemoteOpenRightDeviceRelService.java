package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.RightDeviceRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 权限设备关系管理
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */

@FeignClient(contextId = "remoteOpenRightDeviceRelService", value = "cloudx-open-biz")
public interface RemoteOpenRightDeviceRelService {

    /**
     * 通过id查询区域
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回区域信息
     */
    @GetMapping("/v1/open/right-device-rel/{id}")
    R<RightDeviceRelVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询区域
     *
     * @param pageModel 分页查询条件
     * @return R 返回区域分页数据
     */
    @GetMapping("/v1/open/right-device-rel/page")
    R<Page<RightDeviceRelVo>> page(@RequestBody OpenApiPageModel<RightDeviceRelVo> pageModel);


    /**
     * 新增项目区域
     *
     * @param model 项目区域
     * @return R 返回新增后的项目区域
     */
    @PostMapping("/v1/open/right-device-rel")
    R<RightDeviceRelVo> save(@RequestBody OpenApiModel<RightDeviceRelVo> model);

    /**
     * 修改项目区域
     *
     * @param model 项目区域
     * @return R 返回修改后的项目区域
     */
    @PutMapping("/v1/open/right-device-rel")
    R<RightDeviceRelVo> update(@RequestBody OpenApiModel<RightDeviceRelVo> model);

    /**
     * 通过id删除项目区域
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/right-device-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
