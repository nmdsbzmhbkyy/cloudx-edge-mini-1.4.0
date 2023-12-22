package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.cert.vo.R;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeRecordService;
import com.aurine.cloudx.estate.vo.qrPasscode.DeleteProjectQrPasscodeRecordVo;
import com.aurine.cloudx.estate.vo.qrPasscode.SaveProjectQrPasscodeRecordVo;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "qrcode", tags = "二维码管理接口", hidden = true)
@RestController
@RequestMapping("/open/qr-passcode/inner")
@Inner
public class OpenApiProjectQrPasscodeRecordController {
    @Autowired
    private ProjectQrPasscodeRecordService qrPasscodeRecordService;

    @PostMapping
    public R save(@RequestBody @Validated SaveProjectQrPasscodeRecordVo vo){
        qrPasscodeRecordService.saveRecord(vo);
        return R.ok();
    }

    @DeleteMapping
    public R delete(@RequestBody @Validated DeleteProjectQrPasscodeRecordVo vo){
        if (qrPasscodeRecordService.deleteRecord(vo.getUniqueCode())){
            return R.ok();
        }else{
            return R.failed();
        }
    }
}
