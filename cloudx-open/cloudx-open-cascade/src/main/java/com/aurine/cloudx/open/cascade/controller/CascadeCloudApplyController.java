package com.aurine.cloudx.open.cascade.controller;

import com.aurine.cloudx.open.cascade.service.CascadeCloudApplyService;
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
 * 入云申请
 * 注：级联入云相关接口都是对内接口，在接口处添加SkipCheck注解来跳过检测
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@RestController
@RequestMapping("/v1/cascade/cloud-apply")
@Api(value = "cascadeCloudApply", tags = {"v1", "级联入云相关", "入云申请"})
@Inner
@Slf4j
public class CascadeCloudApplyController {

    @Resource
    private CascadeCloudApplyService cascadeCloudApplyService;


    /**
     * 撤销入云申请
     * （边缘侧 -> 平台侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "撤销入云申请", notes = "撤销入云申请（边缘侧 -> 平台侧）")
    @SysLog("撤销入云申请")
    @PutMapping("/revoke/{projectUUID}/{projectCode}")
    public R<Boolean> revoke(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectUUID") String projectUUID, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - revoke]: 撤销入云申请, header={}, projectCode={}", header, projectCode);
        return cascadeCloudApplyService.revoke(projectUUID, projectCode);
    }

    /**
     * 同意入云申请
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "同意入云申请", notes = "同意入云申请（平台侧 -> 边缘侧）")
    @SysLog("同意入云申请")
    @PutMapping("/accept/{projectCode}")
    public R<Boolean> accept(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - accept]: 同意入云申请, header={}, projectCode={}", header, projectCode);
        return cascadeCloudApplyService.accept(projectCode);
    }

    /**
     * 拒绝入云申请
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @SkipCheck
    @ApiOperation(value = "拒绝入云申请", notes = "拒绝入云申请（平台侧 -> 边缘侧）")
    @SysLog("拒绝入云申请")
    @PutMapping("/reject/{projectCode}")
    public R<Boolean> reject(@Validated @RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode) {
        log.info("[OpenCascadeApplyController - reject]: 拒绝入云申请, header={}, projectCode={}", header, projectCode);
        return cascadeCloudApplyService.reject(projectCode);
    }
}
