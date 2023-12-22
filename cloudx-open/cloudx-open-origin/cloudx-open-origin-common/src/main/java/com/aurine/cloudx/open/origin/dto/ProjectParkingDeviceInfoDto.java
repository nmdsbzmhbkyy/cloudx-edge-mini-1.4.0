package com.aurine.cloudx.open.origin.dto;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName ProjectDeviceInfoDto
 * @Description 设备信息
 * @Author linlx
 * @Date 2022/5/24 17:43
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectParkingDeviceInfoDto extends ProjectDeviceInfo {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备描述
     */
    private String deviceDesc;

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 设备区域
     */
    private String deviceRegionId;

    /**
     * ipv4地址
     */
    private String ipv4;

    /**
     * 安装位置
     */
    private String location;

    private String regionName;
}
