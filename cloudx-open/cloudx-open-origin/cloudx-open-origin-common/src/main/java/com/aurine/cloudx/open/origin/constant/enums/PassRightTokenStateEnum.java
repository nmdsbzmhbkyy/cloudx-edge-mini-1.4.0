package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>通行凭证状态 0 未使用 1 使用中 2 冻结</p>
 * @ClassName: PassRightTokenStateEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/25 8:11
 * @Copyright:
 */
@AllArgsConstructor
public enum PassRightTokenStateEnum {
    /**
     * 使用中
     */
    USED("1"),
    /**
     * 冻结
     */
    FREEZE("2"),
    /**
     * 未使用
     */
    UNUSE("0");
    public String code;
}
