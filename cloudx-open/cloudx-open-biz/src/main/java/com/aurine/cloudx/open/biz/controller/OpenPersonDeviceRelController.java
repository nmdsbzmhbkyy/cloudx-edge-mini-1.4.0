package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPersonDeviceRelService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonDeviceRelVo;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * 人员设备权限关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/person-device-rel")
@Api(value = "openPersonDeviceRel", tags = {"v1", "通行方案相关", "人员设备权限关系管理"})
@Inner
@Slf4j
public class OpenPersonDeviceRelController {

    @Resource
    private OpenPersonDeviceRelService openPersonDeviceRelService;


    /**
     * 通过id查询人员设备权限关系
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回人员设备权限关系信息
     */
    @ApiOperation(value = "通过id查询人员设备权限关系", notes = "通过id查询人员设备权限关系")
    @SysLog("通过id查询人员设备权限关系")
    @GetMapping("/{id}")
    public R<PersonDeviceRelVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonDeviceRelController - getById]: 通过id查询人员设备权限关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonDeviceRelService.getById(id);
    }

    /**
     * 分页查询人员设备权限关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回人员设备权限关系分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询人员设备权限关系", notes = "分页查询人员设备权限关系")
    @SysLog("分页查询人员设备权限关系")
    @PostMapping("/page")
    public R<Page<PersonDeviceRelVo>> page(@Validated @RequestBody OpenApiPageModel<PersonDeviceRelVo> pageModel) {
        log.info("[OpenPersonDeviceRelController - page]: 分页查询人员设备权限关系, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPersonDeviceRelService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回新增后的人员设备权限关系
     */
    @AutoInject
    @ApiOperation(value = "新增人员设备权限关系", notes = "新增人员设备权限关系")
    @SysLog("新增人员设备权限关系")
    @PostMapping
    public R<PersonDeviceRelVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PersonDeviceRelVo> model) {
        log.info("[OpenPersonDeviceRelController - save]: 新增人员设备权限关系, model={}", JSONConvertUtils.objectToString(model));
        return openPersonDeviceRelService.save(model.getData());
    }

    /**
     * 修改人员设备权限关系
     *
     * @param model 人员设备权限关系
     * @return R 返回修改后的人员设备权限关系
     */
    @AutoInject
    @ApiOperation(value = "修改人员设备权限关系", notes = "修改人员设备权限关系")
    @SysLog("通过id修改人员设备权限关系")
    @PutMapping
    public R<PersonDeviceRelVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PersonDeviceRelVo> model) {
        log.info("[OpenPersonDeviceRelController - update]: 修改人员设备权限关系, model={}", JSONConvertUtils.objectToString(model));
        return openPersonDeviceRelService.update(model.getData());
    }

    /**
     * 通过id删除人员设备权限关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员设备权限关系", notes = "通过id删除人员设备权限关系")
    @SysLog("通过id删除人员设备权限关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonDeviceRelController - delete]: 通过id删除人员设备权限关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonDeviceRelService.delete(id);
    }
}
