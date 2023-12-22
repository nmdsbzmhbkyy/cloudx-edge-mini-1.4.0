package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.open.origin.vo.ProjectStaffShiftDetailPageVo;
import com.aurine.cloudx.open.origin.mapper.ProjectStaffShiftDetailMapper;
import com.aurine.cloudx.open.origin.entity.ProjectStaffShiftDetail;
import com.aurine.cloudx.open.origin.vo.ProjectStaffShiftDetailUpdateVo;
import com.aurine.cloudx.open.origin.service.ProjectStaffShiftDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        List<ProjectStaffShiftDetail> staffShiftDetailList = this.list(new QueryWrapper<ProjectStaffShiftDetail>().lambda()
                .eq(ProjectStaffShiftDetail::getStaffId, staffId)
                .eq(ProjectStaffShiftDetail::getPlanYear, year)
                .eq(ProjectStaffShiftDetail::getPlanMonth, month));
        Integer num = 0;
        for (ProjectStaffShiftDetail staffShiftDetail:
                staffShiftDetailList) {
            JSONObject obj = JSONUtil.parseObj(staffShiftDetail);
            for (Integer i = 1; i <= day; i++) {
                String shiftName = (String) obj.get("day" + i);
                if (!"".equals(shiftName) && !"休息".equals(shiftName) && !"排休".equals(shiftName)) {
                    num++;
                }
            }
        }
        return num;
    }

    @Override
    public Page<ProjectStaffShiftDetailPageVo> pageShiftDetail(Page page, ProjectStaffShiftDetailPageVo projectStaffShiftDetailPageVo) {
        Page<ProjectStaffShiftDetailPageVo> detailPageVo = baseMapper.pageShiftDetail(page, projectStaffShiftDetailPageVo);
        //分页出来的集合
        List<ProjectStaffShiftDetailPageVo> records = detailPageVo.getRecords();
        //去重后的集合
        List<ProjectStaffShiftDetailPageVo> distinvtRecords = records.stream().filter(distinctByKey(key -> key.getStaffId())).collect(Collectors.toList());

        //根据员工id将排班计划合并
        List<ProjectStaffShiftDetailPageVo> projectStaffShiftDetailPageVos = distinvtRecords.stream().map(e -> {
            JSONObject returnObject = JSONUtil.parseObj(e);
            List<ProjectStaffShiftDetailPageVo> list = records.stream()
                    .filter(record -> record.getStaffId().equals(e.getStaffId()))
                    .collect(Collectors.toList());
            list.forEach(detail -> {
                JSONObject jsonObject = JSONUtil.parseObj(detail);
                for (int i = 1; i <= 31; i++) {
                    String shiftName = (String) jsonObject.get("day" + i);
                    if (StrUtil.isNotEmpty(shiftName) && StrUtil.isEmpty((String) returnObject.get("day" + i))) {
                        returnObject.putOpt("day" + i, shiftName);
                    }
                }
            });
            return JSONUtil.toBean(returnObject, ProjectStaffShiftDetailPageVo.class);
        }).collect(Collectors.toList());

        detailPageVo.setRecords(projectStaffShiftDetailPageVos);
        detailPageVo.setTotal(projectStaffShiftDetailPageVos.size());
        return detailPageVo;
    }

    @Override
    public Boolean updateShiftDetail(ProjectStaffShiftDetailUpdateVo projectStaffShiftDetailUpdateVo) {
        //需要更新的字段名
        String column = projectStaffShiftDetailUpdateVo.getColumn();
        //转json用于通过字段名称获取值
        JSONObject detailUpdateVo = JSONUtil.parseObj(projectStaffShiftDetailUpdateVo);
        //此员工当前年月的所有排班计划详情
        List<ProjectStaffShiftDetail> projectStaffShiftDetails = list(Wrappers.lambdaQuery(ProjectStaffShiftDetail.class)
                .eq(ProjectStaffShiftDetail::getPlanYear, projectStaffShiftDetailUpdateVo.getPlanYear())
                .eq(ProjectStaffShiftDetail::getPlanMonth, projectStaffShiftDetailUpdateVo.getPlanMonth())
                .eq(ProjectStaffShiftDetail::getStaffId, projectStaffShiftDetailUpdateVo.getStaffId()));
        //用于批量更新
        List<ProjectStaffShiftDetail> details = new ArrayList<>();
        projectStaffShiftDetails.forEach(detail -> {
            JSONObject jsonObject = JSONUtil.parseObj(detail);
            jsonObject.putOpt(column, detailUpdateVo.get(column));
            ProjectStaffShiftDetail projectStaffShiftDetail = JSONUtil.toBean(jsonObject, ProjectStaffShiftDetail.class);
            details.add(projectStaffShiftDetail);
        });
        return updateBatchById(details);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}