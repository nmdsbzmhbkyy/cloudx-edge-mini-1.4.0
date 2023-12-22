package com.aurine.cloudx.estate.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aurine.cloudx.estate.entity.SysOperationLog;
import com.aurine.cloudx.estate.service.SysOperationLogService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

/**
 * 用户操作日志
 *
 * @author pigx code generator
 * @date 2020-06-22 11:15:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projLog")
@Api(value = "projLog", tags = "用户操作日志管理")
public class SysOperationLogController {

    private final SysOperationLogService sysOperationLogService;

    /**
     * 分页查询
     * 
     * @param page            分页对象
     * @param sysOperationLog 用户操作日志
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getSysOperationLogPage(Page page, SysOperationLog sysOperationLog) {
        return R.ok(sysOperationLogService.page(page, Wrappers.query(sysOperationLog)));
    }

    /**
     * 通过id查询用户操作日志
     * 
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{seq}")
    public R getById(@PathVariable("seq") Integer seq) {
        return R.ok(sysOperationLogService.getById(seq));
    }

    /**
     * 新增用户操作日志
     * 
     * @param sysOperationLog 用户操作日志
     * @return R
     */
    @ApiOperation(value = "新增用户操作日志", notes = "新增用户操作日志")
    @PostMapping("/save")
    @Inner
    public R save(@RequestBody SysOperationLog sysOperationLog) {
        return R.ok(sysOperationLogService.save(sysOperationLog));
    }

    /**
     * 修改用户操作日志
     * 
     * @param sysOperationLog 用户操作日志
     * @return R
     */
    @ApiOperation(value = "修改用户操作日志", notes = "修改用户操作日志")
    @PutMapping
    public R updateById(@RequestBody SysOperationLog sysOperationLog) {
        return R.ok(sysOperationLogService.updateById(sysOperationLog));
    }

    /**
     * 通过id删除用户操作日志
     * 
     * @param seq id
     * @return R
     */
    @ApiOperation(value = "通过id删除用户操作日志", notes = "通过id删除用户操作日志")
    @DeleteMapping("/{seq}")
    public R removeById(@PathVariable Integer seq) {
        return R.ok(sysOperationLogService.removeById(seq));
    }

}
