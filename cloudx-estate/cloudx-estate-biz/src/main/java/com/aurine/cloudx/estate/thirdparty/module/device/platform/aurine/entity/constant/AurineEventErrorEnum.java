package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant;

import lombok.AllArgsConstructor;

/**
 * 冠林中台 门禁错误事件
 *
 * @ClassName: AurineEventErrorEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-21 16:08
 * @Copyright:
 */
@AllArgsConstructor
public enum AurineEventErrorEnum {
    /**
     * 无效卡刷卡
     */
    ERR_CARD("2", "无效卡刷卡"),
    /**
     * 无效二维码
     */
    ERR_QR("3", "无效二维码"),
    /**
     * 过期二维码
     */
    OUT_DATE_QR("4", "过期二维码"),
    /**
     * 陌生人长时间逗留
     */
    STRANGER_STAY("5", "陌生人长时间逗留"),
    /**
     * 异常操作
     */
    ERROR("100", "异常操作");

    public String code;
    public String desc;

}
