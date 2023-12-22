package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParCarRegisterService;
import com.aurine.cloudx.open.origin.entity.ProjectParCarRegister;
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
 * 车辆登记管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/par-car-register")
@Api(value = "metaParCarRegister", tags = {"v1", "车辆登记管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParCarRegisterController {

    @Resource
    private MetaParCarRegisterService metaParCarRegisterService;


    /**
     * 新增车辆登记
     *
     * @param model 车辆登记
     * @return R 返回新增后的车辆登记
     */
    @AutoInject
    @ApiOperation(value = "新增车辆登记", notes = "新增车辆登记", hidden = true)
    @SysLog("新增车辆登记")
    @PostMapping
    public R<ProjectParCarRegister> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParCarRegister> model) {
        log.info("[MetaParCarRegisterController - save]: 新增车辆登记, model={}", JSONConvertUtils.objectToString(model));
        return metaParCarRegisterService.save(model.getData());
    }

    /**
     * 修改车辆登记
     *
     * @param model 车辆登记
     * @return R 返回修改后的车辆登记
     */
    @AutoInject
    @ApiOperation(value = "修改车辆登记", notes = "修改车辆登记", hidden = true)
    @SysLog("通过id修改车辆登记")
    @PutMapping
    public R<ProjectParCarRegister> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParCarRegister> model) {
        log.info("[MetaParCarRegisterController - update]: 修改车辆登记, model={}", JSONConvertUtils.objectToString(model));
        return metaParCarRegisterService.update(model.getData());
    }

    /**
     * 通过id删除车辆登记
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车辆登记", notes = "通过id删除车辆登记", hidden = true)
    @SysLog("通过id删除车辆登记")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParCarRegisterController - delete]: 通过id删除车辆登记, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParCarRegisterService.delete(id);
    }
}
