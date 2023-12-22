package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaHousePersonInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
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
 * 住户信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/house-person-info")
@Api(value = "metaHousePersonInfo", tags = {"v1", "住户信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaHousePersonInfoController {

    @Resource
    private MetaHousePersonInfoService metaHousePersonInfoService;


    /**
     * 新增住户信息
     *
     * @param model 住户信息
     * @return R 返回新增后的住户信息
     */
    @AutoInject
    @ApiOperation(value = "新增住户信息", notes = "新增住户信息", hidden = true)
    @SysLog("新增住户信息")
    @PostMapping
    public R<ProjectHousePersonRel> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectHousePersonRel> model) {
        log.info("[MetaHousePersonInfoController - save]: 新增住户信息, model={}", JSONConvertUtils.objectToString(model));
        return metaHousePersonInfoService.save(model.getData());
    }

    /**
     * 修改住户信息
     *
     * @param model 住户信息
     * @return R 返回修改后的住户信息
     */
    @AutoInject
    @ApiOperation(value = "修改住户信息", notes = "修改住户信息", hidden = true)
    @SysLog("通过id修改住户信息")
    @PutMapping
    public R<ProjectHousePersonRel> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectHousePersonRel> model) {
        log.info("[MetaHousePersonInfoController - update]: 修改住户信息, model={}", JSONConvertUtils.objectToString(model));
        return metaHousePersonInfoService.update(model.getData());
    }

    /**
     * 通过id删除住户信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除住户信息", notes = "通过id删除住户信息", hidden = true)
    @SysLog("通过id删除住户信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaHousePersonInfoController - delete]: 通过id删除住户信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaHousePersonInfoService.delete(id);
    }
}
