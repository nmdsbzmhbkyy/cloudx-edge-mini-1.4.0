package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPassPlanService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PassPlanVo;
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
 * 通行方案管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/pass-plan")
@Api(value = "openPassPlan", tags = {"v1", "通行方案相关", "通行方案管理"})
@Inner
@Slf4j
public class OpenPassPlanController {

    @Resource
    private OpenPassPlanService openPassPlanService;


    /**
     * 通过id查询通行方案
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回通行方案信息
     */
    @ApiOperation(value = "通过id查询通行方案", notes = "通过id查询通行方案")
    @SysLog("通过id查询通行方案")
    @GetMapping("/{id}")
    public R<PassPlanVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPassPlanController - getById]: 通过id查询通行方案, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPassPlanService.getById(id);
    }

    /**
     * 分页查询通行方案
     *
     * @param pageModel 分页查询条件
     * @return R 返回通行方案分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询通行方案", notes = "分页查询通行方案")
    @SysLog("分页查询通行方案")
    @PostMapping("/page")
    public R<Page<PassPlanVo>> page(@Validated @RequestBody OpenApiPageModel<PassPlanVo> pageModel) {
        log.info("[OpenPassPlanController - page]: 分页查询通行方案, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPassPlanService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增通行方案
     *
     * @param model 通行方案
     * @return R 返回新增后的通行方案
     */
    @AutoInject
    @ApiOperation(value = "新增通行方案", notes = "新增通行方案")
    @SysLog("新增通行方案")
    @PostMapping
    public R<PassPlanVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PassPlanVo> model) {
        log.info("[OpenPassPlanController - save]: 新增通行方案, model={}", JSONConvertUtils.objectToString(model));
        return openPassPlanService.save(model.getData());
    }

    /**
     * 修改通行方案
     *
     * @param model 通行方案
     * @return R 返回修改后的通行方案
     */
    @AutoInject
    @ApiOperation(value = "修改通行方案", notes = "修改通行方案")
    @SysLog("通过id修改通行方案")
    @PutMapping
    public R<PassPlanVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PassPlanVo> model) {
        log.info("[OpenPassPlanController - update]: 修改通行方案, model={}", JSONConvertUtils.objectToString(model));
        return openPassPlanService.update(model.getData());
    }

    /**
     * 通过id删除通行方案
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除通行方案", notes = "通过id删除通行方案")
    @SysLog("通过id删除通行方案")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPassPlanController - delete]: 通过id删除通行方案, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPassPlanService.delete(id);
    }
}
