package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkEntranceHisService;
import com.aurine.cloudx.open.origin.entity.ProjectParkEntranceHis;
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
 * 车行记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/park-entrance-his")
@Api(value = "metaParkEntranceHis", tags = {"v1", "车行记录管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkEntranceHisController {

    @Resource
    private MetaParkEntranceHisService metaParkEntranceHisService;


    /**
     * 新增车行记录
     *
     * @param model 车行记录
     * @return R 返回新增后的车行记录
     */
    @AutoInject
    @ApiOperation(value = "新增车行记录", notes = "新增车行记录", hidden = true)
    @SysLog("新增车行记录")
    @PostMapping
    public R<ProjectParkEntranceHis> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkEntranceHis> model) {
        log.info("[MetaParkEntranceHisController - save]: 新增车行记录, model={}", JSONConvertUtils.objectToString(model));
        return metaParkEntranceHisService.save(model.getData());
    }

    /**
     * 修改车行记录
     *
     * @param model 车行记录
     * @return R 返回修改后的车行记录
     */
    @AutoInject
    @ApiOperation(value = "修改车行记录", notes = "修改车行记录", hidden = true)
    @SysLog("通过id修改车行记录")
    @PutMapping
    public R<ProjectParkEntranceHis> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkEntranceHis> model) {
        log.info("[MetaParkEntranceHisController - update]: 修改车行记录, model={}", JSONConvertUtils.objectToString(model));
        return metaParkEntranceHisService.update(model.getData());
    }

    /**
     * 通过id删除车行记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车行记录", notes = "通过id删除车行记录", hidden = true)
    @SysLog("通过id删除车行记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkEntranceHisController - delete]: 通过id删除车行记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkEntranceHisService.delete(id);
    }
}
