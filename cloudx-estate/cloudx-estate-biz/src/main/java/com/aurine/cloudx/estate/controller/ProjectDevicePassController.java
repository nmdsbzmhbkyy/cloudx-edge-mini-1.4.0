package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.dto.ProjectDevicePassQRDTO;
import com.aurine.cloudx.estate.service.ProjectDevicePassService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util.AurineQRCodeUnit;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util.HuaweiQRCodeUnit;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description: 通行设备控制器
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-04-25
 * @Copyright:
 */
@RestController
@RequestMapping("projectDevicePass")
@Api(value = "projectDevicePass", tags = "通行设备控制器")
@Slf4j
public class ProjectDevicePassController {

    @Resource
    private ProjectDevicePassService projectDevicePassService;


    /**
     * 获取通行设备二维码文本信息
     *
     * @param qrDto DTO对象
     * @return
     */
    @PostMapping("/qr-code")
    @ApiOperation(value = "获取通行设备二维码文本信息", notes = "获取通行设备二维码文本信息")
    @Inner(false)
    @Validated
    public R getQRCode(@RequestBody ProjectDevicePassQRDTO qrDto) {

        //参数校验
        //执行
        String result = projectDevicePassService.getQRCode(qrDto);

        log.info("二维码返回结果：{}", result);
        switch (result) {
            case "1000":
                return R.failed("住户无房屋");
            case "1001":
                return R.failed("住户房屋获取框架号异常");
            case "2000":
                return R.failed("无访客信息");
            case "3000":
                return R.failed("暂不支持该人员类型");
            case "5000":
                return R.failed("内部错误");
            default:
                return R.ok(result);
        }

    }

    /**
     * 解密V1 QR码
     *
     * @return
     */
    @PostMapping("/decode-qr/{version}")
    @ApiOperation(value = "解密V1 QR码", notes = "解密V1 QR码")
    @Inner(false)
    public R decodeV1Qr(@PathVariable("version") String version, @RequestParam("code") String qrCode) throws Exception {
        //参数校验
        //执行
        if (StringUtils.equalsIgnoreCase(version, "v1")) {
            return R.ok(  AurineQRCodeUnit.getInstance().decrypt(qrCode));
        } else {
            return R.ok(  HuaweiQRCodeUnit.getInstance().qrcodeVerify(qrCode,10000292,"10000202"));
        }


    }

}
