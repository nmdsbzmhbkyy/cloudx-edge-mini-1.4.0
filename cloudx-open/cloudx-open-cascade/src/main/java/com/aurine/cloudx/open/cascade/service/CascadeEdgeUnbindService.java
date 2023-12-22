package com.aurine.cloudx.open.cascade.service;

import com.pig4cloud.pigx.common.core.util.R;

/**
 * 级联解绑业务
 *
 * @author : Qiu
 * @date : 2021 12 24 16:36
 */

public interface CascadeEdgeUnbindService {

    /**
     * 申请级联解绑
     * （从网关 -> 主网关）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> apply(String projectCode);

    /**
     * 撤销级联解绑
     * （从网关 -> 主网关）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> revoke(String projectCode);

    /**
     * 同意级联解绑
     * （主网关 -> 从网关）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> accept(String projectCode);

    /**
     * 拒绝级联解绑
     * （主网关 -> 从网关）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> reject(String projectCode);
}
