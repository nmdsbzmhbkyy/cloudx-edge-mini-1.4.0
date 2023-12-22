package com.aurine.cloudx.estate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>电梯分页查询条件</p>
 *
 * @author 王良俊
 */
@Data
@AllArgsConstructor
public class ElevatorPageFormVo {

    /*
    * 电梯名称
    * */
    private String deviceName;

    /*
    * 电梯状态 0 正常 1 异常 （后面两种可能没有）| 2 控制器故障 3 识别设备故障
    * */
    private String elevatorStatus;
}
