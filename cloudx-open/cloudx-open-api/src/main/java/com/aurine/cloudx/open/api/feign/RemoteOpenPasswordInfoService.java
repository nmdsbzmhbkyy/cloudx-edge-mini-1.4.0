package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPasswordInfoService", value = "cloudx-open-biz")
public interface RemoteOpenPasswordInfoService {

    /**
     * 通过id查询密码信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回密码信息
     */
    @GetMapping("/v1/open/password-info/{id}")
    R<PasswordInfoVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询密码信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回密码信息分页数据
     */
    @GetMapping("/v1/open/password-info/page")
    R<Page<PasswordInfoVo>> page(@RequestBody OpenApiPageModel<PasswordInfoVo> pageModel);

    /**
     * 新增密码信息
     *
     * @param model 密码信息
     * @return R 返回新增后的密码信息
     */
    @PostMapping("/v1/open/password-info")
    R<PasswordInfoVo> save(@RequestBody OpenApiModel<PasswordInfoVo> model);

    /**
     * 修改密码信息
     *
     * @param model 密码信息
     * @return R 返回修改后的密码信息
     */
    @PutMapping("/v1/open/password-info")
    R<PasswordInfoVo> update(@RequestBody OpenApiModel<PasswordInfoVo> model);

    /**
     * 通过id删除密码信息
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/password-info/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
