package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.meta.service.MetaCardInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
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
 * 卡信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/card-info")
@Api(value = "metaCardInfo", tags = {"v1", "卡信息管理"}, hidden = true)
@Inner
@Slf4j
public class MetaCardInfoController {

    @Resource
    private MetaCardInfoService metaCardInfoService;


    /**
     * 新增卡信息
     *
     * @param model 卡信息
     * @return R 返回新增后的卡信息
     */
    @AutoInject
    @ApiOperation(value = "新增卡信息", notes = "新增卡信息", hidden = true)
    @SysLog("新增卡信息")
    @PostMapping
    public R<ProjectCard> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectCard> model) {
        log.info("[MetaCardInfoController - save]: 新增卡信息, model={}", JSONConvertUtils.objectToString(model));
        return metaCardInfoService.save(model.getData());
    }

    /**
     * 修改卡信息
     *
     * @param model 卡信息
     * @return R 返回修改后的卡信息
     */
    @AutoInject
    @ApiOperation(value = "修改卡信息", notes = "修改卡信息", hidden = true)
    @SysLog("通过id修改卡信息")
    @PutMapping
    public R<ProjectCard> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectCard> model) {
        log.info("[MetaCardInfoController - update]: 修改卡信息, model={}", JSONConvertUtils.objectToString(model));
        return metaCardInfoService.update(model.getData());
    }

    /**
     * 通过id删除卡信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除卡信息", notes = "通过id删除卡信息", hidden = true)
    @SysLog("通过id删除卡信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaCardInfoController - delete]: 通过id删除卡信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaCardInfoService.delete(id);
    }
}
