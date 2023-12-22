package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaCardHisService;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
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
 * 卡操作记录
 *
 * @author : zy
 * @date : 2022-11-14 11:25:20
 */
@RestController
@RequestMapping("/v1/meta/card-his")
@Api(value = "metaCardHis", tags = {"v1", "卡操作记录"}, hidden = true)
@Inner
@Slf4j
public class MetaCardHisController {

    @Resource
    private MetaCardHisService metaCardHisService;


    /**
     * 新增卡操作记录
     *
     * @param model 卡操作记录
     * @return R 返回新增后的卡操作记录
     */
    @AutoInject
    @ApiOperation(value = "新增卡操作记录", notes = "新增卡操作记录", hidden = true)
    @SysLog("新增卡操作记录")
    @PostMapping
    public R<ProjectCardHis> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectCardHis> model) {
        log.info("[MetaCardHisController - save]: 新增卡操作记录, model={}", JSONConvertUtils.objectToString(model));
        return metaCardHisService.save(model.getData());
    }

    /**
     * 修改卡操作记录
     *
     * @param model 卡信息
     * @return R 返回修改后的卡操作记录
     */
    @AutoInject
    @ApiOperation(value = "修改卡操作记录", notes = "修改卡操作记录", hidden = true)
    @SysLog("通过id修改卡操作记录")
    @PutMapping
    public R<ProjectCardHis> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectCardHis> model) {
        log.info("[MetaCardHisController - update]: 修改卡操作记录, model={}", JSONConvertUtils.objectToString(model));
        return metaCardHisService.update(model.getData());
    }

}
