package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectNoticeTemplate;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateForm;
import com.aurine.cloudx.estate.vo.ProjectNoticeTemplateVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 推送中心消息模板配置(ProjectNoticeTemplate)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-14 09:34:24
 */
@Mapper
public interface ProjectNoticeTemplateMapper extends BaseMapper<ProjectNoticeTemplate> {


    @SqlParser(filter = true)
    IPage<ProjectNoticeTemplateVo> pageVo(IPage page, @Param("query") ProjectNoticeTemplateForm projectNoticeTemplate);

    @SqlParser(filter = true)
    boolean saveTemplate(@Param("data") ProjectNoticeTemplate projectNoticeTemplate);

    @SqlParser(filter = true)
    ProjectNoticeTemplate getVoById(@Param("id") String id);

    @SqlParser(filter = true)
    boolean updateActiveById(@Param("id") String id, @Param("isActive") String isActive);

    @SqlParser(filter = true)
    Integer countByTypeId(@Param("typeId") String typeId);
}