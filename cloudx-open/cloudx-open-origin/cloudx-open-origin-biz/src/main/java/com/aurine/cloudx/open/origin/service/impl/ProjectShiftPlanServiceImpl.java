package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.open.origin.vo.ProjectShiftPlanFromVo;
import com.aurine.cloudx.open.origin.mapper.ProjectShiftPlanMapper;
import com.aurine.cloudx.open.origin.entity.ProjectShiftPlan;
import com.aurine.cloudx.open.origin.entity.ProjectShiftPlanStaff;
import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.open.origin.vo.ProjectShiftPlanPageVo;
import com.aurine.cloudx.open.origin.service.ProjectShiftPlanService;
import com.aurine.cloudx.open.origin.service.ProjectShiftPlanStaffService;
import com.aurine.cloudx.open.origin.service.ProjectStaffShiftDetailService;
import com.aurine.cloudx.open.common.core.util.HolidayUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 排班计划(ProjectShiftPlan)表服务实现类
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 10:48:33
 */
@Service
public class ProjectShiftPlanServiceImpl extends ServiceImpl<ProjectShiftPlanMapper, ProjectShiftPlan> implements ProjectShiftPlanService {

    @Autowired
    private ProjectShiftPlanStaffService projectShiftPlanStaffService;
    @Autowired
    private ProjectStaffShiftDetailService projectStaffShiftDetailService;

    @Override
    public Page<ProjectShiftPlanPageVo> pageShiftPlan(Page<ProjectShiftPlanPageVo> page, ProjectShiftPlanPageVo projectShiftPlanPageVo) {
        return baseMapper.pageShiftPlan(page, projectShiftPlanPageVo);
    }

    @Override
    public ProjectShiftPlanFromVo getByPlanId(String planId) {
        ProjectShiftPlanFromVo projectShiftPlanFromVo = new ProjectShiftPlanFromVo();
        ProjectShiftPlan projectShiftPlan = getById(planId);
        //获取排班参与人信息
        List<ProjectShiftPlanStaff> projectShiftPlanStaffs = projectShiftPlanStaffService.list(Wrappers.lambdaQuery(ProjectShiftPlanStaff.class)
                .eq(ProjectShiftPlanStaff::getPlanId, planId));
        //员工id列表
        List<String> staffIds = new ArrayList<>();
        projectShiftPlanStaffs.forEach(e -> {
            staffIds.add(e.getStaffId());
        });
        //获取排班明细
        List<ProjectStaffShiftDetail> projectStaffShiftDetails = projectStaffShiftDetailService.list(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class)
                .eq(ProjectStaffShiftDetail::getPlanId, planId));
        BeanUtils.copyProperties(projectShiftPlan, projectShiftPlanFromVo);
        projectShiftPlanFromVo.setStaffIds(staffIds);
        projectShiftPlanFromVo.setProjectStaffShiftDetails(projectStaffShiftDetails);
        return projectShiftPlanFromVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveShiftPlan(ProjectShiftPlanFromVo projectShiftPlanFromVo) {
        LocalDate startDate = projectShiftPlanFromVo.getStartDate();
        LocalDate endDate = projectShiftPlanFromVo.getEndDate();
        List<String> planIds = projectStaffShiftDetailService.list(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class)
                .in(ProjectStaffShiftDetail::getStaffId, projectShiftPlanFromVo.getStaffIds()))
                .stream().map(ProjectStaffShiftDetail::getPlanId).distinct().collect(Collectors.toList());

        if (CollUtil.isNotEmpty(planIds)) {
            List<ProjectShiftPlan> projectShiftPlans = listByIds(planIds);
            projectShiftPlans.forEach(e -> {
                if (startDate.isAfter(e.getStartDate()) && startDate.isBefore(e.getStartDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (endDate.isAfter(e.getStartDate()) && endDate.isBefore(e.getStartDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (startDate.isBefore(e.getStartDate()) && endDate.isAfter(e.getEndDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (startDate.equals(e.getStartDate()) || startDate.equals(e.getEndDate()) || endDate.equals(e.getStartDate()) || endDate.equals(e.getEndDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
            });
        }

        List<ProjectShiftPlan> list = super.list(Wrappers.lambdaQuery(ProjectShiftPlan.class)
                .eq(ProjectShiftPlan::getPlanName, projectShiftPlanFromVo.getPlanName()));
        if (list.size() > 0) {
            throw new RuntimeException("排班名称已存在");
        }
        String planId = UUID.randomUUID().toString().replaceAll("-", "");
        projectShiftPlanFromVo.setPlanId(planId);
        //获取员工id集合
        List<String> staffIds = projectShiftPlanFromVo.getStaffIds();
        //排班计划关系表
        List<ProjectShiftPlanStaff> projectShiftPlanStaffs = new ArrayList<>();
        staffIds.forEach(e -> {
            String relId = UUID.randomUUID().toString().replaceAll("-", "");
            ProjectShiftPlanStaff projectShiftPlanStaff = new ProjectShiftPlanStaff();
            projectShiftPlanStaff.setRelId(relId);
            projectShiftPlanStaff.setStaffId(e);
            projectShiftPlanStaff.setPlanId(planId);
            projectShiftPlanStaffs.add(projectShiftPlanStaff);
        });
        //保存排班参与人信息
        projectShiftPlanStaffService.saveBatch(projectShiftPlanStaffs);

        List<ProjectStaffShiftDetail> projectStaffShiftDetails = projectShiftPlanFromVo.getProjectStaffShiftDetails();
        projectStaffShiftDetails.forEach(e -> {
            String detailId = UUID.randomUUID().toString().replaceAll("-", "");
            e.setDetailId(detailId);
            e.setPlanId(planId);
        });
        //保存排班明细
        projectStaffShiftDetailService.saveBatch(projectStaffShiftDetails);
        ProjectShiftPlan projectShiftPlan = new ProjectShiftPlan();
        BeanUtils.copyProperties(projectShiftPlanFromVo, projectShiftPlan);
        return super.save(projectShiftPlan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateShiftPlan(ProjectShiftPlanFromVo projectShiftPlanFromVo) {
        LocalDate startDate = projectShiftPlanFromVo.getStartDate();
        LocalDate endDate = projectShiftPlanFromVo.getEndDate();
        List<String> planIds = projectStaffShiftDetailService.list(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class)
                .in(ProjectStaffShiftDetail::getStaffId, projectShiftPlanFromVo.getStaffIds())
                .ne(ProjectStaffShiftDetail::getPlanId, projectShiftPlanFromVo.getPlanId()))
                .stream().map(ProjectStaffShiftDetail::getPlanId).distinct().collect(Collectors.toList());

        if (CollUtil.isNotEmpty(planIds)) {
            List<ProjectShiftPlan> projectShiftPlans = listByIds(planIds);
            projectShiftPlans.forEach(e -> {
                if (startDate.isAfter(e.getStartDate()) && startDate.isBefore(e.getStartDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (endDate.isAfter(e.getStartDate()) && endDate.isBefore(e.getStartDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (startDate.isBefore(e.getStartDate()) && endDate.isAfter(e.getEndDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
                if (startDate.equals(e.getStartDate()) || startDate.equals(e.getEndDate()) || endDate.equals(e.getStartDate()) || endDate.equals(e.getEndDate())) {
                    throw new RuntimeException("存在员工在此时间段内已有排班");
                }
            });
        }
        List<ProjectShiftPlan> list = super.list(Wrappers.lambdaQuery(ProjectShiftPlan.class)
                .eq(ProjectShiftPlan::getPlanName, projectShiftPlanFromVo.getPlanName()))
                .stream().filter(e -> !projectShiftPlanFromVo.getPlanId().equals(e.getPlanId()))
                .collect(Collectors.toList());
        if (list.size() > 0) {
            throw new RuntimeException("排班名称已存在");
        }
        String planId = projectShiftPlanFromVo.getPlanId();
        projectShiftPlanStaffService.remove(Wrappers.lambdaQuery(ProjectShiftPlanStaff.class).eq(ProjectShiftPlanStaff::getPlanId, planId));
        projectStaffShiftDetailService.remove(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class).eq(ProjectStaffShiftDetail::getPlanId, planId));
        //获取员工id集合
        List<String> staffIds = projectShiftPlanFromVo.getStaffIds();
        //排班计划关系表
        List<ProjectShiftPlanStaff> projectShiftPlanStaffs = new ArrayList<>();
        staffIds.forEach(e -> {
            String relId = UUID.randomUUID().toString().replaceAll("-", "");
            ProjectShiftPlanStaff projectShiftPlanStaff = new ProjectShiftPlanStaff();
            projectShiftPlanStaff.setRelId(relId);
            projectShiftPlanStaff.setStaffId(e);
            projectShiftPlanStaff.setPlanId(planId);
            projectShiftPlanStaffs.add(projectShiftPlanStaff);
        });
        List<ProjectStaffShiftDetail> projectStaffShiftDetails = projectShiftPlanFromVo.getProjectStaffShiftDetails();
        projectStaffShiftDetails.forEach(e -> {
            String detailId = UUID.randomUUID().toString().replaceAll("-", "");
            e.setDetailId(detailId);
            e.setPlanId(planId);
        });
        //保存新的排班参与人信息
        projectShiftPlanStaffService.saveBatch(projectShiftPlanStaffs);
        //保存新的排班明细
        projectStaffShiftDetailService.saveBatch(projectStaffShiftDetails);
        ProjectShiftPlan projectShiftPlan = new ProjectShiftPlan();
        BeanUtils.copyProperties(projectShiftPlanFromVo, projectShiftPlan);
        return super.updateById(projectShiftPlan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean deleteByPlanId(String planId) {
        projectShiftPlanStaffService.remove(Wrappers.lambdaQuery(ProjectShiftPlanStaff.class).eq(ProjectShiftPlanStaff::getPlanId, planId));
        projectStaffShiftDetailService.remove(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class).eq(ProjectStaffShiftDetail::getPlanId, planId));
        return removeById(planId);
    }

    @Override
    public ProjectShiftPlan getByPlanName(String planName, String planId) {
        if (StringUtils.isEmpty(planId)) {
            return getOne(Wrappers.lambdaQuery(ProjectShiftPlan.class).eq(ProjectShiftPlan::getPlanName, planName));
        }
        return getOne(Wrappers.lambdaQuery(ProjectShiftPlan.class)
                .eq(ProjectShiftPlan::getPlanName, planName)
                .ne(ProjectShiftPlan::getPlanId, planId));
    }

    @Override
    public List<String> listVacations() {
        return HolidayUtil.getVacations();
    }

    @Override
    public void refreshHoliday() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<LocalDate> vacations = listVacations().stream().map(e -> {
            return LocalDate.parse(e, fmt);
        }).collect(Collectors.toList());
        //所有勾选了节假日自动排班的排班计划id
        List<String> planIds = list(Wrappers.lambdaQuery(ProjectShiftPlan.class).eq(ProjectShiftPlan::getAutoSchedule, "1"))
                .stream().map(ProjectShiftPlan::getPlanId).collect(Collectors.toList());
        //找出所有今年的排班计划详情
        if (CollUtil.isNotEmpty(planIds)) {
            List<ProjectStaffShiftDetail> projectStaffShiftDetails = projectStaffShiftDetailService.list(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class)
                    .eq(ProjectStaffShiftDetail::getPlanYear, LocalDate.now().getYear())
                    .in(ProjectStaffShiftDetail::getPlanId, planIds));
            //需要更新的排班集合
            List<ProjectStaffShiftDetail> details = new ArrayList<>();
            projectStaffShiftDetails.forEach(projectStaffShiftDetail -> {
                int planMonth = Integer.parseInt(projectStaffShiftDetail.getPlanMonth());
                List<LocalDate> currentMonthVacations = vacations.stream().filter(e -> e.getMonthValue() == planMonth).collect(Collectors.toList());
                String jsonString = JSONObject.toJSONString(projectStaffShiftDetail);
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                //对当前月份的所有节假日进行排休
                currentMonthVacations.forEach(e -> {
                    String day = jsonObject.getString("day" + e.getDayOfMonth());
                    //如果为空说明并没有安排排班计划 不能排休
                    if (StrUtil.isNotEmpty(day)) {
                        jsonObject.put("day" + e.getDayOfMonth(), "排休");
                    }
                });
                //需要更新的排班详情
                ProjectStaffShiftDetail detail = JSONObject.parseObject(jsonObject.toJSONString(), ProjectStaffShiftDetail.class);
                details.add(detail);
            });
            projectStaffShiftDetailService.updateBatchById(details);
        }
    }

}