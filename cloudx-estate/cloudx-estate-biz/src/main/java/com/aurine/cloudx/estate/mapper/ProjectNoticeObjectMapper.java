package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectNoticeObjectVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectNoticeObject;
import org.apache.ibatis.annotations.Param;

/**
 * 项目信息发布对象配置表(ProjectNoticeObject)表数据库访问层
 *
 * @author xull
 * @since 2021-02-07 17:15:32
 */
@Mapper
public interface ProjectNoticeObjectMapper extends BaseMapper<ProjectNoticeObject> {

    Page<ProjectNoticeObjectVo> pageNoticeObject(Page<ProjectNoticeObjectVo> page,@Param("noticeId")String noticeId, @Param("buildName")String buildName,@Param("unitName") String unitName,@Param("houseName")String houseName);
}
