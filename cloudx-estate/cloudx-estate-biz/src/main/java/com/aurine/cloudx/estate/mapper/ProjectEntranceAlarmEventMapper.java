

package com.aurine.cloudx.estate.mapper;

import com.aurine.cloudx.estate.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.openapi.ToOpenApi;
import com.aurine.cloudx.estate.openapi.enums.OpenApiServiceNameEnum;
import com.aurine.cloudx.estate.openapi.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.annotation.SqlParser;
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
@ToOpenApi(serviceType = OpenPushSubscribeCallbackTypeEnum.EVENT, serviceName = OpenApiServiceNameEnum.ALARM_EVENT)
@Mapper
public interface ProjectEntranceAlarmEventMapper extends BaseMapper<ProjectEntranceAlarmEvent> {

    IPage<ProjectEntranceAlarmEventVo> select(Page page, @Param("param") ProjectEntranceAlarmEventVo param);

    /**
     * 小程序不带操作人的查询
     * @param page
     * @param param
     * @return
     */
    @SqlParser(filter=true)
    IPage<ProjectEntranceAlarmEventVo>  appSelect(Page page, @Param("param") ProjectEntranceAlarmEventVo param);

    /**
     * 小程序带操作人的查询
     * @param page
     * @param param
     * @return
     */
    IPage<ProjectEntranceAlarmEventVo>  operatorAppSelect(Page page, @Param("param") ProjectEntranceAlarmEventVo param);

    ProjectEntranceAlarmEventVo findNum(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);
    
    ProjectEntranceAlarmEventVo countNum(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    ProjectEntranceAlarmEventVo findById(@Param("eventId") String eventId, @Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    List<String> findAllEventId(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    Integer countCurrDayEvent(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    Integer countByMonth(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

    Integer countByMonthOff(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

    /**
     * 查询拥有周界报警菜单的员工
     * @return
     */
    List<ProjectStaff> getProjectStaff(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId);

    Integer findCount(@Param("projectId") Integer projectId, @Param("tenantId") Integer tenantId);

    /**
     * 获取用户姓名
     * @param userId
     * @return
     */
    String findUserName(@Param("userId")Integer userId);

}
