package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.CallBackURIConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.callback.HuaweiCallbackService;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 华为中台，图片临时权限接口
 * @ClassName: HuaweiImgTempAuthController
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-08 16:10
 * @Copyright:
 */
@RestController
@AllArgsConstructor
@RequestMapping(CallBackURIConstant.DEVICE_MIDDLE_HUAWEI_TEMP_AUTH)
@Slf4j
@Api(value = "HuaweiImgTempAuthController", tags = "华为中台临时赋权回调API")
@Deprecated
public class HuaweiImgTempAuthController {


    @Resource
    private HuaweiCallbackService huaweiCallbackService;


    /**
     * 通过临时赋权，回显图片
     *
     * @param token
     * @return
     */
    @Inner(value = false)
    @PostMapping("/img/{token}")
    public JSONObject img(@PathVariable("token") String token) {
        return new JSONObject();
    }

}
