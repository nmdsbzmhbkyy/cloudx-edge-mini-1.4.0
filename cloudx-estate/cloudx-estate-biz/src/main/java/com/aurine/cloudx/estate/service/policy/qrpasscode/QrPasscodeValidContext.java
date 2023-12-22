package com.aurine.cloudx.estate.service.policy.qrpasscode;

import cn.hutool.extra.spring.SpringUtil;
import com.aurine.cloudx.estate.constant.enums.ValidType;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.EdgeCascadeConfService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.model.RemoteOpenDoorResultModel;
import com.aurine.cloudx.estate.util.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class QrPasscodeValidContext {

    public void valid(Integer validType,ProjectDeviceInfo deviceInfo, String passcode, RemoteOpenDoorResultModel resultModel){
        try {
            String validClass = ValidType.findByType(validType);
            if (StringUtils.isBlank(validClass)){
                throw new RuntimeException("尚未实现此类二维码校验模式");
            }
            IValidQrPasscode valid = (IValidQrPasscode)SpringContextUtil.getBean(validClass);
            valid.valid(deviceInfo,passcode,resultModel);
        }catch (Exception e){
            e.printStackTrace();
            resultModel.setResult(RemoteOpenDoorResultModel.ResultType.PASS_RESULT_TYPE_INVALID.getCode());
        }
    }
}
