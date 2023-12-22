package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>楼栋单元类型</p>
 * @ClassName: BuildingUnitTypeEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/9 8:52
 * @Copyright:
 */
@AllArgsConstructor
public enum BuildingUnitTypeEnum {
    /**
     * 数字类型
     */
    NUMBER("1"),
    /**
     * 字母类型
     */
    TENANT("2");

    public String code;
}
