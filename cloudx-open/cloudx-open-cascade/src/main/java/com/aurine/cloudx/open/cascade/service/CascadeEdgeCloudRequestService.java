package com.aurine.cloudx.open.cascade.service;

import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 边缘网关入云申请（边缘侧）
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface CascadeEdgeCloudRequestService {

    /**
     * 边缘网关入云申请更新
     *
     * @param po
     * @return
     */
    R<EdgeCloudRequest> update(EdgeCloudRequest po);
}
