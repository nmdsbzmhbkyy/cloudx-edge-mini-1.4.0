package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenVisitorInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.VisitorInfoVo;
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
 * 访客信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/visitor-info")
@Api(value = "openVisitorInfo", tags = {"v1", "访客信息管理"})
@Inner
@Slf4j
public class OpenVisitorInfoController {

    @Resource
    private OpenVisitorInfoService openVisitorInfoService;


    /**
     * 通过id查询访客信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回访客信息
     */
    @ApiOperation(value = "通过id查询访客信息", notes = "通过id查询访客信息")
    @SysLog("通过id查询访客信息")
    @GetMapping("/{id}")
    public R<VisitorInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenVisitorInfoController - getById]: 通过id查询访客信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openVisitorInfoService.getById(id);
    }

    /**
     * 分页查询访客信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回访客信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询访客信息", notes = "分页查询访客信息")
    @SysLog("分页查询访客信息")
    @PostMapping("/page")
    public R<Page<VisitorInfoVo>> page(@Validated @RequestBody OpenApiPageModel<VisitorInfoVo> pageModel) {
        log.info("[OpenVisitorInfoController - page]: 分页查询访客信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openVisitorInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增访客信息
     *
     * @param model 访客信息
     * @return R 返回新增后的访客信息
     */
    @AutoInject
    @ApiOperation(value = "新增访客信息", notes = "新增访客信息")
    @SysLog("新增访客信息")
    @PostMapping
    public R<VisitorInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<VisitorInfoVo> model) {
        log.info("[OpenVisitorInfoController - save]: 新增访客信息, model={}", JSONConvertUtils.objectToString(model));
        return openVisitorInfoService.save(model.getData());
    }

    /**
     * 修改访客信息
     *
     * @param model 访客信息
     * @return R 返回修改后的访客信息
     */
    @AutoInject
    @ApiOperation(value = "修改访客信息", notes = "修改访客信息")
    @SysLog("通过id修改访客信息")
    @PutMapping
    public R<VisitorInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<VisitorInfoVo> model) {
        log.info("[OpenVisitorInfoController - update]: 修改访客信息, model={}", JSONConvertUtils.objectToString(model));
        return openVisitorInfoService.update(model.getData());
    }

    /**
     * 通过id删除访客信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除访客信息", notes = "通过id删除访客信息")
    @SysLog("通过id删除访客信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenVisitorInfoController - delete]: 通过id删除访客信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openVisitorInfoService.delete(id);
    }
}
