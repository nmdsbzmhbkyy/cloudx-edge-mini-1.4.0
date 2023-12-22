package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-21
 * @Copyright:
 */
@Data
public class WR20DeviceObj {
    private String devId;
    private String devSN;
    /**
     * 是否支持富文本 0否 1是
     */
    private String supportRichTxt;
    /**
     * 设备名称
     */
    private String name;

    /**
     * WR20 Gis信息
     */
    private WR20DeviceGisObj gisInfo;
    private String deviceNo;
    private String mac;
    /**
     * 框架号
     */
    private String frameNo;

}
