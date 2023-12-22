package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.estate.dto.OpenApiProjectHouseServiceDto;
import com.aurine.cloudx.estate.service.OpenApiProjectHouseServiceService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 开放平台内部房屋增值服务管理
 *
 * @author : Qiu
 * @date : 2022/7/14 10:22
 */

@RestController
@RequestMapping("/open/inner/house/service")
@Api(value = "openInnerHouseService", tags = "开放平台内部房屋增值服务管理", hidden = true)
@AllArgsConstructor
@Slf4j
@Inner
public class OpenApiProjectHouseServiceController {

    private final OpenApiProjectHouseServiceService openApiProjectHouseServiceService;

    /**
     * 开放平台内部为房屋新增增值服务
     *
     * @param dto 新增的条件
     * @return 返回新增结果
     */
    @ApiOperation(value = "开放平台内部为房屋新增增值服务", notes = "开放平台内部为房屋新增增值服务")
    @SysLog("开放平台内部新增项目房屋")
    @PostMapping
    public R<Boolean> save(@RequestBody OpenApiProjectHouseServiceDto dto) {
        log.info("[OpenApiProjectHouseServiceController - save]: 开放平台内部为房屋新增增值服务, dto={}", JSONObject.toJSONString(dto));
        try {
            return openApiProjectHouseServiceService.save(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 开放平台内部为房屋删除增值服务
     *
     * @param dto 要删除的条件
     * @return 返回删除结果
     */
    @ApiOperation(value = "开放平台内部为房屋删除增值服务", notes = "开放平台内部为房屋删除增值服务")
    @SysLog("开放平台内部为房屋删除增值服务")
    @DeleteMapping
    public R<Boolean> delete(@RequestBody OpenApiProjectHouseServiceDto dto) {
        log.info("[OpenApiProjectHouseServiceController - delete]: 开放平台内部为房屋删除增值服务, dto={}", JSONObject.toJSONString(dto));
        try {
            return openApiProjectHouseServiceService.delete(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}
