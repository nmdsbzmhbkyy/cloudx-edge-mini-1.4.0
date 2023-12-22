package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkRegionService;
import com.aurine.cloudx.open.origin.entity.ProjectParkRegion;
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
 * 车位区域管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/park-region")
@Api(value = "metaParkRegion", tags = {"v1", "车位区域管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkRegionController {

    @Resource
    private MetaParkRegionService metaParkRegionService;


    /**
     * 新增车位区域
     *
     * @param model 车位区域
     * @return R 返回新增后的车位区域
     */
    @AutoInject
    @ApiOperation(value = "新增车位区域", notes = "新增车位区域", hidden = true)
    @SysLog("新增车位区域")
    @PostMapping
    public R<ProjectParkRegion> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkRegion> model) {
        log.info("[MetaParkRegionController - save]: 新增车位区域, model={}", JSONConvertUtils.objectToString(model));
        return metaParkRegionService.save(model.getData());
    }

    /**
     * 修改车位区域
     *
     * @param model 车位区域
     * @return R 返回修改后的车位区域
     */
    @AutoInject
    @ApiOperation(value = "修改车位区域", notes = "修改车位区域", hidden = true)
    @SysLog("通过id修改车位区域")
    @PutMapping
    public R<ProjectParkRegion> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkRegion> model) {
        log.info("[MetaParkRegionController - update]: 修改车位区域, model={}", JSONConvertUtils.objectToString(model));
        return metaParkRegionService.update(model.getData());
    }

    /**
     * 通过id删除车位区域
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车位区域", notes = "通过id删除车位区域", hidden = true)
    @SysLog("通过id删除车位区域")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkRegionController - delete]: 通过id删除车位区域, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkRegionService.delete(id);
    }
}
