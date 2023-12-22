package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeProcessMasterService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeProcessMaster;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 级联入云对接进度
 *
 * @author : linlx
 * @date : 2022 3 24 9:43
 */

@RestController
@RequestMapping("v1/cascade/process-master")
@Api(value = "cascadeProcessMaster", tags = {"v1", "级联入云相关", "级联入云对接进度"})
@Inner
@Slf4j
public class CascadeProcessMasterController {

    @Resource
    private CascadeProcessMasterService cascadeProcessMasterService;


    /**
     * 新增级联入云对接进度
     *
     * @param model 级联入云对接进度
     * @return R 返回新增后的级联入云对接进度
     */
    @AutoInject
    @ApiOperation(value = "新增级联入云对接进度", notes = "新增级联入云对接进度", hidden = true)
    @SysLog("新增级联入云对接进度")
    @PostMapping
    public R<EdgeCascadeProcessMaster> save(@RequestBody OpenApiModel<EdgeCascadeProcessMaster> model) {
        log.info("[CascadeProcessMasterController - save]: 新增级联入云对接进度, model={}", JSONConvertUtils.objectToString(model));
        return cascadeProcessMasterService.save(model.getData());
    }

    /**
     * 修改级联入云对接进度
     *
     * @param model 级联入云对接进度
     * @return R 返回修改后的级联入云对接进度
     */
    @AutoInject
    @ApiOperation(value = "修改级联入云对接进度", notes = "修改级联入云对接进度", hidden = true)
    @SysLog("修改级联入云对接进度")
    @PutMapping
    public R<EdgeCascadeProcessMaster> update(@RequestBody OpenApiModel<EdgeCascadeProcessMaster> model) {
        log.info("[CascadeProcessMasterController - update]: 修改级联入云对接进度, model={}", JSONConvertUtils.objectToString(model));
        return cascadeProcessMasterService.update(model.getData());
    }
}
