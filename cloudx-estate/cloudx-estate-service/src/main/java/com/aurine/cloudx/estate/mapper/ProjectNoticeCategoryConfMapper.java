package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectNoticeCategoryConf;
import com.aurine.cloudx.estate.vo.ProjectNoticeCategoryConfVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 推送模板类型配置（信息分类），该类型可应用于媒体通知和推送中心(ProjectNoticeCategoryConf)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-14 10:06:46
 */
@Mapper
public interface ProjectNoticeCategoryConfMapper extends BaseMapper<ProjectNoticeCategoryConf> {

    IPage<ProjectNoticeCategoryConfVo> pageVo(Page<ProjectNoticeCategoryConfVo> page, @Param("query") ProjectNoticeCategoryConf projectNoticeCategoryConf);

}