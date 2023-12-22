

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.bean.BeanUtil;
import com.aurine.cloudx.estate.feign.RemoteUmsTokenService;
import com.aurine.cloudx.estate.util.ImgConvertUtil;
import com.aurine.cloudx.estate.vo.*;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * 访客
 * (ProjectRepairRecordController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/1 14:40
 */
@RestController
@RequestMapping("/user")
@Api(value = "/user", tags = "用户管理")
@Slf4j
public class ProjectUserController {
    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private RemoteUmsTokenService remoteTokenService;

    @Resource
    private ImgConvertUtil imgConvertUtil;

    /**
     * 修改用户信息
     *
     * @param user
     * @return success/false
     */
    @PutMapping("/edit")
    @ApiOperation(value = "修改用户信息（用户中心）", notes = "修改用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header")
    })
    public R<Boolean> updateUserInfo (@RequestBody AppUserInfoVo user) throws IOException {
        UserDTO userDto = new UserDTO();
        BeanUtil.copyProperties(user, userDto);
        userDto.setAvatar(imgConvertUtil.base64ToMinio(userDto.getAvatar()));
        return remoteUserService.updateNewUserInfo(userDto);
    }

    @ApiOperation(value = "退出账号（用户中心）", notes = "退出账号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "token", value = "token", required = true, paramType = "path")
    })
    @DeleteMapping("/token/{token}")
    public R<Boolean> removeTokenById (@PathVariable("token") String token) {
        return remoteTokenService.removeTokenById(token, SecurityConstants.FROM_IN);
    }

    @ApiOperation(value = "获取用户信息（用户中心）", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    })
    @GetMapping("/{id}")
    public R<AppUserInfoVo> getUserInfo (@PathVariable Integer id) throws IOException {
        AppUserInfoVo userInfoVo = new AppUserInfoVo();
        R<UserVO> userVO = remoteUserService.user(id);
        BeanUtils.copyProperties(userVO.getData(), userInfoVo);
        userInfoVo.setAvatar(imgConvertUtil.urlToBase64(userInfoVo.getAvatar()));
        return R.ok(userInfoVo);
    }
//
//    @ApiOperation(value = "修改适老化状态", notes = "修改适老化状态")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "type", value = "1:开启 0:关闭", required = true, paramType = "path")
//    })
//    @PutMapping("/upateSuitableForAging/{type}")
//    public R<Boolean> upateSuitableForAging (@PathVariable("type") String type) {
//
//        return  remoteUserService.upateSuitableForAging(type);
//    }

}
