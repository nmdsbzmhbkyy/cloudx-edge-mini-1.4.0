package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @program: cloudx
 * @description:
 * @author: 谢泽毅
 * @create: 2021-08-11 16:36
 **/
@AllArgsConstructor
public enum TencentErrorEnum {
    SUCCESS_CODE("0", "操作成功"),
    ERROR_CODE("1", "操作失败"),
    OTHER_CODE("-99", "出现其它异常，请联系管理员");
    public String code;
    public String value;


    /**
     * @param code
     * @return
     */
    public static TencentErrorEnum getByCode(String code) {
        for (TencentErrorEnum value : TencentErrorEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return TencentErrorEnum.OTHER_CODE;
    }
}
