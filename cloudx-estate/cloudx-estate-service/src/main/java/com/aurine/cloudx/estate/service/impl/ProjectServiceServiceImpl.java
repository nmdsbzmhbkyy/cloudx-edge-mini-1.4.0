
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.mapper.ProjectServiceMapper;
import com.aurine.cloudx.estate.service.ProjectServiceService;
import com.aurine.cloudx.estate.service.SysServiceCfgService;
import com.aurine.cloudx.estate.vo.ServiceProjectSaveVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 11:18:39
 */
@Service
public class ProjectServiceServiceImpl extends ServiceImpl<ProjectServiceMapper, ProjectService> implements ProjectServiceService {
    @Autowired
    private SysServiceCfgService sysServiceCfgService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean saveByServiceIds(ServiceProjectSaveVo serviceProjectSaveVo) {
        Integer projectId = serviceProjectSaveVo.getProjectId();
        baseMapper.delete(Wrappers.lambdaUpdate(ProjectService.class).eq(ProjectService::getProjectId, projectId));
        List<String> serviceIds = serviceProjectSaveVo.getServiceIds();
        List<ProjectService> projectServices = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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


}
