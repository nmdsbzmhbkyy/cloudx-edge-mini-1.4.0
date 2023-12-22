package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaFaceInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectFaceResources;
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
 * 人脸信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/face-info")
@Api(value = "metaFaceInfo", tags = {"v1", "人脸信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaFaceInfoController {

    @Resource
    private MetaFaceInfoService metaFaceInfoService;


    /**
     * 新增人脸信息
     *
     * @param model 人脸信息
     * @return R 返回新增后的人脸信息
     */
    @AutoInject
    @ApiOperation(value = "新增人脸信息", notes = "新增人脸信息", hidden = true)
    @SysLog("新增人脸信息")
    @PostMapping
    public R<ProjectFaceResources> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectFaceResources> model) {
        log.info("[MetaFaceInfoController - save]: 新增人脸信息, model={}", JSONConvertUtils.objectToString(model));
        return metaFaceInfoService.save(model.getData());
    }

    /**
     * 修改人脸信息
     *
     * @param model 人脸信息
     * @return R 返回修改后的人脸信息
     */
    @AutoInject
    @ApiOperation(value = "修改人脸信息", notes = "修改人脸信息", hidden = true)
    @SysLog("通过id修改人脸信息")
    @PutMapping
    public R<ProjectFaceResources> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectFaceResources> model) {
        log.info("[MetaFaceInfoController - update]: 修改人脸信息, model={}", JSONConvertUtils.objectToString(model));
        return metaFaceInfoService.update(model.getData());
    }

    /**
     * 通过id删除人脸信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人脸信息", notes = "通过id删除人脸信息", hidden = true)
    @SysLog("通过id删除人脸信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaFaceInfoController - delete]: 通过id删除人脸信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaFaceInfoService.delete(id);
    }
}
