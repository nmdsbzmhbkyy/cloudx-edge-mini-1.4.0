package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectInspectTaskStaff;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)表数据库访问层
 *
 * @author  * @author 王良俊
 * @since 2020-10-26 11:41:51
 */
@Mapper
public interface ProjectInspectTaskStaffMapper extends BaseMapper<ProjectInspectTaskStaff> {

    /**
     * <p>
     *  根据员工ID和传入的状态集合获取到该员工这些状态的数量
     * </p>
     *
     * @param date 所要查询的日期
     * @param staffId 员工ID
     * @param status 状态集合
     */
    Integer countCompleteByStaffId(@Param("staffId") String staffId, @Param("status") List<String> status, @Param("date") LocalDate date);

}