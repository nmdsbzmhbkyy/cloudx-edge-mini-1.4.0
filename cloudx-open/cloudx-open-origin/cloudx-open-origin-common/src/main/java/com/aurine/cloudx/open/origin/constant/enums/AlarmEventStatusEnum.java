package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>框架类型</p>
 * @ClassName: FrameTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 9:05
 * @Copyright:
 */
@AllArgsConstructor
public enum AlarmEventStatusEnum {
    /**
     * 未处理
     */
    UNPROCESSED("0"),
    /**
     * 已处理
     */
    PROCESSED("2"),
    /**
     * 处理中
     */
    PROCESSING("1");

    public String code;
}
