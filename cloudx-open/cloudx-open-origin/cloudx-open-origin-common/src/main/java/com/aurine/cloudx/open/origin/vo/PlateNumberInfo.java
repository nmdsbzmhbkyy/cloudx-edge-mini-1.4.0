package com.aurine.cloudx.open.origin.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 车辆登记Vo对象 只提取有用的信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/22 15:30
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlateNumberInfo {

    /*
     * 车牌号
     **/
    private String plateNumber;

    /*
     * 车牌状态 0 正常  1 禁止通行
     **/
    private String plateNumberStatus;

}
