package com.aurine.cloudx.edge.sync.biz.service;

import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.pig4cloud.pigx.common.core.util.R;

import java.util.List;

/**
 * @Author: wrm
 * @Date: 2022/01/27 11:48
 * @Package: com.aurine.cloudx.edge.sync.biz.service
 * @Version: 1.0
 * @Remarks: open平台回调控制
 **/
public interface ScribeCallbackService {

    /**
     * 添加config订阅
     */
    R addConfigScribeCallback();

    /**
     * 根据uuid获取订阅信息
     * @param projectUUID
     * @return
     */
    List<OpenPushSubscribeCallbackVo> getScribeCallback(String projectUUID);

    /**
     * 添加Open平台回调地址
     *
     * @param projectUUIDList
     */
    void addScribeCallback(String... projectUUIDList);

    /**
     * 添加级联回调地址
     *
     * @param projectUUID
     */
    void addCascadeScribeCallback(String projectUUID);

    /**
     * 添加其他回调地址
     * @param projectUUID
     */
    void addScribeCallbackWithoutCascade(String projectUUID);

    /**
     * 删除Open平台回调地址
     *
     * @param uuidList
     */
    void deleteScribeCallback(String... uuidList);

    /**
     * 添加级联回调地址
     *
     * @param projectUUID
     */
    void deleteCascadeScribeCallback(String projectUUID);

}
