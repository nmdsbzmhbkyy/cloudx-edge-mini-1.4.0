package com.aurine.tools.eth;

import cn.hutool.json.JSONUtil;
import com.aurine.tools.eth.natives.EthFactory;
import com.aurine.tools.eth.natives.EthLib;
import com.aurine.tools.eth.natives.IfInfo;
import com.aurine.tools.eth.natives.ReceiveCallback;
import com.aurine.tools.eth.protocol.*;
import com.aurine.tools.eth.protocol.constants.Commands;
import com.aurine.tools.eth.protocol.constants.Directs;
import com.aurine.tools.eth.protocol.constants.EchoCodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chensl
 * @date 2022-06-01
 */
public class EthServiceImpl implements EthService {

    private EthLib ethLib;
    private List<IfInfo> ifList = new ArrayList<>();
    private Map<String, IfInfo> ifMap= new HashMap<>();

    private boolean isRunning = false;
    private String currentMac;
    private CallbackOn0x21DeviceBroadcast callbackDeviceBroadcast;
    private CallbackOn0x22DeviceGetNetConfig callbackDeviceGetNetConfig;
    private CallbackOn0x23DeviceNetConfigUpdateResult callbackDeviceUpdateConfigResult;

    ReceiveCallback callback = (srcMac, destMac, type, jsonStr, seq, user) -> {
        System.out.println(String.format("收到数据长度：%d", jsonStr.length()));
        System.out.println(jsonStr);

        // 1、解析
        EthCfgData<String> requestData = EthCfgProtocol.parse(jsonStr);
        String jsonResult;

        if(requestData == null){
            return 1;
        }

        //需要处理：1、请求包；2、命令匹配；3、回调不为空
        if(requestData.getDirect() == Directs.Request) {
            if (requestData.getCommandId() == Commands.Cmd0x21Broadcast && callbackDeviceBroadcast != null) {
                EthCfgData<Data0x21ResponseDeviceBroadcast> response = new EthCfgData<Data0x21ResponseDeviceBroadcast>();
                callbackDeviceBroadcast.byMac(srcMac);
                EthCfgProtocol.createResponse(requestData, response, EchoCodes.Success);

                jsonResult = JSONUtil.toJsonStr(response);
                ethLib.send(srcMac, jsonResult);
            } else if (requestData.getCommandId() == Commands.Cmd0x22GetNetConfig && callbackDeviceGetNetConfig != null) {
                EthCfgData<Data0x22ResponseDeviceGetNetConfig> response = new EthCfgData<Data0x22ResponseDeviceGetNetConfig>();
                Data0x22ResponseDeviceGetNetConfig userConfig = callbackDeviceGetNetConfig.getConfig(srcMac);
                if (userConfig == null) {
                    EthCfgProtocol.createResponse(requestData, response, EchoCodes.Error);
                }else {
                    userConfig.updateCheckValue(srcMac);
                    EthCfgProtocol.createResponse(requestData, response, EchoCodes.Success);
                    response.setBody(userConfig);
                }
                jsonResult = JSONUtil.toJsonStr(response);
                ethLib.send(srcMac, jsonResult);
            } else if (requestData.getCommandId() == Commands.CmdCmd0x23UpdateConfigResult && callbackDeviceUpdateConfigResult != null) {
                EthCfgData<Data0x23ResponseDeviceNetConfigUpdateResult> response = new EthCfgData<Data0x23ResponseDeviceNetConfigUpdateResult>();
                callbackDeviceUpdateConfigResult.deviceUploadResult(srcMac, requestData.getEchoCode() == EchoCodes.Success ? "1" : "2");
                EthCfgProtocol.createResponse(requestData, response, EchoCodes.Success);

                jsonResult = JSONUtil.toJsonStr(response);
                ethLib.send(srcMac, jsonResult);
            }
        }

        return 0;
    };

    public EthServiceImpl(){
        ethLib = EthFactory.getInstance();
        System.out.println("ethLib:"+JSONUtil.toJsonStr(ethLib));
        ifList = ethLib.getIfInfo();
        for(int i = 0; i < ifList.size(); i ++){
            ifMap.put(ifList.get(i).getMac(), ifList.get(i));
            System.out.println("加载mac:"+ifList.get(i));
        }
    }

    @Override
    public boolean bindLocalMac(String localMac, boolean crypt) {

        if(!ifMap.containsKey(localMac)){
            System.out.println("本地找不到对应地址的网口: " + localMac);
            return false;
        }
        IfInfo ifInfo =  ifMap.get(localMac);

        if(isRunning){ // 启动过
            ethLib.stop();
        }else{// 未启动过
        }
        int seq = 1;
        isRunning = ethLib.start(ifInfo.getIndex(), crypt ? 1 : 0, callback, seq, null);
        return false;
    }

    @Override
    public boolean update0x21Listener(CallbackOn0x21DeviceBroadcast deviceBroadcast) {
        callbackDeviceBroadcast = deviceBroadcast;
        return true;
    }

    @Override
    public boolean update0x22Listener(CallbackOn0x22DeviceGetNetConfig deviceGetNetConfig) {
        callbackDeviceGetNetConfig = deviceGetNetConfig;
        return true;
    }

    @Override
    public boolean update0x23Listener(CallbackOn0x23DeviceNetConfigUpdateResult deviceUpdateConfigResult) {
        callbackDeviceUpdateConfigResult = deviceUpdateConfigResult;
        return true;
    }

    @Override
    public boolean updateListener(CallbackOn0x21DeviceBroadcast deviceBroadcast, CallbackOn0x22DeviceGetNetConfig deviceGetNetConfig, CallbackOn0x23DeviceNetConfigUpdateResult deviceUpdateConfigResult) {
        callbackDeviceBroadcast = deviceBroadcast;
        callbackDeviceGetNetConfig = deviceGetNetConfig;
        callbackDeviceUpdateConfigResult = deviceUpdateConfigResult;
        return true;
    }

    @Override
    public void close() {
        ethLib.stop();
        isRunning = false;
    }

    public static void main(String[] args) {
        EthLib ethLib;
        ethLib = EthFactory.getInstance();
        System.out.println("ethLib:"+JSONUtil.toJsonStr(ethLib));
    }
}
