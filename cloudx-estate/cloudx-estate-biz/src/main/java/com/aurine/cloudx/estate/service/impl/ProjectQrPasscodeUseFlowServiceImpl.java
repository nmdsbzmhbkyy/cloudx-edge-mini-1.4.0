package com.aurine.cloudx.estate.service.impl;

import com.aurine.cloudx.common.core.util.BeanUtil;
import com.aurine.cloudx.common.data.base.BaseEntity;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeUseFlow;
import com.aurine.cloudx.estate.mapper.ProjectQrPasscodeUseFlowMapper;
import com.aurine.cloudx.estate.service.ProjectQrPasscodeUseFlowService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ProjectQrPasscodeUseFlowServiceImpl extends ServiceImpl<ProjectQrPasscodeUseFlowMapper, ProjectQrPasscodeUseFlow> implements ProjectQrPasscodeUseFlowService {

    public void insert(ProjectQrPasscodeRecord record, ProjectDeviceInfo deviceInfo, Integer resultType) {
        ProjectQrPasscodeUseFlow flow = new ProjectQrPasscodeUseFlow();
        BeanUtil.copyPropertiesIgnore(record, flow, BaseEntity.class);
        flow.setRecordId(record.getId());
        flow.setDeviceId(deviceInfo.getDeviceId());
        flow.setDeviceNo(deviceInfo.getDeviceCode());
        flow.setResult(resultType);
        this.save(flow);
    }
}
