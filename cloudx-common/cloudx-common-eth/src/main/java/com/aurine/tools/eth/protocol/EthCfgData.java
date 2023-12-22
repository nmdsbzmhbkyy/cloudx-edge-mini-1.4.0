package com.aurine.tools.eth.protocol;

import lombok.Data;

/**
 * @author chensl
 * @date 2022-06-10
 */
@Data
public class EthCfgData<T> {
    private String msgId;
    private int commandId;
    private int srcType;
    private int direct;
    private String sessionId;
    private int echoCode;
    private T body;

    public EthCfgData(){}

    public EthCfgData(EthCfgData request){
        this.msgId = request.msgId;
        this.commandId = request.commandId;
        this.srcType = request.srcType;
        this.direct = request.direct;
        this.sessionId = request.sessionId;
        this.echoCode = request.echoCode;
        //this.body = request.body;
    }
}
