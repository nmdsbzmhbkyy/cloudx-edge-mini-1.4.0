package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 项目实有单位信息(ProjectEmployerInfo)表实体类
 *
 * @author guhl@aurine.cn
 * @since 2020-08-25 14:58:33
 */
@Data
@TableName("project_employer_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目实有单位信息(ProjectEmployerInfo)")
public class ProjectEmployerInfo extends Model<ProjectEmployerInfo> {

    private static final long serialVersionUID = 368308415887883506L;

    /**
     * 单位id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "单位id，uuid")
    private String employerId;

    /**
     * 单位编码，可存储第三方编码
     */
    @ApiModelProperty(value = "单位编码，可存储第三方编码")
    private String employerCode;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String employerName;

    /**
     * 英文名
     */
    @ApiModelProperty(value = "英文名")
    private String englishName;

    /**
     * 缩写
     */
    @ApiModelProperty(value = "缩写")
    private String shortName;

    /**
     * 法定代表人姓名
     */
    @ApiModelProperty(value = "法定代表人姓名")
    private String legalRepresentative;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    /**
     * 单位联系电话
     */
    @ApiModelProperty(value = "单位联系电话")
    private String contactPhone;

    /**
     * 经营范围（主营）
     */
    @ApiModelProperty(value = "经营范围（主营）")
    private String mainBusiness;

    /**
     * 经营范围（兼营）
     */
    @ApiModelProperty(value = "经营范围（兼营）")
    private String sideBusiness;

    /**
     * 经验方式
     */
    @ApiModelProperty(value = "经营方式")
    private String businessMode;

    /**
     * 营业执照号
     */
    @ApiModelProperty(value = "营业执照号")
    private String licenseNo;

    /**
     * 经济类型，字典economic_type
     */
    @ApiModelProperty(value = "经济类型，字典economic_type")
    private String economicType;

    /**
     * 行业类别，字典industry_category
     */
    @ApiModelProperty(value = "行业类别，字典industry_category")
    private String industryCategory;

    /**
     * 营业执照起始
     */
    @ApiModelProperty(value = "营业执照起始")
    private LocalDate effDate;

    /**
     * 营业执照结束
     */
    @ApiModelProperty(value = "营业执照结束")
    private LocalDate expDate;

    /**
     * 委托代理人姓名
     */
    @ApiModelProperty(value = "委托代理人姓名")
    private String proxyName;

    /**
     * 代理人联系电话
     */
    @ApiModelProperty(value = "代理人联系电话")
    private String proxyPhone;

    /**
     * 代理人证件类型
     */
    @ApiModelProperty(value = "代理人证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他 字典credential_type")
    private String proxyCredentialType;

    /**
     * 代理证件号码
     */
    @ApiModelProperty(value = "代理证件号码")
    private String proxyCredentialNo;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
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


}