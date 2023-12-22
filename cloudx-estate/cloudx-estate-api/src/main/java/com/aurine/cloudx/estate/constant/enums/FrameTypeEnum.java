package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>框架类型</p>
 * @ClassName: FrameTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 9:05
 * @Copyright:
 */
@AllArgsConstructor
public enum FrameTypeEnum {
    /**
     * 楼栋
     */
    BUILDING(3),
    /**
     * 单元
     */
    UNIT(2),
    /**
     * 房屋
     */
    HOUSE(1);

    public int code;
}
