package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>证件类型 WR20 - Cloud4.0  映射枚举</p>
 *
 * @ClassName: WR20CredentialTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 16:29
 * @Copyright:
 */
@AllArgsConstructor
public enum WR20CredentialTypeEnum {

    JMSFZ("111", "111", "居民身份证"),
    PTHZ("414", "414", "普通护照"),
    WGRJLZ("544", "554", "外国人居留证"),
    OTHER("990", "990", "其他");

    /**
     * wr20 编码
     */
    public String wr20Code;

    /**
     * 4.0 编码
     */
    public String cloudCode;
    /**
     * 描述
     */
    public String desc;


    /**
     * @param wr20Code
     * @return
     */
    public static WR20CredentialTypeEnum getByWR20Code(String wr20Code) {
        if (StringUtils.isEmpty(wr20Code)) {
            return null;
        }
        for (WR20CredentialTypeEnum value : WR20CredentialTypeEnum.values()) {
            if (value.wr20Code.equals(wr20Code)) {
                return value;
            }
        }
        return WR20CredentialTypeEnum.OTHER;
    }

    /**
     * @param cloudCode
     * @return
     */
    public static WR20CredentialTypeEnum getByCloudCode(String cloudCode) {
        for (WR20CredentialTypeEnum value : WR20CredentialTypeEnum.values()) {
            if (value.cloudCode.equals(cloudCode)) {
                return value;
            }
        }
        return WR20CredentialTypeEnum.OTHER;
    }

}
