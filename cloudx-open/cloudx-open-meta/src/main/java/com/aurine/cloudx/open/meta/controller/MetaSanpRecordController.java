package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaSnapRecordService;
import com.aurine.cloudx.open.origin.entity.ProjectSnapRecord;
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
 * 抓拍记录
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/snap-record")
@Api(value = "metaSnapRecord", tags = {"v1", "框架数据相关", "抓拍记录"}, hidden = true)
@Inner
@Slf4j
public class MetaSanpRecordController {

    @Resource
    private MetaSnapRecordService metaSnapRecordService;


    /**
     * 新增抓拍记录
     *
     * @param model 抓拍记录
     * @return R 返回新增后的抓拍记录
     */
    @AutoInject
    @ApiOperation(value = "新增抓拍记录", notes = "新增抓拍记录", hidden = true)
    @SysLog("新增抓拍记录")
    @PostMapping
    public R<ProjectSnapRecord> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectSnapRecord> model) {
        log.info("[MetaSanpRecordController - save]: 新增房屋信息, model={}", JSONConvertUtils.objectToString(model));
        return metaSnapRecordService.save(model.getData());
    }

    /**
     * 修改抓拍记录
     *
     * @param model 抓拍记录
     * @return R 返回修改后的抓拍记录
     */
    @AutoInject
    @ApiOperation(value = "修改房屋信息", notes = "修改抓拍记录", hidden = true)
    @SysLog("通过id修改房屋信息")
    @PutMapping
    public R<ProjectSnapRecord> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectSnapRecord> model) {
        log.info("[MetaSanpRecordController - update]: 修改抓拍记录, model={}", JSONConvertUtils.objectToString(model));
        return metaSnapRecordService.update(model.getData());
    }

    /**
     * 通过id删除抓拍记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除抓拍记录", notes = "通过id删除抓拍记录", hidden = true)
    @SysLog("通过id删除抓拍记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaSanpRecordController - delete]: 通过id删除抓拍记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaSnapRecordService.delete(id);
    }
}
