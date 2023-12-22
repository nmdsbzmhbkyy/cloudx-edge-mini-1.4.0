package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectShiftPlanStaff;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.estate.service.ProjectShiftPlanStaffService;
import com.aurine.cloudx.estate.service.ProjectStaffShiftDetailService;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanFromVo;
import com.aurine.cloudx.estate.vo.ProjectShiftPlanPageVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectShiftPlanMapper;
import com.aurine.cloudx.estate.entity.ProjectShiftPlan;
import com.aurine.cloudx.estate.service.ProjectShiftPlanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
}