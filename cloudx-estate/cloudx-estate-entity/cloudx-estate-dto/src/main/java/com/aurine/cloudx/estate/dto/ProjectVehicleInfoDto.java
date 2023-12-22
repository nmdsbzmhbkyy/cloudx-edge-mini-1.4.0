package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车辆通行信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 17:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectVehicleInfoDto {

    /*
     * 车道设备第三方ID
     **/
    private String deviceId;

    /*
     * 车牌号
     **/
    private String plateNumber;

    /**
     * 用户id
     */
    private String userId;
}
