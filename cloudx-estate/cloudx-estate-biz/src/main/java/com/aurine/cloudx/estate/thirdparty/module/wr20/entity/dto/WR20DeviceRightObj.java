package com.aurine.cloudx.estate.thirdparty.module.wr20.entity.dto;

import lombok.Data;

/**
 * WR20 住户可通行设备对象
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-23 15:04
 * @Copyright:
 */
@Data
public class WR20DeviceRightObj {
    /**
     * 设备WR20编号，对应信息表ThirdPartyCode
     */
    private String deviceNo;
    /**
     * 设备描述
     */
    private String deviceDesc;


}
