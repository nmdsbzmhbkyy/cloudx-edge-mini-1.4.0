package com.aurine.cloudx.open.meta.service;

import com.aurine.cloudx.open.origin.entity.ProjectParkBillingInfo;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * open平台-缴费记录管理
 *
 * @author : Qiu
 * @date : 2021 12 28 9:49
 */

public interface MetaParkBillingInfoService {

    /**
     * 新增缴费记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkBillingInfo> save(ProjectParkBillingInfo po);

    /**
     * 修改缴费记录
     *
     * @param po
     * @return
     * @author:
     */
    R<ProjectParkBillingInfo> update(ProjectParkBillingInfo po);

    /**
     * 删除缴费记录
     *
     * @param id
     * @return
     * @author:
     */
    R<Boolean> delete(String id);

}
