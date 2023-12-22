package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 报修服务
 *
 * @ClassName: RepairServiceEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "报修服务")
public class RepairServiceEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人", required = true)
    private String contacts;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话", required = true)
    private String phoneNumber;

    /**
     * 派单时间
     */
    @ApiModelProperty(value = "派单时间", required = true)
    private String deliveryTime;

    /**
     * 报修内容
     */
    @ApiModelProperty(value = "报修内容", required = true)
    private String repairContents;

}
