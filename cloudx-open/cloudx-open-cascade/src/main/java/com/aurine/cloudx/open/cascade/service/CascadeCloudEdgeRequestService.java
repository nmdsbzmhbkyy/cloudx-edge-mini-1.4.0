package com.aurine.cloudx.open.cascade.service;

import com.aurine.cloudx.open.origin.entity.CloudEdgeRequest;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 边缘网关入云申请（云平台）
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

public interface CascadeCloudEdgeRequestService {

    /**
     * 边缘网关入云申请更新
     *
     * @param po
     * @return
     */
    R<CloudEdgeRequest> update(CloudEdgeRequest po);
}
