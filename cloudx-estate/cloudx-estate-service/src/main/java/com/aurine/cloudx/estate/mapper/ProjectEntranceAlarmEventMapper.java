

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@Mapper
public interface ProjectEntranceAlarmEventMapper extends BaseMapper<ProjectEntranceAlarmEvent> {

    IPage<ProjectEntranceAlarmEventVo> select(Page page, @Param("param") ProjectEntranceAlarmEventVo param);

    ProjectEntranceAlarmEventVo findNum(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    ProjectEntranceAlarmEventVo findById(@Param("eventId") String eventId, @Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    List<String> findAllEventId(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    Integer countCurrDayEvent(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    Integer countByMonth(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

    Integer countByMonthOff(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

}
