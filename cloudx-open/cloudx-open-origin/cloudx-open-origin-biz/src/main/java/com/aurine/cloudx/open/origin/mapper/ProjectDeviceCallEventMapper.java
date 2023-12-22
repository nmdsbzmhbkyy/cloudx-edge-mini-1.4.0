package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceCallEvent;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
}
