package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.feign.RemoteStaffService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.ProjectPersonCarInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (ProjectPersonInfoController)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/person")
@Api(value = "person", tags = "业主管理")
@Slf4j
public class ProjectPersonInfoController {

    @Resource
    private final ProjectPersonInfoService projectPersonInfoService;



    /**
     * 获取当前账号的业主信息
     *
     * @return
     */
    @GetMapping
    @ApiOperation(value = "获取当前账号的业主信息", notes = "获取当前账号的业主信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectPersonInfo> getInfo() {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPersonInfo);
    }

    /**
     * 根据业主ID获取业主信息
     *
     * @return
     */
    @GetMapping("/{personId}")
    @ApiOperation(value = "根据id获取的业主信息", notes = "获取当前账号的业主信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header")
    })
    public R<ProjectPersonInfo> getInfoById(@PathVariable String personId) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getById(personId);
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        return R.ok(projectPersonInfo);
    }

    /**
     * 获取业主信息
     *
     * @return
     */
    @ApiOperation(value = "根据手机号获取业主信息", notes = "根据手机号获取业主信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
//            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path")
    })
    @GetMapping("/phone/{phone}")
    public R<ProjectPersonInfo> getInfoByPhone(@PathVariable String phone) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getByTelephone(phone);
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.ok();
        }
        return R.ok(projectPersonInfo);
    }

    /**
     * 获取业主车位和车辆信息表
     *
     * @param personId 人员id
     * @return
     */
    @ApiOperation(value = "根据人员ID获取业主车位和车辆信息表(物业)", notes = "获取业主车位和车辆信息表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "personId", value = "人员id", required = true, paramType = "path")
    })
    @GetMapping("/car/{personId}")
    public R<ProjectPersonCarInfoVo> getInfo(@PathVariable("personId") String personId) {

        return R.ok(projectPersonInfoService.getCarInfo(personId));
    }

    /**
     * 更改业主手机号
     *
     * @param phone
     * @return
     */
    @ApiOperation(value = "更改业主手机号", notes = "更改业主手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "用户token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "phone", value = "手机号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "uid", value = "当前登录用户id", required = true, paramType = "query")
    })
    @PutMapping("/updatePhone/{phone}/{newPhone}")
    public R<Boolean> getNewPhoneQRCode(@PathVariable("phone") String phone,@PathVariable("newPhone")String newPhone) {

        return  projectPersonInfoService.updatePhoneById(phone,newPhone);

    }


}
