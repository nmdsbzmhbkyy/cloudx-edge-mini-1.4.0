package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaOpenLaneHisService;
import com.aurine.cloudx.open.origin.entity.ProjectOpenLaneHis;
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
 * 开关闸记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/open-lane-his")
@Api(value = "metaOpenLaneHis", tags = {"v1", "开关闸记录管理"}, hidden = true)
@Inner
@Slf4j
public class MetaOpenLaneHisController {

    @Resource
    private MetaOpenLaneHisService metaOpenLaneHisService;


    /**
     * 新增开关闸记录
     *
     * @param model 开关闸记录
     * @return R 返回新增后的开关闸记录
     */
    @AutoInject
    @ApiOperation(value = "新增开关闸记录", notes = "新增开关闸记录", hidden = true)
    @SysLog("新增开关闸记录")
    @PostMapping
    public R<ProjectOpenLaneHis> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectOpenLaneHis> model) {
        log.info("[MetaOpenLaneHisController - save]: 新增开关闸记录, model={}", JSONConvertUtils.objectToString(model));
        return metaOpenLaneHisService.save(model.getData());
    }

    /**
     * 修改开关闸记录
     *
     * @param model 开关闸记录
     * @return R 返回修改后的开关闸记录
     */
    @AutoInject
    @ApiOperation(value = "修改开关闸记录", notes = "修改开关闸记录", hidden = true)
    @SysLog("通过id修改开关闸记录")
    @PutMapping
    public R<ProjectOpenLaneHis> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectOpenLaneHis> model) {
        log.info("[MetaOpenLaneHisController - update]: 修改开关闸记录, model={}", JSONConvertUtils.objectToString(model));
        return metaOpenLaneHisService.update(model.getData());
    }

    /**
     * 通过id删除开关闸记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除开关闸记录", notes = "通过id删除开关闸记录", hidden = true)
    @SysLog("通过id删除开关闸记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaOpenLaneHisController - delete]: 通过id删除开关闸记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaOpenLaneHisService.delete(id);
    }
}
