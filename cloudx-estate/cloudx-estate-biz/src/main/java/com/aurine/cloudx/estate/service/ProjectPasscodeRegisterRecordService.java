package com.aurine.cloudx.estate.service;

import com.aurine.cloudx.common.data.base.BaseService;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ProjectPasscodeRegisterRecordService {

    void remoteValidOpenDoor(String passcode,String msgId,String thirdDeviceId);
}
