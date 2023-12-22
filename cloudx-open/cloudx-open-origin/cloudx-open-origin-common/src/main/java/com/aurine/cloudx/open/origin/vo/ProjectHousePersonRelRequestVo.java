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
@ApiModel("业主房屋申请表")
@Data
public class ProjectHousePersonRelRequestVo {
    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id", required = true)
    private String houseId;
    /**
     * 1 自住 2 租赁 3 民宿 4 其他
     */
    @ApiModelProperty(value = "1 自住 2 租赁 3 民宿 4 其他")
    private String housePeopleRel;

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type", required = true)
    private String householdType;

    /**
     * 家庭关系 见通用字典项member_type
     */
    @ApiModelProperty(value = "家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他 见通用字典项member_type")
    private String memberType;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    private String personName;

    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码", required = true)
    private String telephone;


    /**
     * 来源
     */
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app  ", required = true)
    private String origin;

    /**
     * 拓展来源来源
     */
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客", required = true)
    private String originEx;

    /**
     * 操作来原
     */
    @ApiModelProperty(value = "操作来原", hidden = true)
    private String remark;


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
     * 身份证正面照
     */
    @ApiModelProperty(value = "身份证正面照 base64")
    private String credentialPicFront;

    /**
     * 身份证反面照
     */
    @ApiModelProperty(value = "身份证反面照 base64")
    private String credentialPicBack;

    /**
     * 入住时间
     */
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;
}
