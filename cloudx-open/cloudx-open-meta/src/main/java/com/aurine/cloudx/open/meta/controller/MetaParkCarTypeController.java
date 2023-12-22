package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkCarTypeService;
import com.aurine.cloudx.open.origin.entity.ProjectParkCarType;
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
 * 车辆类型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/park-car-type")
@Api(value = "metaParkCarType", tags = {"v1", "车辆类型管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkCarTypeController {

    @Resource
    private MetaParkCarTypeService metaParkCarTypeService;


    /**
     * 新增车辆类型
     *
     * @param model 车辆类型
     * @return R 返回新增后的车辆类型
     */
    @AutoInject
    @ApiOperation(value = "新增车辆类型", notes = "新增车辆类型", hidden = true)
    @SysLog("新增车辆类型")
    @PostMapping
    public R<ProjectParkCarType> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkCarType> model) {
        log.info("[MetaParkCarTypeController - save]: 新增车辆类型, model={}", JSONConvertUtils.objectToString(model));
        return metaParkCarTypeService.save(model.getData());
    }

    /**
     * 修改车辆类型
     *
     * @param model 车辆类型
     * @return R 返回修改后的车辆类型
     */
    @AutoInject
    @ApiOperation(value = "修改车辆类型", notes = "修改车辆类型", hidden = true)
    @SysLog("通过id修改车辆类型")
    @PutMapping
    public R<ProjectParkCarType> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkCarType> model) {
        log.info("[MetaParkCarTypeController - update]: 修改车辆类型, model={}", JSONConvertUtils.objectToString(model));
        return metaParkCarTypeService.update(model.getData());
    }

    /**
     * 通过id删除车辆类型
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车辆类型", notes = "通过id删除车辆类型", hidden = true)
    @SysLog("通过id删除车辆类型")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkCarTypeController - delete]: 通过id删除车辆类型, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkCarTypeService.delete(id);
    }
}
