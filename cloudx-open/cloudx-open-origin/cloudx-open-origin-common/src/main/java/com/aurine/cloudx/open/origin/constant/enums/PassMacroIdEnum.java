package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p> 逻辑通行策略宏类型</p>
 * @ClassName: PassPlanPolicyType
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/21 8:21
 * @Copyright:
 */
@AllArgsConstructor
public enum PassMacroIdEnum {
    /**
     * 楼栋梯口机
     */
    BUILDING_LADDER ("所在楼栋梯口机"),
    /**
     * 单元梯口机
     */
    UNIT_LADDER ("所在单元梯口机"),
    /**
     * 所有区口机
     */
    GATE ("所有区口机"),

    /**
     * 所有用户归属组团区口机以及未归属组团的区口机
     */
    FRAME_GATE ("所有用户归属组团区口机以及未归属组团的区口机")

    ;
    public String note;


}
