package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆管理
 *
 * @ClassName: VehicleRegistrationEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "车辆管理")
public class VehicleRegistrationEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审核结过
     * true  已通过
     * false 未通过
     */
    @ApiModelProperty(value = "审核结果\n true:已通过\n false:未通过", required = true)
    private Boolean result;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号", required = true)
    private String numberPlates;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间", required = true)
    private String auditTime;

    /**
     * 备注，审核失败填写失败原因
     */
    @ApiModelProperty(value = "备注，审核失败填写失败原因")
    private String remarks;
}
