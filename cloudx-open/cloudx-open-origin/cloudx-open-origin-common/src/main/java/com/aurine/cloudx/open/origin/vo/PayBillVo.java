package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (PayBillVo) 付款账单id及预存优惠id
 *
 * @author xull
 * @since 2020/7/27 15:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "付款账单id及预存优惠id")
public class PayBillVo {
    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private String id;
    /**
     * 优惠类型Id
     */
    @ApiModelProperty("优惠类型Id")
    private String typeId;

}