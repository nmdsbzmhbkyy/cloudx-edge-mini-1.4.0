package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExportExcelTypeEnum {
    /**
     * 全部导出
     */
    ALL("1"),
    /**
     * 仅导出未激活
     */
    NO_ACTIVE("2"),
    /**
     * 仅导出已激活
     */
    Active("3");

    public String code;

}
