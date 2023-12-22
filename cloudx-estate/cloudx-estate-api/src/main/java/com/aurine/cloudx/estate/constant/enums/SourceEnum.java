package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * Title: SourceEnum
 * Description:数据来源
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/4/22 10:20
 */
@AllArgsConstructor
public enum SourceEnum {
    /**
     * 人口库
     */
    POPULATION_BASE("1"),
    /**
     * 门禁系统
     */
    ACCESS_CONTROL_SYSTEM("2"),
    /**
     * 网络采集
     */
    NETWORK_TO_COLLECT("3");
    public String code;
}
