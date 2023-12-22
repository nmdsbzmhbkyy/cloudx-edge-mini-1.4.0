package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.service.SysDeviceTypeThirdPartyConfigService;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.VersionEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.PerimeterAlarmDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.impl.ProjectPerimeterAlarmAreaServiceHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.config.AurineEdgeConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeDeviceInfoDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgePerimeterDeviceParamsDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeRespondDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.remote.factory.AurineEdgeRemoteDeviceOperateServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManagePerimeterAlarmGetDevParamsHandler extends AbstractHandler<CallBackData> {
    @Resource
    private ProjectPerimeterAlarmAreaServiceHelper projectPerimeterAlarmAreaServiceHelper;
    @Resource
    private SysDeviceTypeThirdPartyConfigService sysDeviceTypeThirdPartyConfigService;


    @Override
    public boolean filter(CallBackData handleEntity) {
        if (!StringUtils.equals(AurineEdgeObjNameEnum.DevParamObj.code, handleEntity.getOnNotifyData().getObjManagerData().getObjName())) {
            return false;
        }

        if (!StringUtils.equals(AurineEdgeActionConstant.GET, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理设备自动注册功能", handleEntity);
        return true;
    }

    @Override
    public boolean doHandle(CallBackData handleEntity) {
        try {
            JSONObject param = handleEntity.getOnNotifyData().getObjManagerData().getParam();
            String devId=param.get("deviceId").toString();



            //自动添加逻辑处理
            this.doLogic(devId);


        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 设备自动注册失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 设备自动注册结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }
    }

    private void doLogic(String devId){
        AurineEdgePerimeterDeviceParamsDTO aurineEdgePerimeterDeviceParamsDTO= projectPerimeterAlarmAreaServiceHelper.queryChannel(devId);
//        AurineEdgeRespondDTO respondDTO  = AurineEdgeRemoteDeviceOperateServiceFactory
//                .getInstance(getVer())
//                .commandsDown(config,deviceInfo.getThirdpartNo(), AurineEdgeServiceEnum.DEVICE_PARAM.code, AurineEdgeActionConstant.SET,objInfo,null);
//        perimeterAlarmDeviceService.queryDevParams(devId,aurineEdgePerimeterDeviceParamsDTO);
        AurineEdgeConfigDTO config = sysDeviceTypeThirdPartyConfigService.getConfigByDeviceId(devId, AurineEdgeConfigDTO.class);
        JSONObject objInfo=JSONObject.parseObject(JSON.toJSONString(aurineEdgePerimeterDeviceParamsDTO));

        AurineEdgeRespondDTO respondDTO  = AurineEdgeRemoteDeviceOperateServiceFactory.getInstance(VersionEnum.V1)
                .objectManage(config,devId, AurineEdgeServiceEnum.DEVICE_PARAM.code, AurineEdgeActionConstant.GET,"",objInfo,null);
        if (respondDTO.getErrorEnum()== AurineEdgeErrorEnum.SUCCESS) {
            //生成返回消息
            log.info("周界报警DeviceParams服务Get命令下发成功");

        } else {

            log.info(MessageFormat.format("周界报警DeviceParams服务Get命令下发失败:{0}",respondDTO.getErrorEnum().code));
        }
    }
}
