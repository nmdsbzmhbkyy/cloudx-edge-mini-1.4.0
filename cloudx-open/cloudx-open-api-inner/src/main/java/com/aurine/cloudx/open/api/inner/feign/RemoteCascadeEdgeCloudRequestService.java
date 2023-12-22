package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.EdgeCloudRequest;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 边缘网关入云申请（边缘侧）
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@FeignClient(contextId = "remoteCascadeEdgeCloudRequest", value = "cloudx-open-biz")
public interface RemoteCascadeEdgeCloudRequestService {

    /**
     * 修改入云申请
     *
     * @param model 入云申请
     * @return 返回修改后的入云申请
     */
    @PutMapping("v1/cascade/edge-cloud-request")
    R<EdgeCloudRequest> update(@RequestBody OpenApiModel<EdgeCloudRequest> model);
}
