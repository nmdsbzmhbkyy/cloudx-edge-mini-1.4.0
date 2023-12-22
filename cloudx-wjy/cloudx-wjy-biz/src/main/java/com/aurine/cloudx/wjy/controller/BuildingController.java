package com.aurine.cloudx.wjy.controller;

import com.aurine.cloudx.wjy.vo.BuildingVo;
import com.aurine.cloudx.wjy.service.BuildingService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 楼栋管理
 * @author ：huangjj
 * @date ：2021/4/15
 * @description：楼栋管理
 */
@RestController
@RequestMapping("/building")
@Api(value = "building", tags = "楼栋接口")
public class BuildingController {
    @Resource
    BuildingService buildingService;
    /**
     * 功能描述: 添加楼栋单元信息
     *
     * @author huangjj
     * @date 2021/4/15
     * @param buildingVo 楼栋数据对象
     * @return 返回添加结果
    */
    @PostMapping("/add")
    @Inner(value = false)
    @ApiOperation(value = "楼栋添加接口", notes = "楼栋添加接口")
    public R addBuilding(@RequestBody BuildingVo buildingVo){
        return buildingService.addBuilding(buildingVo);
    }

}
