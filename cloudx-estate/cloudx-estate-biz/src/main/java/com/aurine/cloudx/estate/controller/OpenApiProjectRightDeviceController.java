package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectBlacklistFaceStatusDto;
import com.aurine.cloudx.estate.dto.OpenApiProjectStaffDto;
import com.aurine.cloudx.estate.service.OpenApiProjectRightDeviceService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: 顾文豪
 * @Date: 2022/06/28 14:31
 * @Package: com.aurine.cloudx.estate.controller
 * @Version: 1.0
 * @Remarks: openApi内部调用方法，解决分布式事务
 **/
@RestController
@RequestMapping("/open/projectRightDevice/inner")
@Api(value = "openApiProjectRightDevice", tags = "openApi权限设备关系表，记录权限（认证介质）的下发状态管理", hidden = true)
@Inner
public class OpenApiProjectRightDeviceController {
    @Resource
    private OpenApiProjectRightDeviceService openApiProjectRightDeviceService;

    /**
     * 人脸黑名单下发状态查询
     *
     * @param thirdFaceId 第三方人脸id
     * @return R
     */
    @ApiOperation(value = "人脸黑名单下发状态查询", notes = "人脸黑名单下发状态查询")
    @SysLog("权限（认证介质）的下发状态管理 - 人脸黑名单下发状态查询")
    @GetMapping
    public R<List<OpenApiProjectBlacklistFaceStatusDto>> selectFaceBlacklist(@RequestParam("thirdFaceId") String thirdFaceId) {
        try {
            return openApiProjectRightDeviceService.selectFaceBlacklist(thirdFaceId);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

}
