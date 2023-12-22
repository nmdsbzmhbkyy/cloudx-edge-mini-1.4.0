
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.enums.AlarmEventStatusEnum;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectEntranceAlarmEventMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 报警事件
 *
 * @author 黄阳光
 * @date 2020-06-04 08:30:07
 */
@Service
public class ProjectEntranceAlarmEventServiceImpl extends ServiceImpl<ProjectEntranceAlarmEventMapper, ProjectEntranceAlarmEvent> implements ProjectEntranceAlarmEventService {

    private static final String OVERRUN = "超限处理";
    private static final String NOT_OVERRUN = "未超限";
    private static final String DETERMINE_CLASS = "1";

    @Resource
    ProjectEntranceAlarmEventMapper projectEntranceAlarmEventMapper;
    @Resource
    ProjectAlarmHandleService projectAlarmHandleService;
    @Resource
    ProjectConfigService projectConfigService;
    @Resource
    private ProjectPersonInfoService projectPersonInfoService;
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectDeviceRegionService projectDeviceRegionService;

    @Override
    public IPage<ProjectEntranceAlarmEventVo> page(Page page, ProjectEntranceAlarmEventVo vo) {
        projectConfigService.getConfig();
        IPage<ProjectEntranceAlarmEventVo> result = projectEntranceAlarmEventMapper.select(page, vo);
        for (ProjectEntranceAlarmEventVo alarmVo : result.getRecords()) {
            if (alarmVo.getDeviceRegionId() != null && alarmVo.getDeviceRegionId().equals("1")) {
                alarmVo.setRegionName("小区");
            }
        }
        return result;
    }

    @Override
    public ProjectEntranceAlarmEventVo num(Integer projectId, Integer tenantId) {
        return projectEntranceAlarmEventMapper.findNum(projectId, tenantId);
    }

    @Override
    public boolean setStatus(ProjectEntranceAlarmEventVo vo) {
        //设置当前事件状态为处理中
//        vo.setStatus(AlarmEventStatusEnum.PROCESSING.code);
        if (DETERMINE_CLASS.equals(vo.getEventCategory())) {
            vo.setStatus(AlarmEventStatusEnum.PROCESSED.code);
        } else {
            vo.setStatus(AlarmEventStatusEnum.PROCESSING.code);
        }

        ProjectEntranceAlarmEvent projectEntranceAlarmEvent = new ProjectEntranceAlarmEvent();
        //vo转po
        BeanUtils.copyProperties(vo, projectEntranceAlarmEvent);
        Duration duration = Duration.between(vo.getEventTime(), LocalDateTime.now());
        //获取项目配置
        ProjectConfig projectConfig = projectConfigService.getConfig();
        if (duration.toMinutes() > projectConfig.getAlarmTimeLimit()) {
            vo.setTimeLeave(OVERRUN);
        } else {
            vo.setTimeLeave(NOT_OVERRUN);
        }
        //存入创建人id
        PigxUser user = SecurityUtils.getUser();
        vo.setHandleOperator(user.getId());
        projectAlarmHandleService.save(vo);
        return super.updateById(projectEntranceAlarmEvent);
    }

    @Override
    public boolean putStatus(ProjectEntranceAlarmEventVo vo) {
        //设置当前事件状态为已处理
        vo.setStatus(AlarmEventStatusEnum.PROCESSED.code);
        ProjectEntranceAlarmEvent projectEntranceAlarmEvent = new ProjectEntranceAlarmEvent();
        //vo转po
        BeanUtils.copyProperties(vo, projectEntranceAlarmEvent);
        projectAlarmHandleService.updateById(vo);
        return super.updateById(projectEntranceAlarmEvent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean putBatchById(ProjectEntranceAlarmEventVo vo) {
        //获取po对象的list集合
//        List<String> eventIdList = new ArrayList<>();
//        Map<String, String> timeLeaveMap = new HashMap<>();
//        for (ProjectAlarmHandleBatchTimeLeaveVo batchVo : vo.getBatchAffirmList()) {
//            eventIdList.add(batchVo.getEventId());
//            timeLeaveMap.put(batchVo.getEventId(), batchVo.getTimeLeave());
//        }

        List<ProjectEntranceAlarmEvent> poList = list(new QueryWrapper<ProjectEntranceAlarmEvent>().lambda().in(ProjectEntranceAlarmEvent::getEventId, vo.getEventIdList()));
        //批量修改事件状态

        List<ProjectAlarmHandle> handleSaveList = new ArrayList<>();
        ProjectAlarmHandle projectAlarmHandle;
        for (ProjectEntranceAlarmEvent po : poList) {
            if (po.getStatus().equals(AlarmEventStatusEnum.UNPROCESSED.code)) {
                //状态为0时进行统一确认
                projectAlarmHandle = new ProjectAlarmHandle();
                BeanUtils.copyProperties(vo, projectAlarmHandle);
                projectAlarmHandle.setEventId(po.getEventId());
                Duration duration = Duration.between(po.getEventTime(), LocalDateTime.now());
                //获取项目配置
                ProjectConfig projectConfig = projectConfigService.getConfig();
                if (duration.toMinutes() > projectConfig.getAlarmTimeLimit()) {
                    projectAlarmHandle.setTimeLeave(OVERRUN);
                } else {
                    projectAlarmHandle.setTimeLeave(NOT_OVERRUN);
                }
                projectAlarmHandle.setHandleBeginTime(LocalDateTime.now());
                handleSaveList.add(projectAlarmHandle);
            }
            po.setStatus(AlarmEventStatusEnum.PROCESSED.code);
        }
        projectAlarmHandleService.saveBatch(handleSaveList);

        List<ProjectAlarmHandle> handleList = projectAlarmHandleService.list(new QueryWrapper<ProjectAlarmHandle>().lambda().in(ProjectAlarmHandle::getEventId, vo.getEventIdList()));

        //批量修改事件记录
        for (ProjectAlarmHandle handle : handleList) {
            if (handle.getHandleBeginTime() == null) {
                handle.setHandleBeginTime(LocalDateTime.now());
            }
            handle.setHandleEndTime(LocalDateTime.now());
            Duration duration = Duration.between(handle.getHandleBeginTime(), handle.getHandleEndTime());
            handle.setDealDuration(duration.toMinutes() + "");
            handle.setResult(vo.getResult());
            handle.setPicUrl(vo.getLivePic());
        }

        projectAlarmHandleService.updateBatchById(handleList);

        //调用mybatis plus的批量修改方法，返回结果
        return super.updateBatchById(poList);
    }

    @Override
    public ProjectEntranceAlarmEventVo getById(String eventId) {
        return projectEntranceAlarmEventMapper.findById(eventId, ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
    }

    @Override
    public boolean allHandle(ProjectEntranceAlarmEventVo vo) {
        //获取所有状态不为已处理的数据
        List<String> eventIdList = projectEntranceAlarmEventMapper.findAllEventId(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        //保存所有返回的数据的ID
        vo.setEventIdList(eventIdList);
        //调用批量处理方法进行修改状态
        return putBatchById(vo);
    }

    @Override
    public boolean save(ProjectEntranceAlarmEventVo vo) {
        ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getById(vo.getDeviceId());
        ProjectEntranceAlarmEvent po = new ProjectEntranceAlarmEvent();
        BeanUtils.copyProperties(vo, po);
        if (deviceInfo != null) {
            po.setDeviceName(deviceInfo.getDeviceName());
            ProjectDeviceRegion region = projectDeviceRegionService.getById(deviceInfo.getDeviceRegionId());
            po.setDeviceRegionName(region.getRegionName());
        }
        return super.save(po);
    }

    /**
     * 获取当天的警报事件数量
     *
     * @return
     * @author: 王伟
     * @since :2020-09-03
     */
    @Override
    public Integer countCurrDay() {
        return this.baseMapper.countCurrDayEvent(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
    }

    @Override
    public Integer countByMonth(Integer projectId, Integer tenantId, String date) {
        return baseMapper.countByMonth(projectId, tenantId, date);
    }

    @Override
    public Integer countByMonthOff(Integer projectId, Integer tenantId, String date) {
        return baseMapper.countByMonthOff(projectId, tenantId, date);
    }
}
