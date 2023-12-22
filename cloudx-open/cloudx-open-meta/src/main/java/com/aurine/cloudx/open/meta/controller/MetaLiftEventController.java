package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.meta.service.MetaLiftEventService;
import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 乘梯记录事件管理
 *
 * @author : zouyu
 * @date : 2022-07-18 09:39:48
 */

@RestController
@RequestMapping("/v1/meta/lift-event")
@Api(value = "metaLiftEvent", tags = {"v1", "乘梯记录事件管理"}, hidden = true)
@Inner
@Slf4j
public class MetaLiftEventController {

    @Resource
    private MetaLiftEventService metaLiftEventService;


    /**
     * 新增乘梯记录事件
     *
     * @param model 乘梯记录事件
     * @return R 返回新增后的乘梯记录事件
     */
    @AutoInject
    @ApiOperation(value = "新增乘梯记录事件", notes = "新增乘梯记录事件", hidden = true)
    @SysLog("新增设备关系")
    @PostMapping
    public R<ProjectLiftEvent> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectLiftEvent> model) {
        log.info("[MetaLiftEventController - save]: 新增设备关系, model={}", JSONConvertUtils.objectToString(model));
        return metaLiftEventService.save(model.getData());
    }
}
