package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectInspectTaskStaff;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
 * 巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)表服务接口
 *
 * @author 王良俊
 * @since 2020-10-26 11:41:50
 */
public interface ProjectInspectTaskStaffService extends IService<ProjectInspectTaskStaff> {


    /**
     * <p>
     *  根据员工ID和传入的状态集合获取到该员工这些状态的数量
     * </p>
     *
     * @param date 所要查询的日期
     * @param staffId 员工ID
     * @param status 状态集合
    */
    Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date);



    /**
     * <p>
     * 获取到已完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countCompleteByStaffId(String staffId, LocalDate date);


    /**
     * <p>
     * 获取到未完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countUnCompleteByStaffId(String staffId, LocalDate date);


    /**
     * <p>
     * 获取到包含已完成和未完成的数量 根据员工ID
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @author: 王良俊
     */
    Integer countAllByStaffId(String staffId, LocalDate date);

}