package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>通行权限状态</p>
 *
 * @ClassName: PassRightActiveTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/22 16:41
 * @Copyright:
 */
@AllArgsConstructor
public enum PassRightActiveTypeEnum {
    /**
     * 启用
     */
    ACTIVED("1"),
    /**
     * 禁用
     */
    DISABLE("0");
    public String code;
}
