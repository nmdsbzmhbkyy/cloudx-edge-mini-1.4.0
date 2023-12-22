package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectNoticeChannelConf;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 消息推送渠道设置(ProjectNoticeChannelConf)表服务接口
 *
 * @author makejava
 * @since 2020-12-11 17:18:19
 */
public interface ProjectNoticeChannelConfService extends IService<ProjectNoticeChannelConf> {
    /**
     *
     * 根据项目id获取已经关闭的推送渠道
     * @param projectId
     * @return
     */
    List<ProjectNoticeChannelConf> getCloseChannelByProjectId(Integer projectId);
}