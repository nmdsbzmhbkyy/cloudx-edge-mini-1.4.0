
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.vo.ProjectDeviceLocationVo;
import com.aurine.cloudx.open.origin.mapper.ProjectDeviceLocationMapper;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import com.aurine.cloudx.open.origin.service.ProjectDeviceLocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
