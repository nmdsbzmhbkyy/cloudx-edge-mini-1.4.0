package com.aurine.cloudx.estate.service;


import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.pig4cloud.pigx.common.core.util.R;
import io.lettuce.core.dynamic.annotation.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目报修服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:36:01
 */
public interface ProjectRepairRecordService extends IService<ProjectRepairRecord> {

    /**
     * 报修服务分页查询
     *
     * @param page
     * @param projectRepairRecordPageVo
     * @return
     */
    Page<ProjectRepairRecordPageVo> pageRepairRecord(Page<ProjectRepairRecordPageVo> page, @Param("query") ProjectRepairRecordPageVo projectRepairRecordPageVo);

    /**
     * 根据报修id获取报修信息
     *
     * @param repairId
     * @return
     */
    ProjectRepairRecordInfoVo getByRepairId(String repairId);

    /**
     * 分页查询报修信息(微信相关接口)
     * @param page 分页参数
     * @param type 类型
     * @param handler 处理人
     * @return
     */
    Page<ProjectRepairRecordVo> pageByType(Page page, String type,String status, String  handler,String phone, String date);




    /**
     * 指派处理人(微信相关接口)
     * @param recordId
     * @param handler
     * @return
     */
    R setHandler(String recordId, String handler);

    /**
     * 处理完成(微信相关接口)
     * @param formVo
     * @return
     */
    R setResult(ProjectRepairRecordResultFormVo formVo);

    /**
     * 登记(微信相关接口)
     * @param formVo
     * @return
     */
    R request(ProjectRepairRecordRequestFormVo formVo);

    /**
     * 根据月份统计数据
     * @param date
     * @return
     */
    Integer countByMonth(String date);

    /**
     * 统计未完成的数据
     * @param date
     * @return
     */
    Integer countByOff();


    /**
     * <p>
     * 根据传入的状态集合以及员工ID获取到该员工处于这些状态数量
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @param status  要统计的状态集合
     * @author: 王良俊
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

    /**
     * 统计报修单(微信相关接口)
     * @param staffId
     * @param date
     * @return
     */
    ProjectStaffWorkVo getCount(String staffId, String date);

    /**
     * 接单(微信相关接口)
     * @param recordId
     * @param handler
     * @return
     */
    R receivingOrders(String recordId, String handler);
}
