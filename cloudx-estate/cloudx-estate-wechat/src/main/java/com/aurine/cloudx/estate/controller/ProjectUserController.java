

package com.aurine.cloudx.estate.controller;


import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.admin.api.vo.UserVO;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
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




    @ApiOperation(value = "获取用户信息（用户中心）", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "id", value = "用户ID", required = true, paramType = "path")
    })
    @GetMapping("/{userId}")
    public R<UserVO> getUserInfo (@PathVariable("userId") Integer userId)  {
        R<UserVO> user = remoteUserService.user(userId);

        return user;
    }




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
