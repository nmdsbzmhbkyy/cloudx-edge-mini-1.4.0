package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeUseFlow;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProjectQrPasscodeUseFlowService extends IService<ProjectQrPasscodeUseFlow> {
    void insert(ProjectQrPasscodeRecord record, ProjectDeviceInfo deviceInfo, Integer resultType);
}
