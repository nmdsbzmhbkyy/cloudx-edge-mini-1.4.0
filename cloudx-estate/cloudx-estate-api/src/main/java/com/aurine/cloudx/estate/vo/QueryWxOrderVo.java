

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;


@Data
@ApiModel(value = "查询微信订单Vo")
@AllArgsConstructor
@NoArgsConstructor
public class QueryWxOrderVo {

    @ApiModelProperty(value = "平台内订单号", required = false)
    private String payOrderNo;


    @ApiModelProperty(value = "微信支付订单号", required = false)
    private String transactionId;

    @ApiModelProperty(value = "子商户号", required = false)
    private String subMchId;


}
