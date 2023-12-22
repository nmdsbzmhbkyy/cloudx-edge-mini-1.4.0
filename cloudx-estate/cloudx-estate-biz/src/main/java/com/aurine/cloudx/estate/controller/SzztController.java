package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectInfoService;
import com.aurine.cloudx.estate.service.SzztService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
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
    private SzztService szztService;

    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @GetMapping("/table/{tableId}/{projectId}")
    public R getById(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId) {
        if ("6193".equals(tableId)) {
            return R.ok(szztService.findTable6193(projectId));
        } else if ("6194".equals(tableId)) {
            return R.ok(szztService.findTable6194(projectId));
        } else if ("6195".equals(tableId)) {
            return R.ok(szztService.findTable6195(projectId));
        } else if ("6196".equals(tableId)) {
            return R.ok(szztService.findTable6196(projectId));
        } else if ("6197".equals(tableId)) {
            return R.ok(szztService.findTable6197(projectId));
        } else if ("6198".equals(tableId)) {
            return R.ok(szztService.findTable6198(projectId));
        } else if ("6199".equals(tableId)) {
            return R.ok(szztService.findTable6199(projectId));
        }

        return R.failed("tableId不存在");
    }

    /**
     * 通过id查询
     * @param projectId projectId
     * @return R
     */
    @ApiOperation(value = "通过项目编号查询", notes = "通过项目编号查询")
    @PostMapping("/table/{tableId}/{projectId}")
    public R pushData(@PathVariable("tableId") String tableId, @PathVariable("projectId") String projectId) {
        return R.ok(szztService.pushData(tableId, projectId));
    }
}
