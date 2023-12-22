package com.aurine.cloudx.estate.controller;

import com.aurine.cloudx.estate.service.ProjectCardHisService;
import com.aurine.cloudx.estate.vo.ProjectCardHisVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 卡操作记录
 *
 * @author:zy
 * @data:2022/10/18 10:18 上午
 */
@RestController
@RequestMapping("/cardHis")
@Api(value = "cardHis", tags = "记录卡的操作记录")
public class ProjectCardHisController {

    @Resource
    private ProjectCardHisService projectCardHisService;


    /**
     * 分页查询卡操作记录
     *
     * @param projectCardVo
     * @return
     */
    @ApiOperation(value = "分页查询卡操作记录", notes = "分页查询卡操作记录")
    @SysLog("分页查询卡操作记录")
    @GetMapping("/page")
    public R page(Page page, ProjectCardHisVo projectCardVo) {
        return R.ok(projectCardHisService.pageVo(page,projectCardVo));
    }
}
