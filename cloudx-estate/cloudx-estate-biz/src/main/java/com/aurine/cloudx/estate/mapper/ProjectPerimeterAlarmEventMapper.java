package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmEvent;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.vo.ProjectPerimeterAlarmEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */

@Mapper
public interface ProjectPerimeterAlarmEventMapper extends BaseMapper<ProjectPerimeterAlarmEvent> {


    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmEventVo
     * @return
     */
    List<ProjectPerimeterAlarmEventVo> selectAll(ProjectPerimeterAlarmEventVo projectPerimeterAlarmEventVo);

    /**
     * 计数
     *
     * @param projectPerimeterAlarmEventVo
     * @return
     */
    Integer getCount(ProjectPerimeterAlarmEventVo projectPerimeterAlarmEventVo);

    /**
     * 通过主机sn和防区channel查询设备信息
     *
     * @param sn
     * @param channel
     * @return
     */
    ProjectPerimeterAlarmEvent getAlarm(@Param("sn") String sn, @Param("channel") String channel);

    /**
     * 通过主机deviceId和防区channelNo和ModuleId查询设备信息
     *
     * @param sn
     * @param channel
     * @return
     */
    ProjectPerimeterAlarmEvent getAlarmArea(@Param("deviceId") String deviceId, @Param("module") String module, @Param("channel") String channel);

    Integer countByoffxj();

    ProjectPerimeterAlarmEvent getAlarmEvent(@Param("devId") String devId, @Param("module") String module, @Param("channel") String channel, @Param("errorType") String errorType);


}