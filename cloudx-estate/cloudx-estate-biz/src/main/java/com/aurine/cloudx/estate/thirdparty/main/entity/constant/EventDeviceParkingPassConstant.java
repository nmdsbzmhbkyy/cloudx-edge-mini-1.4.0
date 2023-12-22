package com.aurine.cloudx.estate.thirdparty.main.entity.constant;

import lombok.Data;

/**
 * @description: 车场车辆通行事件 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class EventDeviceParkingPassConstant {
    /**
     * 车辆入场
     */
    public static final String ACTION_CAR_ENTER = "CAR_ENTER";
    /**
     * 车辆出场
     */
    public static final String ACTION_CAR_OUTER = "CAR_OUTER";
    /**
     * 入场照片
     */
    public static final String ACTION_CAR_ENTER_IMG = "CAR_ENTER_IMG";
    /**
     * 出场照片
     */
    public static final String ACTION_CAR_OUTER_IMG = "CAR_OUTER_IMG";

    /**
     * 在线状态
     */
    public static final String ACTION_CAR_STATUS = "CAR_STATUS";


}
