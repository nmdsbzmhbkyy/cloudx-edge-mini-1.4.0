package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPassPlanPolicyRelService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PassPlanPolicyRelVo;
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
 * 通行方案策略关系管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/pass-plan-policy-rel")
@Api(value = "openPassPlanPolicyRel", tags = {"v1", "通行方案相关", "通行方案策略关系管理"})
@Inner
@Slf4j
public class OpenPassPlanPolicyRelController {

    @Resource
    private OpenPassPlanPolicyRelService openPassPlanPolicyRelService;


    /**
     * 通过id查询通行方案策略关系
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回通行方案策略关系信息
     */
    @ApiOperation(value = "通过id查询通行方案策略关系", notes = "通过id查询通行方案策略关系")
    @SysLog("通过id查询通行方案策略关系")
    @GetMapping("/{id}")
    public R<PassPlanPolicyRelVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPassPlanPolicyRelController - getById]: 通过id查询通行方案策略关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPassPlanPolicyRelService.getById(id);
    }

    /**
     * 分页查询通行方案策略关系
     *
     * @param pageModel 分页查询条件
     * @return R 返回通行方案策略关系分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询通行方案策略关系", notes = "分页查询通行方案策略关系")
    @SysLog("分页查询通行方案策略关系")
    @PostMapping("/page")
    public R<Page<PassPlanPolicyRelVo>> page(@Validated @RequestBody OpenApiPageModel<PassPlanPolicyRelVo> pageModel) {
        log.info("[OpenPassPlanPolicyRelController - page]: 分页查询通行方案策略关系, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPassPlanPolicyRelService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回新增后的通行方案策略关系
     */
    @AutoInject
    @ApiOperation(value = "新增通行方案策略关系", notes = "新增通行方案策略关系")
    @SysLog("新增通行方案策略关系")
    @PostMapping
    public R<PassPlanPolicyRelVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PassPlanPolicyRelVo> model) {
        log.info("[OpenPassPlanPolicyRelController - save]: 新增通行方案策略关系, model={}", JSONConvertUtils.objectToString(model));
        return openPassPlanPolicyRelService.save(model.getData());
    }

    /**
     * 修改通行方案策略关系
     *
     * @param model 通行方案策略关系
     * @return R 返回修改后的通行方案策略关系
     */
    @AutoInject
    @ApiOperation(value = "修改通行方案策略关系", notes = "修改通行方案策略关系")
    @SysLog("通过id修改通行方案策略关系")
    @PutMapping
    public R<PassPlanPolicyRelVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PassPlanPolicyRelVo> model) {
        log.info("[OpenPassPlanPolicyRelController - update]: 修改通行方案策略关系, model={}", JSONConvertUtils.objectToString(model));
        return openPassPlanPolicyRelService.update(model.getData());
    }

    /**
     * 通过id删除通行方案策略关系
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除通行方案策略关系", notes = "通过id删除通行方案策略关系")
    @SysLog("通过id删除通行方案策略关系")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPassPlanPolicyRelController - delete]: 通过id删除通行方案策略关系, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPassPlanPolicyRelService.delete(id);
    }
}
