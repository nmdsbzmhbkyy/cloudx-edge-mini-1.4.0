package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectPerimeterAlarmArea;
import com.aurine.cloudx.estate.service.ProjectDeviceInfoService;
import com.aurine.cloudx.estate.service.ProjectRightDeviceService;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.AsyncNotifyHelper;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManagePerimeterAlarmChannelOperation extends AbstractHandler<CallBackData> {
    @Resource
    private ProjectDeviceInfoService projectDeviceInfoService;
    @Resource
    private ProjectRightDeviceService projectRightDeviceService;
    @Resource
    private AsyncNotifyHelper asyncNotifyHelper;


    @Override
    public boolean filter(CallBackData handleEntity) {
        if (!StringUtils.equals(AurineEdgeServiceEnum.CHANNEl_OPERATION.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            return false;
        }

        if (!StringUtils.equals(AurineEdgeActionConstant.SET, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理周界报警设置结果", handleEntity);
        return true;
    }

    @Override
    public boolean doHandle(CallBackData handleEntity) {
        try {

            //执行下发成功的设备
            String deviceThirdCode = handleEntity.getOnNotifyData().getDevId();
            ProjectDeviceInfo deviceInfo = projectDeviceInfoService.getByThirdPartyCode(deviceThirdCode);

            //获取下发的设备
            if (deviceInfo == null) {
                log.error("[冠林边缘网关] 周界报警设置失败，未找到下发的设备：deviceThirdCode = {}", deviceThirdCode);
                return done();
            }


            //获取下发的卡片结果
            JSONObject objInfo = handleEntity.getOnNotifyData().getObjManagerData().getObjInfo();

            String respKey = "HUAWEI_SYNC_RESP_" + handleEntity.getOnNotifyData().getMsgId();
            RedisUtil.set(respKey, objInfo);

        } catch (ClassCastException cce) {
            log.error("[冠林边缘网关] 周界报警设置结果处理失败，数据结构错误 {}", handleEntity);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("[冠林边缘网关] 周界报警设置结果处理结束 {}", handleEntity.getOnNotifyData().getObjManagerData().getObjInfo());
            return done();
        }
    }
}
