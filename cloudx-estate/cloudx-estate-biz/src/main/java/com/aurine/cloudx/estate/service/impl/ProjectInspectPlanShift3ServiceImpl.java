package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.estate.constant.InspectPlanCronTypeConstants;
import com.aurine.cloudx.estate.constant.InspectPlanCronWeekConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectPlanShift3;
import com.aurine.cloudx.estate.entity.ProjectInspectPlanShiftStaff;
import com.aurine.cloudx.estate.mapper.ProjectInspectPlanShift3Mapper;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShift3Service;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShiftStaffService;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanShift3Vo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备巡检计划班次信息（自定义）(ProjectInspectPlanShift3)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:26
 */
@Service
public class ProjectInspectPlanShift3ServiceImpl extends ServiceImpl<ProjectInspectPlanShift3Mapper,
        ProjectInspectPlanShift3> implements ProjectInspectPlanShift3Service {

    @Resource
    ProjectInspectPlanShiftStaffService projectInspectPlanShiftStaffService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateShiftByDay(Map<String, List<ProjectInspectPlanShift3Vo>> shift3VoMap, String planId) {
        // 先删除原有班次和计划的关系
        this.remove(new QueryWrapper<ProjectInspectPlanShift3>().lambda().eq(ProjectInspectPlanShift3::getPlanId, planId));
        String cron = "0 0 0 * * ? *";
        if (MapUtil.isNotEmpty(shift3VoMap)) {
            List<ProjectInspectPlanShift3Vo> projectInspectPlanShift3VoList = shift3VoMap.get("everyDay");
            for (ProjectInspectPlanShift3Vo vo : projectInspectPlanShift3VoList) {
                ProjectInspectPlanShift3 shift3 = new ProjectInspectPlanShift3();
                shift3.setFrequency(cron);
                shift3.setPlanId(planId);
                shift3.setStartTime(vo.getTimeRange()[0]);
                shift3.setEndTime(vo.getTimeRange()[1]);
                //保存班次数据
                boolean save = this.save(shift3);
                // 保存班次对应的执行人关系
                projectInspectPlanShiftStaffService.saveOrUpdateBatchShiftStaffRel(vo.getStaffIdArr(), shift3.getRecordId());
                if (!save) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateShiftByWeek(Map<String, List<ProjectInspectPlanShift3Vo>> voMap, String planId) {
        // 先删除原有班次和计划的关系
        this.remove(new QueryWrapper<ProjectInspectPlanShift3>().lambda().eq(ProjectInspectPlanShift3::getPlanId, planId));
        if (MapUtil.isNotEmpty(voMap)) {
            Set<String> keySet = voMap.keySet();
            for (String key : keySet) {
                if (!keyIsWeek(key)) {
                    continue;
                }
                List<ProjectInspectPlanShift3Vo> projectInspectPlanShift3VoList = voMap.get(key);
                for (ProjectInspectPlanShift3Vo vo : projectInspectPlanShift3VoList) {
                    String cron = "0 0 0 * * " + getCornWeekByKeySet(key) + " *";
                    ProjectInspectPlanShift3 shift3 = new ProjectInspectPlanShift3();
                    shift3.setStartTime(vo.getTimeRange()[0]);
                    shift3.setEndTime(vo.getTimeRange()[1]);
                    shift3.setPlanId(planId);
                    shift3.setFrequency(cron);
                    // 保存班次数据
                    boolean save = this.save(shift3);
                    // 保存班次对应的执行人关系
                    projectInspectPlanShiftStaffService.saveOrUpdateBatchShiftStaffRel(vo.getStaffIdArr(), shift3.getRecordId());
                    if (!save) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateShiftByCustomer(Map<String, List<ProjectInspectPlanShift3Vo>> voMap, String planId) {
        // 先删除原有班次和计划的关系
        this.remove(new QueryWrapper<ProjectInspectPlanShift3>().lambda().eq(ProjectInspectPlanShift3::getPlanId, planId));
        if (MapUtil.isNotEmpty(voMap)) {
            Set<String> keySet = voMap.keySet();
            for (String shiftTime : keySet) {
                if (keyIsNotCustomer(shiftTime)) {
                    continue;
                }
                List<ProjectInspectPlanShift3Vo> projectInspectPlanShift3VoList = voMap.get(shiftTime);
                for (ProjectInspectPlanShift3Vo vo : projectInspectPlanShift3VoList) {
                    // 这里应该是 2020-8-13 这种格式
                    String[] split = shiftTime.split("-");
                    String cron = "0 0 0 " + split[1] + " " + split[2] + " ? *";
                    ProjectInspectPlanShift3 shift3 = new ProjectInspectPlanShift3();
                    shift3.setStartTime(vo.getTimeRange()[0]);
                    shift3.setEndTime(vo.getTimeRange()[1]);
                    // 这个存放用于后面回显
                    shift3.setCurDay(shiftTime);
                    shift3.setPlanId(planId);
                    shift3.setFrequency(cron);
                    // 保存班次数据
                    boolean save = this.save(shift3);
                    // 保存班次对应的执行人关系
                    projectInspectPlanShiftStaffService.saveOrUpdateBatchShiftStaffRel(vo.getStaffIdArr(), shift3.getRecordId());
                    if (!save) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, List<ProjectInspectPlanShift3Vo>> getShiftListMapByPlanId(String planId, String cronType) {
        List<ProjectInspectPlanShift3> shift3List = this.list(new QueryWrapper<ProjectInspectPlanShift3>().lambda()
                .eq(ProjectInspectPlanShift3::getPlanId, planId).orderByDesc(ProjectInspectPlanShift3::getSeq));
        Map<String, List<ProjectInspectPlanShift3Vo>> stringListMap = new HashMap<>();
        if (CollUtil.isNotEmpty(shift3List)) {
            shift3List.forEach(shift3 -> {
                // getCurDay这里如果是自定义的话方法会返回这个curDay
                String keyByCornType = getKeyByCornType(cronType, shift3.getFrequency(), shift3.getCurDay());
                ProjectInspectPlanShift3Vo shift3Vo = new ProjectInspectPlanShift3Vo();
                String[] staffIdArr = projectInspectPlanShiftStaffService.getStaffIdArrByShiftId(shift3.getRecordId());
                String[] timeRange = {shift3.getStartTime(), shift3.getEndTime()};
                shift3Vo.setStaffIdArr(staffIdArr);
                shift3Vo.setTimeRange(timeRange);
                stringListMap.computeIfAbsent(keyByCornType, key -> new ArrayList<>()).add(shift3Vo);
            });
        }
        return stringListMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeShiftByPlanId(String planId) {
        List<ProjectInspectPlanShift3> shiftList = list(new QueryWrapper<ProjectInspectPlanShift3>().lambda()
                .eq(ProjectInspectPlanShift3::getPlanId, planId));
        List<String> recordIdList = shiftList.stream().map(ProjectInspectPlanShift3::getRecordId).collect(Collectors.toList());
        projectInspectPlanShiftStaffService.removeRel(recordIdList);
        return this.remove(new QueryWrapper<ProjectInspectPlanShift3>().lambda().eq(ProjectInspectPlanShift3::getPlanId, planId));
    }


    @Override
    public boolean removePerson(String personId) {

        /*List<ProjectInspectPlanShiftStaff> inspectPlanShiftStaffList = projectInspectPlanShiftStaffService.list(new QueryWrapper<ProjectInspectPlanShiftStaff>().lambda()
                .eq(ProjectInspectPlanShiftStaff::getStaffId, personId));*/
        projectInspectPlanShiftStaffService.remove(new QueryWrapper<ProjectInspectPlanShiftStaff>().lambda().eq(ProjectInspectPlanShiftStaff::getStaffId, personId));

        return false;
    }

    /**
     * <p>
     * 根据cron表达式的类型获取到map的key
     * </p>
     *
     * @param cron     cron表达式
     * @param cronType cron表达式存储的时间类型
     * @param curDay   具体时间（转换成cron表达式之前的时间备份）
     * @return 该cron表达式对应班次数据的key值
     */
    private String getKeyByCornType(String cronType, String cron, String curDay) {

        if (InspectPlanCronTypeConstants.WEEK.equals(cronType)) {
            String[] cronArr = cron.split(" ");
            switch (cronArr[5]) {
                case "1":
                    return InspectPlanCronWeekConstants.MON;
                case "2":
                    return InspectPlanCronWeekConstants.TUE;
                case "3":
                    return InspectPlanCronWeekConstants.WED;
                case "4":
                    return InspectPlanCronWeekConstants.THU;
                case "5":
                    return InspectPlanCronWeekConstants.FRI;
                case "6":
                    return InspectPlanCronWeekConstants.SAT;
                default:
                    return InspectPlanCronWeekConstants.SUN;
            }
        } else if (InspectPlanCronTypeConstants.CUSTOMIZE.equals(cronType)) {
            return curDay;
        } else {
            // 如果是每天的话默认其map的key是everyDay
            return "everyDay";
        }
    }

    /**
     * <p>
     * 将周信息转换成cron表达式中对应的数值
     * </p>
     *
     * @param key map的key
     * @return 各个key对应的周 如 MON - 1
     */
    private String getCornWeekByKeySet(String key) {
        switch (key) {
            case InspectPlanCronWeekConstants.MON:
                return "1";
            case InspectPlanCronWeekConstants.TUE:
                return "2";
            case InspectPlanCronWeekConstants.WED:
                return "3";
            case InspectPlanCronWeekConstants.THU:
                return "4";
            case InspectPlanCronWeekConstants.FRI:
                return "5";
            case InspectPlanCronWeekConstants.SAT:
                return "6";
            default:
                return "7";
        }
    }

    /**
     * <p>
     * 判断这个key是否是自定义的key
     * </p>
     *
     * @param key 要进行判断的key
     * @return 如果是每天/按周的key则返回true
     */
    boolean keyIsNotCustomer(String key) {
        return "everyDay".equals(key)
                || InspectPlanCronWeekConstants.MON.equals(key)
                || InspectPlanCronWeekConstants.TUE.equals(key)
                || InspectPlanCronWeekConstants.WED.equals(key)
                || InspectPlanCronWeekConstants.THU.equals(key)
                || InspectPlanCronWeekConstants.FRI.equals(key)
                || InspectPlanCronWeekConstants.SAT.equals(key)
                || InspectPlanCronWeekConstants.SUN.equals(key);
    }

    /**
     * <p>
     * 判断这个key是否是自定义的key
     * </p>
     *
     * @param key 要进行判断的key
     * @return 如果是每天/按周的key则返回true
     */
    boolean keyIsWeek(String key) {
        return InspectPlanCronWeekConstants.MON.equals(key)
                || InspectPlanCronWeekConstants.TUE.equals(key)
                || InspectPlanCronWeekConstants.WED.equals(key)
                || InspectPlanCronWeekConstants.THU.equals(key)
                || InspectPlanCronWeekConstants.FRI.equals(key)
                || InspectPlanCronWeekConstants.SAT.equals(key)
                || InspectPlanCronWeekConstants.SUN.equals(key);
    }


}