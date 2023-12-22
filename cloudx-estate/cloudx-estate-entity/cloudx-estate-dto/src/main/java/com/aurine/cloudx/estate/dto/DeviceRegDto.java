package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车道一体机自动注册类
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/6 15:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRegDto extends BaseDTO {

    /*
     * 第三方设备ID
     **/
    private String thirdpartyCode;

    /*
     * 设备ipv4地址
     **/
    private String ipv4;

    /*
     * 设备名
     **/
    private String deviceName;

    /*
     * 设备编号
     **/
    private String deviceCode;

    /*
     * 设备sn
     **/
    private String sn;

    /*
     * 设备产品型号
     **/
    private String productCode;

    /*
     * 设备在线离线状态
     **/
    private String status;

    /*
     * 设备类型
     **/
    private String deviceType;

    /*
     * 摄像头视频URL
     **/
    private String videoUrl;
}
