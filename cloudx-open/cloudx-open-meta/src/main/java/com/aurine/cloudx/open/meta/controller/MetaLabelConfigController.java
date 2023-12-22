package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaLabelConfigService;
import com.aurine.cloudx.open.origin.entity.ProjectLabelConfig;
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
 * open平台-人员标签管理
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@RestController
@RequestMapping("/v1/meta/label-config")
@Api(value = "metaLabelConfig", tags = {"v1", "基础数据相关", "人员标签管理"}, hidden = true)
@Inner
@Slf4j
public class MetaLabelConfigController {

    @Resource
    private MetaLabelConfigService metaLabelConfigService;


    /**
     * 新增人员标签
     *
     * @param model 人员标签
     * @return R 返回新增后的人员标签
     */
    @AutoInject
    @ApiOperation(value = "新增人员标签", notes = "新增人员标签", hidden = true)
    @SysLog("新增人员标签")
    @PostMapping
    public R<ProjectLabelConfig> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectLabelConfig> model) {
        log.info("[MetaLabelConfigController - save]: 新增人员标签, model={}", JSONConvertUtils.objectToString(model));
        return metaLabelConfigService.save(model.getData());
    }

    /**
     * 修改人员标签
     *
     * @param model 人员标签
     * @return R 返回修改后的人员标签
     */
    @AutoInject
    @ApiOperation(value = "修改人员标签", notes = "修改人员标签", hidden = true)
    @SysLog("修改人员标签")
    @PutMapping
    public R<ProjectLabelConfig> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectLabelConfig> model) {
        log.info("[MetaLabelConfigController - update]: 修改人员标签, model={}", JSONConvertUtils.objectToString(model));
        return metaLabelConfigService.update(model.getData());
    }

    /**
     * 删除人员标签
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员标签", notes = "通过id删除人员标签", hidden = true)
    @SysLog("通过id删除人员标签")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaLabelConfigController - delete]: 通过id删除人员标签, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaLabelConfigService.delete(id);
    }

}
