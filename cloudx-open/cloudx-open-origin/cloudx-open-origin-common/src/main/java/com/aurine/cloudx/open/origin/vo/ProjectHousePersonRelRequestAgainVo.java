package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 业主房屋申请视图(ProjectHousePersonRelRequestVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/3 10:51
 */
@ApiModel("业主房屋申请视图")
@Data
public class ProjectHousePersonRelRequestAgainVo {

    @ApiModelProperty(value = "uid，房屋关系id",required = true)
    private String relaId;

    /**
     * 租赁开始时间
     */
    @ApiModelProperty(value = "租赁开始时间")
    private LocalDateTime rentStartTime;

    /**
     * 租赁结束时间
     */
    @ApiModelProperty(value = "租赁结束时间")
    private LocalDateTime rentStopTime;

    /**
     * 申请人姓名
     */
    @ApiModelProperty("申请人姓名")
    private String personName;

    /**
     * 身份证正面照
     */
    @ApiModelProperty(value = "身份证正面照")
    private String credentialPicFront;

    /**
     * 身份证反面照
     */
    @ApiModelProperty(value = "身份证反面照")
    private String credentialPicBack;
}
