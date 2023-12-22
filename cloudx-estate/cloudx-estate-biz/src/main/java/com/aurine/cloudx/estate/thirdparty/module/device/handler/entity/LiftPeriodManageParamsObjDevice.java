package com.aurine.cloudx.estate.thirdparty.module.device.handler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>电梯时段管理设备数据对象</p>
 *
 * @author 王良俊
 * @date "2022/3/4"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LiftPeriodManageParamsObjDevice {

    /*
     * 时段管理
     **/
    List<LiftPeriodItemDevice> periods;
}
