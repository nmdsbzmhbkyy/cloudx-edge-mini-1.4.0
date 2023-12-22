package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 员工信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenStaffInfoService", value = "cloudx-open-biz")
public interface RemoteOpenStaffInfoService {

    /**
     * 通过id查询员工信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回员工信息
     */
    @GetMapping("/v1/open/staff-info/{id}")
    R<StaffInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询员工信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回员工信息分页数据
     */
    @GetMapping("/v1/open/staff-info/page")
    R<Page<StaffInfoVo>> page(@RequestBody OpenApiPageModel<StaffInfoVo> pageModel);

    /**
     * 新增员工信息
     *
     * @param model 员工信息
     * @return R 返回新增后的员工信息
     */
    @PostMapping("/v1/open/staff-info")
    R<StaffInfoVo> save(@RequestBody OpenApiModel<StaffInfoVo> model);

    /**
     * 修改员工信息
     *
     * @param model 员工信息
     * @return R 返回修改后的员工信息
     */
    @PutMapping("/v1/open/staff-info")
    R<StaffInfoVo> update(@RequestBody OpenApiModel<StaffInfoVo> model);

    /**
     * 通过id删除员工信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/staff-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
