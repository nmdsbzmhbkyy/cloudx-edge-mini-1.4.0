package com.aurine.cloudx.open.push.service;

import com.aurine.cloudx.open.common.entity.entity.OpenPushSubscribeCallback;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * 推送订阅管理接口
 *
 * @author : Qiu
 * @date : 2021 12 07 10:32
 */
public interface PushSubscribeService extends IService<OpenPushSubscribeCallback> {

    /**
     * 根据ID获取订阅回调对象
     *
     * @param id
     * @return
     */
    R<OpenPushSubscribeCallbackVo> getById(String id);

    /**
     * 获取订阅回调对象列表
     *
     * @param vo
     * @return
     */
    R<List<OpenPushSubscribeCallbackVo>> getList(OpenPushSubscribeCallbackVo vo);

    /**
     * 根据回调类型和项目UUID获取订阅回调对象列表
     *
     * @param callbackType 回调类型
     * @param projectUUID  项目UUID
     * @return
     */
    R<List<OpenPushSubscribeCallbackVo>> getListByServiceTypeAndProjectUUID(String callbackType, String projectUUID);

    /**
     * 分页查询订阅回调对象
     *
     * @param page
     * @param vo
     * @return
     */
    R<Page<OpenPushSubscribeCallbackVo>> page(Page page, OpenPushSubscribeCallbackVo vo);

    /**
     * 添加订阅回调对象
     *
     * @param vo
     * @return
     */
    R<OpenPushSubscribeCallbackVo> save(OpenPushSubscribeCallbackVo vo);

    /**
     * 批量新增订阅信息
     *
     * @param voList
     * @return
     */
    R<List<OpenPushSubscribeCallbackVo>> saveBatch(List<OpenPushSubscribeCallbackVo> voList);

    /**
     * 修改订阅回调
     *
     * @param vo
     * @return
     */
    R<OpenPushSubscribeCallbackVo> update(OpenPushSubscribeCallbackVo vo);

    /**
     * 通过id删除订阅
     *
     * @param id
     * @return
     */
    R<Boolean> delete(String id);

    /**
     * 通过应用ID删除订阅
     *
     * @param appId
     * @return
     */
    R<Boolean> deleteByAppId(String appId);

    /**
     * 通过应用ID、回调类型、项目UUID删除订阅
     *
     * @param appId
     * @param callbackType
     * @param projectUUID
     * @return
     */
    R<Boolean> delete(String appId, String callbackType, String projectUUID);

    /**
     * 刷新订阅缓存
     *
     * @return
     */
    R<Boolean> clearCache();
}
