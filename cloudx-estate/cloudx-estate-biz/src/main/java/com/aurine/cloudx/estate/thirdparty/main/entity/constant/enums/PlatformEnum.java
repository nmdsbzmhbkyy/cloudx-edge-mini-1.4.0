package com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums;

import lombok.AllArgsConstructor;

/**
 * @description:平台名称与类型
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-29
 * @Copyright:
 */
@AllArgsConstructor
public enum PlatformEnum {

    HUAWEI_MIDDLE("1", "HUAWEI_MIDDLE", "华为设备中台", "1"),
    AURINE_MIDDLE("2", "AURINE_MIDDLE", "冠林设备中台", "2"),
    ALI_MIDDLE("3", "ALI_MIDDLE", "阿里设备中台", "1"),
    AURINE_EDGE_MIDDLE("4", "AURINE_EDGE_MIDDLE", "冠林边缘网关设备中台", "1"),
    INTERCOM_DONGDONG("YDJ-DD", "INTERCOM_DONGDONG", "咚咚云对讲", "1"),
    INTERCOM_TENCENT("YDJ-TX", "INTERCOM_TENCENT", "腾讯云对讲", "1"),
    INTERCOM_HUAWEI_MIDDLE("YDJ-TX", "INTERCOM_HUAWEI_MIDDLE", "华为中台云对讲", "1"),
    BUSINESS_WR20("BUSINESS_WR20", "BUSINESS_WR20", "WR20业务对接", "2"),
    AURINE_PARKING("AURINE_PARKING", "AURINE_PARKING", "冠林车场服务", "1"),

    YUSHI("YUSHI", "YUSHI", "宇视", "1"),

    OTHER("0", "OTHER", "其他平台", "1");

    public String code;
    public String value;
    public String desc;
    /**
     * 厂家类型 1 平台级 2 项目级
     */
    public String type;


    /**
     * @param code
     * @return
     */
    public static PlatformEnum getByCode(String code) {
        for (PlatformEnum value : PlatformEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param value
     * @return
     */
    public static PlatformEnum getByValue(String value) {
        for (PlatformEnum platformEnum : PlatformEnum.values()) {
            if (platformEnum.value.equals(value)) {
                return platformEnum;
            }
        }
        return null;
    }
}
