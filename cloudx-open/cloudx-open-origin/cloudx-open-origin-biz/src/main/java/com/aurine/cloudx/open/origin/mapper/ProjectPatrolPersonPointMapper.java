package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectPatrolPersonPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 人员巡更巡点记录(ProjectPatrolPersonPoint)表数据库访问层
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:22
 */
@Mapper
public interface ProjectPatrolPersonPointMapper extends BaseMapper<ProjectPatrolPersonPoint> {

    /**
     * <p>
     *  根据员工ID和日期查询该员工某年某月的巡更情况
     * </p>
     *
     * @param staffId 员工ID
     * @param date 所要查询的日期
     * @param status 所要查询的状态集合
    */
    Integer countStatusByStaffId(@Param("staffId") String staffId, @Param("status") List<String> status, @Param("date") LocalDate date);

}