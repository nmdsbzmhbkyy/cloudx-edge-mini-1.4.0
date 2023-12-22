package com.aurine.cloudx.estate.service.policy.qrpasscode;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;

public interface IValidQrPasscode {

    void valid(ProjectDeviceInfo deviceInfo, String passcode, RemoteOpenDoorResultModel resultModel);

}
