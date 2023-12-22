package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineTocpicConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-06
 * @Copyright:
 */
@Slf4j
@Deprecated
public class AurineTopicUtil {
    /**
     * 通过设备SN，通过hash值，返回应该存储的Topic地址
     *
     * @param deviceSn
     * @return
     */
    public static String getHashTopic(String deviceSn, AurineConfigDTO aurineConfigDTO) {
        int topicNum = Math.abs(deviceSn.hashCode()) % aurineConfigDTO.getHashCount();
        if (topicNum <= 0) {
            topicNum = 1;
        }

        log.debug("[冠林中台] 生成散列主题 = {}, SN = {}, 设备 HASHCODE = {}", topicNum, deviceSn, Math.abs(deviceSn.hashCode()));

        return AurineTocpicConstant.PREFIX_DRVICE_CERT_TOPIC + topicNum;
    }

    /**
     * @param deviceSn
     * @return
     */
    public static String getSnTopic(String deviceSn) {
        return AurineTocpicConstant.PREFIX_DRVICE_CERT_TOPIC + deviceSn;
    }
}
