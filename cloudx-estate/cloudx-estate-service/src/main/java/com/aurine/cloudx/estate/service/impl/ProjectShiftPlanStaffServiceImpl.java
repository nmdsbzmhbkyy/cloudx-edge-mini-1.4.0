package com.aurine.cloudx.estate.service.impl;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectShiftPlanStaffMapper;
import com.aurine.cloudx.estate.entity.ProjectShiftPlanStaff;
import com.aurine.cloudx.estate.service.ProjectShiftPlanStaffService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 记录排班计划关联的参与人信息(ProjectShiftPlanStaff)表服务实现类
 *
 * @author guhl@aurine.cn
 * @since 2020-07-31 11:01:03
 */
@Service
public class ProjectShiftPlanStaffServiceImpl extends ServiceImpl<ProjectShiftPlanStaffMapper, ProjectShiftPlanStaff> implements ProjectShiftPlanStaffService {


    @Override
    public List<String> getByPlanId(String planId) {
        List<String> staffIds = list(Wrappers.lambdaQuery(ProjectShiftPlanStaff.class)
                .eq(ProjectShiftPlanStaff::getPlanId, planId))
                .stream().map(ProjectShiftPlanStaff::getStaffId)
                .collect(Collectors.toList());
        return staffIds;
    }
}