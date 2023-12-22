package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenHouseDesignService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HouseDesignVo;
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
 * 户型管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/house-design")
@Api(value = "openHouseDesign", tags = {"v1", "基础数据相关", "户型管理"})
@Inner
@Slf4j
public class OpenHouseDesignController {

    @Resource
    private OpenHouseDesignService openHouseDesignService;


    /**
     * 通过id查询户型
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回户型信息
     */
    @ApiOperation(value = "通过id查询户型", notes = "通过id查询户型")
    @SysLog("通过id查询户型")
    @GetMapping("/{id}")
    public R<HouseDesignVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHouseDesignController - getById]: 通过id查询户型, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHouseDesignService.getById(id);
    }

    /**
     * 分页查询户型
     *
     * @param pageModel 分页查询条件
     * @return R 返回户型分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询户型", notes = "分页查询户型")
    @SysLog("分页查询户型")
    @PostMapping("/page")
    public R<Page<HouseDesignVo>> page(@Validated @RequestBody OpenApiPageModel<HouseDesignVo> pageModel) {
        log.info("[OpenHouseDesignController - page]: 分页查询户型, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openHouseDesignService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增户型
     *
     * @param model 户型信息
     * @return R 返回新增后的户型信息
     */
    @AutoInject
    @ApiOperation(value = "新增户型", notes = "新增户型")
    @SysLog("新增户型")
    @PostMapping
    public R<HouseDesignVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<HouseDesignVo> model) {
        log.info("[OpenHouseDesignController - save]: 新增户型, model={}", JSONConvertUtils.objectToString(model));
        return openHouseDesignService.save(model.getData());
    }

    /**
     * 修改户型
     *
     * @param model 户型信息
     * @return R 返回修改后的户型信息
     */
    @AutoInject
    @ApiOperation(value = "修改户型", notes = "修改户型")
    @SysLog("通过id修改户型")
    @PutMapping
    public R<HouseDesignVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<HouseDesignVo> model) {
        log.info("[OpenHouseDesignController - update]: 修改户型, model={}", JSONConvertUtils.objectToString(model));
        return openHouseDesignService.update(model.getData());
    }

    /**
     * 通过id删除户型
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除户型", notes = "通过id删除户型")
    @SysLog("通过id删除户型")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHouseDesignController - delete]: 通过id删除户型, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHouseDesignService.delete(id);
    }
}
