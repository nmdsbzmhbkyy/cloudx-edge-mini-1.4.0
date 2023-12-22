package com.aurine.cloudx.estate.controller;


import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectPersonInfoUpdatePhoneDto;
import com.aurine.cloudx.estate.service.OpenApiProjectPersonInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 开放平台内部人员管理
 *
 * @author : 顾文豪
 * @date : 2023/11/13 15:07
 */
@RestController
@RequestMapping("/open/personInfo/inner")
@Api(value = "openInnerHouse", tags = "开放平台内部人员管理", hidden = true)
@AllArgsConstructor
@Slf4j
@Inner
public class OpenApiProjectPersonInfoController {

    @Resource
    private OpenApiProjectPersonInfoService openApiProjectPersonInfoService;

    /**
     * 修改人员手机号
     *
     * @param dto 修改人员手机号参数
     * @return R
     */
    @ApiOperation(value = "修改人员手机号", notes = "修改人员手机号")
    @SysLog("修改人员手机号")
    @PutMapping(value = "/updateMobile")
    public R<Boolean> updatePersonPhone(@RequestBody OpenApiProjectPersonInfoUpdatePhoneDto dto) {
        try {
            return openApiProjectPersonInfoService.updatePersonPhone(dto.getOldTelephone(),dto.getNewTelephone());
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
