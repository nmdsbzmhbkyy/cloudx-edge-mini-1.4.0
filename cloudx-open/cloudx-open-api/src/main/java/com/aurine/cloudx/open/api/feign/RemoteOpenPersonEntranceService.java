package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonEntranceVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人行事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPersonEntranceService", value = "cloudx-open-biz")
public interface RemoteOpenPersonEntranceService {

    /**
     * 通过id查询人行事件
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回人行事件信息
     */
    @GetMapping("/v1/open/person-entrance/{id}")
    R<PersonEntranceVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询人行事件
     *
     * @param pageModel 分页查询条件
     * @return R 返回人行事件分页数据
     */
    @GetMapping("/v1/open/person-entrance/page")
    R<Page<PersonEntranceVo>> page(@RequestBody OpenApiPageModel<PersonEntranceVo> pageModel);

    /**
     * 新增人行事件
     *
     * @param model 人行事件
     * @return R 返回新增后的人行事件
     */
    @PostMapping("/v1/open/person-entrance")
    R<PersonEntranceVo> save(@RequestBody OpenApiModel<PersonEntranceVo> model);

    /**
     * 修改人行事件
     *
     * @param model 人行事件
     * @return R 返回修改后的人行事件
     */
    @PutMapping("/v1/open/person-entrance")
    R<PersonEntranceVo> update(@RequestBody OpenApiModel<PersonEntranceVo> model);

    /**
     * 通过id删除人行事件
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/person-entrance/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
