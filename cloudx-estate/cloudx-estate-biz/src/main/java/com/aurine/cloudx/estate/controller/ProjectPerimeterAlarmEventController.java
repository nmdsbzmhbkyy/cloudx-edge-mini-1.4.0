package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.service.ProjectPerimeterAlarmAreaService;
import com.aurine.cloudx.estate.service.ProjectPerimeterAlarmEventService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmEventVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
@RestController
@RequestMapping("/projectPerimeterAlarmEvent")
@Api(value = "projectPerimeterAlarmEvent", tags = "周界报警表")
public class ProjectPerimeterAlarmEventController {


    @Resource
    private ProjectPerimeterAlarmEventService projectPerimeterAlarmEventService;




    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmEventVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<Page<ProjectPerimeterAlarmEventVo>> findAll(ProjectPerimeterAlarmEventVo projectPerimeterAlarmEventVo) {

        return R.ok(projectPerimeterAlarmEventService.pagePerimeterAlarmEvent(projectPerimeterAlarmEventVo));
    }

    /**
     * 查询当月报警数
     *
     * @return
     */
    @ApiOperation(value = "当月报警数", notes = "当月报警数")
    @GetMapping("/countPolice")
    public R countPolice() {
        return R.ok(projectPerimeterAlarmEventService.countPolice());
    }

    /**
     * 查询未处理数
     *
     * @return
     */
    @ApiOperation(value = "未处理数", notes = "未处理数")
    @GetMapping("/countUntreated")
    public R countUntreated() {
        return R.ok(projectPerimeterAlarmEventService.count(new QueryWrapper<ProjectPerimeterAlarmEvent>().eq("execStatus","0")));
    }

    /**
     * 消除警报
     *
     * @return
     */
    @ApiOperation(value = "消除警报", notes = "消除警报")
    @DeleteMapping("/{eventId}")
    public R deleteByEventId(@PathVariable String eventId) {
        return R.ok(projectPerimeterAlarmEventService.deleteByEventId(eventId),"设备有问题或警报已被消除");
    }



}
