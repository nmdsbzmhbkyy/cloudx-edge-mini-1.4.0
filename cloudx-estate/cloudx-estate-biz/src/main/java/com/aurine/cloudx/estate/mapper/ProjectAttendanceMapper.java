package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectAttendance;
import com.aurine.cloudx.estate.vo.ProjectAttdanceSummaryVo;
import com.aurine.cloudx.estate.vo.ProjectAttendanceQueryVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工考勤打卡(ProjectAttendance)表数据库访问层
 *
 * @author xull
 * @since 2021-03-03 10:52:25
 */
@Mapper
public interface ProjectAttendanceMapper extends BaseMapper<ProjectAttendance> {
    
    /** 
     * @description: 考勤情况列表 
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/3/22 15:12
     */
    @SqlParser(filter = true)
    IPage<ProjectAttendanceQueryVo> page(Page page, @Param("query") ProjectAttendanceQueryVo query);
    
    /** 
     * @description: 考勤情况数据 
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/3/22 15:12
     */
    @SqlParser(filter = true)
    List<ProjectAttendanceQueryVo> list(@Param("query") ProjectAttendanceQueryVo query);
    
    /** 
     * @description: 考勤汇总列表 
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/3/22 15:12
     */
    @SqlParser(filter = true)
    IPage <ProjectAttdanceSummaryVo>attendanceSummaryPage(Page page,@Param("query")ProjectAttdanceSummaryVo query);
    
    
    /** 
     * @description: 考勤汇总数据
     * @param:  
     * @return:  
     * @author cjw
     * @date: 2021/3/22 15:13
     */
    @SqlParser(filter = true)
    List <ProjectAttdanceSummaryVo>attendanceSummaryList(@Param("query")ProjectAttdanceSummaryVo query);
}
