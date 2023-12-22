package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenHouseInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.HouseInfoVo;
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
 * 房屋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/house-info")
@Api(value = "openHouseInfo", tags = {"v1", "框架数据相关", "房屋信息管理"})
@Inner
@Slf4j
public class OpenHouseInfoController {

    @Resource
    private OpenHouseInfoService openHouseInfoService;


    /**
     * 通过id查询房屋信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回房屋信息
     */
    @ApiOperation(value = "通过id查询房屋信息", notes = "通过id查询房屋信息")
    @SysLog("通过id查询房屋信息")
    @GetMapping("/{id}")
    public R<HouseInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHouseInfoController - getById]: 通过id查询房屋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHouseInfoService.getById(id);
    }

    /**
     * 分页查询房屋信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回房屋信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询房屋信息", notes = "分页查询房屋信息")
    @SysLog("分页查询房屋信息")
    @PostMapping("/page")
    public R<Page<HouseInfoVo>> page(@Validated @RequestBody OpenApiPageModel<HouseInfoVo> pageModel) {
        log.info("[OpenHouseInfoController - page]: 分页查询房屋信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openHouseInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增房屋信息
     *
     * @param model 房屋信息
     * @return R 返回新增后的房屋信息
     */
    @AutoInject
    @ApiOperation(value = "新增房屋信息", notes = "新增房屋信息")
    @SysLog("新增房屋信息")
    @PostMapping
    public R<HouseInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<HouseInfoVo> model) {
        log.info("[OpenHouseInfoController - save]: 新增房屋信息, model={}", JSONConvertUtils.objectToString(model));
        return openHouseInfoService.save(model.getData());
    }

    /**
     * 修改房屋信息
     *
     * @param model 房屋信息
     * @return R 返回修改后的房屋信息
     */
    @AutoInject
    @ApiOperation(value = "修改房屋信息", notes = "修改房屋信息")
    @SysLog("通过id修改房屋信息")
    @PutMapping
    public R<HouseInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<HouseInfoVo> model) {
        log.info("[OpenHouseInfoController - update]: 修改房屋信息, model={}", JSONConvertUtils.objectToString(model));
        return openHouseInfoService.update(model.getData());
    }

    /**
     * 通过id删除房屋信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除房屋信息", notes = "通过id删除房屋信息")
    @SysLog("通过id删除房屋信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenHouseInfoController - delete]: 通过id删除房屋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openHouseInfoService.delete(id);
    }
}
