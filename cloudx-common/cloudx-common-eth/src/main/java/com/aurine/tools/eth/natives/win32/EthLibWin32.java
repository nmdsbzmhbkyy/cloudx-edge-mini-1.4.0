package com.aurine.tools.eth.natives.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

//#ifndef _LCP_COMMON_H_
//#define STDCALL __stdcall
//typedef int (STDCALL *CallbackOriginal)(const char *sa, const char *da, int type, const char *data, int len, int seq, void *user);
//#endif

/**
 * windows下调试：
 * 将linkcp.dll放工程目录下，加jvm调试参数： -Djna.library.path=[工程目录]
 */
interface EthLibWin32 extends StdCallLibrary { //StdCallLibrary Library

    final String libName = "linkcp";

    static boolean isJdk32 = System.getProperty("sun.arch.data.model").equals("32");//System.getProperty("os.name").toLowerCase().contains("win");

    static EthLibWin32 Instance = isJdk32 ? (EthLibWin32) Native.synchronizedLibrary(
            (EthLibWin32)Native.loadLibrary(libName, EthLibWin32.class)
    ) : null;
    Pointer lcp_create(int if_index, int crypt, ReceiveCallbackLibWin32 callback, int seq, Pointer user);
    int lcp_send(Pointer lcp, String destMac, byte[] data, int len);
    void lcp_destroy(Pointer lcp);

    int lcp_if_count();
    int lcp_if_get(int if_index, Pointer name, Pointer desc, Pointer mac);
//    void* STDCALL lcp_create(int if_index, int crypt, CallbackOriginal handlerOriginal, int seq, void*user);
//    int STDCALL lcp_send(void *lcp, const char *da, const char *data, int len);
//    void STDCALL lcp_destroy(void *lcp);
//
//    // 枚举网卡
//    int STDCALL lcp_if_count();
//    int STDCALL lcp_if_get(int if_index, char *name, char *desc, char *mac);
}
