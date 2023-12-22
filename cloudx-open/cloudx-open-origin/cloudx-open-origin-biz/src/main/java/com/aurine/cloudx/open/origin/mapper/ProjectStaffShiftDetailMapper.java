package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectStaffShiftDetailPageVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表数据库访问层
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 13:36:11
 */
@Mapper
public interface ProjectStaffShiftDetailMapper extends BaseMapper<ProjectStaffShiftDetail> {

    /**
     * 分页查询员工排班调整
     * @param page
     * @param projectStaffShiftDetailPageVo
     * @return
     */
    Page<ProjectStaffShiftDetailPageVo> pageShiftDetail(Page page, @Param("query") ProjectStaffShiftDetailPageVo projectStaffShiftDetailPageVo);
}