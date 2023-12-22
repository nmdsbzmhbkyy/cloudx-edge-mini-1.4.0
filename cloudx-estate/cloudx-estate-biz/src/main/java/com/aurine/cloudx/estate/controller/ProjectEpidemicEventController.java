package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectEpidemicEventService;
import com.aurine.cloudx.estate.vo.ProjectEpidemicEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 疫情记录
 *
 * @author 邹宇
 * @date 2021-6-7 11:08:18
 */
@RestController
@RequestMapping("/projectEpidemic")
@Api(value = "projectEpidemic", tags = "疫情记录表，记录下展示健康码的人相关信息")
public class ProjectEpidemicEventController {

    @Resource
    private ProjectEpidemicEventService projectEpidemicEventService;


    /**
     * 分页查询
     * @param projectEpidemicEventVo
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R fetchList( ProjectEpidemicEventVo projectEpidemicEventVo) {
        return R.ok( projectEpidemicEventService.pageEpidemicEvent(projectEpidemicEventVo));
    }


    /**
     * 统计健康码红码的数量
     */
    @ApiOperation(value = "统计健康码红码的数量", notes = "统计健康码红码的数量")
    @GetMapping("/countRedCode")
    public R countRedCode() {

        return R.ok(projectEpidemicEventService.countRedCode());
    }


}
