package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectAddBlacklistFaceDto;
import com.aurine.cloudx.estate.service.OpenApiProjectBlacklistAttrService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/05/17 10:54
 * @Package: com.aurine.openv2.controller
 * @Version: 1.0
 * @Remarks:
 **/
@Slf4j
@RestController
@RequestMapping("/open/projectBlacklistAttr/inner")
@Api(value = "face", tags = "黑名单属性管理", hidden = true)
@Inner
public class OpenApiProjectBlacklistAttrController {
    @Resource
    private OpenApiProjectBlacklistAttrService openApiProjectBlacklistAttrService;

    /**
     * 新增黑名单人脸到项目人脸库
     *
     * @param dto 新增黑名单人脸Dto
     * @return R
     */
    @ApiOperation(value = "保存黑名单人脸数据", notes = "保存黑名单人脸数据")
    @SysLog("保存黑名单人脸数据")
    @PostMapping("/saveFaceBlacklist")
    public R saveFaceBlacklist(@RequestBody OpenApiProjectAddBlacklistFaceDto dto) {
        log.info("[OpenApiProjectBlacklistAttrController - saveFaceBlacklist]: 保存黑名单人脸数据, dto={}", JSONObject.toJSONString(dto));
        try {
            return openApiProjectBlacklistAttrService.saveFaceBlacklist(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }


    /**
     * 根据第三方人脸id删除人脸黑名单
     *
     * @param thirdFaceId 第三方人脸id
     * @return R
     */
    @ApiOperation(value = "根据第三方人脸id删除人脸黑名单", notes = "根据第三方人脸id删除人脸黑名单")
    @SysLog("根据第三方人脸id删除人脸黑名单")
    @DeleteMapping("/delFaceBlacklist")
    public R delFaceBlacklist( @RequestParam(value = "thirdFaceId", required = true) String thirdFaceId) {
        log.info("[OpenApiProjectBlacklistAttrController - delFaceBlacklist]: 根据第三方人脸id删除人脸黑名单, thirdFaceId={}", JSONObject.toJSONString(thirdFaceId));
        try {
            return openApiProjectBlacklistAttrService.delFaceBlacklist(thirdFaceId);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

}
