package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.vo.ProjectNoticeObjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectNoticeObjectMapper;
import com.aurine.cloudx.estate.entity.ProjectNoticeObject;
import com.aurine.cloudx.estate.service.ProjectNoticeObjectService;
import org.springframework.stereotype.Service;

/**
 * 项目信息发布对象配置表(ProjectNoticeObject)表服务实现类
 *
 * @author xull
 * @since 2021-02-07 17:15:32
 */
@Service
public class ProjectNoticeObjectServiceImpl extends ServiceImpl<ProjectNoticeObjectMapper, ProjectNoticeObject> implements ProjectNoticeObjectService {

    @Override
    public Page<ProjectNoticeObjectVo> pageNoticeObject(Page<ProjectNoticeObjectVo> page,String noticeId,String buildName,String unitName ,String houseName) {
        return baseMapper.pageNoticeObject(page,noticeId,buildName,unitName,houseName);
    }
}
