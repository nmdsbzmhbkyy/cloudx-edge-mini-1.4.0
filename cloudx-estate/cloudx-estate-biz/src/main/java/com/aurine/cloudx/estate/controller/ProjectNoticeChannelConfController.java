package com.aurine.cloudx.estate.controller;
import com.aurine.cloudx.estate.service.ProjectNoticeChannelConfService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.common.core.util.R;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurine.cloudx.estate.entity.ProjectNoticeChannelConf;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
import javax.annotation.Resource;
import java.util.List;

/**
 * 消息推送渠道设置(ProjectNoticeChannelConf)表控制层
 *
 * @author makejava
 * @since 2020-12-11 17:18:19
 */
@RestController
@RequestMapping("projectNoticeChannelConf")
@Api(value="projectNoticeChannelConf",tags="消息推送渠道设置")
public class ProjectNoticeChannelConfController  {
    /**
     * 服务对象
     */
    @Resource
    private ProjectNoticeChannelConfService projectNoticeChannelConfService;

    /**
     * 根据项目id获取已经关闭的推送渠道
     *
     * @param projectId 项目id
     * @return 单条数据
     */
    @GetMapping("getCloseChannelByProjectId/{projectId}")
    @ApiOperation(value = "根据项目id获取已经关闭的推送渠道", notes = "根据项目id获取已经关闭的推送渠道")
    public R getCloseChannelByProjectId(@PathVariable("projectId") Integer projectId) {
        return R.ok(this.projectNoticeChannelConfService.getCloseChannelByProjectId(projectId));
    }

    /**
     * 新增数据
     *
     * @param projectNoticeChannelConf 实体对象
     * @return 新增结果
     */
    @PostMapping
    @ApiOperation(value = "新增数据", notes = "新增projectNoticeChannelConf数据")
    public R insert(@RequestBody ProjectNoticeChannelConf projectNoticeChannelConf) {
        return R.ok(this.projectNoticeChannelConfService.save(projectNoticeChannelConf));
    }


    /**
     *
     * 开启消息渠道
     * @param channelId
     * @param projectId
     * @return
     */
    @DeleteMapping("openChannel/{channelId}/{projectId}")
    @ApiOperation(value = "开启消息渠道", notes = "开启消息渠道")
    public R openChannel(@PathVariable("channelId") String channelId, @PathVariable("projectId") Integer projectId) {
        return R.ok(this.projectNoticeChannelConfService.remove(Wrappers.lambdaQuery(ProjectNoticeChannelConf.class)
                .eq(ProjectNoticeChannelConf::getProjectId,projectId)
                .eq(ProjectNoticeChannelConf::getChannelId, channelId)));
    }
}