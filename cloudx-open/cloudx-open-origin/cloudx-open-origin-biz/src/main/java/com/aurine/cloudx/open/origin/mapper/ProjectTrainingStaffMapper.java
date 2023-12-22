package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectTrainingStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 培训员工表(ProjectTrainingStaff)表数据库访问层
 *
 * @author makejava
 * @since 2021-01-13 14:34:29
 */
@Mapper
public interface ProjectTrainingStaffMapper extends BaseMapper<ProjectTrainingStaff> {

    /**
     * 获取参与培训的员工总数
     * @param trainingIds
     * @return
     */
    Integer staffTotalCount(@Param("trainingIds") List<String> trainingIds);
}