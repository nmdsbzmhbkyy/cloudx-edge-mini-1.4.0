package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.feign.RemotePigxUserService;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.feign.RemoteUmsUserService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 *
 *
 * @author pigx code generator
 * @date 2021-03-17 17:29:48
 */
@RestController
@AllArgsConstructor
@RequestMapping("/mobile" )
@Api(value = "/mobile", tags = "手机号操作")
public class SysPhoneController {

    @Resource
    private RemoteUmsUserService remoteUmsUserService;
    @Resource
    private RemotePigxUserService remotePigxUserService;

    @Resource
    private RemoteStaffService remoteStaffService;


    @Resource
    private final ProjectPersonInfoService projectPersonInfoService;


    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return R
     */
    @ApiOperation(value = "验证手机号并发送验证码", notes = "发送手机验证码")
    @GetMapping("/getNewPhoneQRCode/{phone}" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "cGlnOnBpZw==", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path")
    })
    public R mobile(@PathVariable("phone") String phone) {

        return remoteUmsUserService.getNewPhoneQRCode(phone);
    }



    /**
     * 验证新手机验证码并修改手机号
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "验证新手机验证码并修改手机号", notes = "验证新手机验证码并修改手机号，返回值data为true则验证通过，返回值data为false则错误信息输出在msg中")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "code", value = "验证码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "当前登录用户id", required = true, paramType = "query")
    })
    @PutMapping("/checkNewPhoneQRCode/{phone}")
    public R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone, @RequestParam("code") String code) {

        String oldPhoneNumber = SecurityUtils.getUser().getPhone();
        R<Boolean> newPhoneQRCode = remoteUmsUserService.getNewPhoneQRCode(phone, code);
        if (newPhoneQRCode.getData()){
            newPhoneQRCode = remotePigxUserService.updateByBiz(phone);
                             remoteStaffService.updatePhoneById(phone);
                             projectPersonInfoService.updatePhoneById(oldPhoneNumber,phone);
        }
        return  newPhoneQRCode;
    }




}
