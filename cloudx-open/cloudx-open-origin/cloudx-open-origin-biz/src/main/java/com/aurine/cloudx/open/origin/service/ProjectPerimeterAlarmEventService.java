package com.aurine.cloudx.open.origin.service;


import com.aurine.cloudx.open.origin.vo.ProjectPerimeterAlarmEventVo;
import com.aurine.cloudx.open.origin.entity.ProjectPerimeterAlarmEvent;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 周界报警
 *
 * @author 邹宇
 * @date 2021-6-8 14:30:55
 */
public interface ProjectPerimeterAlarmEventService extends  IService<ProjectPerimeterAlarmEvent> {

    /**
     * 分页查询
     *
     * @param projectPerimeterAlarmEventVo
     * @return
     */
    Page<ProjectPerimeterAlarmEventVo> pagePerimeterAlarmEvent(ProjectPerimeterAlarmEventVo projectPerimeterAlarmEventVo);


    /**
     * 查询当月报警数
     *
     * @return
     */
    Integer countPolice();


    /**
     * 添加报警记录
     * @param sn
     * @param channel
     * @param type
     * @return
     */
    Boolean saveAlarm(String sn, String channel, String type);

    /**
     * 消除报警
     * @param eventId
     * @return
     */
    Boolean deleteByEventId(String eventId);
    Integer countByOff();
    Integer countByoffxj();
}
