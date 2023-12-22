package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkingPlaceService;
import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
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
 * 车位管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/parking-place")
@Api(value = "metaParkingPlace", tags = {"v1", "车位管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkingPlaceController {

    @Resource
    private MetaParkingPlaceService metaParkingPlaceService;


    /**
     * 新增车位
     *
     * @param model 车位
     * @return R 返回新增后的车位
     */
    @AutoInject
    @ApiOperation(value = "新增车位", notes = "新增车位", hidden = true)
    @SysLog("新增车位")
    @PostMapping
    public R<ProjectParkingPlace> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkingPlace> model) {
        log.info("[MetaParkingPlaceController - save]: 新增车位, model={}", JSONConvertUtils.objectToString(model));
        return metaParkingPlaceService.save(model.getData());
    }

    /**
     * 修改车位
     *
     * @param model 车位
     * @return R 返回修改后的车位
     */
    @AutoInject
    @ApiOperation(value = "修改车位", notes = "修改车位", hidden = true)
    @SysLog("通过id修改车位")
    @PutMapping
    public R<ProjectParkingPlace> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkingPlace> model) {
        log.info("[MetaParkingPlaceController - update]: 修改车位, model={}", JSONConvertUtils.objectToString(model));
        return metaParkingPlaceService.update(model.getData());
    }

    /**
     * 通过id删除车位
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车位", notes = "通过id删除车位", hidden = true)
    @SysLog("通过id删除车位")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkingPlaceController - delete]: 通过id删除车位, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkingPlaceService.delete(id);
    }
}
