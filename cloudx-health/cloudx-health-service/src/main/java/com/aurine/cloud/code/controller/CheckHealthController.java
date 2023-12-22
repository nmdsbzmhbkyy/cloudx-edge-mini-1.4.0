package com.aurine.cloud.code.controller;


import com.alibaba.fastjson.JSONObject;
import com.aurine.cloud.code.entity.IdCard;
import com.aurine.cloud.code.entity.QrCard;
import com.aurine.cloud.code.entity.dto.ResultCode;
import com.aurine.cloud.code.factory.HealthFactoryProducer;
import com.aurine.cloud.code.util.CheckHealthKafka;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yz
 * @date 2021/6/7
 */
@RestController
@RequestMapping("/code")
public class CheckHealthController {

    @Autowired
    private CheckHealthKafka checkHealthKafka;


    @PostMapping("/idCard")
    @ApiOperation(value = "身份证验证健康码", notes = "身份证验证健康码")
    private R checkHealthByIdCard(@RequestBody IdCard idCard) {

/*
        ResultCode resultCode = HealthFactoryProducer.getFactory(idCard.getQrCodeType()).getPassWayHealthService(idCard.getVersionName()).CheckHealthByCode(idCard);

        if (resultCode.getSuccess()) {

            return R.ok(resultCode);
        } else {

            return R.failed(resultCode.getMsg());
        }
*/

        return R.ok(true);
    }

    @PostMapping("/qr")
    @ApiOperation(value = "二维码验证健康码", notes = "二维码验证健康码")
    private R checkHealthByQrCard(@RequestBody QrCard qrCard) {
        ResultCode resultCode = HealthFactoryProducer.getFactory(qrCard.getQrCodeType()).getPassWayHealthService(qrCard.getVersionName()).CheckHealth(qrCard);
        if (resultCode.getSuccess()) {
            return R.ok(resultCode.getSuccess());
        } else {
            return R.failed(resultCode.getMsg());
        }
    }


}
