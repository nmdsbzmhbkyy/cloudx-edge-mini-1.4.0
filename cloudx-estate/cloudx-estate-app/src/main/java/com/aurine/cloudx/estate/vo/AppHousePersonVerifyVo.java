package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("房屋人员审核视图")
public class AppHousePersonVerifyVo {
    /**
     * uid，关系ID
     */
    @ApiModelProperty(value = "uid，关系ID", required = true)
    private String relaId;
    /**
     * 房屋ID
     */
    @ApiModelProperty(value = "房屋ID", required = true)
    private String houseId;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID", required = true)
    private String personId;

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

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type", required = true)
    private String householdType;

    @ApiModelProperty(value = "审核状态 1 审核中 2 通过  9 拒绝", required = true)
    private String auditStatus;

    @ApiModelProperty(value = "审核不通过原因")
    private String auditReason;

    @ApiModelProperty(value = "姓名", required = true)
    private String personName;

    @ApiModelProperty(value = "是否同名替换 1 是 0 否")
    private String isReplace;

}
