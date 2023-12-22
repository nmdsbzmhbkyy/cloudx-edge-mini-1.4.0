package com.aurine.cloudx.open.origin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.open.origin.mapper.ProjectInspectPointDeviceRelMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectInspectPointDeviceRel;
import com.aurine.cloudx.open.origin.service.ProjectDeviceInfoService;
import com.aurine.cloudx.open.origin.service.ProjectInspectPointDeviceRelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备巡检点与设备关联表(ProjectInspectPointDeviceRel)表服务实现类
 *
 * @author 王良俊
 * @since 2020-07-23 18:26:38
 */
@Service
public class ProjectInspectPointDeviceRelServiceImpl extends ServiceImpl<ProjectInspectPointDeviceRelMapper,
        ProjectInspectPointDeviceRel> implements ProjectInspectPointDeviceRelService {


    @Resource
    ProjectDeviceInfoService projectDeviceInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdatePointDeviceRel(String pointId, String[] deviceIdArr) {
        if (StrUtil.isEmpty(pointId)) {
            return false;
        }
        // 删除原有的设备和巡检点关系(如果不是第一次添加而是更新的话)
        this.remove(new QueryWrapper<ProjectInspectPointDeviceRel>().lambda().eq(ProjectInspectPointDeviceRel::getPointId, pointId));

        List<ProjectInspectPointDeviceRel> pointDeviceRelList = new ArrayList<>();
        List<ProjectDeviceInfo> deviceInfoList = projectDeviceInfoService.list(new LambdaQueryWrapper<ProjectDeviceInfo>().in(ProjectDeviceInfo::getDeviceId, deviceIdArr));
        deviceInfoList.forEach(projectDeviceInfo -> {
            ProjectInspectPointDeviceRel projectInspectPointDeviceRel = new ProjectInspectPointDeviceRel();
            projectInspectPointDeviceRel.setPointId(pointId);
            projectInspectPointDeviceRel.setDeviceId(projectDeviceInfo.getDeviceId());
            projectInspectPointDeviceRel.setDeviceName(projectDeviceInfo.getDeviceName());
            projectInspectPointDeviceRel.setDeviceSn(projectDeviceInfo.getSn());
            projectInspectPointDeviceRel.setDeviceType(projectDeviceInfo.getDeviceType());
            pointDeviceRelList.add(projectInspectPointDeviceRel);
        });
        return this.saveBatch(pointDeviceRelList);
    }

}