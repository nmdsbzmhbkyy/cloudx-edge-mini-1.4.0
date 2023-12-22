package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPaymentRecord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 交易订单(ProjectPaymentRecordVo)视图表
 *
 * @author xull@aurine.cn
 * @date 2020-07-24 09:52:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("交易订单Vo")
public class  ProjectPaymentRecordVo extends ProjectPaymentRecord {


    /**
     * 付费列表
     */
    @ApiModelProperty("付费列表")
    List<PayBillVo> payBills;

    @ApiModelProperty("预存时间")
    Integer months;

    @ApiModelProperty
    String uuid;


    @ApiModelProperty("openid")
    String openId;

    @ApiModelProperty("客户端类型 0: 物业段  1: 业主端  ")
    String clientType;


    @ApiModelProperty("appId")
    String appId;

    @ApiModelProperty("appSecret")
    String appSecret;


    @ApiModelProperty("支付金额")
    String money;


}
