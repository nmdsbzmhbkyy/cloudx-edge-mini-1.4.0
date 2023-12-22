package com.aurine.cloudx.estate.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>设备媒体广告下载结果</p>
 * @author : 王良俊
 * @date : 2021-10-14 15:49:12
 */
@AllArgsConstructor
public enum DeviceMediaAdDlStatusEnum {
    /**
     * 下载失败
     */
    FAIL("0", ""),
    /**
     * 正在下载
     */
    DOWNLOADING("1", ""),
    /**
     * 已下载
     */
    SUCCESS("2", ""),
    /**
     * 空间满
     */
    SPACE_FULL("3", ""),
    /**
     * 已清零
     */
    CLEARED("4", ""),
    /**
     * 其他
     */
    OTHER("99", "");

    /**
     * 系统中的状态code
     */
    public String systemCode;

    /**
     * 第三方的状态code
     */
    public String thirdPartyCode;

    public static DeviceMediaAdDlStatusEnum getEnumsByThirdPartyCode(String thirdPartyCode) {
        DeviceMediaAdDlStatusEnum[] values = values();
        for (DeviceMediaAdDlStatusEnum item : values) {
            if (item.thirdPartyCode.equals(thirdPartyCode)) {
                return item;
            }
        }
        return DeviceMediaAdDlStatusEnum.OTHER;
    }
}
