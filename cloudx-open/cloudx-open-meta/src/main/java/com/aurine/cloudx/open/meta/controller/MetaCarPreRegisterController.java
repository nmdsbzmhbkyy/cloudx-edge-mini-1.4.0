package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaCarPreRegisterService;
import com.aurine.cloudx.open.origin.entity.ProjectCarPreRegister;
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
 * 车辆登记记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/car-pre-register")
@Api(value = "metaCarPreRegister", tags = {"v1", "车辆登记记录管理"}, hidden = true)
@Inner
@Slf4j
public class MetaCarPreRegisterController {

    @Resource
    private MetaCarPreRegisterService metaCarPreRegisterService;


    /**
     * 新增车辆登记记录
     *
     * @param model 车辆登记记录
     * @return R 返回新增后的车辆登记记录
     */
    @AutoInject
    @ApiOperation(value = "新增车辆登记记录", notes = "新增车辆登记记录", hidden = true)
    @SysLog("新增车辆登记记录")
    @PostMapping
    public R<ProjectCarPreRegister> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectCarPreRegister> model) {
        log.info("[MetaCarPreRegisterController - save]: 新增车辆登记记录, model={}", JSONConvertUtils.objectToString(model));
        return metaCarPreRegisterService.save(model.getData());
    }

    /**
     * 修改车辆登记记录
     *
     * @param model 车辆登记记录
     * @return R 返回修改后的车辆登记记录
     */
    @AutoInject
    @ApiOperation(value = "修改车辆登记记录", notes = "修改车辆登记记录", hidden = true)
    @SysLog("通过id修改车辆登记记录")
    @PutMapping
    public R<ProjectCarPreRegister> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectCarPreRegister> model) {
        log.info("[MetaCarPreRegisterController - update]: 修改车辆登记记录, model={}", JSONConvertUtils.objectToString(model));
        return metaCarPreRegisterService.update(model.getData());
    }

    /**
     * 通过id删除车辆登记记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除车辆登记记录", notes = "通过id删除车辆登记记录", hidden = true)
    @SysLog("通过id删除车辆登记记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaCarPreRegisterController - delete]: 通过id删除车辆登记记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaCarPreRegisterService.delete(id);
    }
}
