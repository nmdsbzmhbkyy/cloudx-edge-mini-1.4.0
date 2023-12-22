package com.aurine.cloudx.open.origin.service;

import com.aurine.cloudx.open.origin.vo.ProjectStaffShiftDetailPageVo;
import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.open.origin.vo.ProjectStaffShiftDetailUpdateVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表服务接口
 *
 * @author makejava
 * @since 2020-07-31 13:36:11
 */
public interface ProjectStaffShiftDetailService extends IService<ProjectStaffShiftDetail> {
    /**
     * 根据员工ID、日期获取 员工出勤天数
     * @param date
     * @return
     */
    Integer getAttendanceDay(String staffId, LocalDate date);

    /**
     * 分页查询人员排班调整
     * @param page
     * @param projectStaffShiftDetailPageVo
     * @return
     */
    Page<ProjectStaffShiftDetailPageVo> pageShiftDetail(Page page, @Param("query") ProjectStaffShiftDetailPageVo projectStaffShiftDetailPageVo);

    /**
     * 更新排班明细
     *
     * @param projectStaffShiftDetailUpdateVo
     * @return
     */
    Boolean updateShiftDetail(ProjectStaffShiftDetailUpdateVo projectStaffShiftDetailUpdateVo);
}