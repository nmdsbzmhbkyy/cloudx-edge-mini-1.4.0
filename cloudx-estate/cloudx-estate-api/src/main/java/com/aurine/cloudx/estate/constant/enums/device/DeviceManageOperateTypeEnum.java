package com.aurine.cloudx.estate.constant.enums.device;

import lombok.AllArgsConstructor;

/**
 * <p>设备变更通知操作类型枚举类</p>
 *
 * @author : 顾文豪
 * @date : 2023-11-24 16:22:02
 */
@AllArgsConstructor
public enum DeviceManageOperateTypeEnum {

    /**
     * 新增
     */
    ADD("1"),
    /**
     * 修改
     */
    UPDATE("2"),
    /**
     * 删除
     */
    DELETE("3");

    /**
     * 对应的code
     */
    public String code;
}
