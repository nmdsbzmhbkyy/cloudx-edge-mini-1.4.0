package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.pig4cloud.pigx.common.core.util.R;
import io.lettuce.core.dynamic.annotation.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目投诉服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:34:42
 */
public interface ProjectComplaintRecordService extends IService<ProjectComplaintRecord> {

    /**
     * 投诉服务分页查询
     *
     * @param page
     * @param projectComplaintRecordPageVo
     * @return
     */
    Page<ProjectComplaintRecordPageVo> pageComplaintRecord(Page<ProjectComplaintRecordPageVo> page, @Param("query") ProjectComplaintRecordPageVo projectComplaintRecordPageVo);

    /**
     * 通过complaintId获取投诉详细信息
     *
     * @param complaintId
     * @return
     */
    ProjectComplaintRecordInfoVo getByComplainId(String complaintId);

    /**
     * 通过complaintId删除投诉详细信息
     *
     * @param complaintId
     * @return
     */
    boolean removeByComplainId(String complaintId);

    /**
     * 分页查询投诉信息(微信相关接口)
     * @param page
     * @param type
     * @return
     */
    Page<ProjectComplaintRecordVo> pageByType(Page page, String type,String status,String handler,String phone, String date);

    /**
     * 指派处理人(微信相关接口)
     * @param complaintId
     * @param handler
     * @return
     */
    R setHandler(String complaintId, String handler);

    /**
     * 接单 (微信相关接口)
     * @param complaintId
     * @param handler
     * @return
     */
    R receivingOrders (String complaintId, String handler);
    /**
     * 处理完成(微信相关接口)
     * @param formVo
     * @return
     */
    R setResult(ProjectComplainRecordResultFormVo formVo);

    /**
     * 业主登记(微信相关接口)
     *
     * @param formVo
     * @return
     */
    R request(ProjectComplainRecordRequestFormVo formVo);

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
     * 统计投诉单(微信相关接口)
     * @param staffId
     * @param date
     * @return
     */
    ProjectStaffWorkVo getCount(String staffId, String date);
}
