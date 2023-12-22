package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceCallEvent;
import com.aurine.cloudx.estate.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)Service
 *
 * @author : Qiu
 * @date : 2020 12 16 9:07
 */
public interface ProjectDeviceCallEventService extends IService<ProjectDeviceCallEvent> {

    /**
     * 分页查询呼叫记录
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEvent(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo);

    /**
     * 分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEventByProject(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo);

    /**
     * 分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    Page<ProjectDeviceCallEventVo> pageCallEventByStaff(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo);
}
