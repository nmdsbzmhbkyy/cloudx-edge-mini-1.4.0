package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenRightDeviceRelService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.RightDeviceRelVo;
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
 * 权限设备关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/right-device-rel")
@Api(value = "openRightDeviceRel", tags = {"v1", "权限设备关系管理"})
@Inner
@Slf4j
public class OpenRightDeviceRelController {

    @Resource
    private OpenRightDeviceRelService openRightDeviceRelService;


    /**
     * 通过id查询权限设备关系
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回权限设备关系信息
     */
    @ApiOperation(value = "通过id查询权限设备关系", notes = "通过id查询权限设备关系")
    @SysLog("通过id查询权限设备关系")
    @GetMapping("/{id}")
    public R<RightDeviceRelVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenRightDeviceController - getById]: 通过id查询权限设备关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openRightDeviceRelService.getById(id);
    }

    /**
     * 分页查询权限设备关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回权限设备关系分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询权限设备关系", notes = "分页查询权限设备关系")
    @SysLog("分页查询权限设备关系")
    @PostMapping("/page")
    public R<Page<RightDeviceRelVo>> page(@Validated @RequestBody OpenApiPageModel<RightDeviceRelVo> pageModel) {
        log.info("[OpenRightDeviceController - page]: 分页查询权限设备关系, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openRightDeviceRelService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回新增后的权限设备关系
     */
    @AutoInject
    @ApiOperation(value = "新增权限设备关系", notes = "新增权限设备关系")
    @SysLog("新增权限设备关系")
    @PostMapping
    public R<RightDeviceRelVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<RightDeviceRelVo> model) {
        log.info("[OpenRightDeviceController - save]: 新增权限设备关系, model={}", JSONConvertUtils.objectToString(model));
        return openRightDeviceRelService.save(model.getData());
    }

    /**
     * 修改权限设备关系
     *
     * @param model 权限设备关系
     * @return R 返回修改后的权限设备关系
     */
    @AutoInject
    @ApiOperation(value = "修改权限设备关系", notes = "修改权限设备关系")
    @SysLog("通过id修改权限设备关系")
    @PutMapping
    public R<RightDeviceRelVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<RightDeviceRelVo> model) {
        log.info("[OpenRightDeviceController - update]: 修改权限设备关系, model={}", JSONConvertUtils.objectToString(model));
        return openRightDeviceRelService.update(model.getData());
    }

    /**
     * 通过id删除权限设备关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除权限设备关系", notes = "通过id删除权限设备关系")
    @SysLog("通过id删除权限设备关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenRightDeviceController - delete]: 通过id删除权限设备关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openRightDeviceRelService.delete(id);
    }
}
