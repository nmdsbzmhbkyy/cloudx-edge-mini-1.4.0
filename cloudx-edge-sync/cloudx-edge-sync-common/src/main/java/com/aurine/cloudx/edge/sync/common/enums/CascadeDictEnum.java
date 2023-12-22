package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/02/08 14:22
 * @Package: com.aurine.cloudx.edge.sync.enums
 * @Version: 1.0
 * @Remarks: 级联入云字典
 **/
@AllArgsConstructor
public enum CascadeDictEnum {
    /**
     * 申请
     */
    CASCADE("cascade"),

    /**
     * 拒绝
     */
    CLOUD("cloud");
    public String name;

}
