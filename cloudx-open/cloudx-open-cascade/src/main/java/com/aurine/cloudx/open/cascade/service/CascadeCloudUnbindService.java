package com.aurine.cloudx.open.cascade.service;

import com.pig4cloud.pigx.common.core.util.R;

/**
 * 入云解绑业务
 *
 * @author : Qiu
 * @date : 2021 12 24 16:36
 */

public interface CascadeCloudUnbindService {

    /**
     * 申请入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> apply(String projectCode);

    /**
     * 撤销入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> revoke(String projectCode);

    /**
     * 同意入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> accept(String projectCode);

    /**
     * 拒绝入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> reject(String projectCode);
}
