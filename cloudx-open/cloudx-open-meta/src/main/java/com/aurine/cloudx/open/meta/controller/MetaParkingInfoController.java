package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkingInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
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
 * 车场信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/parking-info")
@Api(value = "metaParkingInfo", tags = {"v1", "车场信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkingInfoController {

    @Resource
    private MetaParkingInfoService metaParkingInfoService;


    /**
     * 新增车场信息
     *
     * @param model 车场信息
     * @return R 返回新增后的车场信息
     */
    @AutoInject
    @ApiOperation(value = "新增车场信息", notes = "新增车场信息", hidden = true)
    @SysLog("新增车场信息")
    @PostMapping
    public R<ProjectParkingInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkingInfo> model) {
        log.info("[MetaParkingInfoController - save]: 新增车场信息, model={}", JSONConvertUtils.objectToString(model));
        return metaParkingInfoService.save(model.getData());
    }

    /**
     * 修改车场信息
     *
     * @param model 车场信息
     * @return R 返回修改后的车场信息
     */
    @AutoInject
    @ApiOperation(value = "修改车场信息", notes = "修改车场信息", hidden = true)
    @SysLog("通过id修改车场信息")
    @PutMapping
    public R<ProjectParkingInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkingInfo> model) {
        log.info("[MetaParkingInfoController - update]: 修改车场信息, model={}", JSONConvertUtils.objectToString(model));
        return metaParkingInfoService.update(model.getData());
    }

    /**
     * 通过id删除车场信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车场信息", notes = "通过id删除车场信息", hidden = true)
    @SysLog("通过id删除车场信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkingInfoController - delete]: 通过id删除车场信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkingInfoService.delete(id);
    }
}
