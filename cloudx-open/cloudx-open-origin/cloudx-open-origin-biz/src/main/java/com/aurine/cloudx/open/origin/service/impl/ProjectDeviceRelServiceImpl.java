package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceRel;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceRelMapper;
import com.aurine.cloudx.open.origin.service.ProjectDeviceRelService;
import com.aurine.cloudx.open.origin.vo.ProjectDeviceRelVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 设备关系表
 *
 * @author 黄健杰
 * @date 2022-02-07
 */
@Service
public class ProjectDeviceRelServiceImpl extends ServiceImpl<ProjectDeviceRelMapper, ProjectDeviceRel> implements ProjectDeviceRelService {


    @Override
    public void removeByParDeviceId(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectDeviceRel>().eq(ProjectDeviceRel::getParDeviceId, deviceId));
    }

    @Override
    public void removeByDeviceId(String deviceId) {
        this.remove(new LambdaQueryWrapper<ProjectDeviceRel>().eq(ProjectDeviceRel::getDeviceId, deviceId));
    }

    @Override
    public List<ProjectDeviceRelVo> ListByDeviceId(String deviceId) {
        return this.baseMapper.listByDeviceId(deviceId);
    }

    @Override
    public List<ProjectDeviceRelVo> ListByDeviceIdAndDeviceType(String deviceId, String deviceType) {
        return this.baseMapper.listByDeviceIdAndDeviceType(deviceId, deviceType);
    }
}
