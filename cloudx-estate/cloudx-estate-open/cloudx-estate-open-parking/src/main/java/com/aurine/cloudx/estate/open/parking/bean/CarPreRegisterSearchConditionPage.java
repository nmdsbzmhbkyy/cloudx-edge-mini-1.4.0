package com.aurine.cloudx.estate.open.parking.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 车辆预登记页面查询类
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-03-08 11:20:17
 */
@Data
public class CarPreRegisterSearchConditionPage extends Page {

    /**
     * 车牌号
     */
    @ApiModelProperty("车牌号")
    private String plateNumber;

    /**
     * 车主姓名
     */
    @ApiModelProperty("车主姓名")
    private String personName;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;

}
