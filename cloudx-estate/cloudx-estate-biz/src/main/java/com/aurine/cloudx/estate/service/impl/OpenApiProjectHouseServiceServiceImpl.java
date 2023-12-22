package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.common.core.exception.OpenApiServiceException;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.dto.OpenApiProjectHouseServiceDto;
import com.aurine.cloudx.estate.entity.ProjectHouseInfo;
import com.aurine.cloudx.estate.entity.ProjectHouseService;
import com.aurine.cloudx.estate.entity.ProjectService;
import com.aurine.cloudx.estate.entity.SysServiceCfg;
import com.aurine.cloudx.estate.mapper.ProjectHouseServiceMapper;
import com.aurine.cloudx.estate.service.*;
import com.aurine.cloudx.estate.thirdparty.module.intercom.factory.IntercomFactoryProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 开放平台内部项目房屋增值服务ServiceImpl
 *
 * @author : Qiu
 * @date : 2022/7/13 15:31
 */

@Service
public class OpenApiProjectHouseServiceServiceImpl extends ServiceImpl<ProjectHouseServiceMapper, ProjectHouseService> implements OpenApiProjectHouseServiceService {

    @Resource
    private SysServiceCfgService sysServiceCfgService;

    @Resource
    private ProjectServiceService projectServiceService;

    @Resource
    private ProjectHouseServiceService projectHouseServiceService;

    @Resource
    private ProjectHouseInfoService projectHouseInfoService;


    @Override
    public R<Boolean> save(OpenApiProjectHouseServiceDto dto) {
        String houseId = dto.getHouseId();
        List<String> houseIdList = dto.getHouseIdList();

        if (StringUtil.isNotBlank(houseId)) {
            return this.saveOne(dto);
        }

        if (CollUtil.isNotEmpty(houseIdList)) {
            return this.saveBatch(dto);
        }

        throw new OpenApiServiceException("为房屋新增增值服务失败，缺少房屋ID（houseId）或房屋ID列表（houseIdList）");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> saveOne(OpenApiProjectHouseServiceDto dto) {
        String houseId = dto.getHouseId();
        String sourceServiceId = dto.getServiceId();
        List<String> serviceIdList = dto.getServiceIdList();
        LocalDateTime expTime = dto.getExpTime();

        // 参数校验
        if (StringUtil.isBlank(houseId)) {
            throw new OpenApiServiceException("为房屋新增增值服务失败，缺少房屋ID（houseId）");
        }
        if (StringUtil.isBlank(sourceServiceId) && CollUtil.isEmpty(serviceIdList)) {
            throw new OpenApiServiceException("为房屋新增增值服务失败，缺少服务ID（serviceId）或服务ID列表（serviceIdList）");
        }

        // 把sourceServiceId放入serviceIdList中
        if (StringUtil.isNotBlank(sourceServiceId)) {
            if (CollUtil.isEmpty(serviceIdList)) {
                serviceIdList = new ArrayList<>();
            }
            serviceIdList.add(sourceServiceId);
        }

        Integer projectId = ProjectContextHolder.getProjectId();
        for (String serviceId : serviceIdList) {
            SysServiceCfg sysServiceCfg = sysServiceCfgService.getById(serviceId);
            if (sysServiceCfg == null) {
                throw new OpenApiServiceException(String.format("不存在该增值服务，serviceId=%s", serviceId));
            }

            int existCount = projectHouseServiceService.count(new LambdaQueryWrapper<ProjectHouseService>()
                    .eq(ProjectHouseService::getHouseId, houseId)
                    .eq(ProjectHouseService::getServiceId, serviceId)
            );
            if (existCount > 0) {
                throw new OpenApiServiceException(String.format("该增值服务已开通，serviceId=%s", serviceId));
            }

            // 如果没传入失效时间，则获取项目开通且未过期的增值服务，查询出失效时间
            if (expTime == null) {
                List<ProjectService> projectServiceList = projectServiceService.list(new LambdaQueryWrapper<ProjectService>()
                        .eq(ProjectService::getProjectId, projectId)
                        .eq(ProjectService::getServiceId, serviceId)
                        .and(qw -> qw.isNull(ProjectService::getExpTime).or().gt(ProjectService::getExpTime, LocalDateTime.now())));
                if (CollUtil.isEmpty(projectServiceList)) {
                    throw new OpenApiServiceException("项目未开通该增值服务或该增值服务已失效");
                }

                expTime = projectServiceList.get(0).getExpTime();
                dto.setExpTime(expTime);
            }

            // 设置po对象
            ProjectHouseService projectHouseService = new ProjectHouseService();
            projectHouseService.setHouseId(houseId);
            projectHouseService.setServiceId(serviceId);
            projectHouseService.setExpTime(expTime);

            // 保存房屋增值服务
            super.save(projectHouseService);

            // 为房屋中的住户配置增值服务
            IntercomFactoryProducer.getFactory(sysServiceCfg.getServiceCode()).getIntercomService().addByHouse(houseId, projectId);
        }

        // 修改房屋的增值服务到期时间
        projectHouseInfoService.update(Wrappers.lambdaUpdate(ProjectHouseInfo.class)
                .set(ProjectHouseInfo::getServiceExpTime, expTime)
                .eq(ProjectHouseInfo::getHouseId, houseId)
        );

        return R.ok(true);
    }

    @Override
    public R<Boolean> saveBatch(OpenApiProjectHouseServiceDto dto) {
        List<String> houseIdList = dto.getHouseIdList();
        if (CollUtil.isEmpty(houseIdList)) {
            throw new OpenApiServiceException("为房屋新增增值服务失败，房屋ID列表（houseIdList）为空");
        }

        // 因存在复杂逻辑，所以这里循环调用新增单个增值服务的方法
        for (String houseId : houseIdList) {
            dto.setHouseId(houseId);
            this.saveOne(dto);
        }

        return R.ok(true);
    }

    @Override
    public R<Boolean> delete(OpenApiProjectHouseServiceDto dto) {
        String houseId = dto.getHouseId();
        List<String> houseIdList = dto.getHouseIdList();

        if (StringUtil.isNotBlank(houseId)) {
            return this.deleteOne(dto);
        }

        if (CollUtil.isNotEmpty(houseIdList)) {
            return this.deleteBatch(dto);
        }

        throw new OpenApiServiceException("为房屋删除增值服务失败，缺少房屋ID（houseId）或房屋ID列表（houseIdList）");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> deleteOne(OpenApiProjectHouseServiceDto dto) {
        String houseId = dto.getHouseId();
        String sourceServiceId = dto.getServiceId();
        List<String> serviceIdList = dto.getServiceIdList();

        // 参数校验
        if (StringUtil.isBlank(houseId)) {
            throw new OpenApiServiceException("为房屋删除增值服务失败，缺少房屋ID（houseId）");
        }
        if (StringUtil.isBlank(sourceServiceId) && CollUtil.isEmpty(serviceIdList)) {
            throw new OpenApiServiceException("为房屋删除增值服务失败，缺少服务ID（serviceId）或服务ID列表（serviceIdList）");
        }

        // 把sourceServiceId放入serviceIdList中
        if (StringUtil.isNotBlank(sourceServiceId)) {
            if (CollUtil.isEmpty(serviceIdList)) {
                serviceIdList = new ArrayList<>();
            }
            serviceIdList.add(sourceServiceId);
        }

        // 为房屋删除增值服务
        projectHouseServiceService.remove(new LambdaQueryWrapper<ProjectHouseService>()
                .eq(ProjectHouseService::getHouseId, houseId)
                .in(ProjectHouseService::getServiceId, serviceIdList)
        );

        // 删除房屋远端相关增值服务
        projectHouseServiceService.removeRemoteHouseService(houseId, serviceIdList);

        return R.ok(true);
    }

    @Override
    public R<Boolean> deleteBatch(OpenApiProjectHouseServiceDto dto) {
        List<String> houseIdList = dto.getHouseIdList();
        if (CollUtil.isEmpty(houseIdList)) {
            throw new OpenApiServiceException("为房屋删除增值服务失败，房屋ID列表（houseIdList）为空");
        }

        // 因存在复杂逻辑，所以这里循环调用删除单个增值服务的方法
        for (String houseId : houseIdList) {
            dto.setHouseId(houseId);
            this.deleteOne(dto);
        }

        return R.ok(true);
    }
}
