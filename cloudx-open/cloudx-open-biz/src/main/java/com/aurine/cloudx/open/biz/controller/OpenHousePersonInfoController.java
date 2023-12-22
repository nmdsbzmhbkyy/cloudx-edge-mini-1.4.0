package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenHousePersonInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HousePersonInfoVo;
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
 * 住户信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/house-person-info")
@Api(value = "openHousePersonInfo", tags = {"v1", "住户信息管理"})
@Inner
@Slf4j
public class OpenHousePersonInfoController {

    @Resource
    private OpenHousePersonInfoService openHousePersonInfoService;


    /**
     * 通过id查询住户信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回住户信息
     */
    @ApiOperation(value = "通过id查询住户信息", notes = "通过id查询住户信息")
    @SysLog("通过id查询住户信息")
    @GetMapping("/{id}")
    public R<HousePersonInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHousePersonInfoController - getById]: 通过id查询住户信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHousePersonInfoService.getById(id);
    }

    /**
     * 分页查询住户信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回住户信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询住户信息", notes = "分页查询住户信息")
    @SysLog("分页查询住户信息")
    @PostMapping("/page")
    public R<Page<HousePersonInfoVo>> page(@Validated @RequestBody OpenApiPageModel<HousePersonInfoVo> pageModel) {
        log.info("[OpenHousePersonInfoController - page]: 分页查询住户信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openHousePersonInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增住户信息
     *
     * @param model 住户信息
     * @return R 返回新增后的住户信息
     */
    @AutoInject
    @ApiOperation(value = "新增住户信息", notes = "新增住户信息")
    @SysLog("新增住户信息")
    @PostMapping
    public R<HousePersonInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<HousePersonInfoVo> model) {
        log.info("[OpenHousePersonInfoController - save]: 新增住户信息, model={}", JSONConvertUtils.objectToString(model));
        return openHousePersonInfoService.save(model.getData());
    }

    /**
     * 修改住户信息
     *
     * @param model 住户信息
     * @return R 返回修改后的住户信息
     */
    @AutoInject
    @ApiOperation(value = "修改住户信息", notes = "修改住户信息")
    @SysLog("通过id修改住户信息")
    @PutMapping
    public R<HousePersonInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<HousePersonInfoVo> model) {
        log.info("[OpenHousePersonInfoController - update]: 修改住户信息, model={}", JSONConvertUtils.objectToString(model));
        return openHousePersonInfoService.update(model.getData());
    }

    /**
     * 通过id删除住户信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除住户信息", notes = "通过id删除住户信息")
    @SysLog("通过id删除住户信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHousePersonInfoController - delete]: 通过id删除住户信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHousePersonInfoService.delete(id);
    }
}
