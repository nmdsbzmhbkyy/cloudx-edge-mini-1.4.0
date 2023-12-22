package com.aurine.cloudx.open.biz.controller;

import com.aurine.cloudx.open.biz.service.OpenStaffInfoService;
import com.aurine.cloudx.open.common.core.annotation.AutoInject;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.common.entity.model.OpenApiPageModel;
import com.aurine.cloudx.open.common.entity.vo.StaffInfoVo;
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
 * 员工信息管理
 *
 * @author : Qiu
 * @date : 2021 12 16 14:34
 */

@RestController
@RequestMapping("/v1/open/staff-info")
@Api(value = "openStaffInfo", tags = {"v1", "员工信息管理"})
@Inner
@Slf4j
public class OpenStaffInfoController {

    @Resource
    private OpenStaffInfoService openStaffInfoService;


    /**
     * 通过id查询员工信息
     *
     * @param header 请求头信息
     * @param id     要查询的主键id
     * @return R 返回员工信息
     */
    @ApiOperation(value = "通过id查询员工信息", notes = "通过id查询员工信息")
    @SysLog("通过id查询员工信息")
    @GetMapping("/{id}")
    public R<StaffInfoVo> getById(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenStaffInfoController - getById]: 通过id查询员工信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openStaffInfoService.getById(id);
    }

    /**
     * 分页查询员工信息
     *
     * @param pageModel 分页查询条件
     * @return R 返回员工信息分页数据
     */
    @AutoInject
    @ApiOperation(value = "分页查询员工信息", notes = "分页查询员工信息")
    @SysLog("分页查询员工信息")
    @PostMapping("/page")
    public R<Page<StaffInfoVo>> page(@Validated @RequestBody OpenApiPageModel<StaffInfoVo> pageModel) {
        log.info("[OpenStaffInfoController - page]: 分页查询员工信息, page={}, query={}", JSONConvertUtils.objectToString(pageModel.getPage()), JSONConvertUtils.objectToString(pageModel.getData()));
        return openStaffInfoService.page(pageModel.getPage(), pageModel.getData());
    }

    /**
     * 新增员工信息
     *
     * @param model 员工信息
     * @return R 返回新增后的员工信息
     */
    @AutoInject
    @ApiOperation(value = "新增员工信息", notes = "新增员工信息")
    @SysLog("新增员工信息")
    @PostMapping
    public R<StaffInfoVo> save(@Validated({InsertGroup.class}) @RequestBody OpenApiModel<StaffInfoVo> model) {
        log.info("[OpenStaffInfoController - save]: 新增员工信息, model={}", JSONConvertUtils.objectToString(model));
        return openStaffInfoService.save(model.getData());
    }

    /**
     * 修改员工信息
     *
     * @param model 员工信息
     * @return R 返回修改后的员工信息
     */
    @AutoInject
    @ApiOperation(value = "修改员工信息", notes = "修改员工信息")
    @SysLog("通过id修改员工信息")
    @PutMapping
    public R<StaffInfoVo> update(@Validated({UpdateGroup.class}) @RequestBody OpenApiModel<StaffInfoVo> model) {
        log.info("[OpenStaffInfoController - update]: 修改员工信息, model={}", JSONConvertUtils.objectToString(model));
        return openStaffInfoService.update(model.getData());
    }

    /**
     * 通过id删除员工信息
     *
     * @param header 请求头信息
     * @param id     要删除的主键id
     * @return R 返回删除结果
     */
    @ApiOperation(value = "通过id删除员工信息", notes = "通过id删除员工信息")
    @SysLog("通过id删除员工信息")
    @DeleteMapping("/{id}")
    public R<Boolean> delete(@Validated OpenApiHeader header, @PathVariable("id") String id) {
        log.info("[OpenStaffInfoController - delete]: 通过id删除员工信息, header={}, id={}", JSONConvertUtils.objectToString(header), id);
        return openStaffInfoService.delete(id);
    }
}
