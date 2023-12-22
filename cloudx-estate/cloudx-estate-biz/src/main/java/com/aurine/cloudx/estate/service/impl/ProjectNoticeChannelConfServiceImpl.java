package com.aurine.cloudx.estate.service.impl;
import com.aurine.cloudx.estate.service.ProjectNoticeChannelConfService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectNoticeChannelConfMapper;
import com.aurine.cloudx.estate.entity.ProjectNoticeChannelConf;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息推送渠道设置(ProjectNoticeChannelConf)表服务实现类
 *
 * @author makejava
 * @since 2020-12-11 17:18:19
 */
@Service
public class ProjectNoticeChannelConfServiceImpl extends ServiceImpl<ProjectNoticeChannelConfMapper, ProjectNoticeChannelConf> implements ProjectNoticeChannelConfService {

    @Override
    public List<ProjectNoticeChannelConf> getCloseChannelByProjectId(Integer projectId) {
        return super.list(Wrappers.lambdaQuery(ProjectNoticeChannelConf.class).eq(ProjectNoticeChannelConf::getProjectId, projectId)
                .eq(ProjectNoticeChannelConf::getIsActive, "0"));
    }
}