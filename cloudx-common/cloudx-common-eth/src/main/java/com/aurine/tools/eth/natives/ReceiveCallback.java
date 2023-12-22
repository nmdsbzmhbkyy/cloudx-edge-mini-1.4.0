package com.aurine.tools.eth.natives;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * 事件监听器/回调
 * @author chensl
 * @date 2022-05-28
 */
@FunctionalInterface
public interface ReceiveCallback extends StdCallCallback
{
    /**
     * callback 事件监听方法
     * @param srcMac
     * @param destMac
     * @param type
     * @param jsonStr
     * @param seq
     * @param user
     * @return int
     */
    public int callback(String srcMac, String destMac, int type, String jsonStr, int seq, Pointer user);
}
