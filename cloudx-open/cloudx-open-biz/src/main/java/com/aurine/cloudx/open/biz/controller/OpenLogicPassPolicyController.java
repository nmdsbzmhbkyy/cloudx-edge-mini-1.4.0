package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenLogicPassPolicyService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.LogicPassPolicyVo;
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
 * 逻辑策略管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/logic-pass-policy")
@Api(value = "openLogicPassPolicy", tags = {"v1", "通行方案相关", "逻辑策略管理"})
@Inner
@Slf4j
public class OpenLogicPassPolicyController {

    @Resource
    private OpenLogicPassPolicyService openLogicPassPolicyService;


    /**
     * 通过id查询逻辑策略
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回逻辑策略信息
     */
    @ApiOperation(value = "通过id查询逻辑策略", notes = "通过id查询逻辑策略")
    @SysLog("通过id查询逻辑策略")
    @GetMapping("/{id}")
    public R<LogicPassPolicyVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenLogicPassPolicyController - getById]: 通过id查询逻辑策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openLogicPassPolicyService.getById(id);
    }

    /**
     * 分页查询逻辑策略
     *
     * @param pageModel 分页查询条件
     * @return R 返回逻辑策略分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询逻辑策略", notes = "分页查询逻辑策略")
    @SysLog("分页查询逻辑策略")
    @PostMapping("/page")
    public R<Page<LogicPassPolicyVo>> page(@Validated @RequestBody OpenApiPageModel<LogicPassPolicyVo> pageModel) {
        log.info("[OpenLogicPassPolicyController - page]: 分页查询逻辑策略, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openLogicPassPolicyService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增逻辑策略
     *
     * @param model 逻辑策略
     * @return R 返回新增后的逻辑策略
     */
    @AutoInject
    @ApiOperation(value = "新增逻辑策略", notes = "新增逻辑策略")
    @SysLog("新增逻辑策略")
    @PostMapping
    public R<LogicPassPolicyVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<LogicPassPolicyVo> model) {
        log.info("[OpenLogicPassPolicyController - save]: 新增逻辑策略, model={}", JSONConvertUtils.objectToString(model));
        return openLogicPassPolicyService.save(model.getData());
    }

    /**
     * 修改逻辑策略
     *
     * @param model 逻辑策略
     * @return R 返回修改后的逻辑策略
     */
    @AutoInject
    @ApiOperation(value = "修改逻辑策略", notes = "修改逻辑策略")
    @SysLog("通过id修改逻辑策略")
    @PutMapping
    public R<LogicPassPolicyVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<LogicPassPolicyVo> model) {
        log.info("[OpenLogicPassPolicyController - update]: 修改逻辑策略, model={}", JSONConvertUtils.objectToString(model));
        return openLogicPassPolicyService.update(model.getData());
    }

    /**
     * 通过id删除逻辑策略
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除逻辑策略", notes = "通过id删除逻辑策略")
    @SysLog("通过id删除逻辑策略")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenLogicPassPolicyController - delete]: 通过id删除逻辑策略, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openLogicPassPolicyService.delete(id);
    }
}
