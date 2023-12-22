package com.aurine.cloudx.estate.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectTrainingFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 培训资料表(ProjectTrainingFile)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-13 14:29:10
 */
@Mapper
public interface ProjectTrainingFileMapper extends BaseMapper<ProjectTrainingFile> {

}