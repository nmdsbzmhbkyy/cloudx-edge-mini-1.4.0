package com.aurine.cloudx.estate.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 13:36:11
 */
@Mapper
public interface ProjectStaffShiftDetailMapper extends BaseMapper<ProjectStaffShiftDetail> {

}