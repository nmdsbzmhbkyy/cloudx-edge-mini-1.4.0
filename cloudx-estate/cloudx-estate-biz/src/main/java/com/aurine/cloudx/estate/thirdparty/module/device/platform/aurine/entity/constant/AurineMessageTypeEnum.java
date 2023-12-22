package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 消息类型 映射字典枚举
 *
 * @ClassName: AurineMessageTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-01-13 10:30
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineMessageTypeEnum {

    TEXT(0, "纯文本", "1"),
    RTF(1, "富文本", "2");

    public Integer aurineMiddleCode;
    public String desc;
    public String cloudCode;

    public static AurineMessageTypeEnum getByWR20Code(Integer wr20Code) {
        AurineMessageTypeEnum[] entityEnums = values();
        for (AurineMessageTypeEnum entity : entityEnums) {
            if (entity.aurineMiddleCode.intValue() == wr20Code.intValue()) {
                return entity;
            }
        }
        return null;
    }

    public static AurineMessageTypeEnum getByCloudCode(String cloudCode) {
        AurineMessageTypeEnum[] entityEnums = values();
        for (AurineMessageTypeEnum entity : entityEnums) {
            if (entity.cloudCode.equals(cloudCode)) {
                return entity;
            }
        }
        return null;
    }
}
