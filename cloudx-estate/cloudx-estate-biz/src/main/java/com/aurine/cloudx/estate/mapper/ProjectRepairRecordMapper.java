package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.vo.ProjectRepairRecordPageVo;
import com.aurine.cloudx.estate.vo.ProjectRepairRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aurine.cloudx.estate.entity.ProjectRepairRecord;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目报修服务记录
 *
 * @author guhl@aurine.cn
 * @date 2020-07-20 13:36:01
 */
@Mapper
public interface ProjectRepairRecordMapper extends BaseMapper<ProjectRepairRecord> {

    /**
     * 报修服务分页查询
     *
     * @param page
     * @param projectRepairRecordPageVo
     * @return
     */
    Page<ProjectRepairRecordPageVo> pageRepairRecord(Page<ProjectRepairRecordPageVo> page, @Param("query") ProjectRepairRecordPageVo projectRepairRecordPageVo);

    /**
     * 报修服务分页查询（微信相关接口）
     * @param page 分页查询对象
     * @param type 类型
     * @param handler 处理人
     * @param phone 联系人
     * @return
     */
    Page<ProjectRepairRecordVo> pageByType(Page page,@Param("type") String type,@Param("status") String status,@Param("handler") String handler,@Param("phone")String phone, @Param("date")String date);

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
     *  根据状态集合以及员工ID获取到这些状态的数量
     * </p>
     *
     * @param staffId 员工ID
     * @param status 状态列集合
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

    String getStatusByType(@Param("status") String status, @Param("type") String type);

}
