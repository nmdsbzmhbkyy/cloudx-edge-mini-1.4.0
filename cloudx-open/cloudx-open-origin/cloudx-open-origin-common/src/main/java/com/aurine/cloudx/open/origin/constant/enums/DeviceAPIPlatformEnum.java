package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;

/**
 * <p>设备API连接平台</p>
 *
 * @ClassName: DeviceAPIPlatformEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-27 15:08
 * @Copyright:
 */
@AllArgsConstructor
public enum DeviceAPIPlatformEnum {
    /**
     * 对接中台
     */
    MIDDLE("1", "中台"),
    /**
     * 对接WR20平台
     */
    WR20("2", "WR20");

    
    public String value;
    public String companyName;


}
