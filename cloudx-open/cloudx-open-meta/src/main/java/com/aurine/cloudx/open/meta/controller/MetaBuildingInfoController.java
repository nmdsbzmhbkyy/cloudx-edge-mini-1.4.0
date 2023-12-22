package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaBuildingInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectBuildingInfo;
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
 * 楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/building-info")
@Api(value = "metaBuildingInfo", tags = {"v1", "框架数据相关", "楼栋信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaBuildingInfoController {

    @Resource
    private MetaBuildingInfoService metaBuildingInfoService;


    /**
     * 新增楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回新增后的楼栋信息
     */
    @AutoInject
    @ApiOperation(value = "新增楼栋信息", notes = "新增楼栋信息", hidden = true)
    @SysLog("新增楼栋信息")
    @PostMapping
    public R<ProjectBuildingInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectBuildingInfo> model) {
        log.info("[MetaBuildingInfoController - save]: 新增楼栋信息, model={}", JSONConvertUtils.objectToString(model));
        return metaBuildingInfoService.save(model.getData());
    }

    /**
     * 修改楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回修改后的楼栋信息
     */
    @AutoInject
    @ApiOperation(value = "修改楼栋信息", notes = "修改楼栋信息", hidden = true)
    @SysLog("通过id修改楼栋信息")
    @PutMapping
    public R<ProjectBuildingInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectBuildingInfo> model) {
        log.info("[MetaBuildingInfoController - update]: 修改楼栋信息, model={}", JSONConvertUtils.objectToString(model));
        return metaBuildingInfoService.update(model.getData());
    }

    /**
     * 通过id删除楼栋信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除楼栋信息", notes = "通过id删除楼栋信息", hidden = true)
    @SysLog("通过id删除楼栋信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaBuildingInfoController - delete]: 通过id删除楼栋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaBuildingInfoService.delete(id);
    }
}
