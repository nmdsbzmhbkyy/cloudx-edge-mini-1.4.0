package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 推送订阅管理
 *
 * @author : Qiu
 * @date : 2021 12 07 10:31
 */

@FeignClient(contextId = "remotePushSubscribeService", value = "cloudx-open-biz")
public interface RemotePushSubscribeService {

    /**
     * 通过id查询推送订阅信息
     *
     * @param appId 应用ID
     * @param id    id
     * @return R 返回推送订阅信息
     */
    @GetMapping("/v1/push/subscribe/{id}")
    R<OpenPushSubscribeCallbackVo> getById(@RequestParam("appId") String appId, @PathVariable("id") String id);

    /**
     * 分页查询推送订阅
     *
     * @param pageModel 分页查询条件
     * @return R 返回推送订阅分页数据
     */
    @PostMapping("/v1/push/subscribe/page")
    R<Page<OpenPushSubscribeCallbackVo>> page(@RequestBody OpenApiPageModel<OpenPushSubscribeCallbackVo> pageModel);

    /**
     * 根据查询条件获取订阅列表
     *
     * @param model 查询条件
     * @return R 返回推送订阅列表
     */
    @PostMapping("/v1/push/subscribe/list")
    R<List<OpenPushSubscribeCallbackVo>> list(@RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model);

    /**
     * 新增订阅信息
     *
     * @param model 订阅信息对象
     * @return R 返回新增后的订阅信息对象
     */
    @PostMapping("/v1/push/subscribe")
    R<OpenPushSubscribeCallbackVo> save(@RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model);

    /**
     * 批量新增订阅信息
     *
     * @param model 订阅信息对象列表
     * @return R 返回新增后的订阅信息对象列表
     */
    @PostMapping("/v1/push/subscribe/batch")
    R<List<OpenPushSubscribeCallbackVo>> saveBatch(@RequestBody OpenApiModel<List<OpenPushSubscribeCallbackVo>> model);

    /**
     * 修改订阅信息
     *
     * @param model 订阅信息对象
     * @return R 返回修改后的订阅信息对象
     */
    @PutMapping("/v1/push/subscribe")
    R<OpenPushSubscribeCallbackVo> update(@RequestBody OpenApiModel<OpenPushSubscribeCallbackVo> model);

    /**
     * 通过id删除订阅
     *
     * @param appId 应用ID
     * @param id    id
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/push/subscribe/{id}")
    R<Boolean> delete(@RequestParam("appId") String appId, @PathVariable("id") String id);

    /**
     * 通过应用id、回调类型、项目UUID删除订阅
     *
     * @param appId        应用ID
     * @param callbackType 回调类型
     * @param projectUUID  项目UUID
     * @return R 返回删除结果
     */
    @DeleteMapping("/v1/push/subscribe/delete/{callbackType}/{projectUUID_}")
    R<Boolean> delete(@RequestParam("appId") String appId, @PathVariable("callbackType") String callbackType, @PathVariable("projectUUID_") String projectUUID);

    /**
     * 清空订阅缓存
     *
     * @param appId 应用ID
     * @return R 返回清空缓存的结果
     */
    @GetMapping("/v1/push/subscribe/clearCache")
    R<Boolean> clearCache(@RequestParam("appId") String appId);
}
