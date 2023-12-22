package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 访客邀约
 *
 * @ClassName: InviteVisitorsEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "访客邀约")
public class InviteVisitorsEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审核结过
     * true  已通过
     * false 未通过
     */
    @ApiModelProperty(value = "审核结果\n true:已通过\n false:未通过", required = true)
    private Boolean result;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间", required = true)
    private String auditTime;

    /**
     * 访客姓名
     */
    @ApiModelProperty(value = "访客姓名", required = true)
    private String invitorName;

    /**
     *  备注，审核失败填写失败原因
     */
    @ApiModelProperty(value = "备注，审核失败填写失败原因")
    private String remarks;
}
