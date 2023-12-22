package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p> 通行方案策略类型</p>
 * @ClassName: PassPlanPolicyType
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 8:21
 * @Copyright:
 */
@AllArgsConstructor
public enum PassPlanPolicyTypeEnum {
    /**
     * 男
     */
    PHYSICS("2"),
    /**
     * 女
     */
    LOGIC("1");
    public String code;
}
