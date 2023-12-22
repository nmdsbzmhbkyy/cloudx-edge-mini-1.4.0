package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeEdgeUnbindService;
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
 * 级联解绑
 * 注：级联入云相关接口都是对内接口，在接口处添加SkipCheck注解来跳过检测
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@RestController
@RequestMapping("/v1/cascade/edge-unbind")
@Api(value = "cascadeEdgeUnbind", tags = {"v1", "级联入云相关", "级联解绑"})
@Inner
@Slf4j
public class CascadeEdgeUnbindController {

    @Resource
    private CascadeEdgeUnbindService cascadeEdgeUnbindService;


    /**
     * 申请级联解绑
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "申请级联解绑", notes = "申请级联解绑（从网关 -> 主网关）")
    @SysLog("申请级联解绑")
    @PutMapping("/apply/{projectCode}")
    public R<Boolean> apply(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - apply]: 发起级联解绑, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeUnbindService.apply(projectCode);
    }

    /**
     * 撤销级联解绑
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "撤销级联解绑", notes = "撤销级联解绑（从网关 -> 主网关）")
    @SysLog("撤销级联解绑")
    @PutMapping("/revoke/{projectCode}")
    public R<Boolean> revoke(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - revoke]: 撤销级联入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeUnbindService.revoke(projectCode);
    }

    /**
     * 同意级联解绑
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "同意级联解绑", notes = "同意级联解绑（主网关 -> 从网关）")
    @SysLog("同意级联解绑")
    @PutMapping("/accept/{projectCode}")
    public R<Boolean> accept(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - accept]: 同意级联解绑, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeUnbindService.accept(projectCode);
    }

    /**
     * 拒绝级联解绑
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "拒绝级联解绑", notes = "拒绝级联解绑（主网关 -> 从网关）")
    @SysLog("拒绝级联解绑")
    @PutMapping("/reject/{projectCode}")
    public R<Boolean> reject(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeUnbindController - reject]: 拒绝级联入云解绑, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeUnbindService.reject(projectCode);
    }
}
