package com.aurine.cloudx.push.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 投诉建议
 *
 * @ClassName: ComplaintSuggestEntity
 * @author: 邹宇
 * @date: 2021-8-27 14:06:49
 * @Copyright:
 */
@Data
@ApiModel(value = "投诉建议")
public class ComplaintSuggestEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人", required = true)
    private String contacts;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", required = true)
    private String phoneNumber;

    /**
     * 房屋地址
     * 如XXX小区XXX项目组团XX栋XX单元XXX房屋
     */
    @ApiModelProperty(value = "房屋地址 如XXX小区XXX项目组团XX栋XX单元XXX房屋", required = true)
    private String roomNo;

    /**
     * 投诉建议内容
     */
    @ApiModelProperty(value = "投诉建议内容", required = true)
    private String contents;

    /**
     * 时间
     */
    @ApiModelProperty(value = "时间", required = true)
    private String time;
}
