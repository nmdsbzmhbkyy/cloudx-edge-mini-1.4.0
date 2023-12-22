package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.InspectTaskStatusConstant;
import com.aurine.cloudx.estate.entity.ProjectInspectTaskStaff;
import com.aurine.cloudx.estate.mapper.ProjectInspectTaskStaffMapper;
import com.aurine.cloudx.estate.service.ProjectInspectTaskStaffService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 巡检任务人员列表，用于app推送和领取(ProjectInspectTaskStaff)表服务实现类
 *
 * @author  * @author 王良俊
 * @since 2020-10-26 11:41:50
 */
@Service
public class ProjectInspectTaskStaffServiceImpl extends ServiceImpl<ProjectInspectTaskStaffMapper, ProjectInspectTaskStaff> implements ProjectInspectTaskStaffService {

    @Override
    public Integer countStatusByStaffId(String staffId, List<String> status, LocalDate date) {
        if (StrUtil.isEmpty(staffId) || CollUtil.isEmpty(status)) {
            throw new RuntimeException("未获取到员工ID和状态");
        }
        return baseMapper.countCompleteByStaffId(staffId, status, date);
    }

    @Override
    public Integer countCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 巡检中
        status.add(InspectTaskStatusConstant.PROCESSING);
        return this.countStatusByStaffId(staffId, status, date);
    }

    @Override
    public Integer countUnCompleteByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 任务完成
        status.add(InspectTaskStatusConstant.COMPLETED);
        return this.countStatusByStaffId(staffId, status, date);

    }

    @Override
    public Integer countAllByStaffId(String staffId, LocalDate date) {
        List<String> status = new ArrayList<>();
        // 巡检中
        status.add(InspectTaskStatusConstant.PROCESSING);
        // 任务完成
        status.add(InspectTaskStatusConstant.COMPLETED);
        return this.countStatusByStaffId(staffId, status, date);

    }
}