package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */

@FeignClient(contextId = "remoteMetaCardHis", value = "cloudx-open-biz")
public interface RemoteMetaCardHisService {

    /**
     * 新增卡操作记录
     *
     * @param model 卡操作记录
     * @return R 返回新增后的卡操作记录
     */
    @PostMapping("/v1/meta/card-his")
    R<ProjectCardHis> save(@RequestBody OpenApiModel<ProjectCardHis> model);

    /**
     * 修改卡操作记录
     *
     * @param model 卡操作记录
     * @return R 返回修改后卡操作记录
     */
    @PutMapping("/v1/meta/card-his")
    R<ProjectCardHis> update(@RequestBody OpenApiModel<ProjectCardHis> model);


}