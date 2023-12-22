package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectStaffRotaDetail;
import com.aurine.cloudx.estate.service.ProjectStaffRotaDetailService;
import com.aurine.cloudx.estate.vo.ProjectStaffRotaFromVo;
import com.aurine.cloudx.estate.vo.ProjectStaffRotaPageVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectStaffRotaMapper;
import com.aurine.cloudx.estate.entity.ProjectStaffRota;
import com.aurine.cloudx.estate.service.ProjectStaffRotaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 项目员工值班表信息(ProjectStaffRota)表服务实现类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-03 11:48:49
 */
@Service
public class ProjectStaffRotaServiceImpl extends ServiceImpl<ProjectStaffRotaMapper, ProjectStaffRota> implements ProjectStaffRotaService {

    @Autowired
    private ProjectStaffRotaDetailService projectStaffRotaDetailService;

    @Override
    public List<ProjectStaffRotaPageVo> listStaffRota() {
        //获取员工值班表所有信息
        List<ProjectStaffRota> projectStaffRotas = super.list();
        //员工值班表详细信息集合
        List<ProjectStaffRotaPageVo> projectStaffRotaPageVos = new ArrayList<>();
        projectStaffRotas.forEach(e -> {
            //员工值班表详细信息
            ProjectStaffRotaPageVo projectStaffRotaPageVo = new ProjectStaffRotaPageVo();
            List<ProjectStaffRotaDetail> projectStaffRotaDetails = projectStaffRotaDetailService.list(Wrappers.lambdaQuery(ProjectStaffRotaDetail.class)
                    .eq(ProjectStaffRotaDetail::getRotaId, e.getRotaId()));
            projectStaffRotaPageVo.setProjectStaffRotaDetails(projectStaffRotaDetails);
            BeanUtils.copyProperties(e, projectStaffRotaPageVo);
            projectStaffRotaPageVos.add(projectStaffRotaPageVo);
        });
        return projectStaffRotaPageVos;
    }

    @Override
    public Boolean saveProjectStaffRota(ProjectStaffRotaFromVo projectStaffRotaFromVo) {
        projectStaffRotaFromVo.setStartDate(LocalDate.parse(projectStaffRotaFromVo.getStartDateString()));
        projectStaffRotaFromVo.setEndDate(LocalDate.parse(projectStaffRotaFromVo.getEndDateString()));
        LocalDate now = LocalDate.now();
        if (projectStaffRotaFromVo.getStartDate().isBefore(now)) {
            throw new RuntimeException("开始日期只能选择在操作当天以后的时间");
        }
        LocalDate startDate = projectStaffRotaFromVo.getStartDate();
        LocalDate endDate = projectStaffRotaFromVo.getEndDate();
        //获取所有值班信息
        List<ProjectStaffRota> projectStaffRotas = super.list();
        projectStaffRotas.forEach(e -> {
            if (startDate.isAfter(e.getStartDate()) && startDate.isBefore(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
            if (e.getStartDate().isAfter(startDate) && e.getEndDate().isBefore(endDate)) {
                throw new RuntimeException("已有重叠日期");
            }
            if (endDate.isAfter(e.getStartDate()) && endDate.isBefore(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
            if (startDate.equals(e.getStartDate()) || startDate.equals(e.getEndDate()) || endDate.equals(e.getStartDate()) || endDate.equals(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
        });
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        projectStaffRotaFromVo.setRotaId(uuid);
        ProjectStaffRota projectStaffRota = new ProjectStaffRota();
        BeanUtils.copyProperties(projectStaffRotaFromVo, projectStaffRota);
        return super.save(projectStaffRota);
    }

    @Override
    public Boolean updateProjectStaffRota(ProjectStaffRotaFromVo projectStaffRotaFromVo) {
        projectStaffRotaFromVo.setStartDate(LocalDate.parse(projectStaffRotaFromVo.getStartDateString()));
        projectStaffRotaFromVo.setEndDate(LocalDate.parse(projectStaffRotaFromVo.getEndDateString()));
        LocalDate now = LocalDate.now();
        if (projectStaffRotaFromVo.getStartDate().isBefore(now)) {
            throw new RuntimeException("开始日期只能选择在操作当天以后的时间");
        }
        LocalDate startDate = projectStaffRotaFromVo.getStartDate();
        LocalDate endDate = projectStaffRotaFromVo.getEndDate();
        //获取除本身外的所有值班信息
        List<ProjectStaffRota> projectStaffRotas = super.list().stream()
                .filter(e -> !projectStaffRotaFromVo.getRotaId().equals(e.getRotaId()))
                .collect(Collectors.toList());
        projectStaffRotas.forEach(e -> {
            if (startDate.isAfter(e.getStartDate()) && startDate.isBefore(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
            if (e.getStartDate().isAfter(startDate) && e.getEndDate().isBefore(endDate)) {
                throw new RuntimeException("已有重叠日期");
            }
            if (endDate.isAfter(e.getStartDate()) && endDate.isBefore(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
            if (startDate.equals(e.getStartDate()) || startDate.equals(e.getEndDate()) || endDate.equals(e.getStartDate()) || endDate.equals(e.getEndDate())) {
                throw new RuntimeException("已有重叠日期");
            }
        });

        ProjectStaffRota projectStaffRota = new ProjectStaffRota();
        BeanUtils.copyProperties(projectStaffRotaFromVo, projectStaffRota);
        return super.updateById(projectStaffRota);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean removeByRotaId(String rotaId) {
        projectStaffRotaDetailService.remove(Wrappers.lambdaQuery(ProjectStaffRotaDetail.class).eq(ProjectStaffRotaDetail::getRotaId, rotaId));
        return super.removeById(rotaId);
    }
}