package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeActionConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeObjNameEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeSyncReqEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Auther: 邹宇
 * @Date: 2022-05-18 15:02:58
 * @Description: 设备删除回调处理器
 */
@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageDeviceDeleteCallbackHandler extends AbstractHandler<CallBackData> {
    @Override
    public boolean filter(CallBackData handleEntity) {
        if (!StringUtils.equals(AurineEdgeObjNameEnum.DeviceInfo.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            return false;
        }

        if (!StringUtils.equals(AurineEdgeActionConstant.DELETE, handleEntity.getOnNotifyData().getObjManagerData().getAction())) {
            return false;
        }

        log.info("[冠林边缘网关] {} 开始处理设备删除回调功能", handleEntity);
        return true;
    }

    @Override
    public boolean doHandle(CallBackData handleEntity) {
        //缓存暂存
        RedisUtil.set(AurineEdgeSyncReqEnum.EDGE_SYNC_RESP.code + handleEntity.getOnNotifyData().getMsgId(), JSONObject.toJSONString(handleEntity), 20);
        return done();
    }
}
