package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.mapper.ProjectServiceMapper;
import com.aurine.cloudx.open.origin.entity.ProjectService;
import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
import com.aurine.cloudx.open.origin.vo.ProjectServiceInfoVo;
import com.aurine.cloudx.open.origin.vo.ServiceProjectSaveVo;
import com.aurine.cloudx.open.origin.service.ProjectConfigService;
import com.aurine.cloudx.open.origin.service.ProjectServiceService;
import com.aurine.cloudx.open.origin.service.SysServiceCfgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:18:39
 */
@Service
public class ProjectServiceServiceImpl extends ServiceImpl<ProjectServiceMapper, ProjectService> implements ProjectServiceService {
    @Resource
    private ProjectServiceService projectServiceService;
    @Resource
    private SysServiceCfgService sysServiceCfgService;
    @Resource
    private ProjectConfigService projectConfigService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveByServiceIds(ServiceProjectSaveVo serviceProjectSaveVo) {
        Integer projectId = serviceProjectSaveVo.getProjectId();
        baseMapper.delete(Wrappers.lambdaUpdate(ProjectService.class).eq(ProjectService::getProjectId, projectId));
        List<String> serviceIds = serviceProjectSaveVo.getServiceIds();
        List<ProjectService> projectServices = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        projectConfigService.updateServiceExpTime(projectId, serviceProjectSaveVo.getExpTime());
        serviceIds.forEach(e -> {
            ProjectService projectService = new ProjectService();
            projectService.setServiceId(e);
            projectService.setProjectId(projectId);
            projectService.setExpTime(LocalDateTime.parse(serviceProjectSaveVo.getExpTime(), df));
            projectServices.add(projectService);
        });
        return super.saveBatch(projectServices);
    }

    @Override
    public List<SysServiceCfg> getHouseServiceList() {
        List<ProjectService> projectServiceList = super.list();
        List<String> serviceIds = new ArrayList<>();
        projectServiceList.forEach(e -> {
            serviceIds.add(e.getServiceId());
        });
        return sysServiceCfgService.listByIds(serviceIds);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeExpireProjectService() {
        //该项目下已经过期的增值服务id
        List<String> serviceIds = projectServiceService.list(Wrappers.lambdaUpdate(ProjectService.class)
                .lt(ProjectService::getExpTime, LocalDateTime.now())
                .eq(ProjectService::getProjectId, ProjectContextHolder.getProjectId()))
                .stream().map(ProjectService::getServiceId)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(serviceIds)) {
//            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(serviceIds);
//            sysServiceCfgs.forEach(e -> {
//                IntercomFactoryProducer.getFactory(e.getServiceCode()).getIntercomService().delProject(ProjectContextHolder.getProjectId());
//            });

            projectServiceService.remove(Wrappers.lambdaUpdate(ProjectService.class)
                    .lt(ProjectService::getExpTime, LocalDateTime.now())
                    .eq(ProjectService::getProjectId, ProjectContextHolder.getProjectId()));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void removeRemoteProjectService(Integer projectId, List<String> serviceIds) {
//        List<ProjectServiceInfoVo> projectServiceInfoVos = getHouseServiceByProjectId(projectId);
//        //需要关闭的远端增值服务集合
//        List<String> serviceCodes = projectServiceInfoVos.stream()
//                .filter(projectService -> !serviceIds.contains(projectService.getServiceId()))
//                .map(ProjectServiceInfoVo::getServiceCode)
//                .collect(Collectors.toList());
//        serviceCodes.forEach(e -> {
//            IntercomFactoryProducer.getFactory(e).getIntercomService().delProject(projectId);
//        });
    }

    @Override
    public List<ProjectServiceInfoVo> getHouseServiceByProjectId(Integer projectId) {
        List<ProjectService> projectServices = projectServiceService.list(Wrappers.lambdaQuery(ProjectService.class)
                .eq(ProjectService::getProjectId, projectId).gt(ProjectService::getExpTime, LocalDateTime.now()));
        List<String> serviceIds = new ArrayList<>();
        projectServices.forEach(e -> {
            String serviceId = e.getServiceId();
            serviceIds.add(serviceId);
        });
        if (serviceIds.size() > 0) {
            //同一个项目下只使用一个截止时间
            LocalDateTime expTime = projectServices.get(0).getExpTime();
            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(serviceIds);
            List<ProjectServiceInfoVo> projectServiceInfoVos = new ArrayList<>();
            sysServiceCfgs.forEach(e -> {
                ProjectServiceInfoVo projectServiceInfoVo = new ProjectServiceInfoVo();
                BeanUtils.copyProperties(e, projectServiceInfoVo);
                projectServiceInfoVo.setExpTime(expTime);
                projectServiceInfoVos.add(projectServiceInfoVo);
            });
            return projectServiceInfoVos;
        } else {
            return new ArrayList<>();
        }

    }
}
