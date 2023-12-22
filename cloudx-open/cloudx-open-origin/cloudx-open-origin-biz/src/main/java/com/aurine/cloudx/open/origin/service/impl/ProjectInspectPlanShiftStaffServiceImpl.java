package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.open.origin.mapper.ProjectInspectPlanShiftStaffMapper;
import com.aurine.cloudx.open.origin.entity.ProjectInspectPlanShiftStaff;
import com.aurine.cloudx.open.origin.service.ProjectInspectPlanShiftStaffService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 巡检计划班次执行人员列表(ProjectInspectPlanShiftStaff)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-27 10:37:23
 */
@Service
public class ProjectInspectPlanShiftStaffServiceImpl extends ServiceImpl<ProjectInspectPlanShiftStaffMapper,
        ProjectInspectPlanShiftStaff> implements ProjectInspectPlanShiftStaffService {

    @Override
    public boolean saveOrUpdateBatchShiftStaffRel(String[] staffIdArr, String recordId) {
        // 这里先删除原有关系（如果有的话）
        this.remove(new QueryWrapper<ProjectInspectPlanShiftStaff>().lambda().eq(ProjectInspectPlanShiftStaff::getRecordId, recordId));
        List<ProjectInspectPlanShiftStaff> shiftStaffList = new ArrayList<>();
        Arrays.stream(staffIdArr).forEach(staffId -> {
            ProjectInspectPlanShiftStaff planShiftStaff = new ProjectInspectPlanShiftStaff();
            planShiftStaff.setStaffId(staffId);
            planShiftStaff.setRecordId(recordId);
            shiftStaffList.add(planShiftStaff);
        });
        return this.saveBatch(shiftStaffList);
    }

    @Override
    public String[] getStaffIdArrByShiftId(String recordId) {
        List<ProjectInspectPlanShiftStaff> shiftStaffList = this.list(new QueryWrapper<ProjectInspectPlanShiftStaff>()
                .lambda().eq(ProjectInspectPlanShiftStaff::getRecordId, recordId));
        List<String> staffIdList = shiftStaffList.stream().map(ProjectInspectPlanShiftStaff::getStaffId).collect(Collectors.toList());
        String[] staffArr = new String[staffIdList.size()];
        return staffIdList.toArray(staffArr);
    }

    @Override
    public boolean removeRel(List<String> recordIdList) {
        if (CollUtil.isNotEmpty(recordIdList)) {
            return this.remove(new QueryWrapper<ProjectInspectPlanShiftStaff>().lambda().in(ProjectInspectPlanShiftStaff::getRecordId, recordIdList));
        }
        return true;
    }

}