package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.InspectTaskCheckInStatusConstant;
import com.aurine.cloudx.estate.constant.PatrolStatus;
import com.aurine.cloudx.estate.entity.*;
import com.aurine.cloudx.estate.mapper.ProjectInspectTaskDetailMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备巡检任务明细(ProjectInspectTaskDetail)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:59
 */
@Service
public class ProjectInspectTaskDetailServiceImpl extends
    ServiceImpl<ProjectInspectTaskDetailMapper, ProjectInspectTaskDetail> implements ProjectInspectTaskDetailService {

    @Resource
    ProjectInspectTaskService projectInspectTaskService;
    @Resource
    ProjectInspectPointConfService projectInspectPointConfService;
    @Resource
    ProjectInspectPlanService projectInspectPlanService;
    @Resource
    ProjectInspectDetailDeviceService projectInspectDetailDeviceService;
    @Resource
    ProjectInspectCheckinDetailService projectInspectCheckinDetailService;

    @Resource
    ProjectInspectDetailCheckItemService projectInspectDetailCheckItemService;

    private final Map<String, String> routeIdMap = new HashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initTaskDetails(List<ProjectInspectTask> taskList) {
        routeIdMap.clear();
        List<ProjectInspectTaskDetail> taskDetailList = new ArrayList<>();
        // 根据任务对象列表生成巡检任务明细列表（巡检点表的备份）
        taskList.forEach(task -> {
            String planId = task.getInspectPlanId();
            String taskId = task.getTaskId();
            List<ProjectInspectPointConfVo> pointConfVoList =
                projectInspectPointConfService.listByInspectPointByRouteId(getRouteId(planId));
            pointConfVoList.forEach(pointConfVo -> {
                ProjectInspectTaskDetail taskDetail = new ProjectInspectTaskDetail();
                BeanUtil.copyProperties(pointConfVo, taskDetail);
                taskDetail.setIsHumidity(pointConfVo.getHumidity());
                taskDetail.setIsTemper(pointConfVo.getTemperature());
                taskDetail.setTemperature(0.0);
                taskDetail.setHumidity(0.0);
                taskDetail.setOperator(0);
                taskDetail.setTaskId(taskId);
                taskDetailList.add(taskDetail);
            });
        });
        if (CollUtil.isNotEmpty(taskDetailList)) {
            boolean save = this.saveBatch(taskDetailList);
            projectInspectDetailDeviceService.initDetailDevice(taskDetailList);
            return save;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeDetailByTaskId(String taskId) {
        List<ProjectInspectTaskDetail> taskDetailList = this.list(
            new QueryWrapper<ProjectInspectTaskDetail>().lambda().eq(ProjectInspectTaskDetail::getTaskId, taskId));
        if (CollUtil.isNotEmpty(taskDetailList)) {
            List<String> detailIdList =
                taskDetailList.stream().map(ProjectInspectTaskDetail::getDetailId).collect(Collectors.toList());
            // 删除任务明细设备
            projectInspectDetailDeviceService.removeDetailDeviceByDetailId(detailIdList);
            // 删除任务明细签到详情
            projectInspectCheckinDetailService.removeByDetailIdList(detailIdList);
            return this.remove(new QueryWrapper<ProjectInspectTaskDetail>().lambda()
                .in(ProjectInspectTaskDetail::getDetailId, detailIdList));
        }
        return true;
    }

    @Override
    public List<ProjectInspectTaskDetailVo> listByTaskId(String taskId) {
        return baseMapper.listByTaskId(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveInspectTaskDetail(ProjectInspectTaskDetail taskDetail) {
        baseMapper.insert(taskDetail);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sign(ProjectInspectVo taskVo) {

        ProjectInspectTaskDetail projectInspectTaskDetail = new ProjectInspectTaskDetail();

        List<ProjectInspectDetailDeviceFormVo> projectInspectDetailDevices = taskVo.getDevices();

        List<ProjectInspectDetailDevice> inspectDetailDevices = new ArrayList<>();
        // 异常个数
        int error = 0;
        for (ProjectInspectDetailDeviceFormVo p : projectInspectDetailDevices) {
            ProjectInspectDetailDevice projectInspectDetailDevice = new ProjectInspectDetailDevice();
            List<ProjectInspectDetailCheckItem> projectInspectDetailCheckItems = p.getDetailCheckItems();
            // 过滤掉存在检查项为空的数据
            if (ObjectUtil.isNotEmpty(projectInspectDetailCheckItems) && projectInspectDetailCheckItems.size() > 0) {
                int errNum = 0;
                // 判断检查项是否存在异常
                for (ProjectInspectDetailCheckItem i : projectInspectDetailCheckItems) {
                    // 检查项目不通过异常次数加1
                    if ("2".equals(i.getStatus())) {
                        errNum++;
                    }
                }
                if (errNum > 0) {
                    p.setResult("2");
                } else {
                    p.setResult("1");
                }
                projectInspectDetailCheckItemService.updateBatchById(projectInspectDetailCheckItems);
            }
            BeanUtils.copyProperties(p, projectInspectDetailDevice);
            if ("2".equals(p.getResult())) {
                error++;
            }
            inspectDetailDevices.add(projectInspectDetailDevice);
        }

        // 更新巡检设备明细
        projectInspectDetailDeviceService.updateBatchById(inspectDetailDevices);

        if (error > 0) {
            taskVo.setResult("2");
        } else {
            taskVo.setResult("1");
        }

        BeanUtils.copyProperties(taskVo, projectInspectTaskDetail);
        projectInspectTaskDetail.setEndTime(LocalDateTime.now());

        // 更新巡检明细
        baseMapper.updateById(projectInspectTaskDetail);
        // 设置签到方式
        List<ProjectInspectCheckinDetail> checkinDetails = taskVo.getCheckinDetails();

        LocalDateTime time = LocalDateTime.now();
        if (checkinDetails.size() == 2) {
            for (ProjectInspectCheckinDetail p : checkinDetails) {
                if ("2".equals(p.getCheckInType())) {
                    time = p.getCheckInTime();
                    break;
                }
            }
        } else if (checkinDetails.size() == 1) {
            time = checkinDetails.get(0).getCheckInTime();
        }

        for (ProjectInspectCheckinDetail e : checkinDetails) {
            e.setCheckInTime(time);
            e.setDetailId(projectInspectTaskDetail.getDetailId());
        }
        // 添加签到方式信息
        projectInspectCheckinDetailService.saveBatch(checkinDetails);

        ProjectInspectTask projectInspectTask = projectInspectTaskService.getById(taskVo.getTaskId());
        // 获取计划执行时间来访申请
        String planTimeString = projectInspectTask.getPlanInspectTime();
        String[] planTime = planTimeString.split("至");
        // 获取最后计划签到时间
        LocalDateTime endTime =
            DateUtil.parseLocalDateTime(planTime[1].trim(), DatePattern.NORM_DATETIME_MINUTE_PATTERN);

        List<ProjectInspectTaskDetailVo> taskDetailVos = baseMapper.listByTaskId(taskVo.getTaskId());

        // 未巡检数量
        int countNum = 0;
        // 异常数量
        int errorNum = 0;
        // 记录最后一次签到时间
        LocalDateTime lastTime = LocalDateTime.MIN;
        // 记录第一次签到时间
        LocalDateTime fistTime = LocalDateTime.MAX;
        for (ProjectInspectTaskDetailVo taskDetailVo : taskDetailVos) {

            if (taskDetailVo.getDetailId().equals(projectInspectTaskDetail.getDetailId())) {
                // 同一事务里不会提交刚刚更新的值故这里把传入值重新更新到获取的数据里面
                BeanUtils.copyProperties(projectInspectTaskDetail, taskDetailVo);
                taskDetailVo.setCheckInTime(time);
            }
            // 未巡检
            if (StringUtils.isBlank(taskDetailVo.getResult()) || "0".equals(taskDetailVo.getResult())) {
                countNum++;

            } else {
                if (ObjectUtil.isNotEmpty(taskDetailVo.getEndTime()) && lastTime.isBefore(taskDetailVo.getEndTime())) {
                    lastTime = taskDetailVo.getEndTime();
                }
                if (ObjectUtil.isNotEmpty(taskDetailVo.getCheckInTime())
                    && fistTime.isAfter(taskDetailVo.getCheckInTime())) {
                    fistTime = taskDetailVo.getCheckInTime();
                }
            }
            // 异常
            if ("2".equals(taskDetailVo.getResult())) {
                errorNum++;
            }

        }
        projectInspectTask.setStatus(PatrolStatus.PATROLLING);
        // 如果没有未签到即已经完成该巡检任务，执行校验逻辑
        if (countNum == 0) {
            // 实际耗时（分钟）

            int minutes = 1;
            // 获取到时间差精确到秒
            int seconds = (int)ChronoUnit.SECONDS.between(fistTime, lastTime);
            // 如果大于60钟取值到分钟否则默认设置为1
            if (seconds > 60) {
                minutes = (int)ChronoUnit.MINUTES.between(fistTime, lastTime);
            }

            projectInspectTask.setTimeElapsed(minutes);

            if (endTime.isBefore(lastTime)) {
                // 如果最后签到时间大于计划签到时间则设置为超时
                projectInspectTask.setCheckInStatus(InspectTaskCheckInStatusConstant.TIMEOUT);
            } else {
                projectInspectTask.setCheckInStatus(InspectTaskCheckInStatusConstant.NORMAL);
            }
            if (errorNum > 0) {
                // 异常格式大于0则为巡检结果为异常
                projectInspectTask.setResult("2");
            } else {
                projectInspectTask.setResult("1");
            }

            // 更新实际签到状态
            projectInspectTask.setInspectTime(DateUtil.format(fistTime, DatePattern.PURE_TIME_PATTERN) + ","
                + DateUtil.format(lastTime, DatePattern.PURE_TIME_PATTERN));
            projectInspectTask.setStatus(PatrolStatus.FINISH);

        }
        // 更新设备巡检任务信息
        projectInspectTaskService.updateById(projectInspectTask);

    }

    @Override
    public List<ProjectInspectCheckinDetail> listCheckInDetailById(String detailId) {
        List<ProjectInspectCheckinDetail> projectInspectCheckinDetails = new ArrayList<>();
        String data = baseMapper.listCheckInDetailById(detailId);
        if (StringUtils.isNotBlank(data)) {
            String[] types = data.split(",");
            for (String type : types) {
                ProjectInspectCheckinDetail projectInspectCheckinDetail = new ProjectInspectCheckinDetail();
                projectInspectCheckinDetail.setCheckInType(Character.valueOf(type.toCharArray()[0]));
                projectInspectCheckinDetail.setDetailId(detailId);
                projectInspectCheckinDetails.add(projectInspectCheckinDetail);

            }
            return projectInspectCheckinDetails;
        }
        return new ArrayList<>();
    }

    private String getRouteId(String planId) {
        // 因为一个计划有多个班次每个班次都是有对应的任务，但是同计划的班次路线都是一样的
        String routeId = routeIdMap.get(planId);
        if (StrUtil.isBlank(routeId)) {
            ProjectInspectPlanVo plan = projectInspectPlanService.getPlanById(planId);
            routeId = plan.getInspectRouteId();
            routeIdMap.put(planId, routeId);
        }
        return routeId;
    }

}