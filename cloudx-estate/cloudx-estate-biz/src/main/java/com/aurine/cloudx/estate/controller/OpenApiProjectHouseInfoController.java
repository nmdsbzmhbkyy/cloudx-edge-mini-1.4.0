package com.aurine.cloudx.estate.controller;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.dto.OpenApiProjectHouseInfoDto;
import com.aurine.cloudx.estate.service.OpenApiProjectHouseInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 开放平台内部房屋管理
 *
 * @author : Qiu
 * @date : 2022/7/13 15:51
 */

@RestController
@RequestMapping("/open/inner/house")
@Api(value = "openInnerHouse", tags = "开放平台内部房屋管理", hidden = true)
@AllArgsConstructor
@Slf4j
@Inner
public class OpenApiProjectHouseInfoController {

    private final OpenApiProjectHouseInfoService openApiProjectHouseInfoService;


    /**
     * 开放平台内部新增项目房屋
     *
     * @param dto 新增的项目房屋
     * @return 返回新增后的项目房屋
     */
    @ApiOperation(value = "开放平台内部新增项目房屋", notes = "开放平台内部新增项目房屋")
    @SysLog("开放平台内部新增项目房屋")
    @PostMapping
    public R<OpenApiProjectHouseInfoDto> save(@RequestBody OpenApiProjectHouseInfoDto dto) {
        log.info("[OpenApiProjectHouseInfoController - save]: 开放平台内部新增项目房屋, dto={}", JSONObject.toJSONString(dto));
        try {
            return openApiProjectHouseInfoService.save(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 开放平台内部修改项目房屋
     *
     * @param dto 修改的项目房屋
     * @return 返回修改后的项目房屋
     */
    @ApiOperation(value = "开放平台内部修改项目房屋", notes = "开放平台内部修改项目房屋")
    @SysLog("开放平台内部修改项目房屋")
    @PutMapping
    public R<OpenApiProjectHouseInfoDto> update(@RequestBody OpenApiProjectHouseInfoDto dto) {
        log.info("[OpenApiProjectHouseInfoController - update]: 开放平台内部修改项目房屋, dto={}", JSONObject.toJSONString(dto));
        try {
            return openApiProjectHouseInfoService.update(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 开放平台内部删除项目房屋
     *
     * @param dto 要删除的条件
     * @return 返回删除结果
     */
    @ApiOperation(value = "开放平台内部删除项目房屋", notes = "开放平台内部删除项目房屋")
    @SysLog("开放平台内部删除项目房屋")
    @DeleteMapping
    public R<Boolean> delete(OpenApiProjectHouseInfoDto dto) {
        log.info("[OpenApiProjectHouseInfoController - delete]: 开放平台内部删除项目房屋, dto={}", JSONObject.toJSONString(dto));

        String buildingUnit = dto.getBuildingUnit();
        String unitId = dto.getUnitId();

        if (StringUtil.isNotBlank(buildingUnit)) {
            if (StringUtil.isBlank(unitId)) dto.setUnitId(buildingUnit);
        } else {
            if (StringUtil.isNotBlank(unitId)) dto.setBuildingUnit(unitId);
        }

        try {
            return openApiProjectHouseInfoService.delete(dto);
        } catch (OpenApiServiceException e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }
}

