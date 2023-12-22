package com.aurine.cloudx.common.uniview.model;

import lombok.Data;

@Data
public class GetTokenRequest extends VcsRequest<GetTokenResponse> {
    // 应用ID
    private Long appId;
    // 秘钥
    private String secretKey;
}
