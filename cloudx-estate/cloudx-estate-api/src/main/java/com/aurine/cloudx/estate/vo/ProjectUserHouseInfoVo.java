package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 当前用户房屋信息
 *
 * @author
 * @date
 */
@Data
@ApiModel(value = "当前用户房屋信息")
public class ProjectUserHouseInfoVo {

    /**
     * 房屋编号
     */
    @ApiModelProperty(value = "房屋编号")
    private String houseId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;

    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;

    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 审核中 2 通过 9 未通过")
    private String auditStatus;

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
    /**
     * 当前房屋入住人数
     */
    @ApiModelProperty(value = "当前房屋入住人数")
    private String housePersonNum;
}

