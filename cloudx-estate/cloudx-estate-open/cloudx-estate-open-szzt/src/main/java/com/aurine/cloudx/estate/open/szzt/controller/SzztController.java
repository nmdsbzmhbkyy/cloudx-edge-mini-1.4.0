package com.aurine.cloudx.estate.open.szzt.controller;

import com.aurine.cloudx.estate.open.szzt.fegin.RemoteSzztService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 和数字政通对接用的数据接口
 */
@RestController
@AllArgsConstructor
@RequestMapping("/szzt" )
@Api(value = "/szzt", tags = "数字政通数据查询接口")
public class SzztController {
    @Resource
    private RemoteSzztService szztService;

    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @GetMapping("/table/{tableId}/{projectId}")
    public R getById(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId) {
        return szztService.getById(tableId, projectId);
    }

    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @PostMapping("/table/{tableId}/{projectId}")
    public R pushData(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId) {
        return szztService.pushData(tableId, projectId);
    }
}
