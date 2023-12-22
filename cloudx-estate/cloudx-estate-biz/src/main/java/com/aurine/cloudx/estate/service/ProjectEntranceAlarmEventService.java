

package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectEntranceAlarmEvent;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
public interface ProjectEntranceAlarmEventService extends IService<ProjectEntranceAlarmEvent> {

    String OVERRUN = "超限处理";
    String NOT_OVERRUN = "未超限";
    String DETERMINE_CLASS = "1";

    IPage<ProjectEntranceAlarmEventVo> page(Page page, ProjectEntranceAlarmEventVo vo);

    /**
     * 用于小程序和app
     * @param page
     * @param vo
     * @return
     */
    IPage<ProjectEntranceAlarmEventVo>  appPage(Page page, ProjectEntranceAlarmEventVo vo);

    ProjectEntranceAlarmEventVo num(Integer projectId, Integer tenantId);
    
    ProjectEntranceAlarmEventVo allNum(Integer projectId, Integer tenantId);

    boolean setStatus(ProjectEntranceAlarmEventVo vo);

    boolean putStatus(ProjectEntranceAlarmEventVo vo);

    boolean putBatchById(ProjectEntranceAlarmEventVo vo);

    ProjectEntranceAlarmEventVo getById(String eventId);

    boolean allHandle(ProjectEntranceAlarmEventVo vo);

    boolean save(ProjectEntranceAlarmEventVo vo);

    /**
     * 获取当天的警报事件数量
     * @author: 王伟
     * @since :2020-09-03
     * @return
     */
    Integer countCurrDay();

    /**
     * 统计月份的数据
     *
     * @return Integer
     */
    Integer countByMonth(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

    /**
     * 统计月份的未处理报警
     *
     * @return Integer
     */
    Integer countByMonthOff(@Param("projectId")Integer projectId, @Param("tenantId")Integer tenantId, @Param("date")String date);

    Integer countByOff();


    Integer findCount(Integer projectId, Integer tenantId);


    /**
     * 判断处理是否超限
     * @param start
     * @param end
     * @return
     */
    Boolean checkOverrun(LocalDateTime start, LocalDateTime end);

    /**
     * 获取未超限字符串
     * @return
     */
    String getNotOverrun();

    /**
     * 获取超限处理字符串
     * @return
     */
    String getOverrun();

    String getDetermineClass();
}
