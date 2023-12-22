package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.enums;

import lombok.AllArgsConstructor;

/**
 * <p>
 * 第三方设备类型ID枚举 
 * </p>
 * @author : 王良俊
 * @date : 2021-07-19 13:53:37
 */
@AllArgsConstructor
public enum MsgTypeEnum {
    /**
     * 0:纯文本；
     * 1:富文本；
     * 2:图片
     * 3:音乐
     * 4:纯文本&音乐
     * 5:富文本&音乐
     * 6:图片&音乐
     */
    TEXT(0, "1"),
    RICH_TEXT(1, "2");

    /**
     * 设备端的信息内容类型
     */
    public Integer deviceContentType;
    /**
     * 我们系统定义的类型
     */
    public String contentType;

    public static Integer getByContentType(String contentType) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].contentType.equals(contentType)) {
                return values()[i].deviceContentType;
            }
        }
        return MsgTypeEnum.RICH_TEXT.deviceContentType;
    }
}
