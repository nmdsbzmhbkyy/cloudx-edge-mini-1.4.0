package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPhysicalPassPolicyService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PhysicalPassPolicyVo;
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
 * 物理策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/physical-pass-policy")
@Api(value = "openPhysicalPassPolicy", tags = {"v1", "通行方案相关", "物理策略管理"})
@Inner
@Slf4j
public class OpenPhysicalPassPolicyController {

    @Resource
    private OpenPhysicalPassPolicyService openPhysicalPassPolicyService;


    /**
     * 通过id查询物理策略
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回物理策略信息
     */
    @ApiOperation(value = "通过id查询物理策略", notes = "通过id查询物理策略")
    @SysLog("通过id查询物理策略")
    @GetMapping("/{id}")
    public R<PhysicalPassPolicyVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPhysicalPassPolicyController - getById]: 通过id查询物理策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPhysicalPassPolicyService.getById(id);
    }

    /**
     * 分页查询物理策略
     *
     * @param pageModel 分页查询条件
     * @return R 返回物理策略分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询物理策略", notes = "分页查询物理策略")
    @SysLog("分页查询物理策略")
    @PostMapping("/page")
    public R<Page<PhysicalPassPolicyVo>> page(@Validated @RequestBody OpenApiPageModel<PhysicalPassPolicyVo> pageModel) {
        log.info("[OpenPhysicalPassPolicyController - page]: 分页查询物理策略, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPhysicalPassPolicyService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增物理策略
     *
     * @param model 物理策略
     * @return R 返回新增后的物理策略
     */
    @AutoInject
    @ApiOperation(value = "新增物理策略", notes = "新增物理策略")
    @SysLog("新增物理策略")
    @PostMapping
    public R<PhysicalPassPolicyVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PhysicalPassPolicyVo> model) {
        log.info("[OpenPhysicalPassPolicyController - save]: 新增物理策略, model={}", JSONConvertUtils.objectToString(model));
        return openPhysicalPassPolicyService.save(model.getData());
    }

    /**
     * 修改物理策略
     *
     * @param model 物理策略
     * @return R 返回修改后的物理策略
     */
    @AutoInject
    @ApiOperation(value = "修改物理策略", notes = "修改物理策略")
    @SysLog("通过id修改物理策略")
    @PutMapping
    public R<PhysicalPassPolicyVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PhysicalPassPolicyVo> model) {
        log.info("[OpenPhysicalPassPolicyController - update]: 修改物理策略, model={}", JSONConvertUtils.objectToString(model));
        return openPhysicalPassPolicyService.update(model.getData());
    }

    /**
     * 通过id删除物理策略
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除物理策略", notes = "通过id删除物理策略")
    @SysLog("通过id删除物理策略")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPhysicalPassPolicyController - delete]: 通过id删除物理策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPhysicalPassPolicyService.delete(id);
    }
}
