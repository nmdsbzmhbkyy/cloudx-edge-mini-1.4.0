package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.entity.ProjectDeviceCallEvent;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectDeviceCallEventMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceCallEventService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.vo.ProjectDeviceCallEventVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 项目呼叫事件(ProjectDeviceCallEvent)ServiceImpl
 *
 * @author : Qiu
 * @date : 2020 12 16 9:42
 */
@Service
public class ProjectDeviceCallEventServiceImpl extends ServiceImpl<ProjectDeviceCallEventMapper, ProjectDeviceCallEvent> implements ProjectDeviceCallEventService {

    @Resource
    private ProjectStaffService projectStaffService;

    /**
     * 分页查询呼叫记录
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEvent(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        return baseMapper.pageCallEvent(page, projectDeviceCallEventVo);
    }

    /**
     * 分页查询项目呼叫事件(查询当前登录用户所在项目的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEventByProject(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        projectDeviceCallEventVo.setProjectId(ProjectContextHolder.getProjectId());
        return baseMapper.pageCallEventByProject(page, projectDeviceCallEventVo);
    }

    /**
     * 分页查询呼叫记录(查询当前登录用户的员工ID为接收方的呼叫记录)
     * @param page
     * @param projectDeviceCallEventVo
     * @return
     */
    @Override
    public Page<ProjectDeviceCallEventVo> pageCallEventByStaff(Page page, ProjectDeviceCallEventVo projectDeviceCallEventVo) {
        ProjectStaff staff = projectStaffService.getOne(new QueryWrapper<ProjectStaff>().lambda()
                .eq(ProjectStaff::getUserId, SecurityUtils.getUser().getId())
                .eq(ProjectStaff::getProjectId, ProjectContextHolder.getProjectId()));
        if (staff == null) {
            return new Page<>();
        }
        projectDeviceCallEventVo.setStaffId(staff.getStaffId());
        return baseMapper.pageCallEventByStaff(page, projectDeviceCallEventVo);
    }
}
