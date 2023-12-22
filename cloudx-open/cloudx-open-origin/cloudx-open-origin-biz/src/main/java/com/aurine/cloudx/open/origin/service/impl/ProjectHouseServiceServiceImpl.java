package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.vo.ServiceHouseIdsSaveVo;
import com.aurine.cloudx.open.origin.mapper.ProjectHouseServiceMapper;
import com.aurine.cloudx.open.origin.entity.ProjectHouseInfo;
import com.aurine.cloudx.open.origin.entity.ProjectHouseService;
import com.aurine.cloudx.open.origin.entity.SysServiceCfg;
import com.aurine.cloudx.open.origin.vo.ProjectHouseServiceInfoVo;
import com.aurine.cloudx.open.origin.vo.ServiceHouseSaveVo;
import com.aurine.cloudx.open.origin.service.ProjectHouseInfoService;
import com.aurine.cloudx.open.origin.service.ProjectHouseServiceService;
import com.aurine.cloudx.open.origin.service.SysServiceCfgService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 房屋增值服务
 *
 * @author guhl@aurine.cn
 * @date 2020-06-04 15:23:14
 */
@Service
@Slf4j
public class ProjectHouseServiceServiceImpl extends ServiceImpl<ProjectHouseServiceMapper, ProjectHouseService> implements ProjectHouseServiceService {

    @Resource
    private SysServiceCfgService sysServiceCfgService;

    @Resource
    ProjectHouseInfoService projectHouseInfoService;

    @Override
    protected Class<ProjectHouseService> currentModelClass() {
        return ProjectHouseService.class;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeExpireHouseService() {
//        //增值服务已经到期的房屋id集合
//        List<String> houseIds = this.list(Wrappers.lambdaUpdate(ProjectHouseService.class)
//                .lt(ProjectHouseService::getExpTime, LocalDateTime.now()))
//                .stream().map(ProjectHouseService::getHouseId)
//                .collect(Collectors.toList());
//        houseIds.forEach(houseId -> {
//            List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = this.getHouseServiceByHouseId(houseId);
//            projectHouseServiceInfoVos.forEach(houseService -> {
//                IntercomFactoryProducer.getFactory(houseService.getServiceCode()).getIntercomService().delByHouse(houseId);
//            });
//        });

        this.remove(Wrappers.lambdaUpdate(ProjectHouseService.class)
                .lt(ProjectHouseService::getExpTime, LocalDateTime.now()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeRemoteHouseService(String houseId, List<String> serviceIds) {
//        List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = this.getHouseServiceByHouseId(houseId);
//        //需要关闭的远端增值服务集合
//        List<String> serviceCodes = projectHouseServiceInfoVos.stream()
//                .filter(houseService -> !serviceIds.contains(houseService.getServiceId()))
//                .map(ProjectHouseServiceInfoVo::getServiceCode)
//                .collect(Collectors.toList());
//        serviceCodes.forEach(e -> {
//            IntercomFactoryProducer.getFactory(e).getIntercomService().delByHouse(houseId);
//        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveByHouse(ServiceHouseSaveVo serviceHouseSaveVo) {
        LocalDateTime date = null;
        List<String> serviceIds = serviceHouseSaveVo.getServiceIds();
        String houseId = serviceHouseSaveVo.getHouseId();
        List<ProjectHouseService> projectHouseServices = new ArrayList<>();
        baseMapper.delete(Wrappers.lambdaUpdate(ProjectHouseService.class).eq(ProjectHouseService::getHouseId, houseId));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if (serviceHouseSaveVo.getExpTime() != null && !"".equals(serviceHouseSaveVo.getExpTime())) {
            date = LocalDateTime.parse(serviceHouseSaveVo.getExpTime(), pattern);
        }
        for (String serviceId : serviceIds) {
            ProjectHouseService projectHouseService = new ProjectHouseService();
            projectHouseService.setServiceId(serviceId);
            projectHouseService.setHouseId(houseId);
            if (StrUtil.isNotEmpty(serviceHouseSaveVo.getExpTime())) {
                projectHouseService.setExpTime(date);
            }
            projectHouseServices.add(projectHouseService);
        }
       /* serviceIds.forEach(e -> {
            ProjectHouseService projectHouseService = new ProjectHouseService();
            projectHouseService.setServiceId(e);
            projectHouseService.setHouseId(houseId);
            if (StrUtil.isNotEmpty(serviceHouseSaveVo.getExpTime())) {
                projectHouseService.setExpTime(date);
            }
            projectHouseServices.add(projectHouseService);
        });*/
        projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                .set(ProjectHouseInfo::getServiceExpTime, date)
                .eq(ProjectHouseInfo::getHouseId, houseId));
        return super.saveBatch(projectHouseServices);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveByHouseIds(ServiceHouseIdsSaveVo serviceHouseIdsSaveVo) {

        List<ProjectHouseService> projectHouseServices = new ArrayList<>();
        List<String> houseIds = serviceHouseIdsSaveVo.getHouseIds();
        List<String> serviceIds = serviceHouseIdsSaveVo.getServiceIds();
        baseMapper.delete(Wrappers.lambdaUpdate(ProjectHouseService.class).in(ProjectHouseService::getHouseId, houseIds));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        if(serviceHouseIdsSaveVo.getExpTime()!= null){
            LocalDateTime date = LocalDateTime.parse(serviceHouseIdsSaveVo.getExpTime(), pattern);
            houseIds.forEach(e -> {
                serviceIds.forEach(f -> {
                    ProjectHouseService projectHouseService = new ProjectHouseService();
                    projectHouseService.setHouseId(e);
                    projectHouseService.setServiceId(f);
                    if (StrUtil.isNotEmpty(serviceHouseIdsSaveVo.getExpTime())) {
//                    LocalDateTime parse = LocalDateTime.parse(serviceHouseIdsSaveVo.getExpTime(),df);
                        projectHouseService.setExpTime(date);
                    }
                    projectHouseServices.add(projectHouseService);
                });
                projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                        .set(ProjectHouseInfo::getServiceExpTime, date)
                        .eq(ProjectHouseInfo::getHouseId, e));
            });
        }else{
            houseIds.forEach(e -> {
                serviceIds.forEach(f -> {
                    ProjectHouseService projectHouseService = new ProjectHouseService();
                    projectHouseService.setHouseId(e);
                    projectHouseService.setServiceId(f);
                    projectHouseServices.add(projectHouseService);
                });
            });
        }

        return super.saveBatch(projectHouseServices);
    }

    @Override
    public List<ProjectHouseServiceInfoVo> getHouseServiceByHouseId(String houseId) {
        List<ProjectHouseService> projectHouseServices = baseMapper.selectList(Wrappers.lambdaQuery(ProjectHouseService.class)
                .eq(ProjectHouseService::getHouseId, houseId)
                .and(qw -> qw.gt(ProjectHouseService::getExpTime, LocalDateTime.now()).or().isNull(ProjectHouseService::getExpTime)));
        List<String> serviceIds = new ArrayList<>();

        projectHouseServices.forEach(e -> {
          Integer num=  baseMapper.findCountNum(e.getServiceId(), ProjectContextHolder.getProjectId());
          log.info("开始判断有无项目增值服务:"+num+"服务id"+e.getServiceId());
          if (num>=1){
              serviceIds.add(e.getServiceId());
          }
        });
        if (CollectionUtil.isNotEmpty(serviceIds)) {
            LocalDateTime expTime = projectHouseServices.get(0).getExpTime();
            List<SysServiceCfg> sysServiceCfgs = sysServiceCfgService.listByIds(serviceIds);
            List<ProjectHouseServiceInfoVo> projectHouseServiceInfoVos = new ArrayList<>();
            sysServiceCfgs.forEach(e -> {
                ProjectHouseServiceInfoVo projectHouseServiceInfoVo = new ProjectHouseServiceInfoVo();
                BeanUtils.copyProperties(e, projectHouseServiceInfoVo);
                projectHouseServiceInfoVo.setExpTime(expTime);
                projectHouseServiceInfoVos.add(projectHouseServiceInfoVo);
            });
            return projectHouseServiceInfoVos;
        } else {
            return new ArrayList<>();
        }
    }

}
