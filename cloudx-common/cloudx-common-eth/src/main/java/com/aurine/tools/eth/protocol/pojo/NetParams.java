package com.aurine.tools.eth.protocol.pojo;

import lombok.Data;

@Data
public class NetParams {
    private String ip;
    private String mask;
    private String gateway;
    private String dns1;
    private String dns2;
    private String centerIp;
}
