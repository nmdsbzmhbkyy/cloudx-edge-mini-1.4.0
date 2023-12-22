package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>门禁凭证下发消费者，用于将异步门禁转为同步下发，匹配设备功能</p>
 *
 * @ClassName: EventDeviceParkingPayConsumer
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06 14:02
 * @Copyright:
 */
@Component
@Slf4j
public class AurineCertHandle extends AbstractAurineCertHandle {
    @Resource
    private AurineCertSendHandleV2_1 aurineCertSendHandleV2_1;


    @Override
    public void readActiveQueue(String message) {
        AurineCertSendKafkaDTO aurineCertSendKafkaDTO = JSONObject.parseObject(message, AurineCertSendKafkaDTO.class);
        if ("add".equalsIgnoreCase(aurineCertSendKafkaDTO.getCommand())) {
            log.info("[冠林中台] 执行凭证下发流程   内容 ：{}", message);
            aurineCertSendHandleV2_1.addCert(aurineCertSendKafkaDTO.getConfigDTO(), aurineCertSendKafkaDTO.getDeviceVo(), aurineCertSendKafkaDTO.getCertList());
        } else {
            log.info("[冠林中台] 执行凭证删除流程    内容 ：{}", message);
            aurineCertSendHandleV2_1.delCert(aurineCertSendKafkaDTO.getConfigDTO(), aurineCertSendKafkaDTO.getDeviceVo(), aurineCertSendKafkaDTO.getCertList());
        }
    }

}
