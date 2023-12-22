
package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;
import com.aurine.cloudx.estate.mapper.ProjectDeviceLocationMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceLocationService;
import com.aurine.cloudx.estate.vo.ProjectDeviceLocationVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * 记录设备平面图位置打点信息
 *
 * @author lingang
 * @date 2020-06-15 16:07:41
 */
@Service
public class ProjectDeviceLocationServiceImpl extends ServiceImpl<ProjectDeviceLocationMapper, ProjectDeviceLocation> implements ProjectDeviceLocationService {

    @Override
    public List<ProjectDeviceLocationVo> getPoints(String picId) {
        return baseMapper.getPoints(picId);
    }
}
