package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.InspectPlanCronTypeConstants;
import com.aurine.cloudx.estate.constant.InspectPlanStatusConstants;
import com.aurine.cloudx.estate.constant.InspectStatusConstants;
import com.aurine.cloudx.estate.entity.ProjectInspectPlan;
import com.aurine.cloudx.estate.mapper.ProjectInspectPlanMapper;
import com.aurine.cloudx.estate.service.ProjectInspectPlanService;
import com.aurine.cloudx.estate.service.ProjectInspectPlanShift3Service;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanSearchConditionVo;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanShift3Vo;
import com.aurine.cloudx.estate.vo.ProjectInspectPlanVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 设备巡检计划设置(ProjectInspectPlan)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:34:05
 */
@Service
public class ProjectInspectPlanServiceImpl extends ServiceImpl<ProjectInspectPlanMapper, ProjectInspectPlan>
        implements ProjectInspectPlanService {

    @Resource
    ProjectInspectPlanShift3Service projectInspectPlanShift3Service;
    @Resource
    ProjectInspectPlanMapper projectInspectPlanMapper;

    @Override
    public Page<ProjectInspectPlanVo> fetchList(Page page, ProjectInspectPlanSearchConditionVo query) {
        return projectInspectPlanMapper.fetchList(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdatePlan(ProjectInspectPlanVo vo) {
        ProjectInspectPlan projectInspectPlan = new ProjectInspectPlan();
        BeanUtil.copyProperties(vo, projectInspectPlan);
        boolean result;
        if (StrUtil.isNotBlank(vo.getPlanId())) {
            result = this.updateById(projectInspectPlan);
        } else {
            projectInspectPlan.setStatus(InspectStatusConstants.ACTIVITY);
            result = this.save(projectInspectPlan);
        }
        // 根据班次执行周期类型调用不同的方法
        switch (vo.getCronType()) {
            case InspectPlanCronTypeConstants.DAY:
                projectInspectPlanShift3Service.saveOrUpdateShiftByDay(vo.getPlanShiftMap(), projectInspectPlan.getPlanId());
                break;
            case InspectPlanCronTypeConstants.WEEK:
                projectInspectPlanShift3Service.saveOrUpdateShiftByWeek(vo.getPlanShiftMap(), projectInspectPlan.getPlanId());
                break;
            default:
                projectInspectPlanShift3Service.saveOrUpdateShiftByCustomer(vo.getPlanShiftMap(), projectInspectPlan.getPlanId());
        }
        return result;
    }

    @Override
    public ProjectInspectPlanVo getPlanById(String planId) {
        List<ProjectInspectPlan> planList = this.list(new QueryWrapper<ProjectInspectPlan>().lambda()
                .eq(ProjectInspectPlan::getPlanId, planId));
        if (CollUtil.isNotEmpty(planList)) {
            ProjectInspectPlan projectInspectPlan = planList.get(0);
            Map<String, List<ProjectInspectPlanShift3Vo>> shiftListMapByPlanId = projectInspectPlanShift3Service
                    .getShiftListMapByPlanId(projectInspectPlan.getPlanId(), projectInspectPlan.getCronType());
            ProjectInspectPlanVo planVo = new ProjectInspectPlanVo();
            BeanUtil.copyProperties(projectInspectPlan, planVo);
            planVo.setPlanShiftMap(shiftListMapByPlanId);
            return planVo;
        }
        return new ProjectInspectPlanVo();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removePlan(String planId) {
        projectInspectPlanShift3Service.removeShiftByPlanId(planId);
        return this.remove(new QueryWrapper<ProjectInspectPlan>().lambda().eq(ProjectInspectPlan::getPlanId, planId));
    }

    @Override
    public boolean changeStatus(String planId, char status) {
        ProjectInspectPlan projectInspectPlan = new ProjectInspectPlan();
        projectInspectPlan.setPlanId(planId);
        projectInspectPlan.setStatus(status);
        return this.updateById(projectInspectPlan);
    }

    @Override
    public boolean dealOutdatedPlan() {
        List<String> planIdList = projectInspectPlanMapper.listAllOutdatedPlan();
        if (CollUtil.isNotEmpty(planIdList)) {
            List<ProjectInspectPlan> planList = new ArrayList<>();
            planIdList.forEach(planId -> {
                ProjectInspectPlan projectInspectPlan = new ProjectInspectPlan();
                projectInspectPlan.setPlanId(planId);
                projectInspectPlan.setStatus(InspectPlanStatusConstants.OVER);
                planList.add(projectInspectPlan);
            });
            return this.updateBatchById(planList);
        }
        return true;
    }

    @Override
    public boolean routeIsUsed(String inspectRouteId) {
        List<ProjectInspectPlan> planList = this.list(new QueryWrapper<ProjectInspectPlan>().lambda().eq(ProjectInspectPlan::getInspectRouteId, inspectRouteId));
        return planList.size() > 0;
    }

}