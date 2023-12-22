package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2021/12/16 14:46
 * @Package: com.aurine.cloudx.open.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum MessageTypeEnum {
    /**
     * 请求
     */
    REQUEST("request"),
    /**
     * 响应
     */
    RESPONSE("response")
    ;
    public String name;
}
