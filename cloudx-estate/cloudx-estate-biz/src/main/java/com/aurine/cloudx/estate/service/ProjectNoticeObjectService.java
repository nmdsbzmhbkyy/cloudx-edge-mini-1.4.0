package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.ProjectNoticeObjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectNoticeObject;

/**
 * 项目信息发布对象配置表(ProjectNoticeObject)表服务接口
 *
 * @author xull
 * @since 2021-02-07 17:15:31
 */
public interface ProjectNoticeObjectService extends IService<ProjectNoticeObject> {

    Page<ProjectNoticeObjectVo> pageNoticeObject(Page<ProjectNoticeObjectVo> page,String noticeId,String buildName,String unitName ,String houseName);
}
