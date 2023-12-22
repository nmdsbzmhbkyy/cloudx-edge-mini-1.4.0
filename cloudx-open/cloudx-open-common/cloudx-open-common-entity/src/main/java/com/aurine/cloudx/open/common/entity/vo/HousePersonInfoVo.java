package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 住户信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "住户信息Vo")
public class HousePersonInfoVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 房屋人员关系id
     */
    @JsonProperty("relaId")
    @JSONField(name = "relaId")
    @ApiModelProperty(value = "房屋人员关系id")
    @Null(message = "房屋人员关系id（relaId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "房屋人员关系id（relaId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "房屋人员关系id（relaId）长度不能超过32")
    private String relaId;

    /**
     * 关系编码，可用于第三方存储
     */
    @JsonProperty("relaCode")
    @JSONField(name = "relaCode")
    @ApiModelProperty(value = "关系编码，可用于第三方存储")
    @Size(max = 64, message = "关系编码（relaCode）长度不能超过64")
    private String relaCode;

    /**
     * 房屋id
     */
    @JsonProperty("houseId")
    @JSONField(name = "houseId")
    @ApiModelProperty(value = "房屋id")
    @NotBlank(message = "房屋id（houseId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "房屋id（houseId）长度不能小于1")
    @Size(max = 32, message = "房屋id（houseId）长度不能超过32")
    private String houseId;

    /**
     * 人员id
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id")
    @NotBlank(message = "人员id（personId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员id（personId）长度不能小于1")
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 人屋关系 1 自住 2 租赁 3 民宿 4 其他,见通用字典house_people_rel
     */
    @JsonProperty("housePeopleRel")
    @JSONField(name = "housePeopleRel")
    @ApiModelProperty(value = "人屋关系 1 自住 2 租赁 3 民宿 4 其他")
    @NotBlank(message = "人屋关系（housePeopleRel）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人屋关系（housePeopleRel）长度不能小于1")
    @Size(max = 1, message = "人屋关系（housePeopleRel）长度不能超过1")
    private String housePeopleRel;

    /**
     * 住户类型 1 业主（产权人） 2 家属 3 租客 见通用字典household_type
     */
    @JsonProperty("householdType")
    @JSONField(name = "householdType")
    @ApiModelProperty(value = "住户类型 1 业主（产权人） 2 家属 3 租客")
    @NotBlank(message = "住户类型（householdType）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "住户类型（householdType）长度不能小于1")
    @Size(max = 1, message = "住户类型（householdType）长度不能超过1")
    private String householdType;

    /**
     * 家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他 见通用字典项member_type
     */
    @JsonProperty("memberType")
    @JSONField(name = "memberType")
    @ApiModelProperty(value = "家庭关系 1: 配偶 2: 子 3: 女 4: 孙子、孙女或外孙子、外孙女 5: 父母 6: 祖父母或外祖父母 7: 兄、弟、姐、妹 8: 其他")
    @Size(max = 1, message = "家庭关系（memberType）长度不能超过1")
    private String memberType;

    /**
     * 入住时间
     */
    @JsonProperty("checkInTime")
    @JSONField(name = "checkInTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入住时间")
    private LocalDateTime checkInTime;

    /**
     * 租赁开始时间
     */
    @JsonProperty("rentStartTime")
    @JSONField(name = "rentStartTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "租赁开始时间")
    private LocalDateTime rentStartTime;

    /**
     * 租赁结束时间
     */
    @JsonProperty("rentStopTime")
    @JSONField(name = "rentStopTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "租赁结束时间")
    private LocalDateTime rentStopTime;

    /**
     * 是否产权人 0 否 1 是
     */
    @JsonProperty("isOwner")
    @JSONField(name = "isOwner")
    @ApiModelProperty(value = "是否产权人 0 否 1 是")
    @Size(max = 1, message = "是否产权人（isOwner）长度不能超过1")
    private String isOwner;

    /**
     * 状态 0 冻结 1 启用
     */
    @JsonProperty("status")
    @JSONField(name = "status")
    @ApiModelProperty(value = "状态 0 冻结 1 启用")
    @NotBlank(message = "状态（status）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "状态（status）长度不能小于1")
    @Size(max = 1, message = "状态（status）长度不能超过1")
    private String status;

    /**
     * 房屋产权证号
     */
    @JsonProperty("fwcqzh")
    @JSONField(name = "fwcqzh")
    @ApiModelProperty(value = "房屋产权证号")
    @Size(max = 32, message = "房屋产权证号（fwcqzh）长度不能超过32")
    private String fwcqzh;

    /**
     * 委托代理人姓名
     */
    @JsonProperty("wtdlrxm")
    @JSONField(name = "wtdlrxm")
    @ApiModelProperty(value = "委托代理人姓名")
    @Size(max = 20, message = "委托代理人姓名（wtdlrxm）长度不能超过20")
    private String wtdlrxm;

    /**
     * 代理人联系电话
     */
    @JsonProperty("dlrlxdh")
    @JSONField(name = "dlrlxdh")
    @ApiModelProperty(value = "代理人联系电话")
    @Size(max = 20, message = "代理人联系电话（dlrlxdh）长度不能超过20")
    private String dlrlxdh;

    /**
     * 代理人证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他  字典credential_type
     */
    @JsonProperty("dlrzjlx")
    @JSONField(name = "dlrzjlx")
    @ApiModelProperty(value = "代理人证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他")
    @Size(max = 5, message = "代理人证件类型（dlrzjlx）长度不能超过5")
    private String dlrzjlx;

    /**
     * 代理人证件号码
     */
    @JsonProperty("dlrzjhm")
    @JSONField(name = "dlrzjhm")
    @ApiModelProperty(value = "代理人证件号码")
    @Size(max = 32, message = "代理人证件号码（dlrzjhm）长度不能超过32")
    private String dlrzjhm;

    /**
     * 审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status
     */
    @JsonProperty("auditStatus")
    @JSONField(name = "auditStatus")
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过")
    @NotBlank(message = "审核状态（auditStatus）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "审核状态（auditStatus）长度不能小于1")
    @Size(max = 1, message = "审核状态（auditStatus）长度不能超过1")
    private String auditStatus;

    /**
     * 拒绝原因
     */
    @JsonProperty("auditReason")
    @JSONField(name = "auditReason")
    @ApiModelProperty(value = "拒绝原因")
    @Size(max = 128, message = "审核状态（auditStatus）长度不能超过128")
    private String auditReason;

    /**
     * 来源 1.web 2.小程序  3.app
     */
    @JsonProperty("origin")
    @JSONField(name = "origin")
    @ApiModelProperty(value = "来源 1.web 2.小程序  3.app")
    @NotBlank(message = "来源（origin）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "来源（origin）长度不能小于1")
    @Size(max = 1, message = "来源（origin）长度不能超过1")
    private String origin;

    /**
     * 来源 0:未知 1：物业 2：业主 3：访客
     */
    @JsonProperty("originEx")
    @JSONField(name = "originEx")
    @ApiModelProperty(value = "来源 0:未知 1：物业 2：业主 3：访客")
    @NotBlank(message = "来源（originEx）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "来源（originEx）长度不能小于1")
    @Size(max = 1, message = "来源（originEx）长度不能超过1")
    private String originEx;

    /**
     * 身份证正面照
     */
    @JsonProperty("credentialPicFront")
    @JSONField(name = "credentialPicFront")
    @ApiModelProperty(value = "身份证正面照")
    @Size(max = 255, message = "身份证正面照（credentialPicFront）长度不能超过255")
    private String credentialPicFront;

    /**
     * 身份证反面照
     */
    @JsonProperty("credentialPicBack")
    @JSONField(name = "credentialPicBack")
    @ApiModelProperty(value = "身份证反面照")
    @Size(max = 255, message = "身份证反面照（credentialPicBack）长度不能超过255")
    private String credentialPicBack;

    /**
     * 备注
     */
    @JsonProperty("remark")
    @JSONField(name = "remark")
    @ApiModelProperty(value = "备注")
    @Size(max = 128, message = "备注（remark）长度不能超过128")
    private String remark;

    /**
     * 证件照正面图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("credentialPicFrontBase64")
    @JSONField(name = "credentialPicFrontBase64")
    @ApiModelProperty(value = "证件照正面图片Base64（自定义，非数据库字段）")
    private String credentialPicFrontBase64;

    /**
     * 证件照背面图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("credentialPicBackBase64")
    @JSONField(name = "credentialPicBackBase64")
    @ApiModelProperty(value = "证件照背面图片Base64（自定义，非数据库字段）")
    private String credentialPicBackBase64;
}

