package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeProcessMaster;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 级联入云对接进度
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@FeignClient(contextId = "remoteCascadeProcessMaster", value = "cloudx-open-biz")
public interface RemoteCascadeProcessMasterService {

    /**
     * 新增级联入云对接进度
     *
     * @param model
     * @return
     */
    @PostMapping("v1/cascade/process-master")
    R<EdgeCascadeProcessMaster> save(@RequestBody OpenApiModel<EdgeCascadeProcessMaster> model);

    /**
     * 修改级联入云对接进度
     *
     * @param model
     * @return
     */
    @PutMapping("v1/cascade/process-master")
    R<EdgeCascadeProcessMaster> update(@RequestBody OpenApiModel<EdgeCascadeProcessMaster> model);

}
