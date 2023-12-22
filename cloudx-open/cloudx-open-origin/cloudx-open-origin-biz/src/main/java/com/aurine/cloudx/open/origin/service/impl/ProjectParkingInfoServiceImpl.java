
package com.aurine.cloudx.open.origin.service.impl;

import com.aurine.cloudx.open.origin.entity.ProjectParkingInfo;
import com.aurine.cloudx.open.origin.mapper.ProjectParkingInfoMapper;
import com.aurine.cloudx.open.origin.service.ProjectParkingInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 停车区域
 *
 * @author 王良俊
 * @date 2020-05-07 09:13:25
 */
@Service
@AllArgsConstructor
public class ProjectParkingInfoServiceImpl extends ServiceImpl<ProjectParkingInfoMapper, ProjectParkingInfo> implements ProjectParkingInfoService {

    @Override
    public void init(String projectName, Integer projectId, Integer tenantId) {
        ProjectParkingInfo parkingInfo = new ProjectParkingInfo();
        parkingInfo.setProjectId(projectId);
        parkingInfo.setParkName(projectName + "停车场");
        this.save(parkingInfo);
    }
}
