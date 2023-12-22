package com.aurine.cloudx.estate.thirdparty.business.entity.constant;

import lombok.AllArgsConstructor;

/**
 *第三方业务平台
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-18
 * @Copyright:
 */
@AllArgsConstructor
public enum ThirdPartyBusinessPlatformEnum {
    WR20("WR20", "WR20");


    public String code;
    public String desc;


    /**
     * @param code
     * @return
     */
    public static ThirdPartyBusinessPlatformEnum getByCode(String code) {
        for (ThirdPartyBusinessPlatformEnum value : ThirdPartyBusinessPlatformEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
