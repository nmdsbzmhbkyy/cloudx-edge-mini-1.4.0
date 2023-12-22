

package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.feign.RemoteParkingInfoService;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@AllArgsConstructor
@RequestMapping("/baseParkingArea")
@Api(value = "baseParkingArea", tags = "停车场管理")
public class ProjectParkingInfoController {

    @Resource
    private RemoteParkingInfoService remoteParkingInfoService;

    /**
     * 获取当前项目下所有数据
     *
     * @return
     */
    @ApiOperation(value = "获取所有停车场信息的列表", notes = "获取所有停车场信息的列表")
    @GetMapping("/list")
    public R list() {
        return remoteParkingInfoService.list();
    }

}
