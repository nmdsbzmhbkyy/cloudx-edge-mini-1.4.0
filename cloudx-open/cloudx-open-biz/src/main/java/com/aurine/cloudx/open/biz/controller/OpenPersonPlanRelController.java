package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPersonPlanRelService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonPlanRelVo;
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
 * 人员通行方案关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/person-plan-rel")
@Api(value = "openPersonPlanRel", tags = {"v1", "通行方案相关", "人员通行方案关系管理"})
@Inner
@Slf4j
public class OpenPersonPlanRelController {

    @Resource
    private OpenPersonPlanRelService openPersonPlanRelService;


    /**
     * 通过id查询人员通行方案关系
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回人员通行方案关系信息
     */
    @ApiOperation(value = "通过id查询人员通行方案关系", notes = "通过id查询人员通行方案关系")
    @SysLog("通过id查询人员通行方案关系")
    @GetMapping("/{id}")
    public R<PersonPlanRelVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonPlanRelController - getById]: 通过id查询人员通行方案关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonPlanRelService.getById(id);
    }

    /**
     * 分页查询人员通行方案关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回人员通行方案关系分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询人员通行方案关系", notes = "分页查询人员通行方案关系")
    @SysLog("分页查询人员通行方案关系")
    @PostMapping("/page")
    public R<Page<PersonPlanRelVo>> page(@Validated @RequestBody OpenApiPageModel<PersonPlanRelVo> pageModel) {
        log.info("[OpenPersonPlanRelController - page]: 分页查询人员通行方案关系, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPersonPlanRelService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回新增后的人员通行方案关系
     */
    @AutoInject
    @ApiOperation(value = "新增人员通行方案关系", notes = "新增人员通行方案关系")
    @SysLog("新增人员通行方案关系")
    @PostMapping
    public R<PersonPlanRelVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PersonPlanRelVo> model) {
        log.info("[OpenPersonPlanRelController - save]: 新增人员通行方案关系, model={}", JSONConvertUtils.objectToString(model));
        return openPersonPlanRelService.save(model.getData());
    }

    /**
     * 修改人员通行方案关系
     *
     * @param model 人员通行方案关系
     * @return R 返回修改后的人员通行方案关系
     */
    @AutoInject
    @ApiOperation(value = "修改人员通行方案关系", notes = "修改人员通行方案关系")
    @SysLog("通过id修改人员通行方案关系")
    @PutMapping
    public R<PersonPlanRelVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PersonPlanRelVo> model) {
        log.info("[OpenPersonPlanRelController - update]: 修改人员通行方案关系, model={}", JSONConvertUtils.objectToString(model));
        return openPersonPlanRelService.update(model.getData());
    }

    /**
     * 通过id删除人员通行方案关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员通行方案关系", notes = "通过id删除人员通行方案关系")
    @SysLog("通过id删除人员通行方案关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonPlanRelController - delete]: 通过id删除人员通行方案关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonPlanRelService.delete(id);
    }
}
