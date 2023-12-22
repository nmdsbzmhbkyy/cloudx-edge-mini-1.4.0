package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>框架层级枚举</p>
 *
 * @ClassName: FrameLevelEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-02-25 9:02
 * @Copyright:
 */
@AllArgsConstructor
public enum FrameLevelEnum {

    HOUSE(1),
    UNIT(2),
    BUILDING(3),
    GROUP4(4),
    GROUP5(5),
    GROUP6(6),
    GROUP7(7);

    public Integer code;
}
