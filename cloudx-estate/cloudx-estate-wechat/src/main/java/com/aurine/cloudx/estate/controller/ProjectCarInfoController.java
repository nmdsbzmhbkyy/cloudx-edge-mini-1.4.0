package com.aurine.cloudx.estate.controller;

import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.service.ProjectCarInfoService;
import com.aurine.cloudx.estate.vo.ProjectCarInfoVo;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (LoginContrller)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/13 13:44
 */
@RestController
@RequestMapping("/projectCarInfo")
@Api(value = "projectCarInfo", tags = "车辆信息")
public class ProjectCarInfoController {

    @Resource
    private ProjectCarInfoService projectCarInfoService;

    /**
     * 根据车牌号，获取VO,显示车辆以及所属人员的信息
     *
     * @param plateNumber
     * @return
     */
    @ApiOperation(value ="根据车牌号获取人员信息", notes = "根据车牌号获取人员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "plateNumber", value = "车牌号", required = true, paramType = "query")
    })
    @GetMapping
    public R<ProjectCarInfoVo> getVoByPlateNumber(@RequestParam String plateNumber) {
        ProjectCarInfoVo projectCarInfoVo = projectCarInfoService.getVoByPlateNumber(plateNumber);
        if (ObjectUtil.isEmpty(projectCarInfoVo)) {
            return R.ok();
        }
        return R.ok(projectCarInfoVo);
    }

}
