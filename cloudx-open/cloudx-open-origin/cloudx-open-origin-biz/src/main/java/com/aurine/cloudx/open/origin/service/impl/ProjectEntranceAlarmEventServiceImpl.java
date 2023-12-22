
package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.constant.enums.DeviceTypeEnum;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.common.entity.vo.AlarmEventVo;
import com.aurine.cloudx.open.origin.constant.enums.AlarmEventStatusEnum;
import com.aurine.cloudx.open.origin.entity.*;
import com.aurine.cloudx.open.origin.mapper.ProjectEntranceAlarmEventMapper;
import com.aurine.cloudx.open.origin.service.*;
import com.aurine.cloudx.open.origin.util.NoticeUtil;
import com.aurine.cloudx.open.origin.util.WebSocketNotifyUtil;
import com.aurine.cloudx.open.origin.vo.ProjectEntranceAlarmEventVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
    @Resource
    private NoticeUtil noticeUtil;
    @Resource
    private SysEventTypeConfService sysEventTypeConfService;
    @Resource
    private ProjectWebSocketService projectWebSocketService;
//    @Resource
//    private EventFactory eventFactory;

    @Override
    public IPage<ProjectEntranceAlarmEventVo> page(Page page, ProjectEntranceAlarmEventVo vo) {
        projectConfigService.getConfig();
        vo.setDeviceTypeName(StrUtil.isNotEmpty(vo.getDeviceType()) ? DeviceTypeEnum.getByCode(vo.getDeviceType()).getNote() : null);
        IPage<ProjectEntranceAlarmEventVo> result = projectEntranceAlarmEventMapper.select(page, vo);
        for (ProjectEntranceAlarmEventVo alarmVo : result.getRecords()) {
            if (alarmVo.getDeviceRegionId() != null && "1".equals(alarmVo.getDeviceRegionId())) {
                alarmVo.setRegionName("小区");
            }
        }
        return result;
    }

    @Override
    public IPage<ProjectEntranceAlarmEventVo> appPage(Page page, ProjectEntranceAlarmEventVo vo) {
        projectConfigService.getConfig();
        IPage<ProjectEntranceAlarmEventVo> result = null;
        vo.setDeviceTypeName(StrUtil.isNotEmpty(vo.getDeviceType()) ? DeviceTypeEnum.getByCode(vo.getDeviceType()).getNote() : null);
        if (vo.getHandleOperator() != null) {
            result = projectEntranceAlarmEventMapper.operatorAppSelect(page, vo);
        } else {
            PigxUser user = SecurityUtils.getUser();
            vo.setHandleOperator(user.getId());
            result = projectEntranceAlarmEventMapper.appSelect(page, vo);
        }

        for (ProjectEntranceAlarmEventVo alarmVo : result.getRecords()) {
            if (alarmVo.getDeviceRegionId() != null && "1".equals(alarmVo.getDeviceRegionId())) {
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
    public ProjectEntranceAlarmEventVo allNum(Integer projectId, Integer tenantId) {
        return projectEntranceAlarmEventMapper.countNum(projectId, tenantId);
    }

    @Override
    public boolean setStatus(ProjectEntranceAlarmEventVo vo) {
        //设置当前事件状态为处理中
        if (DETERMINE_CLASS.equals(vo.getEventCategory())) {
            vo.setStatus(AlarmEventStatusEnum.PROCESSED.code);
        } else {
            vo.setStatus(AlarmEventStatusEnum.PROCESSING.code);
        }
        ProjectEntranceAlarmEvent projectEntranceAlarmEvent = new ProjectEntranceAlarmEvent();
        //vo转po
        BeanUtils.copyProperties(vo, projectEntranceAlarmEvent);

        if (checkOverrun(vo.getEventTime(), LocalDateTime.now())) {
            vo.setTimeLeave(OVERRUN);
        } else {
            vo.setTimeLeave(NOT_OVERRUN);
        }
        //存入创建人id
        PigxUser user = SecurityUtils.getUser();
        vo.setHandleOperator(user.getId());
        projectAlarmHandleService.save(vo);
        String userName = baseMapper.findUserName(user.getId());
        projectEntranceAlarmEvent.setPersonName(userName);
        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
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
        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
//        // 通知中台
//        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
//                .eq(ProjectDeviceInfo::getDeviceId, vo.getDeviceId()));
//        if(CollectionUtil.isNotEmpty(deviceInfoList)) {
//            ProjectDeviceInfo deviceInfo = deviceInfoList.get(0);
//            DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
//                    TenantContextHolder.getTenantId()).getDeviceService().dealAlarm(vo.getEventId());
//        }

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
                if (checkOverrun(po.getEventTime(), LocalDateTime.now())) {
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
        boolean flag = super.updateBatchById(poList);
//        poList.forEach(item -> {
//            // 通知中台
//            List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new QueryWrapper<ProjectDeviceInfo>().lambda()
//                    .eq(ProjectDeviceInfo::getDeviceId, item.getDeviceId()));
//            if(CollectionUtil.isNotEmpty(deviceInfoList)) {
//                ProjectDeviceInfo deviceInfo = deviceInfoList.get(0);
//                DeviceFactoryProducer.getFactory(deviceInfo.getDeviceType(), ProjectContextHolder.getProjectId(),
//                        TenantContextHolder.getTenantId()).getDeviceService().dealAlarm(item.getEventId());
//            }
//        });
        //调用mybatis plus的批量修改方法，返回结果
        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));

        return flag;
    }

//    @Override
//    public ProjectEntranceAlarmEventVo getById(String eventId) {
//        return projectEntranceAlarmEventMapper.findById(eventId, ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
//    }

    @Override
    public boolean allHandle(ProjectEntranceAlarmEventVo vo) {
        //获取所有状态不为已处理的数据
        List<String> eventIdList = projectEntranceAlarmEventMapper.findAllEventId(ProjectContextHolder.getProjectId(), TenantContextHolder.getTenantId());
        //保存所有返回的数据的ID
        vo.setEventIdList(eventIdList);
        //调用批量处理方法进行修改状态
        boolean flag = putBatchById(vo);
        WebSocketNotifyUtil.sendMessgae(ProjectContextHolder.getProjectId().toString(), JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));


        return flag;
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

//        EventSubscribeService eventSubscribeService = eventFactory.GetProduct(ALERT_TYPE);
//        String poString = JSONObject.toJSONString(po);
//        eventSubscribeService.send(JSONObject.parseObject(poString), deviceInfo.getProjectId(), ALERT_TYPE);

        boolean save = super.save(po);

        //给拥有住户审核模块的员工发送消息
        Integer projectId = deviceInfo.getProjectId();
        TenantContextHolder.setTenantId(1);
        if (save) {
            List<ProjectStaff> projectStaff = baseMapper.getProjectStaff(projectId, 1);
            sendAssignNotice(projectStaff, po);
        }


        return save;
    }

    private void sendAssignNotice(List<ProjectStaff> projectStaff, ProjectEntranceAlarmEvent po) {
        SysEventTypeConf sysEventTypeConfServiceOne = sysEventTypeConfService.getOne(new LambdaQueryWrapper<SysEventTypeConf>()
                .eq(SysEventTypeConf::getEventTypeId, po.getEventTypeId()));
        for (ProjectStaff staff : projectStaff) {
            try {
                noticeUtil.send(true, "设备报警提醒",
                        "有新的设备报警，请尽快确认处理<br>" +
                                "设备名称：" + po.getDeviceName() +
                                "<br>报警类型：" + sysEventTypeConfServiceOne.getEventTypeName() +
                                "<br>报警时间：" + po.getEventTime() +
                                "", staff.getStaffId());
            } catch (Exception e) {
                log.error(e.getMessage() + e.getStackTrace());
                log.warn("消息发送异常");
            }

        }
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

    @Override
    public Integer countByOff() {
        return this.count(Wrappers.lambdaQuery(ProjectEntranceAlarmEvent.class)
                .or(i -> i.eq(ProjectEntranceAlarmEvent::getStatus, "0").or().eq(ProjectEntranceAlarmEvent::getStatus, "1"))
                .eq(ProjectEntranceAlarmEvent::getProjectId, ProjectContextHolder.getProjectId())
                .eq(ProjectEntranceAlarmEvent::getTenantId, TenantContextHolder.getTenantId()));
    }

    @Override
    public Integer findCount(Integer projectId, Integer tenantId) {
        return baseMapper.findCount(projectId, tenantId);
    }

    @Override
    public Boolean checkOverrun(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        //获取项目配置
        ProjectConfig projectConfig = projectConfigService.getConfig();
        return duration.toMinutes() > projectConfig.getAlarmTimeLimit();
    }

    @Override
    public String getNotOverrun() {
        return NOT_OVERRUN;
    }

    @Override
    public String getOverrun() {
        return OVERRUN;
    }

    @Override
    public String getDetermineClass() {
        return DETERMINE_CLASS;
    }

    @Override
    public Page<AlarmEventVo> page(Page page, AlarmEventVo vo) {
        ProjectEntranceAlarmEvent po = new ProjectEntranceAlarmEvent();
        BeanUtils.copyProperties(vo, po);

        return baseMapper.page(page, po);
    }
}
