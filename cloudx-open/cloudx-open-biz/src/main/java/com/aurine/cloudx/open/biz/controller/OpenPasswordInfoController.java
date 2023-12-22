package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenPasswordInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.PasswordInfoVo;
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
 * 密码信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/password-info")
@Api(value = "openPasswordInfo", tags = {"v1", "密码信息管理"})
@Inner
@Slf4j
public class OpenPasswordInfoController {

    @Resource
    private OpenPasswordInfoService openPasswordInfoService;


    /**
     * 通过id查询密码信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回密码信息
     */
    @ApiOperation(value = "通过id查询密码信息", notes = "通过id查询密码信息")
    @SysLog("通过id查询密码信息")
    @GetMapping("/{id}")
    public R<PasswordInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPasswordInfoController - getById]: 通过id查询密码信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPasswordInfoService.getById(id);
    }

    /**
     * 分页查询密码信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回密码信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询密码信息", notes = "分页查询密码信息")
    @SysLog("分页查询密码信息")
    @PostMapping("/page")
    public R<Page<PasswordInfoVo>> page(@Validated @RequestBody OpenApiPageModel<PasswordInfoVo> pageModel) {
        log.info("[OpenPasswordInfoController - page]: 分页查询密码信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openPasswordInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增密码信息
     *
     * @param model 密码信息
     * @return R 返回新增后的密码信息
     */
    @AutoInject
    @ApiOperation(value = "新增密码信息", notes = "新增密码信息")
    @SysLog("新增密码信息")
    @PostMapping
    public R<PasswordInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<PasswordInfoVo> model) {
        log.info("[OpenPasswordInfoController - save]: 新增密码信息, model={}", JSONConvertUtils.objectToString(model));
        return openPasswordInfoService.save(model.getData());
    }

    /**
     * 修改密码信息
     *
     * @param model 密码信息
     * @return R 返回修改后的密码信息
     */
    @AutoInject
    @ApiOperation(value = "修改密码信息", notes = "修改密码信息")
    @SysLog("通过id修改密码信息")
    @PutMapping
    public R<PasswordInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<PasswordInfoVo> model) {
        log.info("[OpenPasswordInfoController - update]: 修改密码信息, model={}", JSONConvertUtils.objectToString(model));
        return openPasswordInfoService.update(model.getData());
    }

    /**
     * 通过id删除密码信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除密码信息", notes = "通过id删除密码信息")
    @SysLog("通过id删除密码信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenPasswordInfoController - delete]: 通过id删除密码信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openPasswordInfoService.delete(id);
    }
}
