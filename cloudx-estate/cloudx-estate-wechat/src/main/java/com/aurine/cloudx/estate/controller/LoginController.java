package com.aurine.cloudx.estate.controller;

import cn.hutool.json.JSONObject;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.service.LoginService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.pig4cloud.pigx.admin.api.feign.RemoteSocialDetailsService;
import com.pig4cloud.pigx.admin.api.vo.ChangeInfoVo;
import com.pig4cloud.pigx.admin.api.vo.RegisterVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatRegisterVo;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (LoginContrller)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/13 13:44
 */
@RestController
@RequestMapping("/login")
@Api(value = "login", tags = "微信登录接口")
public class LoginController {


    @Resource
    private LoginService loginService;

    /**
     * 微信注册或校验微信用户账号更新用户数据
     *
     * @return
     */
    @PostMapping("/register")
    @ApiOperation(value = "微信注册或校验用户账号", notes = "微信注册或校验用户账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
    })
    @Inner(value = false)
    public R<RegisterVo> register(@RequestBody WeChatRegisterVo weChatRegisterVo) {
        return loginService.register(weChatRegisterVo);
    }

    /**
     * 根据openId更新旧手机为新手机号或绑定到openId到新手机号
     *
     * @return
     */
    @PostMapping("/change")
    @ApiOperation(value = "根据openId更新旧手机为新手机号或绑定到openId到新手机号", notes = "微信注册或校验用户账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
    })
    @Inner(value = false)
    public R<Integer> change(@RequestBody RegisterVo registerVo) {
        return loginService.change(registerVo);
    }
}
