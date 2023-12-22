package com.aurine.tools.eth.natives.linux;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * 事件监听器/回调
 * @author chensl
 */
@FunctionalInterface
interface ReceiveCallbackLibLinux  extends Callback
{
    /**
     * callback 事件监听方法
     * @param srcMac
     * @param destMac
     * @param type
     * @param data
     * @param len
     * @param seq
     * @param user
     * @return int
     */
    public int callback(String srcMac, String destMac, int type, Pointer data, int len, int seq, Pointer user);
}
