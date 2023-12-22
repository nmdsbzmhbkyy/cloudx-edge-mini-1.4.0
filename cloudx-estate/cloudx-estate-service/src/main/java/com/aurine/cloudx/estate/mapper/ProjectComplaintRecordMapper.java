package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectComplaintRecordPageVo;
import com.aurine.cloudx.estate.vo.ProjectComplaintRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectComplaintRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目投诉服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:34:42
 */
@Mapper
public interface ProjectComplaintRecordMapper extends BaseMapper<ProjectComplaintRecord> {

    /**
     * 投诉服务分页查询
     *
     * @param page
     * @param projectComplaintRecordPageVo
     * @return
     */
    Page<ProjectComplaintRecordPageVo> pageComplaintRecord(Page<ProjectComplaintRecordPageVo> page, @Param("query") ProjectComplaintRecordPageVo projectComplaintRecordPageVo);

    /**
     * 投诉服务分页查询（微信相关接口）
     * @param page
     * @param type 类型
     * @param handler 处理人
     * @return
     */
    Page<ProjectComplaintRecordVo> pageByType(Page page,@Param("type") String type,@Param("status") String status,@Param("handler")String handler,@Param("phone")String phone, @Param("date")String date);

    /**
     * 根据月份统计数据
     * @param date
     * @return
     */
    Integer countByMonth(@Param("date")String date);

    /**
     * 统计未完成的数据
     * @param date
     * @return
     */
    Integer countByOff();

    /**
     * <p>
     * 统计未完成的数据
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     * @param status  要统计的状态集合
     * @author: 王良俊
     */
    Integer countStatusByStaffId(@Param("staffId") String staffId, @Param("status") List<String> status, @Param("date") LocalDate date);

    /**
     * <p>
     * 统计数据（微信相关接口）
     * </p>
     *
     * @param staffId 要统计对应状态数量的员工ID
     */
    Integer getCount(@Param("staffId") String staffId, @Param("status") String status, @Param("date") String date);


}
