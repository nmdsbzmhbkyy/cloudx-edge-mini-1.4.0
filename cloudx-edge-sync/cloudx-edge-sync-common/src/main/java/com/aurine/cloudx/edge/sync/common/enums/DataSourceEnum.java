package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/03/07 14:01
 * @Package: com.aurine.cloudx.edge.sync.enums
 * @Version: 1.0
 * @Remarks: 数据来源
 **/
@AllArgsConstructor
public enum DataSourceEnum {
    /**
     * 从边缘网关
     */
    SLAVE("slave"),
    /**
     * 主边缘网关
     */
    MASTER("master"),

    /**
     * 云端
     */
    PLATFORM("platform");
    public String name;
}
