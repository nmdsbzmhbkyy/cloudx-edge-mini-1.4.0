package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonChangeHis;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * open平台-房屋人员变更日志
 *
 * @author zouyu
 */

@FeignClient(contextId = "remoteMetaHousePersonChangeHis", value = "cloudx-open-biz")
public interface RemoteMetaHousePersonChangeHisService {

    /**
     * 新增房屋人员变更日志
     *
     * @param model 房屋人员变更日志
     * @return R 返回新增后的房屋人员变更日志
     */
    @PostMapping("/v1/meta/house-person-change-his")
    R<ProjectHousePersonChangeHis> save(@RequestBody OpenApiModel<ProjectHousePersonChangeHis> model);


}
