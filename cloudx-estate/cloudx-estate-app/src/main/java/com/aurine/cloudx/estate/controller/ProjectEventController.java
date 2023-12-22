

package com.aurine.cloudx.estate.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.service.ProjectEntranceEventService;
import com.aurine.cloudx.estate.service.ProjectPersonInfoService;
import com.aurine.cloudx.estate.vo.AppPage;
import com.aurine.cloudx.estate.vo.ProjectAppEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * 通行事件记录
 *
 * @author pigx code generator
 * @date 2020-05-20 13:27:59
 */
@RestController
@AllArgsConstructor
@RequestMapping("/event" )
@Api(value = "event", tags = "通行事件记录管理")
public class ProjectEventController {

    @Resource
    private ProjectPersonInfoService projectPersonInfoService;

    private final ProjectEntranceEventService projectEntranceEventService;

    @ApiOperation(value = "获取某天的开门记录列表（开门记录）", notes = "获取某天的开门记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "size", value = "单页条数", paramType = "query"),
            @ApiImplicitParam(name = "current", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM-dd)",  paramType = "query")
    })
    @GetMapping("/page")
    public R<IPage<ProjectAppEventVo>> getEventPage (@RequestParam(value = "size",required = false) Long size,
                                                     @RequestParam(value = "current",required = false) Long current,
                                                     @RequestParam(value = "date",required = false) String date) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        AppPage page = new AppPage(current, size);
        return R.ok(projectEntranceEventService.getPageByPersonId(page, projectPersonInfo.getPersonId(), date));
    }

    @ApiOperation(value = "获取某月的开门时间列表（开门记录）", notes = "获取某月的开门时间列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "授权token", required = true, paramType = "header"),
            @ApiImplicitParam(name = "PROJECT-ID", value = "小区Id", required = true, paramType = "header"),
            @ApiImplicitParam(name = "date", value = "日期(格式为 yyyy-MM)",  paramType = "query")
    })
    @GetMapping("/list")
    public R<List<String>> getEventList (@RequestParam(value = "date",required = false) String date) {
        ProjectPersonInfo projectPersonInfo = projectPersonInfoService.getPersonByOwner();
        //判断业主访客是否存在并且申请已经通过
        if (ObjectUtil.isEmpty(projectPersonInfo)) {
            return R.failed("您在该小区下还未登记");
        }
        List<String> eventTimeList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
        projectEntranceEventService.getListByPersonId(projectPersonInfo.getPersonId(), date).stream()
                .forEach(e -> {
                    Boolean isExist = false;
                    for (String eventTime : eventTimeList) {
                        if (LocalDateTime.parse(e.getEventTime(),dateTimeFormatter).getDayOfMonth() == LocalDateTime.parse(eventTime, dateTimeFormatter).getDayOfMonth()) {
                            isExist = true;
                        }
                    }
                    if (!isExist) {
                        eventTimeList.add(e.getEventTime());
                    }
                });
        return R.ok(eventTimeList);
    }

}
