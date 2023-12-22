package com.aurine.tools.eth.natives.linux;

import com.aurine.tools.eth.natives.EthLib;
import com.aurine.tools.eth.natives.IfInfo;
import com.aurine.tools.eth.natives.ReceiveCallback;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 以太网通过库-linux
 * @author chensl
 */
public class EthLinux implements EthLib {
    static {
        System.setProperty("jna.encoding","GB2312");
        Native.setProtected(false);
    }
    private Pointer ethServer;

    private int ifIndex;
    private int crypt;
    private ReceiveCallback callback;
    private int seq;
    private Pointer user;


    private ReceiveCallbackLibLinux callbackInner = (srcMac, destMac, type, data, len, seq, user) -> {
        if(callback != null) {
            String jsonStr = data.getString(0, StandardCharsets.UTF_8.name());
            return callback.callback(srcMac, destMac, type, jsonStr, seq, user);
        }
        return 0;
    };

    public EthLinux(){
        setParams(-1, 0, null, 0, null);
    }

    public EthLinux(int ifIndex, int crypt, ReceiveCallback callback, int seq, Pointer user){
        setParams(ifIndex, crypt, callback, seq, user);
    }

    @Override
    public boolean start(){
        if(ifIndex == -1){
            return false;
        }
        ethServer = EthLibLinux.Instance.lcp_create(ifIndex, crypt, callbackInner, seq, user);
        return true;
    }

    @Override
    public boolean start(int ifIndex, int crypt, ReceiveCallback callback, int seq, Pointer user){
        setParams(ifIndex, crypt, callback, seq, user);
        return start();
    }

    @Override
    public void stop(){
        if(ethServer == null) {
            return;
        }
        EthLibLinux.Instance.lcp_destroy(ethServer);
        ethServer = null;
    }

    @Override
    public int send(String destMac, String jsonStr){
        if(ethServer == null) {
            return -1;
        }
        byte[]data = new byte[0];
        try {
            data = jsonStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return EthLibLinux.Instance.lcp_send(ethServer, destMac, data, data.length);
    }

    @Override
    public List<IfInfo> getIfInfo() {
        int count = EthLibLinux.Instance.lcp_if_count();
        ArrayList<IfInfo> ary = new ArrayList<>();
        for(int i = 0; i < count; i ++){
            Pointer name = new Memory(MAX_STRING);
            Pointer desc = new Memory(MAX_STRING);
            Pointer mac = new Memory(MAX_MAC);
            EthLibLinux.Instance.lcp_if_get(i, name, desc, mac);

            IfInfo info = new IfInfo();
            info.setIndex(i);
            info.setName(name.getString(0));
            info.setDesc(desc.getString(0));
            info.setMac(mac.getString(0));

            ary.add(info);
        }
        return ary;
    }

    private void setParams(int ifIndex, int crypt, ReceiveCallback callback, int seq, Pointer user){
        this.ifIndex = ifIndex;
        this.crypt = crypt;
        this.callback = callback;
        this.seq = seq;
        this.user = user;
    }
}
