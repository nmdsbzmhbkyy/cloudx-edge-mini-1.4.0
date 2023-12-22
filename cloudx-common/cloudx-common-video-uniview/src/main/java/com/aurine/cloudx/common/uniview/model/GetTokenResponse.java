package com.aurine.cloudx.common.uniview.model;

import java.util.Date;

public class GetTokenResponse extends VcsResponse<GetToken> {
    /**
     * 获取token
     * @return
     */
    public String getToken() {
        return this.getData().getAccessToken();
    }

    /**
     * 验证是否过期
     * @return
     */
    public boolean isExpire() {
        return this.getData().getExpireTime() < new Date().getTime() / 1000;
    }
}
