package com.aurine.cloudx.open.meta.controller;

import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.aurine.cloudx.open.meta.service.MetaParkBillingInfoService;
import com.aurine.cloudx.open.origin.entity.ProjectParkBillingInfo;
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
 * 缴费记录管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/meta/park-billing-info")
@Api(value = "metaParkBillingInfo", tags = {"v1", "缴费记录管理"}, hidden = true)
@Inner
@Slf4j
public class MetaParkBillingInfoController {

    @Resource
    private MetaParkBillingInfoService metaParkBillingInfoService;


    /**
     * 新增缴费记录
     *
     * @param model 缴费记录
     * @return R 返回新增后的缴费记录
     */
    @AutoInject
    @ApiOperation(value = "新增缴费记录", notes = "新增缴费记录", hidden = true)
    @SysLog("新增缴费记录")
    @PostMapping
    public R<ProjectParkBillingInfo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectParkBillingInfo> model) {
        log.info("[MetaParkBillingInfoController - save]: 新增缴费记录, model={}", JSONConvertUtils.objectToString(model));
        return metaParkBillingInfoService.save(model.getData());
    }

    /**
     * 修改缴费记录
     *
     * @param model 缴费记录
     * @return R 返回修改后的缴费记录
     */
    @AutoInject
    @ApiOperation(value = "修改缴费记录", notes = "修改缴费记录", hidden = true)
    @SysLog("通过id修改缴费记录")
    @PutMapping
    public R<ProjectParkBillingInfo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectParkBillingInfo> model) {
        log.info("[MetaParkBillingInfoController - update]: 修改缴费记录, model={}", JSONConvertUtils.objectToString(model));
        return metaParkBillingInfoService.update(model.getData());
    }

    /**
     * 通过id删除缴费记录
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除缴费记录", notes = "通过id删除缴费记录", hidden = true)
    @SysLog("通过id删除缴费记录")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[MetaParkBillingInfoController - delete]: 通过id删除缴费记录, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return metaParkBillingInfoService.delete(id);
    }
}
