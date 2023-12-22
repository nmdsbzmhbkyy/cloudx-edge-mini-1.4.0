package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPersonInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PersonInfoVo;
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
 * 人员信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/person-info")
@Api(value = "openPersonInfo", tags = {"v1", "人员信息管理"})
@Inner
@Slf4j
public class OpenPersonInfoController {

    @Resource
    private OpenPersonInfoService openPersonInfoService;


    /**
     * 通过id查询人员信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回人员信息
     */
    @ApiOperation(value = "通过id查询人员信息", notes = "通过id查询人员信息")
    @SysLog("通过id查询人员信息")
    @GetMapping("/{id}")
    public R<PersonInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonInfoController - getById]: 通过id查询人员信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonInfoService.getById(id);
    }

    /**
     * 分页查询人员信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回人员信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询人员信息", notes = "分页查询人员信息")
    @SysLog("分页查询人员信息")
    @PostMapping("/page")
    public R<Page<PersonInfoVo>> page(@Validated @RequestBody OpenApiPageModel<PersonInfoVo> pageModel) {
        log.info("[OpenPersonInfoController - page]: 分页查询人员信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPersonInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增人员信息
     *
     * @param model 人员信息
     * @return R 返回新增后的人员信息
     */
    @AutoInject
    @ApiOperation(value = "新增人员信息", notes = "新增人员信息")
    @SysLog("新增人员信息")
    @PostMapping
    public R<PersonInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PersonInfoVo> model) {
        log.info("[OpenPersonInfoController - save]: 新增人员信息, model={}", JSONConvertUtils.objectToString(model));
        return openPersonInfoService.save(model.getData());
    }

    /**
     * 修改人员信息
     *
     * @param model 人员信息
     * @return R 返回修改后的人员信息
     */
    @AutoInject
    @ApiOperation(value = "修改人员信息", notes = "修改人员信息")
    @SysLog("通过id修改人员信息")
    @PutMapping
    public R<PersonInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PersonInfoVo> model) {
        log.info("[OpenPersonInfoController - update]: 修改人员信息, model={}", JSONConvertUtils.objectToString(model));
        return openPersonInfoService.update(model.getData());
    }

    /**
     * 通过id删除人员信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除人员信息", notes = "通过id删除人员信息")
    @SysLog("通过id删除人员信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPersonInfoController - delete]: 通过id删除人员信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPersonInfoService.delete(id);
    }
}
