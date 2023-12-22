package com.aurine.cloudx.open.origin.mapper;

import com.aurine.cloudx.open.origin.vo.ProjectPerimeterAlarmEventVo;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmEvent;
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

    Integer countByoffxj();

}