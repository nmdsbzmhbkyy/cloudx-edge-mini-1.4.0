package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

@Data
public class GetToken {
    // 访问令牌
    private String accessToken;
    // 过期时间，UTC时间
    private Long expireTime;
    // 服务器所在的地址（IP或域名）
    private String serverAddress;
    // 地区代码
    private String regionCode;
}
