package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.component.chain.AbstractHandleChain;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.Chain;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamHis;
import com.aurine.cloudx.estate.entity.ProjectDeviceParamInfo;
import com.aurine.cloudx.estate.entity.SysDeviceProductMap;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoProxyService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamHisService;
import com.aurine.cloudx.estate.service.ProjectDeviceParamInfoService;
import com.aurine.cloudx.estate.service.SysDeviceProductMapService;
import com.aurine.cloudx.estate.thirdparty.module.device.factory.param.DeviceParamFactoryProducer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeParamCallbackConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.DevMessageData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.ObjManagerData;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.aurine.cloudx.estate.util.delay.TaskUtil;
import com.aurine.cloudx.estate.vo.ProjectDeviceInfoProxyVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * <p>参数下发结果回调</p>
 *
 * @author : 王良俊
 * @date : 2021-10-21 15:28:12
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectParamCallbackHandler extends AbstractHandler<CallBackData> {

    @Resource
    ProjectDeviceInfoProxyService projectDeviceInfoProxyService;
    @Resource
    ProjectDeviceParamInfoService projectDeviceParamInfoService;
    @Resource
    ProjectDeviceParamHisService projectDeviceParamHisService;
    @Resource
    SysDeviceProductMapService sysDeviceProductMapService;


    @Override
    public boolean filter(CallBackData handleEntity) {
        return "DeviceParams".equals(handleEntity.getOnNotifyData().getObjManagerData().getServiceId());
    }

    @Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED, rollbackFor = Exception.class)
    public boolean doHandle(CallBackData handleEntity) {
        DevMessageData onNotifyData = handleEntity.getOnNotifyData();
        ProjectDeviceInfoProxyVo deviceInfo = projectDeviceInfoProxyService.getByThirdPartyCode(onNotifyData.getDevId());
        if (deviceInfo != null) {
            SysDeviceProductMap productMap = sysDeviceProductMapService.getOne(new LambdaQueryWrapper<SysDeviceProductMap>().eq(SysDeviceProductMap::getProductId, deviceInfo.getProductId()));
            if (productMap != null) {
                DeviceParamFactoryProducer.getFactory(deviceInfo.getDeviceId()).getParamDataService(productMap.getManufacture(), deviceInfo.getDeviceType()).deviceDataUpdate(JSON.toJSONString(onNotifyData), deviceInfo);
            }
        }
        return done();
    }


}
