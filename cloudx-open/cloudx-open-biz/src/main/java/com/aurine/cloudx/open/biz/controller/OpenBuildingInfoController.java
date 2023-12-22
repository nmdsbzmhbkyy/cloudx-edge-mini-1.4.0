package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenBuildingInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.BuildingInfoVo;
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
 * 楼栋信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/building-info")
@Api(value = "openBuildingInfo", tags = {"v1", "框架数据相关", "楼栋信息管理"})
@Inner
@Slf4j
public class OpenBuildingInfoController {

    @Resource
    private OpenBuildingInfoService openBuildingInfoService;


    /**
     * 通过id查询楼栋信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回楼栋信息
     */
    @ApiOperation(value = "通过id查询楼栋信息", notes = "通过id查询楼栋信息")
    @SysLog("通过id查询楼栋信息")
    @GetMapping("/{id}")
    public R<BuildingInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenBuildingInfoController - getById]: 通过id查询楼栋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openBuildingInfoService.getById(id);
    }

    /**
     * 分页查询楼栋信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回楼栋信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询楼栋信息", notes = "分页查询楼栋信息")
    @SysLog("分页查询楼栋信息")
    @PostMapping("/page")
    public R<Page<BuildingInfoVo>> page(@Validated @RequestBody OpenApiPageModel<BuildingInfoVo> pageModel) {
        log.info("[OpenBuildingInfoController - page]: 分页查询楼栋信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openBuildingInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回新增后的楼栋信息
     */
    @AutoInject
    @ApiOperation(value = "新增楼栋信息", notes = "新增楼栋信息")
    @SysLog("新增楼栋信息")
    @PostMapping
    public R<BuildingInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<BuildingInfoVo> model) {
        log.info("[OpenBuildingInfoController - save]: 新增楼栋信息, model={}", JSONConvertUtils.objectToString(model));
        return openBuildingInfoService.save(model.getData());
    }

    /**
     * 修改楼栋信息
     *
     * @param model 楼栋信息
     * @return R 返回修改后的楼栋信息
     */
    @AutoInject
    @ApiOperation(value = "修改楼栋信息", notes = "修改楼栋信息")
    @SysLog("通过id修改楼栋信息")
    @PutMapping
    public R<BuildingInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<BuildingInfoVo> model) {
        log.info("[OpenBuildingInfoController - update]: 修改楼栋信息, model={}", JSONConvertUtils.objectToString(model));
        return openBuildingInfoService.update(model.getData());
    }

    /**
     * 通过id删除楼栋信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除楼栋信息", notes = "通过id删除楼栋信息")
    @SysLog("通过id删除楼栋信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenBuildingInfoController - delete]: 通过id删除楼栋信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openBuildingInfoService.delete(id);
    }
}
