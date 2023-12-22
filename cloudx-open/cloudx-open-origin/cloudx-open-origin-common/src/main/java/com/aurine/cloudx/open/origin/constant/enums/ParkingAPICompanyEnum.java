package com.aurine.cloudx.open.origin.constant.enums;

import lombok.AllArgsConstructor;


/**
 * <p>停车场对接厂商枚举</p>
 * @ClassName: ParkingAPICompanyEnum
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/7/7 15:00
 * @Copyright:
 */
@AllArgsConstructor
public enum ParkingAPICompanyEnum {
    /**
     * 深圳赛菲姆
     */
    SFIRM("CC-SFM","赛菲姆"),
    /**
     * 富士智能车场
     */
    FUJICA("CC-FUJICA","富士"),

    /**
     * 立方车场
     */
    REFORMER("CC-REFORMER","立方");

    public String value;
    public String companyName;




}
