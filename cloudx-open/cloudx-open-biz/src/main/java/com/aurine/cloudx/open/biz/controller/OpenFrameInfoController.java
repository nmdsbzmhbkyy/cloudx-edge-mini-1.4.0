package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenFrameInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.FrameInfoVo;
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
 * 框架信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/frame-info")
@Api(value = "openFrameInfo", tags = {"v1", "框架数据相关", "框架信息管理"})
@Inner
@Slf4j
public class OpenFrameInfoController {

    @Resource
    private OpenFrameInfoService openFrameInfoService;


    /**
     * 通过id查询框架信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回框架信息
     */
    @ApiOperation(value = "通过id查询框架信息", notes = "通过id查询框架信息")
    @SysLog("通过id查询框架信息")
    @GetMapping("/{id}")
    public R<FrameInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenFrameInfoController - getById]: 通过id查询框架信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openFrameInfoService.getById(id);
    }

    /**
     * 分页查询框架信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回框架信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询框架信息", notes = "分页查询框架信息")
    @SysLog("分页查询框架信息")
    @PostMapping("/page")
    public R<Page<FrameInfoVo>> page(@Validated @RequestBody OpenApiPageModel<FrameInfoVo> pageModel) {
        log.info("[OpenFrameInfoController - page]: 分页查询框架信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openFrameInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增框架信息
     *
     * @param model 框架信息
     * @return R 返回新增后的框架信息
     */
    @AutoInject
    @ApiOperation(value = "新增框架信息", notes = "新增框架信息")
    @SysLog("新增框架信息")
    @PostMapping
    public R<FrameInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<FrameInfoVo> model) {
        log.info("[OpenFrameInfoController - save]: 新增框架信息, model={}", JSONConvertUtils.objectToString(model));
        return openFrameInfoService.save(model.getData());
    }

    /**
     * 修改框架信息
     *
     * @param model 框架信息
     * @return R 返回修改后的框架信息
     */
    @AutoInject
    @ApiOperation(value = "修改框架信息", notes = "修改框架信息")
    @SysLog("通过id修改框架信息")
    @PutMapping
    public R<FrameInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<FrameInfoVo> model) {
        log.info("[OpenFrameInfoController - update]: 修改框架信息, model={}", JSONConvertUtils.objectToString(model));
        return openFrameInfoService.update(model.getData());
    }

    /**
     * 通过id删除框架信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除框架信息", notes = "通过id删除框架信息")
    @SysLog("通过id删除框架信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenFrameInfoController - delete]: 通过id删除框架信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openFrameInfoService.delete(id);
    }
}
