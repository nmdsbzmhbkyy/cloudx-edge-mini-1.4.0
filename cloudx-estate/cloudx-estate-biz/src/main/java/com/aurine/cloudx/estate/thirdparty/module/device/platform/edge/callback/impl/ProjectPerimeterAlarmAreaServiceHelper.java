package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.impl;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.mapper.ProjectPerimeterAlarmAreaMapper;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.impl.ProjectPerimeterAlarmAreaServiceImpl;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoVo;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectPerimeterAlarmAreaServiceHelper extends ServiceImpl<ProjectPerimeterAlarmAreaMapper, ProjectPerimeterAlarmArea>{
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectPerimeterAlarmAreaServiceImpl projectPerimeterAlarmAreaService;

    private final String DEFENSE_AREA = "防区";

    public AurineEdgePerimeterDeviceParamsDTO queryChannel(String deviceId){
        ProjectDeviceInfo deviceInfo = baseMapper.reacquireDefenseArea(deviceId);
        List<String> channelNos = new ArrayList<>();
        List<ProjectPerimeterAlarmArea> projectPerimeterAlarmAreas = list(Wrappers.lambdaQuery(ProjectPerimeterAlarmArea.class)
                .eq(ProjectPerimeterAlarmArea::getDeviceId, deviceId));
        projectPerimeterAlarmAreas.forEach(e -> {
            String channelName= String.format("Module%03dZone%03d",Integer.parseInt(e.getModuleNo()),Integer.parseInt(e.getChannelNo()));
            channelNos.add(channelName);
        });
        ProjectDeviceInfoVo projectDeviceInfoById = projectDeviceInfoService.getProjectDeviceInfoById(deviceId);

        AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO=new AurineEdgePerimeterDeviceParamsDTO();
        aurineEdgePerimeterDeviceParamsDTO.setChannelNameList(channelNos);
        aurineEdgePerimeterDeviceParamsDTO.setParamDevId(projectDeviceInfoById.getDeviceCode());
        aurineEdgePerimeterDeviceParamsDTO.setPasswd(projectDeviceInfoById.getCompanyPasswd());
        return aurineEdgePerimeterDeviceParamsDTO;

    }
}
