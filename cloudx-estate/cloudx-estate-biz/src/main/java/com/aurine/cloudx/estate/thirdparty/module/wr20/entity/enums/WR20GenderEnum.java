package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.enums;

import lombok.AllArgsConstructor;

/**
 * <p>性别 WR20 - Cloud4.0  映射枚举</p>
 *
 * @ClassName: WR20CredentialTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 16:43
 * @Copyright:
 */
@AllArgsConstructor
public enum WR20GenderEnum {

    UNKNOW("0", "0", "未知的性别"),
    MALE("1", "1", "男性"),
    FEMALE("2", "2", "女性"),
    HARD_TO_TELL("5", "9", "未说明的性别");

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
    public static WR20GenderEnum getByWR20Code(String wr20Code) {
        for (WR20GenderEnum value : WR20GenderEnum.values()) {
            if (value.wr20Code.equals(wr20Code)) {
                return value;
            }
        }
        return WR20GenderEnum.HARD_TO_TELL;
    }

    /**
     * @param cloudCode
     * @return
     */
    public static WR20GenderEnum getByCloudCode(String cloudCode) {
        for (WR20GenderEnum value : WR20GenderEnum.values()) {
            if (value.cloudCode.equals(cloudCode)) {
                return value;
            }
        }
        return WR20GenderEnum.HARD_TO_TELL;
    }

}
