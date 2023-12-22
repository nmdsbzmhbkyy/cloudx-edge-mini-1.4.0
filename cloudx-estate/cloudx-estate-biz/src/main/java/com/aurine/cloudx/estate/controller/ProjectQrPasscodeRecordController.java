package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.data.base.PageParam;
import com.aurine.cloudx.estate.cert.vo.R;
import com.aurine.cloudx.estate.dto.qrPasscode.QrPasscodePageDto;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeRecordService;
import com.aurine.cloudx.estate.vo.qrPasscode.DeleteProjectQrPasscodeRecordVo;
import com.aurine.cloudx.estate.vo.qrPasscode.SaveProjectQrPasscodeRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "二维码保存接口")
@RestController
@RequestMapping("/qr-passcode")
public class ProjectQrPasscodeRecordController {
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

    @GetMapping("page")
    public R<Page<QrPasscodePageDto>> page(Page<Object> page){
        return R.ok(qrPasscodeRecordService.queryPage(page));
    }
}
