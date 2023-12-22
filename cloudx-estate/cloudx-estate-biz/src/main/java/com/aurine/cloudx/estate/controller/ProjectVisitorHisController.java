package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectVisitorHisService;
import com.aurine.cloudx.estate.vo.ProjectVisitorSearchConditionVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ClassName ProjectVisitorHisController
 * @Description
 * @Author linlx
 * @Date 2022/7/7 17:42
 **/
@RestController
@RequestMapping("/serviceVisitorHis")
@Api(value = "serviceVisitorHis", tags = "访客记录管理")
public class ProjectVisitorHisController {

    @Resource
    private ProjectVisitorHisService projectVisitorHisService;

    @GetMapping("/getByPlateNumber/{plateNumber}")
    @Inner
    public R getByPlateNumber(@PathVariable("plateNumber")String plateNumber) {
        return R.ok(projectVisitorHisService.getByPlateNumber(plateNumber));
    }
}
