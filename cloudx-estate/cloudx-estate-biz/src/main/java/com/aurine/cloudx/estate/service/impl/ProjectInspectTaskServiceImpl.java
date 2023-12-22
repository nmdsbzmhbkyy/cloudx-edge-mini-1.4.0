package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.constant.*;
import com.aurine.cloudx.estate.entity.ProjectInspectTask;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskStaff;
import com.aurine.cloudx.estate.entity.ProjectStaff;
import com.aurine.cloudx.estate.mapper.ProjectInspectTaskMapper;
import com.aurine.cloudx.estate.service.ProjectInspectTaskDetailService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskService;
import com.aurine.cloudx.estate.service.ProjectInspectTaskStaffService;
import com.aurine.cloudx.estate.service.ProjectStaffService;
import com.aurine.cloudx.estate.util.MessageTextUtil;
import com.aurine.cloudx.estate.util.NoticeUtil;
import com.aurine.cloudx.estate.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.constant.enums.TaskStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备巡检任务(ProjectInspectTask)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-28 17:06:49
 */
@Service
@Slf4j
public class ProjectInspectTaskServiceImpl extends ServiceImpl<ProjectInspectTaskMapper, ProjectInspectTask>
        implements ProjectInspectTaskService {

    @Resource
    ProjectInspectTaskMapper projectInspectTaskMapper;
    @Resource
    ProjectInspectTaskDetailService projectInspectTaskDetailService;
    @Resource
    ProjectInspectTaskStaffService projectInspectTaskStaffService;
    @Resource
    ProjectStaffService projectStaffService;
    @Resource
    NoticeUtil noticeUtil;

    private final String formatStr = "yyyy-MM-dd";

    @Override
    public Page<ProjectInspectTaskPageVo> fetchList(Page page, ProjectInspectTaskSearchConditionVo query) {
        return projectInspectTaskMapper.fetchList(page, query);
    }

    @Override
    public boolean cancel(String taskId) {
        ProjectInspectTask projectInspectTask = new ProjectInspectTask();
        projectInspectTask.setTaskId(taskId);
        projectInspectTask.setStatus(InspectTaskStatusConstant.CANCEL);
        return this.updateById(projectInspectTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean initTask() {
        // 获取到所有明天可以进行的巡检任务
        List<ProjectInspectTaskVo> voList = projectInspectTaskMapper.listAllPlanTask();
        List<ProjectInspectTask> taskList = new ArrayList<>();
        List<ProjectInspectTaskStaff> staffList = new ArrayList<>();
        Set<String> staffIdSet = new HashSet<>();
        //遍历出任务对象列表用于数据库添加
        voList.forEach(vo -> {
            //如果根据cron表达式判断不能生成任务则跳过
            if (resolveCron(vo)) {
                log.info("任务生成：{}", vo.toString());
                String taskId = UUID.randomUUID().toString().replaceAll("-", "");
                // 生成任务数据
                ProjectInspectTask task = new ProjectInspectTask();
                BeanUtil.copyProperties(vo, task);
                task.setOperator(0);
                task.setStatus(InspectTaskStatusConstant.PENDING);
                task.setTaskId(taskId);
                // 巡检情况和巡检结果在生成任务的时候不再默认设置
//                task.setCheckInStatus(InspectTaskSituationConstant.NOT_CHECKIN);
//                task.setResult(InspectTaskResultConstant.NOT_INSPECTED);
                task.setInspectTaskCode(baseMapper.getTaskCode(ProjectContextHolder.getProjectId()));
                taskList.add(task);
                String planStaffIds = vo.getPlanStaffIds();
                String[] staffIdArr = planStaffIds.split(",");
                if (ArrayUtil.isNotEmpty(staffIdArr)) {
                    for (String staffId : staffIdArr) {
                        ProjectInspectTaskStaff staff = new ProjectInspectTaskStaff();
                        staff.setStaffId(staffId);
                        staff.setTaskId(taskId);
                        staff.setStaffType(InspectTaskStaffTypeConstants.PLAN_STAFF);
                        staffList.add(staff);
                    }
                    staffIdSet.addAll(Arrays.stream(staffIdArr).collect(Collectors.toSet()));
                }
            }
        });
        noticeUtil.send(true, "巡检通知",
                MessageTextUtil.init().append("您有新的巡检任务，请查收").toString(),
                new ArrayList<>(staffIdSet));
        try {
            if (CollUtil.isNotEmpty(taskList)) {
                boolean saveBatch = this.saveBatch(taskList);
                // 这里保存任务计划人员
                projectInspectTaskStaffService.saveBatch(staffList);
                projectInspectTaskDetailService.initTaskDetails(taskList);
                return saveBatch;
            }
        } catch (Exception base) {
            base.printStackTrace();
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeTaskById(String taskId) {
        projectInspectTaskDetailService.removeDetailByTaskId(taskId);
        // 根据巡检任务ID删除巡检计划人员
        projectInspectTaskStaffService.remove(new QueryWrapper<ProjectInspectTaskStaff>().lambda().eq(ProjectInspectTaskStaff::getTaskId, taskId));
        return this.remove(new QueryWrapper<ProjectInspectTask>().lambda().eq(ProjectInspectTask::getTaskId, taskId));
    }

    @Override
    public ProjectInspectTaskAndDetailVo getTaskAndDetailById(String taskId) {
        ProjectInspectTaskAndDetailVo projectInspectTaskAndDetailVo = new ProjectInspectTaskAndDetailVo();
        ProjectInspectTask projectInspectTask = baseMapper.selectById(taskId);
        List<ProjectInspectTaskDetailVo> projectInspectTaskDetails = projectInspectTaskDetailService.listByTaskId(taskId);
        if (InspectTaskStatusConstant.COMPLETED.equals(projectInspectTask.getStatus())) {
            String inspectEndTime = projectInspectTask.getPlanInspectTime().split(" 至 ")[1];
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime endTime = LocalDateTime.parse(inspectEndTime, dateTimeFormatter);
            projectInspectTaskDetails.stream().forEach(e -> {
                Duration between = Duration.between(e.getEndTime(), endTime);
                e.setCheckInStatus(between.toMinutes() < 0 ? InspectTaskCheckInStatusConstant.TIMEOUT : InspectTaskCheckInStatusConstant.NORMAL);
            });
        }
        BeanUtils.copyProperties(projectInspectTask, projectInspectTaskAndDetailVo);
        List<ProjectInspectTaskStaff> projectInspectTaskStaffs = projectInspectTaskStaffService
                .list(Wrappers.lambdaQuery(ProjectInspectTaskStaff.class).eq(ProjectInspectTaskStaff::getTaskId, taskId));

        //设置计划执行人和实际执行人id
        if (projectInspectTaskStaffs != null && projectInspectTaskStaffs.size() > 0) {
            List<String> planStaffIds = projectInspectTaskStaffs.stream().filter(e -> "1".equals(e.getStaffType())).map(ProjectInspectTaskStaff::getStaffId).collect(Collectors.toList());
            List<String> exeStaffIds = projectInspectTaskStaffs.stream().filter(e -> "2".equals(e.getStaffType())).map(ProjectInspectTaskStaff::getStaffId).collect(Collectors.toList());
            if (planStaffIds != null && planStaffIds.size() > 0) {
                String planStaffString = StringUtils.join(planStaffIds, ",");
                projectInspectTaskAndDetailVo.setPlanStaffIds(planStaffString);
            }
            if (exeStaffIds != null && exeStaffIds.size() > 0) {
                String exeStaffIdString = StringUtils.join(exeStaffIds, ",");
                projectInspectTaskAndDetailVo.setExecStaffIds(exeStaffIdString);
            }
        }
        projectInspectTaskAndDetailVo.setProjectInspectTaskDetails(projectInspectTaskDetails);
        return projectInspectTaskAndDetailVo;
    }

    @Override
    public ProjectStaffWorkVo getCount(String staffId, String date) {
        ProjectStaffWorkVo staffWorkVo = new ProjectStaffWorkVo();
        staffWorkVo.setAllTaskNum(baseMapper.getCount(staffId, null, date));
        if (staffWorkVo.getAllTaskNum().equals(0)) {
            staffWorkVo.setCompletedTaskNum(0);
            staffWorkVo.setUnCompletedTaskNum(0);
        } else {
            staffWorkVo.setCompletedTaskNum(baseMapper.getCount(staffId, "2", date));
            staffWorkVo.setUnCompletedTaskNum(staffWorkVo.getAllTaskNum() - staffWorkVo.getCompletedTaskNum());
        }
        return staffWorkVo;
    }

    @Override
    public boolean dealTimeOut() {
        // 这里获取到所有已超时且为未巡检但未设置为超时的任务id列表
        projectInspectTaskMapper.updateAllTimeOut();
        return true;
    }

    @Override
    public void completeTask(String taskId) {
        List<ProjectInspectTask> taskList = this.list(new QueryWrapper<ProjectInspectTask>().lambda().eq(ProjectInspectTask::getTaskId, taskId));
        if (CollUtil.isNotEmpty(taskList)) {
            ProjectInspectTask task = taskList.get(0);
            String inspectEndTime = task.getPlanInspectTime().split(" 至 ")[1];
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime endTime = LocalDateTime.parse(inspectEndTime, dateTimeFormatter);
            Duration between = Duration.between(endTime, LocalDateTime.now());
            // 这里判断任务是否超时完成
            if (between.toDays() < 0) {
                task.setCheckInStatus(InspectTaskCheckInStatusConstant.TIMEOUT);
            } else {
                task.setCheckInStatus(InspectTaskCheckInStatusConstant.NORMAL);
            }
            task.setStatus(InspectTaskStatusConstant.COMPLETED);
            this.updateById(task);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void claimTask(String taskId, String staffId) {

        ProjectInspectTask task = baseMapper.selectById(taskId);

        // 这里如果任务已被取消或超时则无法领取该任务或已被领取
        if (ObjectUtil.isNotEmpty(task.getStatus()) && !InspectTaskStatusConstant.PENDING.equals(task.getStatus())) {
            throw new RuntimeException("无法领取该任务");
        }

        ProjectStaff staff = projectStaffService.getById(staffId);
        task.setExecStaff(staff.getStaffName());
        //状态巡检中
        task.setStatus("1");
        //更新任务状态
        baseMapper.updateById(task);

        ProjectInspectTaskStaff inspectTaskStaff = new ProjectInspectTaskStaff();
        inspectTaskStaff.setTaskId(taskId);
        inspectTaskStaff.setStaffId(staffId);
        inspectTaskStaff.setStaffType(InspectTaskStaffTypeConstants.ACTUAL_STAFF);

        projectInspectTaskStaffService.save(inspectTaskStaff);

    }

    @Override
    public Page<ProjectInspectTaskPageVo> selectForMe(Page page, String staffId) {
        return baseMapper.selectForMe(page, staffId);
    }

    @Override
    public Page<ProjectInspectTaskPageVo> selectToDo(Page page, String staffId) {
        return baseMapper.selectToDo(page, staffId);
    }

    @Override
    public Page<ProjectInspectTaskPageVo> selectDateToDo(Page page, String staffId, String date) {
        return baseMapper.selectDateToDo(page, staffId, date);
    }


    /**
     * <p>
     * 解析cron表达式判断这个班次是否能够生成任务
     * </p>
     *
     * @param taskVo 任务vo对象
     * @return 解析结果
     */
    private boolean resolveCron(ProjectInspectTaskVo taskVo) {
        String gapType = taskVo.getGapType();
        String ifSatOrSun = taskVo.getIfSatOrSun();
        String planId = taskVo.getInspectPlanId();
        String cron = taskVo.getFrequency();
        String curDay = taskVo.getCurDay();
        switch (taskVo.getCronType()) {
            case InspectPlanCronTypeConstants.DAY:
                return resolveCronByDay(gapType, ifSatOrSun, planId);
            case InspectPlanCronTypeConstants.WEEK:
                return resolveCronByWeek(cron, gapType, planId);
            case InspectPlanCronTypeConstants.CUSTOMIZE:
                return resolveCronByCustomize(curDay);
            default:
                return true;
        }
    }

    /**
     * <p>
     * 根据cron（每天）判断是否生成该班次的任务
     * </p>
     *
     * @param gapType    是连续还是间隔一天 InspectionGapTypeConstants
     * @param ifSatOrSun 是跳过周六周天还是正常执行 InspectionSkipSatOrSunConstants
     * @param planId     计划id
     * @return 处理结果
     */
    private boolean resolveCronByDay(String gapType, String ifSatOrSun, String planId) {
        //这个班次能否生成任务
        if (InspectGapTypeConstants.INTERVAL.equals(gapType)) {
            // 获取到已有任务表同计划的最新任务
            List<ProjectInspectTask> taskList = this.list(new QueryWrapper<ProjectInspectTask>().lambda()
                    .eq(ProjectInspectTask::getInspectPlanId, planId)
                    .orderByDesc(ProjectInspectTask::getCreateTime));
            if (CollUtil.isNotEmpty(taskList)) {
                ProjectInspectTask projectInspectTask = taskList.get(0);
                Duration duration = Duration.between(projectInspectTask.getCreateTime(), LocalDateTime.now());
                // 间隔一天所需要的天数
                int numberOfDaysBetween = 2;
                if (duration.toDays() < numberOfDaysBetween) {
                    return false;
                }
            }
        }
        if (InspectSkipSatOrSunConstants.SKIP.equals(ifSatOrSun)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            int week = calendar.get(Calendar.DAY_OF_WEEK);
            // 周六
            int sat = 7;
            //周天
            int sun = 1;
            // 这里如果是周天生成下周一的任务
            return week != sat && week != sun;
        }
        return true;
    }

    /**
     * <p>
     * 根据cron（按周）判断是否生成该班次的任务
     * </p>
     *
     * @param cron    cron表达式
     * @param gapType 连续还是间隔一周 InspectionGapTypeConstants
     * @param planId  计划ID
     * @return 处理结果
     */
    private boolean resolveCronByWeek(String cron, String gapType, String planId) {
        String[] cronArr = cron.split(" ");
        Calendar calendar = Calendar.getInstance();
        // 获取间隔一周至少要相差的天数 如周五则至少要间隔12天才能算间隔一周
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        int minimumDays = week + 7;
        if (InspectGapTypeConstants.INTERVAL.equals(gapType)) {
            // 获取到已有任务表同计划的最新任务
            if (StrUtil.isBlank(planId)) {
                return false;
            }
            // 这里取出的列表已经是本周前生成的任务了
            List<ProjectInspectTask> taskList = projectInspectTaskMapper.listByLastWeek(getDate(Calendar.SUNDAY), planId);
            if (CollUtil.isNotEmpty(taskList)) {
                ProjectInspectTask task = taskList.get(0);
                // 这里获取数据库中上一次本计划最新生成的任务时间用来判断这次生成是否和上次相隔一周
                /*LocalDateTime createTime = task.getCreateTime();
                LocalDateTime taskDay = createTime.plusDays(1);*/
                String format = DateUtil.format(task.getCreateTime(), formatStr);
                DateTime parse = DateUtil.parse(format, formatStr);

                String formatNow = DateUtil.format(DateUtil.date(), formatStr);
                DateTime parseNow = DateUtil.parse(formatNow, formatStr);
                long between = DateUtil.between(parse, parseNow, DateUnit.DAY);
                // 判断是否有间隔一周
                if (minimumDays > between) {
                    return false;
                }
            }
        }
        // 如果今天不是这个班次要生成任务的则返回false跳过
        return cronArr[5].equals(week + "");
    }


    /**
     * <p>
     * 根据日期判断是否生成该班次的任务
     * </p>
     *
     * @param curDay 日期信息如 2020-8-6
     * @return 处理结果
     */
    private boolean resolveCronByCustomize(String curDay) {
        LocalDate now = LocalDate.now().plusDays(1);
        String nowDate = now.toString();
        return DateUtil.between(DateUtil.parse(curDay, formatStr), DateUtil.parse(nowDate, formatStr), DateUnit.DAY) == 0;
    }

    /**
     * <p>
     * 获取本周第一天的日期 原文：<a href="https://blog.csdn.net/rocling/article/details/82431632">链接</a>
     * </p>
     *
     * @param calendarDay 如Calendar.SUNDAY 这里是把星期天当做每周的第一天 MONDAY就是星期一
     * @return 返回本周第一天的日期
     */
    public String getDate(int calendarDay) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        // 这里根据传入的参数设置周几未每周的第一天
        cal.setFirstDayOfWeek(calendarDay);
        // 获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);
        Date mondayDate = cal.getTime();
        return sdf.format(mondayDate);
    }

}