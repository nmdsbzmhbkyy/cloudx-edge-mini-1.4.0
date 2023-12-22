package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaHouseInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
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
 * 房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/house-info")
@Api(value = "metaHouseInfo", tags = {"v1", "框架数据相关", "房屋信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaHouseInfoController {

    @Resource
    private MetaHouseInfoService metaHouseInfoService;


    /**
     * 新增房屋信息
     *
     * @param model 房屋信息
     * @return R 返回新增后的房屋信息
     */
    @AutoInject
    @ApiOperation(value = "新增房屋信息", notes = "新增房屋信息", hidden = true)
    @SysLog("新增房屋信息")
    @PostMapping
    public R<ProjectHouseInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectHouseInfo> model) {
        log.info("[MetaHouseInfoController - save]: 新增房屋信息, model={}", JSONConvertUtils.objectToString(model));
        return metaHouseInfoService.save(model.getData());
    }

    /**
     * 修改房屋信息
     *
     * @param model 房屋信息
     * @return R 返回修改后的房屋信息
     */
    @AutoInject
    @ApiOperation(value = "修改房屋信息", notes = "修改房屋信息", hidden = true)
    @SysLog("通过id修改房屋信息")
    @PutMapping
    public R<ProjectHouseInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectHouseInfo> model) {
        log.info("[MetaHouseInfoController - update]: 修改房屋信息, model={}", JSONConvertUtils.objectToString(model));
        return metaHouseInfoService.update(model.getData());
    }

    /**
     * 通过id删除房屋信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除房屋信息", notes = "通过id删除房屋信息", hidden = true)
    @SysLog("通过id删除房屋信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaHouseInfoController - delete]: 通过id删除房屋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaHouseInfoService.delete(id);
    }
}
