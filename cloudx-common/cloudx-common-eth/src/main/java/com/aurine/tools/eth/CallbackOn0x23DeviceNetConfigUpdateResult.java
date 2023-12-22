package com.aurine.tools.eth;

/**
 * @author chensl
 * @date 2022-06-01
 */
@FunctionalInterface
public interface CallbackOn0x23DeviceNetConfigUpdateResult {
    void deviceUploadResult(String deviceMac, String result);
}
