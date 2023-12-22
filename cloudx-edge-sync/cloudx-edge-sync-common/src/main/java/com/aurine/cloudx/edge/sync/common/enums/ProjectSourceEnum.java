package com.aurine.cloudx.edge.sync.common.enums;

import lombok.AllArgsConstructor;

/**
 * @Author: wrm
 * @Date: 2022/02/22 15:27
 * @Package: com.aurine.cloudx.edge.sync.enums
 * @Version: 1.0
 * @Remarks:
 **/
@AllArgsConstructor
public enum ProjectSourceEnum {
    /**
     * 本地项目
     **/
    LOCAL(0),
    /**
     * 非本地项目
     **/
    UN_LOCAL(1);
    public Integer code;
}
