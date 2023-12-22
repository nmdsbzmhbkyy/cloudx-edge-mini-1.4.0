package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenPersonPlanRelService", value = "cloudx-open-biz")
public interface RemoteOpenPersonPlanRelService {

    /**
     * 通过id查询人员通行方案关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回人员通行方案关系信息
     */
    @GetMapping("/v1/open/person-plan-rel/{id}")
    R<PersonPlanRelVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询人员通行方案关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回人员通行方案关系分页数据
     */
    @GetMapping("/v1/open/person-plan-rel/page")
    R<Page<PersonPlanRelVo>> page(@RequestBody OpenApiPageModel<PersonPlanRelVo> pageModel);

    /**
     * 新增人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回新增后的人员通行方案关系
     */
    @PostMapping("/v1/open/person-plan-rel")
    R<PersonPlanRelVo> save(@RequestBody OpenApiModel<PersonPlanRelVo> model);

    /**
     * 修改人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回修改后的人员通行方案关系
     */
    @PutMapping("/v1/open/person-plan-rel")
    R<PersonPlanRelVo> update(@RequestBody OpenApiModel<PersonPlanRelVo> model);

    /**
     * 通过id删除人员通行方案关系
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/person-plan-rel/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
