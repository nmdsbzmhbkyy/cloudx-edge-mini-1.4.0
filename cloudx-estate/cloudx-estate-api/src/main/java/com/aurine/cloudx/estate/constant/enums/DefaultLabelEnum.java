package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>默认标签名称</p>
 *
 * @ClassName: DefaultLabelEnum
 * @author: 黄阳光 <huangyg@aurine.cn>
 * @date: 2020/9/7 14:05
 * @Copyright:
 */
@AllArgsConstructor
public enum DefaultLabelEnum {
    /**
     * 孤寡老人
     */
    OLD_MAN("孤寡老人"),
    /**
     * 欠费业主
     */
    ARREARS("欠费业主"),
    /**
     * 困难人员
     */
    DIFFICULTY("困难人员");

    public String code;
}
