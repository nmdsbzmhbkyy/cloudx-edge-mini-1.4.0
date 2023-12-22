package com.aurine.cloudx.estate.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "住户信息Vo")
public class OpenApiProjectHousePersonRelDto {

	/**
	 * uid，关系ID
	 */
	@ApiModelProperty(value = "uid，关系ID")
	private String relaId;

	/**
	 * 性别
	 */
	@ApiModelProperty(value = "性别")
	private String gender;

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
	private String rentStartTime;

	/**
	 * 租赁结束时间
	 */
	@ApiModelProperty(value = "租赁结束时间")
	private String rentStopTime;

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

	//----------------额外参数----------------

	/**
	 * 门禁卡号
	 */
	@ApiModelProperty(value = "门禁卡号")
	private String cardNo;

	/**
	 * 是否重点人员
	 */
	@ApiModelProperty(value = "是否重点人员")
	private String IsFocusPerson;

	/**
	 * 重点人员管理类别（全局）数组
	 */
	@ApiModelProperty(value = "重点人员管理类别（全局）数组")
	private String[] focusCategoryArr;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String mobile;

	/**
	 * 项目ID
	 */
	@ApiModelProperty(value = "项目ID")
	private Integer projectId;

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
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String personName;

	/**
	 * (入参)人脸照片图片
	 */
	@ApiModelProperty("人脸照片图片")
	private String picUrl;

	/**
	 * 证件类型
	 */
	@ApiModelProperty(value = "证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他   字典credential_type")
	private String credentialType;

	/**
	 * 证件号码
	 */
	@ApiModelProperty(value = "证件号码")
	private String credentialNo;

	/**
	 * 人脸图片地址
	 */
	@ApiModelProperty(value = "人脸图片地址")
	private String faceUrl;

	/**
	 * 是否重点人员：1 是 ,2 否
	 */
	@NotBlank(message = "是否重点人员不能为空")
	@Pattern(regexp = "[12]", message = "未定义的重点人员类型, 1:是 ,2:否")
	@ApiModelProperty(value = "是否重点人员：1:是 ,2:否", required = true)
	private String isFocusPerson;

	/**
	 * 人口登记类型
	 */
	@ApiModelProperty(value = "人口登记类型 1 常住人口 2 暂住人口 字典population_register_type")
	private String registerType;

	/**
	 * 曾用名
	 */
	@ApiModelProperty(value = "曾用名")
	private String oldName;

	/**
	 * 出生日期
	 */
	@ApiModelProperty(value = "出生日期")
	private String birthDate;

	/**
	 * 国家编码
	 */
	@ApiModelProperty(value = "国家编码，参见字典表 nationality_code")
	private String nationalityCode;

	/**
	 * 省编码
	 */
	@ApiModelProperty(value = "省编码")
	private String provinceCode;

	/**
	 * 市编码
	 */
	@ApiModelProperty(value = "市编码")
	private String cityCode;

	/**
	 * 县(区)编码
	 */
	@ApiModelProperty(value = "县(区)编码")
	private String countyCode;

	/**
	 * 街道编码
	 */
	@ApiModelProperty(value = "街道编码")
	private String streetCode;

	/**
	 * 详细地址
	 */
	@ApiModelProperty(value = "详细地址")
	private String address;

	/**
	 * 人员标签
	 */
	@ApiModelProperty(value = "人员标签")
	private String tag;

    /**
     * 通行方案id
     */
	@ApiModelProperty(value = "通行方案id")
    private String planId;
}