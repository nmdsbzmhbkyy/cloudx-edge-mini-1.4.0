package com.aurine.cloudx.estate.service.impl;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectStaffShiftDetailMapper;
import com.aurine.cloudx.estate.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.estate.service.ProjectStaffShiftDetailService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * 员工排班明细记录(ProjectStaffShiftDetail)表服务实现类
 *
 * @author makejava
 * @since 2020-07-31 13:36:11
 */
@Service
public class ProjectStaffShiftDetailServiceImpl extends ServiceImpl<ProjectStaffShiftDetailMapper, ProjectStaffShiftDetail> implements ProjectStaffShiftDetailService {

    @Override
    public Integer getAttendanceDay(String staffId, LocalDate date) {
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
        ProjectStaffShiftDetail staffShiftDetail = this.getOne(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        JSONObject staffShiftDetailObj = JSONUtil.parseObj(staffShiftDetail);
        Integer num = 0;
        for (Integer i = 1; i <= day; i++) {
            String shiftName = (String) staffShiftDetailObj.get("day" + i);
            if (shiftName != null && !"".equals(shiftName) && !"休息".equals(shiftName) && !"排休".equals(shiftName)) {
                num++;
            }
        }
        return num;
    }

    @Override
    public Integer getAttendanceDay(String staffId) {
        LocalDate date = LocalDate.now();
        Integer year = date.getYear();
        Integer month = date.getMonthValue();
        Integer day = date.getDayOfMonth();
        ProjectStaffShiftDetail staffShiftDetail = this.getOne(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        JSONObject staffShiftDetailObj = JSONUtil.parseObj(staffShiftDetail);
        Integer num = 0;
        for (Integer i = 1; i <= day; i++) {
            String shiftName = (String) staffShiftDetailObj.get("day" + i);
            if (shiftName == null || "".equals(shiftName) || !"休息".equals(shiftName) || !"排休".equals(shiftName)) {
                num++;
            }
        }
        return day - num;
    }
}