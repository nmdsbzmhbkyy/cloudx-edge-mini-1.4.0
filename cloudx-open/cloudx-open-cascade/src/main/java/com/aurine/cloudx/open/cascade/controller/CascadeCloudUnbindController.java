package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeCloudUnbindService;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.aurine.cloudx.open.common.core.annotation.SkipCheck;
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
 * 入云解绑
 * 注：级联入云相关接口都是对内接口，在接口处添加SkipCheck注解来跳过检测
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@RestController
@RequestMapping("/v1/cascade/cloud-unbind")
@Api(value = "cascadeCloudUnbind", tags = {"v1", "级联入云相关", "入云解绑"})
@Inner
@Slf4j
public class CascadeCloudUnbindController {

    @Resource
    private CascadeCloudUnbindService cascadeCloudUnbindService;


    /**
     * 申请入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "申请入云解绑", notes = "申请入云解绑（边缘侧 -> 平台侧）")
    @SysLog("申请入云解绑")
    @PutMapping("/apply/{projectCode}")
    public R<Boolean> apply(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - apply]: 发起入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeCloudUnbindService.apply(projectCode);
    }

    /**
     * 撤销入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "撤销入云解绑", notes = "撤销入云解绑（边缘侧 -> 平台侧）")
    @SysLog("撤销入云解绑")
    @PutMapping("/revoke/{projectCode}")
    public R<Boolean> revoke(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - revoke]: 撤销入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeCloudUnbindService.revoke(projectCode);
    }

    /**
     * 同意入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "同意入云解绑", notes = "同意入云解绑（平台侧 -> 边缘侧）")
    @SysLog("同意入云解绑")
    @PutMapping("/accept/{projectCode}")
    public R<Boolean> accept(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - accept]: 同意入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeCloudUnbindService.accept(projectCode);
    }

    /**
     * 拒绝入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "拒绝入云解绑", notes = "拒绝入云解绑（平台侧 -> 边缘侧）")
    @SysLog("拒绝入云解绑")
    @PutMapping("/reject/{projectCode}")
    public R<Boolean> reject(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - reject]: 拒绝入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeCloudUnbindService.reject(projectCode);
    }
}
