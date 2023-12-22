package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-21
 * @Copyright:
 */
@Data
public class WR20DeviceGisObj {
    /**
     * WGS84
     */
    private String gistype;
    /**
     * ,12342.23
     */
    private String latiude;

    /**
     * 2323.23343
     */
    private String longitude;
    private String alt;
}
