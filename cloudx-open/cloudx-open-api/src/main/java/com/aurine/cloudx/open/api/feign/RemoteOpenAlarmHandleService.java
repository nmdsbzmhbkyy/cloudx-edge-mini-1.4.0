package com.aurine.cloudx.open.api.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.AlarmHandleVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 报警处理管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@FeignClient(contextId = "remoteOpenAlarmHandleService", value = "cloudx-open-biz")
public interface RemoteOpenAlarmHandleService {

    /**
     * 通过id查询报警处理
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要查询的主键id
     * @return R 返回报警处理信息
     */
    @GetMapping("/v1/open/alarm-handle/{id}")
    R<AlarmHandleVo> getById(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);

    /**
     * 分页查询报警处理
     *
     * @param pageModel 分页查询条件
     * @return R 返回报警处理分页数据
     */
    @GetMapping("/v1/open/alarm-handle/page")
    R<Page<AlarmHandleVo>> page(@RequestBody OpenApiPageModel<AlarmHandleVo> pageModel);

    /**
     * 新增报警处理
     *
     * @param model 报警处理
     * @return R 返回新增后的报警处理
     */
    @PostMapping("/v1/open/alarm-handle")
    R<AlarmHandleVo> save(@RequestBody OpenApiModel<AlarmHandleVo> model);

    /**
     * 修改报警处理
     *
     * @param model 报警处理
     * @return R 返回修改后的报警处理
     */
    @PutMapping("/v1/open/alarm-handle")
    R<AlarmHandleVo> update(@RequestBody OpenApiModel<AlarmHandleVo> model);

    /**
     * 通过id删除报警处理
     *
     * @param appId       应用ID
     * @param projectUUID 项目UUID
     * @param tenantId    租户ID
     * @param id          要删除的主键id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/open/alarm-handle/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @RequestParam("projectUUID") String projectUUID, @RequestParam("tenantId") Integer tenantId, @PathVariable("id") String id);
}
