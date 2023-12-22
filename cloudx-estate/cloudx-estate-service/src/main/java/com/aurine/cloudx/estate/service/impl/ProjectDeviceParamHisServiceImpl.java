package com.aurine.cloudx.estate.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamHisPageVo;
import com.aurine.cloudx.estate.vo.ProjectDeviceParamSetResultVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aurine.cloudx.estate.mapper.ProjectDeviceParamHisMapper;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamHis;
import com.aurine.cloudx.estate.service.ProjectDeviceParamHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 记录项目设备参数配置的历史记录(ProjectDeviceParamHis)表服务实现类
 *
 * @author makejava
 * @since 2020-12-23 09:31:51
 */
@Service
public class ProjectDeviceParamHisServiceImpl extends ServiceImpl<ProjectDeviceParamHisMapper, ProjectDeviceParamHis> implements ProjectDeviceParamHisService {
    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    SysDeviceProductMapService sysDeviceProductMapService;

    @Override
    public Page<ProjectDeviceParamHisPageVo> pageHis(Page page, ProjectDeviceParamHisPageVo projectDeviceParamHisPageVo) {
        return baseMapper.page(page, projectDeviceParamHisPageVo);
    }


    @Override
    public void addFailedParamHis(List<ProjectDeviceInfo> deviceInfoList) {
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            deviceInfoList.forEach(deviceInfo -> {
                ProjectDeviceParamHis projectDeviceParamHis = new ProjectDeviceParamHis();
                projectDeviceParamHis.setDeviceId(deviceInfo.getDeviceId());
                projectDeviceParamHis.setSN(deviceInfo.getSn());
                projectDeviceParamHis.setProductId(deviceInfo.getProductId());
                projectDeviceParamHis.setModelId(deviceInfo.getBrand());
                projectDeviceParamHis.setConfigTime(LocalDateTime.now());
                projectDeviceParamHis.setDeviceDesc(StrUtil.isNotEmpty(deviceInfo.getDeviceDesc()) ? deviceInfo.getDeviceDesc() : deviceInfo.getDeviceName());
                projectDeviceParamHis.setExecResult("0");
                projectDeviceParamHis.setResult("0");
                this.save(projectDeviceParamHis);
            });
        }
    }

    @Override
    public void addSuccessParamHis(List<ProjectDeviceInfo> deviceInfoList) {
        if (CollUtil.isNotEmpty(deviceInfoList)) {
            Set<String> deviceIdSet = deviceInfoList.stream().map(ProjectDeviceInfo::getDeviceId).collect(Collectors.toSet());
            SysDeviceProductMap sysDeviceProductMap = sysDeviceProductMapService.getOne(new QueryWrapper<SysDeviceProductMap>().lambda()
                    .eq(SysDeviceProductMap::getProductId, deviceInfoList.get(0).getProductId()));
            // 这里将所有记录都变成已重配
            this.update(new UpdateWrapper<ProjectDeviceParamHis>().lambda().in(ProjectDeviceParamHis::getDeviceId, deviceIdSet)
                    .set(ProjectDeviceParamHis::getExecResult, "1"));
            List<ProjectDeviceParamHis> deviceParamHisList = new ArrayList<>();
            deviceInfoList.forEach(deviceInfo -> {
                ProjectDeviceParamHis projectDeviceParamHis = new ProjectDeviceParamHis();
                projectDeviceParamHis.setDeviceId(deviceInfo.getDeviceId());
                projectDeviceParamHis.setSN(deviceInfo.getSn());
                projectDeviceParamHis.setProductId(deviceInfo.getProductId());
                projectDeviceParamHis.setDeviceDesc(StrUtil.isNotEmpty(deviceInfo.getDeviceDesc()) ? deviceInfo.getDeviceDesc() : deviceInfo.getDeviceName());
                projectDeviceParamHis.setModelId(sysDeviceProductMap.getModelId());
                projectDeviceParamHis.setConfigTime(LocalDateTime.now());
                projectDeviceParamHis.setExecResult("1");
                projectDeviceParamHis.setResult("1");
                deviceParamHisList.add(projectDeviceParamHis);
            });
            this.saveBatch(deviceParamHisList);
        }
    }

    @Override
    public void updateSuccessParamHis(List<String> deviceIdList) {
        if (CollUtil.isNotEmpty(deviceIdList)) {
            // 这里将所有记录都变成已重配
            this.update(new UpdateWrapper<ProjectDeviceParamHis>().lambda().in(ProjectDeviceParamHis::getDeviceId, deviceIdList)
                    .set(ProjectDeviceParamHis::getExecResult, "1"));
        }
    }

}