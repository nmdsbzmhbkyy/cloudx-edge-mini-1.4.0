package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.callback.handler;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import com.aurine.cloudx.estate.component.chain.annotation.ChainHandler;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.ResponseOperateConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.TopicConstant;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.dto.ResponseOperateDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeAlarmStatusEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.constant.AurineEdgeServiceEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.dto.AurineEdgeAlarmStateChangeDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.CallBackData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.DevMessageData;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond.ObjManagerData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 报警事件状态变更回调处理器
 *
 * @description:
 * @author : 邱家标 <qiujb@miligc.com>
 * @date : 2021 11 16 11:19
 * @Copyright:
 */

@ChainHandler(chainClass = AurineEdgeObjectManageChain.class)
@Component
@Slf4j
public class AurineEdgeObjectManageAlarmStateChangeCallbackHandler extends AbstractHandler<CallBackData> {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * handler校验函数，不满足该函数则跳过处理
     *
     * @param handleEntity
     * @return
     */
    @Override
    public boolean filter(CallBackData handleEntity) {
        boolean filter = true;
        if (!StringUtils.equals(AurineEdgeServiceEnum.ALARM_STATE.code, handleEntity.getOnNotifyData().getObjManagerData().getServiceId())) {
            filter = false;
        }
        if (handleEntity.getOnNotifyData().getObjManagerData().getObjInfo() == null) {
            filter = false;
        }
        return filter;
    }
    /**
     * 执行方法，执行后返回调用next继续下一组handle，调用done则结束处理
     *
     * @param handleEntity
     */
    @Override
    public boolean doHandle(CallBackData handleEntity) {
        try {
            DevMessageData onNotifyData = handleEntity.getOnNotifyData();
            ObjManagerData objManagerData = onNotifyData.getObjManagerData();
            JSONObject objInfo = objManagerData.getObjInfo();

            AurineEdgeAlarmStateChangeDTO data = objInfo.toJavaObject(AurineEdgeAlarmStateChangeDTO.class);
            String devId = data.getDevId();
            Integer flowId = data.getFlowId();
            Integer holdPwdAlarm = data.getHoldPwdAlarm();
            Integer destroyAlarm = data.getDestroyAlarm();
            Integer errorPwdAlarm = data.getErrorPwdAlarm();
            Integer foreOpendoorAlarm = data.getForeOpendoorAlarm();
            Integer doorOpenTimeoutAlarm = data.getDoorOpenTimeoutAlarm();
            if (StringUtils.isEmpty(devId)) devId = onNotifyData.getDevId();

            int status = 0;
            if (destroyAlarm != null) {
                status = destroyAlarm;
            } else if (holdPwdAlarm != null) {
                status = holdPwdAlarm;
            } else if (errorPwdAlarm != null) {
                status = errorPwdAlarm;
            } else if (foreOpendoorAlarm != null) {
                status = foreOpendoorAlarm;
            } else if (doorOpenTimeoutAlarm != null) {
                status = doorOpenTimeoutAlarm;
            }

            ResponseOperateDTO noticeDTO = new ResponseOperateDTO();
            noticeDTO.setAction(ResponseOperateConstant.ACTION_UPDATE_ALARM_STATUS);
            noticeDTO.setCompany(PlatformEnum.AURINE_EDGE_MIDDLE.name());
            noticeDTO.setAlarmStatus(AurineEdgeAlarmStatusEnum.getByCode(String.valueOf(status)).cloudCode);//转换为云系统字典值
            noticeDTO.setThirdPartyCode(devId);
            noticeDTO.setEventCode(flowId);

            this.sendMsg(TopicConstant.SDI_RESPONSE_OPERATE, noticeDTO);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return done();
        }
    }

    /**
     * 发送消息到kafka
     *
     * @param topic   主题
     * @param message 内容体
     */
    private <T> void sendMsg(String topic, T message) {
        kafkaTemplate.send(topic, JSONObject.toJSONString(message));
    }
}
