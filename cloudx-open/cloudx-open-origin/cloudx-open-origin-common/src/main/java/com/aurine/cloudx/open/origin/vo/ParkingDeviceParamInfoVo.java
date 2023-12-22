package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *  设备参数服务Vo对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/19 9:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingDeviceParamInfoVo implements Serializable {

    /*
     * 需要配置参数的设备ID
     **/
    private String deviceId;

    /*
     * 设备的第三方ID
     **/
    private String thirdPartyCode;

    /*
     * 参数数据列表
     **/
    List<ParkingParamDataVo> paramList;
}
