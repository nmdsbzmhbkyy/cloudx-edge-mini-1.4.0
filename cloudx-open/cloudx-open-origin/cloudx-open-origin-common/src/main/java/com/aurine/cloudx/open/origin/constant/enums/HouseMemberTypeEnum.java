package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>住户家属类型</p>
 * @ClassName: HouseMemberTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-24 10:06
 * @Copyright:
 */
@AllArgsConstructor
public enum HouseMemberTypeEnum {

    SPOUSE ("1","配偶"),
    SON ("2","子"),
    DAUGHTER ("3","女"),
    GRANDSON ("4","孙子、孙女或外孙子、外孙女"),
    PARENTS ("5","父母"),
    GRANDPARENTS ("6","祖父母或外祖父母"),
    /**
     * 家属
     */
    BROTHER ("7","兄、弟、姐、妹"),
    /**
     * 其他
     */
    OTHER("8","其他");

    public String code;
    public String desc;
}
