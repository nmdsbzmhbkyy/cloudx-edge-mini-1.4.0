package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant;

import lombok.AllArgsConstructor;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-10-10
 * @Copyright:
 */
@AllArgsConstructor
public enum HuaweiCallTypeEnum {

    CALL_ROOM("0", "1", "呼叫室内"),
    CALL_CENTER("1", "1","呼叫中心"),
    CALL_APP("2", "2","呼叫APP"),
    CALL_FORWARDING("3", "2","呼叫转移（云电话）");

    public String code;
    public String type;
    public String desc;



    public static HuaweiCallTypeEnum getByCode(String code) {
        HuaweiCallTypeEnum[] huaweiEventEnums = values();
        for (HuaweiCallTypeEnum huaweiEventEnum : huaweiEventEnums) {
            if (huaweiEventEnum.code().equals(code)) {
                return huaweiEventEnum;
            }
        }
        return null;
    }

    /**
     * <p>
     * 根据传入的code来获取呼叫/接听方类型
     * </p>
     *
    */
    public static String getTypeByCode(String code) {
        HuaweiCallTypeEnum[] huaweiEventEnums = values();
        for (HuaweiCallTypeEnum huaweiEventEnum : huaweiEventEnums) {
            if (huaweiEventEnum.code().equals(code)) {
                return huaweiEventEnum.type;
            }
        }
        return "1";
    }

    private String code() {
        return this.code;
    }
}
