package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 乘梯事件记录
 *
 * @author : zy
 * @date : 2022-07-18 09:46:54
 */

@FeignClient(contextId = "remoteMetaLiftEvent", value = "cloudx-open-biz")
public interface RemoteMetaLiftEventService {

    /**
     * 新增乘梯事件记录
     *
     * @param model 乘梯事件记录
     * @return R 返回新增后的乘梯事件记录
     */
    @PostMapping("/v1/meta/lift-event")
    R<ProjectLiftEvent> save(@RequestBody OpenApiModel<ProjectLiftEvent> model);
}