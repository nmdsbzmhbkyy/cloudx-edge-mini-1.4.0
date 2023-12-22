package com.aurine.cloudx.estate.service.impl;
import com.aurine.cloudx.estate.entity.ProjectConfig;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.ProjectConfigService;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectPasscodeRegisterRecordService;
import com.aurine.cloudx.estate.service.policy.qrpasscode.QrPasscodeValidContext;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.DeviceFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/***
 * @title ProjectPasscodeRegisterRecordServiceImpl
 * @description
 * @author cyw
 * @version 1.0.0
 * @create 2023/6/9 11:34
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectPasscodeRegisterRecordServiceImpl  implements ProjectPasscodeRegisterRecordService {


    private final ProjectDeviceInfoService deviceInfoService;

    private final QrPasscodeValidContext qrPasscodeValidContext;
    private final ProjectConfigService projectConfigService;

    @Override
    public void remoteValidOpenDoor(String passcode, String msgId, String thirdDeviceId) {
        log.info("【二维码校验】：收到开门请求：passcode：{}，msgId：{}，thirdDeviceId：{}", passcode, msgId, thirdDeviceId);
        ProjectDeviceInfo deviceInfo = deviceInfoService.lambdaQuery().eq(ProjectDeviceInfo::getThirdpartyCode, thirdDeviceId).one();
        RemoteOpenDoorResultModel resultModel = new RemoteOpenDoorResultModel();
        ProjectConfig config = projectConfigService.getConfig();
        qrPasscodeValidContext.valid(config.getQrReconWay(),deviceInfo, passcode, resultModel);
        DeviceFactoryProducer.getFactory(deviceInfo.getDeviceId()).getPassWayDeviceService().remoteOpenDoor(deviceInfo.getDeviceId(), resultModel, msgId);
    }

}
