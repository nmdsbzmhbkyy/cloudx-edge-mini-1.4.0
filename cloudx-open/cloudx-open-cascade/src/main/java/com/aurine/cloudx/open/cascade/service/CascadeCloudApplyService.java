package com.aurine.cloudx.open.cascade.service;

import com.pig4cloud.pigx.common.core.util.R;

/**
 * 入云申请业务
 *
 * @author : Qiu
 * @date : 2021 12 24 16:36
 */

public interface CascadeCloudApplyService {

    /**
     * 撤销入云申请
     * （边缘侧 -> 平台侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> revoke(String projectUUID, String projectCode);

    /**
     * 同意入云申请
     * （平台侧 -> 边缘侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> accept(String projectCode);

    /**
     * 拒绝入云申请
     * （平台侧 -> 边缘侧）
     *
     * @param projectCode
     * @return
     */
    R<Boolean> reject(String projectCode);
}
