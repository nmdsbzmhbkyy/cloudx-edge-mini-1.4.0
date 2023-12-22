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
 * 人员信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员信息Vo")
public class PersonInfoVo extends OpenBaseVo {

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
     * 人员id
     */
    @JsonProperty("personId")
    @JSONField(name = "personId")
    @ApiModelProperty(value = "人员id")
    @Null(message = "人员id（personId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "人员id（personId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "人员id（personId）长度不能超过32")
    private String personId;

    /**
     * 人员编码，可传入第三方编码
     */
    @JsonProperty("personCode")
    @JSONField(name = "personCode")
    @ApiModelProperty(value = "人员编码，可传入第三方编码")
    @Size(max = 64, message = "人员编码（personCode）长度不能超过64")
    private String personCode;

    /**
     * 姓名
     */
    @JsonProperty("personName")
    @JSONField(name = "personName")
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名（personName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "姓名（personName）长度不能小于1")
    @Size(max = 64, message = "姓名（personName）长度不能超过64")
    private String personName;

    /**
     * 证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他   字典credential_type
     */
    @JsonProperty("credentialType")
    @JSONField(name = "credentialType")
    @ApiModelProperty(value = "证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他")
    @Size(max = 5, message = "证件类型（credentialType）长度不能超过5")
    private String credentialType;

    /**
     * 证件号码
     */
    @JsonProperty("credentialNo")
    @JSONField(name = "credentialNo")
    @ApiModelProperty(value = "证件号码")
    @Size(max = 32, message = "证件号码（credentialNo）长度不能超过32")
    private String credentialNo;

    /**
     * 数据来源 1 人口库 2 门禁系统 3 网络采集
     */
    @JsonProperty("source")
    @JSONField(name = "source")
    @ApiModelProperty(value = "数据来源 1 人口库 2 门禁系统 3 网络采集")
    @Size(max = 5, message = "数据来源（source）长度不能超过5")
    private String source;

    /**
     * 户籍信息
     */
    @JsonProperty("domiciile")
    @JSONField(name = "domiciile")
    @ApiModelProperty(value = "户籍信息")
    @Size(max = 64, message = "户籍信息（domiciile）长度不能超过64")
    private String domiciile;

    /**
     * 民族
     */
    @JsonProperty("nationCode")
    @JSONField(name = "nationCode")
    @ApiModelProperty(value = "民族")
    @Size(max = 5, message = "民族（nationCode）长度不能超过5")
    private String nationCode;

    /**
     * 生日
     */
    @JsonProperty("birth")
    @JSONField(name = "birth", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生日")
    private LocalDateTime birth;

    /**
     * 性别 1 男 2 女
     */
    @JsonProperty("gender")
    @JSONField(name = "gender")
    @ApiModelProperty(value = "性别 1 男 2 女")
    @Size(max = 5, message = "性别（gender）长度不能超过5")
    private String gender;

    /**
     * 省编码
     */
    @JsonProperty("provinceCode")
    @JSONField(name = "provinceCode")
    @ApiModelProperty(value = "省编码")
    @Size(max = 32, message = "省编码（provinceCode）长度不能超过32")
    private String provinceCode;

    /**
     * 市编码
     */
    @JsonProperty("cityCode")
    @JSONField(name = "cityCode")
    @ApiModelProperty(value = "市编码")
    @Size(max = 32, message = "市编码（cityCode）长度不能超过32")
    private String cityCode;

    /**
     * 县(区)编码
     */
    @JsonProperty("countyCode")
    @JSONField(name = "countyCode")
    @ApiModelProperty(value = "县(区)编码")
    @Size(max = 32, message = "县(区)编码（countyCode）长度不能超过32")
    private String countyCode;

    /**
     * 街道编码
     */
    @JsonProperty("streetCode")
    @JSONField(name = "streetCode")
    @ApiModelProperty(value = "街道编码")
    @Size(max = 32, message = "街道编码（streetCode）长度不能超过32")
    private String streetCode;

    /**
     * 详细地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    @ApiModelProperty(value = "详细地址")
    @Size(max = 1024, message = "详细地址（address;）长度不能超过1024")
    private String address;

    /**
     * 图片url
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "图片url")
    @Size(max = 255, message = "图片url（picUrl;）长度不能超过255")
    private String picUrl;

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
     * 籍贯
     */
    @JsonProperty("origin")
    @JSONField(name = "origin")
    @ApiModelProperty(value = "籍贯")
    @Size(max = 128, message = "籍贯（origin）长度不能超过128")
    private String origin;

    /**
     * 人员流动类型 1 户籍人口 2 流动人口
     */
    @JsonProperty("peopleMobileType")
    @JSONField(name = "peopleMobileType")
    @ApiModelProperty(value = "人员流动类型 1 户籍人口 2 流动人口 字典people_mobie_type")
    @Size(max = 1, message = "人员流动类型（peopleMobileType）长度不能超过1")
    private String peopleMobileType;

    /**
     * 人员类型 1 业主 2 访客 9 其他
     */
    @JsonProperty("peopleTypeCode")
    @JSONField(name = "peopleTypeCode")
    @ApiModelProperty(value = "人员类型 1 业主 2 访客 9 其他")
    @Size(max = 5, message = "人员类型（peopleTypeCode）长度不能超过5")
    private String peopleTypeCode;

    /**
     * 人员特征 字典 people_tag
     */
    @JsonProperty("peopleTag")
    @JSONField(name = "peopleTag")
    @ApiModelProperty(value = "人员特征 1.普通人员 2.知名人员 3.残疾人员 4.独居老人 5.重点治疗人员 6.安置帮教人员 7.重点关注人员 8.其他关注人员 字典 people_tag")
    @Size(max = 5, message = "人员特征（peopleTag）长度不能超过5")
    private String peopleTag;

    /**
     * 人员状态 0 已删除 1 正常
     */
    @JsonProperty("pStatus")
    @JSONField(name = "pStatus")
    @ApiModelProperty(value = "人员状态 0 已删除 1 正常")
    @NotBlank(message = "人员状态（pStatus）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "人员状态（pStatus）长度不能小于1")
    @Size(max = 1, message = "人员状态（pStatus）长度不能超过1")
    private String pStatus;

    /**
     * 居住信息
     */
    @JsonProperty("residence")
    @JSONField(name = "residence")
    @ApiModelProperty(value = "居住信息")
    @Size(max = 128, message = "居住信息（residence）长度不能超过128")
    private String residence;

    /**
     * 单位名称
     */
    @JsonProperty("employer")
    @JSONField(name = "employer")
    @ApiModelProperty(value = "单位名称")
    @Size(max = 64, message = "单位名称（employer）长度不能超过64")
    private String employer;

    /**
     * 单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他 字典employer_type
     */
    @JsonProperty("employerType")
    @JSONField(name = "employerType")
    @ApiModelProperty(value = "单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他")
    @Size(max = 64, message = "单位类别（employerType）长度不能超过64")
    private String employerType;

    /**
     * 单位省编码
     */
    @JsonProperty("employerProvinceCode")
    @JSONField(name = "employerProvinceCode")
    @ApiModelProperty(value = "单位省编码")
    @Size(max = 64, message = "单位省编码（employerProvinceCode）长度不能超过64")
    private String employerProvinceCode;

    /**
     * 单位市编码
     */
    @JsonProperty("employerCityCode")
    @JSONField(name = "employerCityCode")
    @ApiModelProperty(value = "单位市编码")
    @Size(max = 64, message = "单位市编码（employerCityCode）长度不能超过64")
    private String employerCityCode;

    /**
     * 单位县(区)编码
     */
    @JsonProperty("employerCountyCode")
    @JSONField(name = "employerCountyCode")
    @ApiModelProperty(value = "单位县(区)编码")
    @Size(max = 64, message = "单位县(区)编码（employerCountyCode）长度不能超过64")
    private String employerCountyCode;

    /**
     * 单位街道编码
     */
    @JsonProperty("employerStreetCode")
    @JSONField(name = "employerStreetCode")
    @ApiModelProperty(value = "单位街道编码")
    @Size(max = 64, message = "单位街道编码（employerStreetCode）长度不能超过64")
    private String employerStreetCode;

    /**
     * 单位详细地址
     */
    @JsonProperty("employerAddress")
    @JSONField(name = "employerAddress")
    @ApiModelProperty(value = "单位详细地址")
    @Size(max = 64, message = "单位详细地址（employerAddress）长度不能超过64")
    private String employerAddress;

    /**
     * 组织结构代码
     */
    @JsonProperty("orgCode")
    @JSONField(name = "orgCode")
    @ApiModelProperty(value = "组织结构代码")
    @Size(max = 64, message = "组织结构代码（orgCode）长度不能超过64")
    private String orgCode;

    /**
     * 单位联系电话
     */
    @JsonProperty("employerPhone")
    @JSONField(name = "employerPhone")
    @ApiModelProperty(value = "单位联系电话")
    @Size(max = 64, message = "单位联系电话（employerPhone）长度不能超过64")
    private String employerPhone;

    /**
     * 单位法定代表人
     */
    @JsonProperty("employerOwner")
    @JSONField(name = "employerOwner")
    @ApiModelProperty(value = "单位法定代表人")
    @Size(max = 64, message = "单位法定代表人（employerOwner）长度不能超过64")
    private String employerOwner;

    /**
     * 学历编码
     */
    @JsonProperty("educationCode")
    @JSONField(name = "educationCode")
    @ApiModelProperty(value = "学历编码")
    @Size(max = 5, message = "学历编码（educationCode）长度不能超过5")
    private String educationCode;

    /**
     * 婚姻状况
     */
    @JsonProperty("maritalStatusCode")
    @JSONField(name = "maritalStatusCode")
    @ApiModelProperty(value = "婚姻状况 10 未婚 20 已婚 21 初婚 22 再婚 23 复婚 30 丧偶 40 离婚 90 未说明的婚姻状况 通用字典项 marital_status")
    @Size(max = 5, message = "婚姻状况（maritalStatusCode）长度不能超过5")
    private String maritalStatusCode;

    /**
     * 配偶姓名
     */
    @JsonProperty("spouseName")
    @JSONField(name = "spouseName")
    @ApiModelProperty(value = "配偶姓名")
    @Size(max = 32, message = "配偶姓名（spouseName）长度不能超过32")
    private String spouseName;

    /**
     * 配偶证件类型
     */
    @JsonProperty("spouseIdType")
    @JSONField(name = "spouseIdType")
    @ApiModelProperty(value = "配偶证件类型")
    @Size(max = 5, message = "配偶证件类型（spouseIdType）长度不能超过5")
    private String spouseIdType;

    /**
     * 配偶证件号码
     */
    @JsonProperty("spouseIdNo")
    @JSONField(name = "spouseIdNo")
    @ApiModelProperty(value = "配偶证件号码")
    @Size(max = 32, message = "配偶证件号码（spouseIdNo）长度不能超过32")
    private String spouseIdNo;

    /**
     * 配偶身份证正面照
     */
    @JsonProperty("spousePicFront")
    @JSONField(name = "spousePicFront")
    @ApiModelProperty(value = "配偶身份证正面照")
    @Size(max = 255, message = "配偶身份证正面照（spousePicFront）长度不能超过255")
    private String spousePicFront;

    /**
     * 配偶身份证反面照
     */
    @JsonProperty("spousePicBack")
    @JSONField(name = "spousePicBack")
    @ApiModelProperty(value = "配偶身份证反面照")
    @Size(max = 255, message = "配偶身份证反面照（spousePicBack）长度不能超过255")
    private String spousePicBack;

    /**
     * 配偶联系电话
     */
    @JsonProperty("spousePhone")
    @JSONField(name = "spousePhone")
    @ApiModelProperty(value = "配偶联系电话")
    @Size(max = 20, message = "配偶联系电话（spousePhone）长度不能超过20")
    private String spousePhone;

    /**
     * 国家编码
     */
    @JsonProperty("nationalityCode")
    @JSONField(name = "nationalityCode")
    @ApiModelProperty(value = "国家编码")
    @Size(max = 5, message = "国家编码（nationalityCode）长度不能超过5")
    private String nationalityCode;

    /**
     * 入境时间
     */
    @JsonProperty("entryTime")
    @JSONField(name = "entryTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入境时间")
    private LocalDateTime entryTime;

    /**
     * 外文姓
     */
    @JsonProperty("surnameEng")
    @JSONField(name = "surnameEng")
    @ApiModelProperty(value = "外文姓")
    @Size(max = 32, message = "外文姓（surnameEng）长度不能超过32")
    private String surnameEng;

    /**
     * 外文名
     */
    @JsonProperty("nameEng")
    @JSONField(name = "nameEng")
    @ApiModelProperty(value = "外文名")
    @Size(max = 64, message = "外文名（nameEng）长度不能超过64")
    private String nameEng;

    /**
     * 电话号码
     */
    @JsonProperty("telephone")
    @JSONField(name = "telephone")
    @ApiModelProperty(value = "电话号码")
    @Size(max = 32, message = "电话号码（telephone）长度不能超过32")
    private String telephone;

    /**
     * 归属人姓名
     */
    @JsonProperty("ownName")
    @JSONField(name = "ownName")
    @ApiModelProperty(value = "归属人姓名")
    @Size(max = 32, message = "归属人姓名（telephone）长度不能超过32")
    private String ownName;

    /**
     * 归属人证件类型
     */
    @JsonProperty("ownIdType")
    @JSONField(name = "ownIdType")
    @ApiModelProperty(value = "归属人证件类型")
    @Size(max = 5, message = "归属人证件类型（ownIdType）长度不能超过5")
    private String ownIdType;

    /**
     * 归属人证件号码
     */
    @JsonProperty("ownIdNo")
    @JSONField(name = "ownIdNo")
    @ApiModelProperty(value = "归属人证件号码")
    @Size(max = 32, message = "归属人证件号码（ownIdNo）长度不能超过32")
    private String ownIdNo;

    /**
     * 出入类型
     */
    @JsonProperty("entranceTypeCode")
    @JSONField(name = "entranceTypeCode")
    @ApiModelProperty(value = "出入类型")
    @Size(max = 5, message = "出入类型（entranceTypeCode）长度不能超过5")
    private String entranceTypeCode;

    /**
     * 人员标签
     */
    @JsonProperty("tag")
    @JSONField(name = "tag")
    @ApiModelProperty(value = "人员标签")
    @Size(max = 255, message = "人员标签（tag）长度不能超过255")
    private String tag;

    /**
     * 通行开始时间
     */
    @JsonProperty("passBeginTime")
    @JSONField(name = "passBeginTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "通行开始时间")
    private LocalDateTime passBeginTime;

    /**
     * 通行结束时间
     */
    @JsonProperty("passEndTime")
    @JSONField(name = "passEndTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "通行结束时间")
    private LocalDateTime passEndTime;

    /**
     * 是否重点人员 1 是 0 否
     */
    @JsonProperty("isFocusPerson")
    @JSONField(name = "isFocusPerson")
    @ApiModelProperty(value = "是否重点人员 1 是 0 否")
    @NotBlank(message = "是否重点人员（isFocusPerson）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "是否重点人员（isFocusPerson）长度不能小于1")
    @Size(max = 1, message = "是否重点人员（isFocusPerson）长度不能超过1")
    private String isFocusPerson;

    /**
     * 人口登记类型
     */
    @JsonProperty("registerType")
    @JSONField(name = "registerType")
    @ApiModelProperty(value = "人口登记类型 1 常住人口 2 暂住人口 字典population_register_type")
    @Size(max = 20, message = "人口登记类型（registerType）长度不能超过20")
    private String registerType;

    /**
     * 居住事由
     */
    @JsonProperty("resideReason")
    @JSONField(name = "resideReason")
    @ApiModelProperty(value = "居住事由")
    @Size(max = 64, message = "居住事由（resideReason）长度不能超过64")
    private String resideReason;

    /**
     * 曾用名
     */
    @JsonProperty("oldName")
    @JSONField(name = "oldName")
    @ApiModelProperty(value = "曾用名")
    @Size(max = 64, message = "曾用名（oldName）长度不能超过64")
    private String oldName;

    /**
     * 居住方式
     */
    @JsonProperty("resideWay")
    @JSONField(name = "resideWay")
    @ApiModelProperty(value = "居住方式")
    @Size(max = 20, message = "居住方式（resideWay）长度不能超过20")
    private String resideWay;

    /**
     * 人员登记类型
     */
    @JsonProperty("personRegType")
    @JSONField(name = "personRegType")
    @ApiModelProperty(value = "人员登记类型")
    @Size(max = 20, message = "人员登记类型（personRegType）长度不能超过20")
    private String personRegType;

    /**
     * 特殊身份
     */
    @JsonProperty("specialIdentity")
    @JSONField(name = "specialIdentity")
    @ApiModelProperty(value = "特殊身份")
    @Size(max = 20, message = "特殊身份（specialIdentity）长度不能超过20")
    private String specialIdentity;

    /**
     * 政治面貌
     */
    @JsonProperty("politicalStatus")
    @JSONField(name = "politicalStatus")
    @ApiModelProperty(value = "政治面貌")
    @Size(max = 5, message = "政治面貌（politicalStatus）长度不能超过5")
    private String politicalStatus;

    /**
     * 宗教信仰
     */
    @JsonProperty("religiousBelief")
    @JSONField(name = "religiousBelief")
    @ApiModelProperty(value = "宗教信仰")
    @Size(max = 5, message = "宗教信仰（religiousBelief）长度不能超过5")
    private String religiousBelief;

    /**
     * 配偶籍贯省
     */
    @JsonProperty("spouseProvince")
    @JSONField(name = "spouseProvince")
    @ApiModelProperty(value = "配偶籍贯省")
    @Size(max = 20, message = "配偶籍贯省（spouseProvince）长度不能超过20")
    private String spouseProvince;

    /**
     * 配偶籍贯市
     */
    @JsonProperty("spouseCity")
    @JSONField(name = "spouseCity")
    @ApiModelProperty(value = "配偶籍贯市")
    @Size(max = 20, message = "配偶籍贯市（spouseCity）长度不能超过20")
    private String spouseCity;

    /**
     * 配偶籍贯县
     */
    @JsonProperty("spouseCounty")
    @JSONField(name = "spouseCounty")
    @ApiModelProperty(value = "配偶籍贯县")
    @Size(max = 20, message = "配偶籍贯县（spouseCounty）长度不能超过20")
    private String spouseCounty;

    /**
     * 户类型
     */
    @JsonProperty("hlx")
    @JSONField(name = "hlx")
    @ApiModelProperty(value = "户类型")
    @Size(max = 20, message = "户类型（hlx）长度不能超过20")
    private String hlx;

    /**
     * 户口性质
     */
    @JsonProperty("hkxz")
    @JSONField(name = "hkxz")
    @ApiModelProperty(value = "户口性质")
    @Size(max = 20, message = "户口性质（hkxz）长度不能超过20")
    private String hkxz;

    /**
     * 职业类别
     */
    @JsonProperty("zylb")
    @JSONField(name = "zylb")
    @ApiModelProperty(value = "职业类别")
    @Size(max = 20, message = "职业类别（zylb）长度不能超过20")
    private String zylb;

    /**
     * 用户id
     */
    @JsonProperty("userId")
    @JSONField(name = "userId")
    @ApiModelProperty(value = "用户id")
    @Max(value = Integer.MAX_VALUE, message = "用户id（userId）数值过大")
    private Integer userId;

    /**
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;

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

    /**
     * 配偶身份证正面照图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("spousePicFrontBase64")
    @JSONField(name = "spousePicFrontBase64")
    @ApiModelProperty(value = "配偶身份证正面照图片Base64（自定义，非数据库字段）")
    private String spousePicFrontBase64;

    /**
     * 配偶身份证反面照图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("spousePicBackBase64")
    @JSONField(name = "spousePicBackBase64")
    @ApiModelProperty(value = "配偶身份证反面照图片Base64（自定义，非数据库字段）")
    private String spousePicBackBase64;
}
