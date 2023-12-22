package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.dto.SysUserLockDTO;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.entity.SysUserLock;
import com.aurine.cloudx.estate.feign.RemoteUmsUserService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.SysUserLockService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 *
 *
 * @author pigx code generator
 * @date 2021-03-17 17:29:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user_lock" )
@Api(value = "/user_lock", tags = "注销账号管理")
public class SysUserLockController {

    private final SysUserLockService sysUserLockService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;








    /**
     * 验证用户是否注销
     * @param phone 手机号
     * @return R
     */
    @ApiOperation(value = "验证用户是否注销", notes = "验证用户是否注销")
    @GetMapping("/{phone}" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path")
    })
    public R<SysUserLock> checkUser(@PathVariable("phone") String phone) {
        SysUserLock sysUserLock = sysUserLockService.checkUser(phone);
        if (ObjectUtil.isNotEmpty(sysUserLock)) {
            sysUserLockService.removeById(sysUserLock.getId());
        }
        return R.ok(sysUserLock);
    }

    /**
     * 新增
     * @param sysUserLockDTO
     * @return R
     */
    @ApiOperation(value = "新增注销账号", notes = "新增注销账号")
    @SysLog("新增" )
    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header")
    })
    public R<Boolean> save(@RequestBody SysUserLockDTO sysUserLockDTO) {
        SysUserLock sysUserLock = new SysUserLock();
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(sysUserLockDTO.getPhone());
        sysUserLock.setPhone(sysUserLockDTO.getPhone());
        sysUserLock.setFlag("1");
        sysUserLock.setUpdateTime(StrUtil.isNotBlank(sysUserLockDTO.getUpdateTime()) ?
                LocalDateTime.parse(sysUserLockDTO.getUpdateTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : LocalDateTime.now());
        sysUserLock.setUserId(projectPersonInfo.getUserId());
        return R.ok(sysUserLockService.save(sysUserLock));
    }

}
