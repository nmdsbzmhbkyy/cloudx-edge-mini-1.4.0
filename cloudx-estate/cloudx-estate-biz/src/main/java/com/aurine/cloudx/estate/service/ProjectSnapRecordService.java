package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectSnapRecord;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.yushi.entity.YushiCallBackObj;
import com.aurine.cloudx.estate.vo.ProjectSnapRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProjectSnapRecordService extends IService<ProjectSnapRecord> {

    Boolean saveRecord(YushiCallBackObj callBackObj, ProjectDeviceInfo deviceInfo);

    IPage<ProjectSnapRecordVo> page(Page page, ProjectSnapRecordVo vo);

}
