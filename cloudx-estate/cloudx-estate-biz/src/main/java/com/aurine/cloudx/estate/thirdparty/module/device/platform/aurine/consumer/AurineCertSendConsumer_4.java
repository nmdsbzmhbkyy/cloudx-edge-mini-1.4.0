package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.consumer;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle.AurineCertSendHandleV2_1;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * <p>门禁凭证下发消费者，用于将异步门禁转为同步下发，匹配设备功能</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
//@Component
@Slf4j
public class AurineCertSendConsumer_4 {
    @Resource
    private AurineCertSendHandleV2_1 aurineCertSendHandleV2_1;


//    @KafkaListener(topics = AurineTocpicConstant.PREFIX_DRVICE_CERT_TOPIC + "4", errorHandler = "", containerFactory = "kafkaListenerContainerFactory")
    public void readActiveQueue(String message) {
        log.info("冠林中台 执行凭证下发 通道 4 ：{}", message);
        AurineCertSendKafkaDTO aurineCertSendKafkaDTO = JSONObject.parseObject(message, AurineCertSendKafkaDTO.class);
        if ("add".equalsIgnoreCase(aurineCertSendKafkaDTO.getCommand())) {
            aurineCertSendHandleV2_1.addCert(aurineCertSendKafkaDTO.getConfigDTO(), aurineCertSendKafkaDTO.getDeviceVo(), aurineCertSendKafkaDTO.getCertList());
        } else {
            aurineCertSendHandleV2_1.delCert(aurineCertSendKafkaDTO.getConfigDTO(), aurineCertSendKafkaDTO.getDeviceVo(), aurineCertSendKafkaDTO.getCertList());
        }
    }

}
