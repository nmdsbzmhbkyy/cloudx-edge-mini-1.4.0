package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.vo.ProjectStaffRotaDetailFromVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectStaffRotaDetailMapper;
import com.aurine.cloudx.estate.entity.ProjectStaffRotaDetail;
import com.aurine.cloudx.estate.service.ProjectStaffRotaDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 值班明细(ProjectStaffRotaDetail)表服务实现类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:49:09
 */
@Service
public class ProjectStaffRotaDetailServiceImpl extends ServiceImpl<ProjectStaffRotaDetailMapper, ProjectStaffRotaDetail> implements ProjectStaffRotaDetailService {

    @Override
    public Boolean saveProjectStaffRotaDetail(ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo) {
        projectStaffRotaDetailFromVo.setStartTime(LocalTime.parse(projectStaffRotaDetailFromVo.getStartTimeString()));
        projectStaffRotaDetailFromVo.setEndTime(LocalTime.parse(projectStaffRotaDetailFromVo.getEndTimeString()));
        LocalTime startTime = projectStaffRotaDetailFromVo.getStartTime();
        LocalTime endTime = projectStaffRotaDetailFromVo.getEndTime();
        //获取当前日期下的所有值班明细
        List<ProjectStaffRotaDetail> projectStaffRotaDetails = super.list(Wrappers.lambdaQuery(ProjectStaffRotaDetail.class)
                .eq(ProjectStaffRotaDetail::getRotaId, projectStaffRotaDetailFromVo.getRotaId()));
        projectStaffRotaDetails.forEach(e -> {
            if (startTime.isAfter(e.getStartTime()) && startTime.isBefore(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
            if (e.getStartTime().isAfter(startTime) && e.getEndTime().isBefore(endTime)) {
                throw new RuntimeException("已有重叠时间");
            }
            if (endTime.isAfter(e.getStartTime()) && endTime.isBefore(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
            if (startTime.equals(e.getStartTime()) || startTime.equals(e.getEndTime()) || endTime.equals(e.getStartTime()) || endTime.equals(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
        });
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectStaffRotaDetailFromVo.setRotaDetailId(uuid);
        ProjectStaffRotaDetail projectStaffRotaDetail = new ProjectStaffRotaDetail();
        BeanUtils.copyProperties(projectStaffRotaDetailFromVo, projectStaffRotaDetail);
        return super.save(projectStaffRotaDetail);
    }

    @Override
    public Boolean updateProjectStaffRotaDetail(ProjectStaffRotaDetailFromVo projectStaffRotaDetailFromVo) {
        projectStaffRotaDetailFromVo.setStartTime(LocalTime.parse(projectStaffRotaDetailFromVo.getStartTimeString()));
        projectStaffRotaDetailFromVo.setEndTime(LocalTime.parse(projectStaffRotaDetailFromVo.getEndTimeString()));
        LocalTime startTime = projectStaffRotaDetailFromVo.getStartTime();
        LocalTime endTime = projectStaffRotaDetailFromVo.getEndTime();
        //获取当前日期下除本身外的所有值班明细
        List<ProjectStaffRotaDetail> projectStaffRotaDetails = super.list(Wrappers.lambdaQuery(ProjectStaffRotaDetail.class)
                .eq(ProjectStaffRotaDetail::getRotaId, projectStaffRotaDetailFromVo.getRotaId())).stream()
                .filter(e -> !projectStaffRotaDetailFromVo.getRotaDetailId().equals(e.getRotaDetailId()))
                .collect(Collectors.toList());
        projectStaffRotaDetails.forEach(e -> {
            if (startTime.isAfter(e.getStartTime()) && startTime.isBefore(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
            if (e.getStartTime().isAfter(startTime) && e.getEndTime().isBefore(endTime)) {
                throw new RuntimeException("已有重叠时间");
            }
            if (endTime.isAfter(e.getStartTime()) && endTime.isBefore(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
            if (startTime.equals(e.getStartTime()) || startTime.equals(e.getEndTime()) || endTime.equals(e.getStartTime()) || endTime.equals(e.getEndTime())) {
                throw new RuntimeException("已有重叠时间");
            }
        });
        ProjectStaffRotaDetail projectStaffRotaDetail = new ProjectStaffRotaDetail();
        BeanUtils.copyProperties(projectStaffRotaDetailFromVo, projectStaffRotaDetail);
        return super.updateById(projectStaffRotaDetail);
    }
}