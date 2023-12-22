package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: ProjectHouseResidentVo
 * @author: 王良俊
 * @date: 2020年05月09日 下午03:50:43
 * @Copyright:
 */
@Data
public class ProjectHouseResidentVo {

    /**
     * 种类
     */
    @ApiModelProperty(value = "种类")
    private final String type = "房屋";
    /**
     * 房屋关系id
     */
    @ApiModelProperty("人员房屋关系id")
    private final String relaId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String personName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;

    /**
     * 楼栋业主关系类型 (1 业主 2 租赁 3 其他)
     */
    @ApiModelProperty(value = "1 业主 2 租赁 3 其他")
    private String housePeopleRel;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private String birth;

    /**
     * 用户身份证号
     */
    @ApiModelProperty(value = "用户证件号")
    private String credentialNo;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客
     */
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type")
    private String householdType;
    /**
     * 家庭关系 见通用字典项member_type
     */
    @ApiModelProperty(value = "家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他 见通用字典项member_type")
    private String memberType;
    /**
     * 入住时间
     */
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;
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
     * 是否产权人 0 否 1 是
     */
    @ApiModelProperty(value = "是否产权人 0 否 1 是")
    private String isOwner;
    /**
     * 状态 0 冻结 1 启用
     */
    @ApiModelProperty(value = "状态 0 冻结 1 启用")
    private String status;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;

    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private  String auditReason;

    /**
     * 身份证背面
     */
    @ApiModelProperty(value = "身份证背面")
    private String   credentialPicBack;
    /**
     * 身份证正面
     */
    @ApiModelProperty(value = "身份证正面")
    private String   credentialPicFront;

}
