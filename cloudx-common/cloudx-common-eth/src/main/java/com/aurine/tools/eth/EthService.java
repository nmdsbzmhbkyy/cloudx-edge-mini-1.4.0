package com.aurine.tools.eth;

/**
 * @author chensl
 * @date 2022-06-01
 */
public interface EthService {
    boolean bindLocalMac(String localMac, boolean crypt);

    boolean update0x21Listener(CallbackOn0x21DeviceBroadcast deviceBroadcast);
    boolean update0x22Listener(CallbackOn0x22DeviceGetNetConfig deviceGetNetConfig);
    boolean update0x23Listener(CallbackOn0x23DeviceNetConfigUpdateResult deviceUpdateConfigResult);
    boolean updateListener(CallbackOn0x21DeviceBroadcast deviceBroadcast, CallbackOn0x22DeviceGetNetConfig deviceGetNetConfig, CallbackOn0x23DeviceNetConfigUpdateResult deviceUpdateConfigResult);

    void close();
}
