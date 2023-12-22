package com.aurine.cloudx.estate.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;

import java.time.LocalDate;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表服务接口
 *
 * @author makejava
 * @since 2020-07-31 13:36:11
 */
public interface ProjectStaffShiftDetailService extends IService<ProjectStaffShiftDetail> {

    /**
     * 根据员工ID、日期获取 员工应勤天数
     * @param date
     * @return
     */
    Integer getAttendanceDay (String staffId, LocalDate date);


    /**
     * 根据员工ID获取截止到当天的员工应勤天数
     * @return
     */
    Integer getAttendanceDay (String staffId);
}