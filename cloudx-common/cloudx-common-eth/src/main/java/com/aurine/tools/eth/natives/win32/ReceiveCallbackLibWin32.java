package com.aurine.tools.eth.natives.win32;

import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * 事件监听器/回调
 * @author chensl
 */
@FunctionalInterface
interface ReceiveCallbackLibWin32 extends StdCallCallback
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
