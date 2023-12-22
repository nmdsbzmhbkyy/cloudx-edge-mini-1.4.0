package com.aurine.tools.eth.natives.linux;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

//#ifndef _LCP_COMMON_H_
//#define STDCALL __stdcall
//typedef int (STDCALL *CallbackOriginal)(const char *sa, const char *da, int type, const char *data, int len, int seq, void *user);
//#endif
interface EthLibLinux extends Library {

    final String libName = "linkcp";

    static EthLibLinux Instance = (EthLibLinux) Native.synchronizedLibrary(
            (EthLibLinux)Native.loadLibrary(libName, EthLibLinux.class)
    );
    Pointer lcp_create(int if_index, int crypt, ReceiveCallbackLibLinux callback, int seq, Pointer user);
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
