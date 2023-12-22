

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>住户PO</p>
 *
 * @ClassName: ProjectHousePersonRel
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 8:32
 * @Copyright:
 */
@Data
@TableName("project_house_person_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "住户")
public class ProjectHousePersonRel extends OpenBasePo<ProjectHousePersonRel> {

    private static final long serialVersionUID = 1L;

    /**
     * uid，关系ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uid，关系ID")
    private String relaId;

    /**
     * 关系编码，可用于第三方存储
     */
    @ApiModelProperty(value = "关系编码，可用于第三方存储")
    private String relaCode;

    /**
     * 房屋ID
     */
    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员ID")
    private String personId;

    /**
     * 人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel
     */
    @ApiModelProperty(value = "人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel")
    private String housePeopleRel;

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
     * 操作来原
     */
    @ApiModelProperty(value = "操作来原")
    private String remark;
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
     * 房屋产权证号
     */
    @ApiModelProperty(value = "房屋产权证号")
    private String fwcqzh;

    /**
     * 委托代理人姓名
     */
    @ApiModelProperty(value = "委托代理人姓名")
    private String wtdlrxm;

    /**
     * 代理人联系电话
     */
    @ApiModelProperty(value = "代理人联系电话")
    private String dlrlxdh;

    /**
     * 代理人证件类型
     */
    @ApiModelProperty(value = "代理人证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他  字典credential_type")
    private String dlrzjlx;

    /**
     * 代理人证件号码
     */
    @ApiModelProperty(value = "代理人证件号码")
    private String dlrzjhm;

    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;

    /**
     * 拒绝原因
     */
    @ApiModelProperty(value = "拒绝原因")
    private String auditReason;

    /**
     * 来源
     */
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app  ")
    private String origin;

    /**
     * 拓展来源来源
     */
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客")
    private String originEx;

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
