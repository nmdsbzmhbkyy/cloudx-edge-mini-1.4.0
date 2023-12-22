package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *  设备车牌下发情况查询条件
 * </p>
 *
 * @author 王良俊
 * @since 2022/5/26 15:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlateNumberDeviceSearchCondition {

    /*
     * 设备ID
     **/
    private String deviceId;
}
