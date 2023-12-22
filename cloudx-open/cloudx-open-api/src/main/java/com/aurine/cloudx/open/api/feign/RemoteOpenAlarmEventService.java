package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.AlarmEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 报警事件管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenAlarmEventService", value = "cloudx-open-biz")
public interface RemoteOpenAlarmEventService {

    /**
     * 通过id查询报警事件
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回报警事件信息
     */
    @GetMapping("/v1/open/alarm-event/{id}")
    R<AlarmEventVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询报警事件
     *
     * @param pageModel 分页查询条件
     * @return R 返回报警事件分页数据
     */
    @GetMapping("/v1/open/alarm-event/page")
    R<Page<AlarmEventVo>> page(@RequestBody OpenApiPageModel<AlarmEventVo> pageModel);

    /**
     * 新增报警事件
     *
     * @param model 报警事件
     * @return R 返回新增后的报警事件
     */
    @PostMapping("/v1/open/alarm-event")
    R<AlarmEventVo> save(@RequestBody OpenApiModel<AlarmEventVo> model);

    /**
     * 修改报警事件
     *
     * @param model 报警事件
     * @return R 返回修改后的报警事件
     */
    @PutMapping("/v1/open/alarm-event")
    R<AlarmEventVo> update(@RequestBody OpenApiModel<AlarmEventVo> model);

    /**
     * 通过id删除报警事件
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/alarm-event/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
