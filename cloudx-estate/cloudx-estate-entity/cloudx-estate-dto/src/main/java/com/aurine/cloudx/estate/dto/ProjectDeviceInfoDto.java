package com.aurine.cloudx.estate.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 设备DTO
 * @author: wangwei
 * @date: 2022/5/19 10:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDeviceInfoDto {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * ipv4地址
     */
    private String ipv4;

    /**
     * 第三方设备ID
     */
    private String thirdpartyCode;

    /**
     * 设备产品型号
     */
    private String productKey;

    /**
     * 设备在线离线状态
     */
    private String status;

    /**
     * 设备SN
     */
    private String sn;

    /**
     * 其他参数列表（接口返回才会有的数据）
     */
    private List<DeviceExtendAttr> otherParamList;

    private Integer projectId;
}
