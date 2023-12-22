package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.dto.SysUserLockDTO;
import com.aurine.cloudx.estate.entity.SysUserLock;
import com.aurine.cloudx.estate.service.SysUserLockService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


/**
 * 
 *
 * @author pigx code generator
 * @date 2021-03-17 17:29:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user_lock" )
@Api(value = "/user_lock", tags = "管理")
public class SysUserLockController {

    private final SysUserLockService sysUserLockService;

    /**
     * 通过id查询
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过用户id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id") Integer id) {
        return R.ok(sysUserLockService.getById(id));
    }

    /**
     * 验证用户是否注销
     * @param phone 手机号
     * @return R
     */
    @ApiOperation(value = "验证用户是否注销", notes = "验证用户是否注销")
    @GetMapping("/{phone}" )
    public R checkUser(@PathVariable("phone") String phone) {
        return R.ok(sysUserLockService.checkUser(phone));
       // return R.ok(false);
    }

    /**
     * 新增
     * @param sysUserLockDTO
     * @return R
     */
    @ApiOperation(value = "新增", notes = "新增")
    @SysLog("新增" )
    @PostMapping
    public R save(@RequestBody SysUserLockDTO sysUserLockDTO) {
        SysUserLock sysUserLock = new SysUserLock();
        sysUserLock.setPhone(sysUserLockDTO.getPhone());
        sysUserLock.setFlag("1");
        sysUserLock.setUpdateTime(LocalDateTime.now());
        return R.ok(sysUserLockService.save(sysUserLock));
    }

    /**
     * 通过id删除
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
    @SysLog("通过id删除" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysUserLockService.removeById(id));
    }

}
