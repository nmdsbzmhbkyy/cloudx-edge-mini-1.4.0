package com.aurine.tools.eth.natives;

import com.aurine.tools.eth.natives.linux.EthLinux;
import com.aurine.tools.eth.natives.win32.EthWin32;

/**
 * 事件监听器/回调
 * @author chensl
 * @date 2022-05-28
 */
public abstract class EthFactory {

    private static EthLib ethLib = null;
    private static boolean isWin;
    static {
        isWin = System.getProperty("os.name").toLowerCase().contains("win");
        if(isWin){
            ethLib = new EthWin32();
        }else{
            ethLib = new EthLinux();
        }
    }

    public static EthLib getInstance(){
        return ethLib;
    }

    @Deprecated
    public static EthLib createInstance(){
        EthLib ethLib = null;

        if(isWin){
            ethLib = new EthWin32();
        }else{
            ethLib = new EthLinux();
        }
        return ethLib;
    }
}
