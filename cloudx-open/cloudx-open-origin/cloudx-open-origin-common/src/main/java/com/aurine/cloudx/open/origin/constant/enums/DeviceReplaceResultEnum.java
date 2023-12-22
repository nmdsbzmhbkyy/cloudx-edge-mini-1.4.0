package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 设备替换的结果
 * </p>
 *
 * @ClassName: DeviceReplaceStatusEnum
 * @author: 王良俊 <>
 * @date: 2021年01月07日 上午10:53:35
 * @Copyright:
 */
@AllArgsConstructor
public enum DeviceReplaceResultEnum {


    /**
     * 设备替换失败
     * */
    REPLACE_FAILED("0"),

    /**
     * 设备替换成功
     * */
    REPLACE_SUCCESS("1"),

    /**
     * 设备更换后产品ID改变了（设备产品变更参数需要重新配置）
     * */
    PRODUCT_ID_CHANGE("2"),

    /**
     * 设备替换后使用旧的设备参数进行设备设置但是失败的情况
     * */
    PARAM_SETTING_FILED("3");

    public String code;


}
