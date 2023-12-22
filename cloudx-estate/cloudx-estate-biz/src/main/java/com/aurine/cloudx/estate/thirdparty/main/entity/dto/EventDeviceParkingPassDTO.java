package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description: 车场车辆通行事件 DTO
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-06
 * @Copyright:
 */

@Data
public class EventDeviceParkingPassDTO extends BaseDTO {


    private String thirdCode; //车场第三方编号
    private String orderNo; //订单号
    private LocalDateTime enterDateTime; //入场时间
    private LocalDateTime outDateTime; //出场时间
    private String gateName; //车道名称
    private String operatorName; //管理人
    private String carNo; //车牌号
    private String imgUrl; //图片
}
