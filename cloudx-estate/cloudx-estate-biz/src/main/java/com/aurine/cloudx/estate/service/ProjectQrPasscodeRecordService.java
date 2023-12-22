package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.common.data.base.PageParam;
import com.aurine.cloudx.estate.dto.qrPasscode.QrPasscodePageDto;
import com.aurine.cloudx.estate.entity.ProjectQrPasscodeRecord;
import com.aurine.cloudx.estate.vo.qrPasscode.SaveProjectQrPasscodeRecordVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProjectQrPasscodeRecordService extends IService<ProjectQrPasscodeRecord> {
    void saveRecord(SaveProjectQrPasscodeRecordVo vo);

    boolean deleteRecord(String uniqueCode);


    Page<QrPasscodePageDto> queryPage(Page<Object> page);
}
