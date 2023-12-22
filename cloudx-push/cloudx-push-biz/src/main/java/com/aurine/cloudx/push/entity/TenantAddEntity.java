package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 我的房屋-添加租客
 *
 * @ClassName: TenantAddEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "我的房屋-添加租客")
public class TenantAddEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 审核结过
     * true  已通过
     * false 未通过
     */
    @ApiModelProperty(value = "审核结果\n true:已通过\n false:未通过", required = true)
    private Boolean result;

    /**
     * 租客姓名
     */
    @ApiModelProperty(value = "租客姓名", required = true)
    private String tenantName;

    /**
     * 房屋地址
     * XXX小区XXX项目组团XX栋XX单元XXX房屋
     */
    @ApiModelProperty(value = "房屋位置 如XXX小区XXX项目组团XX栋XX单元XXX房屋", required = true)
    private String houseLocation;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间", required = true)
    private String auditTime;

    /**
     *  备注，审核失败填写失败原因
     */
    @ApiModelProperty(value = "备注，审核失败填写失败原因")
    private String remarks;
}
