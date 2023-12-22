package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectDeviceCallEvent;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectPersonInfo;
import com.aurine.cloudx.estate.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)Mapper
 *
 * @author : Qiu
 * @date : 2020 12 16 8:49
 */
@Mapper
public interface ProjectDeviceCallEventMapper extends BaseMapper<ProjectDeviceCallEvent> {

    /**
     * 分页查询项目呼叫事件
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEvent(Page page, @Param("query") ProjectDeviceCallEventVo projectDeviceCallEventVo);

    /**
     * 分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEventByProject(Page page, @Param("query") ProjectDeviceCallEventVo projectDeviceCallEventVo);

    /**
     * 分页查询项目呼叫事件(查询当前登录用户的员工ID为接收方的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEventByStaff(Page page, @Param("query") ProjectDeviceCallEventVo projectDeviceCallEventVo);

    @SqlParser(filter = true)
    SysUser getPhone(@Param("userId") String userId);

    @SqlParser(filter = true)
    List<ProjectPersonInfo> getPerson(@Param("phone") String phone, @Param("userId") Integer userId, @Param("projectId") Integer projectId);

    @SqlParser(filter = true)
    ProjectHouseInfo getHouse(@Param("callTarget") String callTarget, @Param("projectId") Integer projectId);

    /**
     * <p>根据房屋ID获取框架名</p>
     *
     * @param houseId 房屋id
     * @return 组团名..+楼栋+单元+房屋
     * @author: 顾文豪
     */
    String getFrameNameByHouseId(@Param("houseId") String houseId);
}
