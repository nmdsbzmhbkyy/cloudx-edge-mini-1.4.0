package com.aurine.tools.eth.natives;

import com.sun.jna.Pointer;

import java.util.List;

/**
 * 事件监听器/回调
 * @author chensl
 * @date 2022-05-28
 */
public interface EthLib {
    int MAX_STRING = 256;
    int MAX_MAC = 128;


    boolean start();
    boolean start(int ifIndex, int crypt, ReceiveCallback callback, int seq, Pointer user);
    void stop();
    int send(String destMac, String jsonStr);

    List<IfInfo> getIfInfo();
}
