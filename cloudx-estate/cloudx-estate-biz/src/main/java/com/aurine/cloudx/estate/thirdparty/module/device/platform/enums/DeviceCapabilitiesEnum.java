package com.aurine.cloudx.estate.thirdparty.module.device.platform.enums;

import lombok.AllArgsConstructor;

/**
 * @author:zy
 * @data:2023/7/3 9:29 上午
 */
@AllArgsConstructor
public enum DeviceCapabilitiesEnum {


    CARD("card", "卡片"),
    FACE("face", "人脸"),
    FINGER("finger", "指纹"),
    BLE("ble", "蓝牙"),
    YUN_TALK("yuntalk", "云对讲"),
    YUN_TEL("yuntel", "云电话"),
    YUN_MONITOR("yunMonitor", "云远程监视"),
    TEXT_NOTICE("textNotice", "文本公告"),
    RICH_TEXT_NOTICE("richTextNotice", "富文本公告"),
    MEDIA_ADVERT("mediaAdvert", "媒体广告"),
    VIDEO("video", "本地视频播放"),
    CERT_MULTI_USER("certMultiUser", "一凭证多户");


    public String code;


    public String alias;

}
