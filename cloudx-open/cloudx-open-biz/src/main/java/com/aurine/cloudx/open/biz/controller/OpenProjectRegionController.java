package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenProjectRegionService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.ProjectRegionVo;
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
 * 项目区域管理
 *
 * @author xull@aurine.cn
 * @date 2020-05-25 08:50:24
 */

@RestController
@RequestMapping("/v1/open/project-region")
@Api(value = "openProjectRegion", tags = {"v1", "基础数据相关", "项目区域管理"})
@Inner
@Slf4j
public class OpenProjectRegionController {

    @Resource
    private OpenProjectRegionService openProjectRegionService;


    /**
     * 通过id查询区域
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回区域信息
     */
    @ApiOperation(value = "通过id查询区域", notes = "通过id查询区域")
    @SysLog("通过id查询区域")
    @GetMapping("/{id}")
    public R<ProjectRegionVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenProjectRegionController - getById]: 通过id查询区域, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openProjectRegionService.getById(id);
    }

    /**
     * 分页查询区域
     *
     * @param pageModel 分页查询条件
     * @return R 返回区域分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询区域", notes = "分页查询区域")
    @SysLog("分页查询区域")
    @PostMapping("/page")
    public R<Page<ProjectRegionVo>> page(@Validated @RequestBody OpenApiPageModel<ProjectRegionVo> pageModel) {
        log.info("[OpenProjectRegionController - page]: 分页查询区域, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openProjectRegionService.page(pageModel.getPage(), pageModel.getData());
    }


    /**
     * 新增项目区域
     *
     * @param model 项目区域
     * @return R 返回新增后的项目区域
     */
    @AutoInject
    @ApiOperation(value = "新增项目区域", notes = "新增项目区域")
    @SysLog("新增项目区域")
    @PostMapping
    public R<ProjectRegionVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<ProjectRegionVo> model) {
        log.info("[OpenProjectRegionController - save]: 新增项目区域, model={}", JSONConvertUtils.objectToString(model));
        return openProjectRegionService.save(model.getData());
    }

    /**
     * 修改项目区域
     *
     * @param model 项目区域
     * @return R 返回修改后的项目区域
     */
    @AutoInject
    @ApiOperation(value = "修改项目区域", notes = "修改项目区域")
    @SysLog("修改项目区域")
    @PutMapping
    public R<ProjectRegionVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<ProjectRegionVo> model) {
        log.info("[OpenProjectRegionController - update]: 修改项目区域, model={}", JSONConvertUtils.objectToString(model));
        return openProjectRegionService.update(model.getData());
    }

    /**
     * 通过id删除项目区域
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除项目区域", notes = "通过id删除项目区域")
    @SysLog("通过id删除项目区域")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenProjectRegionController - delete]: 通过id删除项目区域, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openProjectRegionService.delete(id);
    }
}
