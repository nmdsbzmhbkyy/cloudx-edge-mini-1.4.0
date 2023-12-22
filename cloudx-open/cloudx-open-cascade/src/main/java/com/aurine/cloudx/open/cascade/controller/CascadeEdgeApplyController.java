package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeEdgeApplyService;
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
 * 级联申请
 * 注：级联入云相关接口都是对内接口，在接口处添加SkipCheck注解来跳过检测
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@RestController
@RequestMapping("/v1/cascade/edge-apply")
@Api(value = "cascadeEdgeApply", tags = {"v1", "级联入云相关", "级联申请"})
@Inner
@Slf4j
public class CascadeEdgeApplyController {

    @Resource
    private CascadeEdgeApplyService cascadeEdgeApplyService;


    /**
     * 撤销级联申请
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "撤销级联申请", notes = "撤销级联申请（从网关 -> 主网关）")
    @SysLog("撤销级联申请")
    @PutMapping("/revoke/{projectCode}")
    public R<Boolean> revoke(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - revoke]: 撤销级联申请, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeApplyService.revoke(projectCode);
    }

    /**
     * 同意级联申请
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "同意级联申请", notes = "同意级联申请（主网关 -> 从网关）")
    @SysLog("同意级联申请")
    @PutMapping("/accept/{projectCode}")
    public R<Boolean> accept(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - accept]: 同意级联申请, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeApplyService.accept(projectCode);
    }

    /**
     * 拒绝级联申请
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "拒绝级联申请", notes = "拒绝级联申请（主网关 -> 从网关）")
    @SysLog("拒绝级联申请")
    @PutMapping("/reject/{projectCode}")
    public R<Boolean> reject(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - reject]: 拒绝级联申请, header={}, projectCode={}", header, projectCode);
        return cascadeEdgeApplyService.reject(projectCode);
    }
}
