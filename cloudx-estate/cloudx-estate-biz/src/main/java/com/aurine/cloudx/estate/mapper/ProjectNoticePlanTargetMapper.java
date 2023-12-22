package com.aurine.cloudx.estate.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectNoticePlanTarget;

/**
 * 住户通知计划发送对象(ProjectNoticePlanTarget)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-15 11:41:39
 */
@Mapper
public interface ProjectNoticePlanTargetMapper extends BaseMapper<ProjectNoticePlanTarget> {

}