

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>住户-人员信息 数据列表 VO</p>
 *
 * @ClassName: ProjectHousePersonRelVo
 * @author:
 * @date: 2020/5/11 10:16
 * @Copyright:
 */
@Data
@ApiModel(value = "住户-人员信息 数据列表 VO")
public class ProjectHousePersonRelRecordVo {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "uid，房屋关系ID")
    private String relaId;

    /**
     * 房屋名
     */
    @ApiModelProperty(value = "房屋名")
    private String houseName;

    /**
     * 房屋id
     */
    @ApiModelProperty(value = "房屋id")
    private String houseId;

    /**
     * 单元名
     */
    @ApiModelProperty(value = "单元名")
    private String unitName;

    /**
     * 楼栋名
     */
    @ApiModelProperty(value = "楼栋名")
    private String buildingName;

    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;

    /**
     * 性别
     */
    @ApiModelProperty(value = "住户名")
    private String personName;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别")
    private String gender;

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
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private String personId;
    /**
     * 1 业主 2 租赁 3 其他 （租赁的话显示到期时间）
     */
    @ApiModelProperty(value = "1 业主 2 租赁 3 其他")
    private String housePeopleRel;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String telephone;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态  1 待审核 2 已通过 9 未通过字典wechat_audit_status")
    private String auditStatus;

    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private String auditReason;

    /**
     * 身份证背面
     */
    @ApiModelProperty(value = "身份证背面")
    private String credentialPicBack;
    /**
     * 身份证正面
     */
    @ApiModelProperty(value = "身份证正面")
    private String credentialPicFront;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private String createTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    private String updateTime;
}
