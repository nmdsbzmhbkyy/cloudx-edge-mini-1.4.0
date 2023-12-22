package com.aurine.tools.eth;

import com.aurine.tools.eth.protocol.Data0x22ResponseDeviceGetNetConfig;

/**
 * @author chensl
 * @date 2022-06-01
 */
@FunctionalInterface
public interface CallbackOn0x22DeviceGetNetConfig {
    Data0x22ResponseDeviceGetNetConfig getConfig(String deviceMac);
}
