package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectDeviceAbnormalService;
import com.aurine.cloudx.estate.vo.ProjectDeviceAbnormalSearchCondition;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * <p>设备异常记录Controller</p>
 *
 * @author : 王良俊
 * @date : 2021-09-26 14:02:50
 */
@RestController
@AllArgsConstructor
@RequestMapping("/projectDeviceAbnormal")
@Api(value = "projectDeviceAbnormal", tags = "设备异常记录")
public class ProjectDeviceAbnormalController {

    @Resource
    ProjectDeviceAbnormalService projectDeviceAbnormalService;

    /**
     * <p>异常设备分页查询</p>
     *
     * @param condition 查询条件
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getProjectEventPage(Page page, @ModelAttribute ProjectDeviceAbnormalSearchCondition condition) {
        return R.ok(projectDeviceAbnormalService.fetchList(page, condition));
    }

    /**
     * <p>处理（这里处理就是删除记录）</p>
     *
     * @param seq 异常主键
     */
    @ApiOperation(value = "处理", notes = "处理")
    @GetMapping("/handle/{seq}")
    public R handle(@PathVariable("seq") String seq) {
        return R.ok(projectDeviceAbnormalService.removeById(Integer.parseInt(seq)));
    }


}
