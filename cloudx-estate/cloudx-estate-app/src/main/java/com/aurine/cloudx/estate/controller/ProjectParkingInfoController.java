

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectParkingInfo;
import com.aurine.cloudx.estate.feign.RemoteParkingInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/car-park")
@Api(value = "car-park", tags = "停车场管理")
public class ProjectParkingInfoController {

    @Resource
    private RemoteParkingInfoService remoteParkingInfoService;

    /**
     * 获取当前项目下所有数据
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取所有停车场信息的列表", notes = "获取所有停车场信息的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "TENANT-ID", value = "租户Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", required = true, value = "小区Id", paramType = "header")
    })
    public R<ProjectParkingInfo> list() {
        return remoteParkingInfoService.list();
    }
}
