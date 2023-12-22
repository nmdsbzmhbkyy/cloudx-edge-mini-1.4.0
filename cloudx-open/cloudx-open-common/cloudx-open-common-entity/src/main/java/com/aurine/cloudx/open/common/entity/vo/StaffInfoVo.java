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
 * 员工信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 20 16:35
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "员工信息Vo")
public class StaffInfoVo extends OpenBaseVo {

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
     * 员工id
     */
    @JsonProperty("staffId")
    @JSONField(name = "staffId")
    @ApiModelProperty(value = "员工id")
    @Null(message = "员工id（staffId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "员工id（staffId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "员工id（staffId）长度不能超过32")
    private String staffId;

    /**
     * 员工编码，可传入第三方编码
     */
    @JsonProperty("staffCode")
    @JSONField(name = "staffCode")
    @ApiModelProperty(value = "员工编码，可传入第三方编码")
    @Size(max = 64, message = "员工编码（staffCode）长度不能超过64")
    private String staffCode;

    /**
     * 姓名
     */
    @JsonProperty("staffName")
    @JSONField(name = "staffName")
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名（staffName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "姓名（staffName）长度不能小于1")
    @Size(max = 64, message = "姓名（staffName）长度不能超过64")
    private String staffName;

    /**
     * 性别 1 男 2 女
     */
    @JsonProperty("sex")
    @JSONField(name = "sex")
    @ApiModelProperty(value = "性别 1 男 2 女")
    @Size(max = 1, message = "性别（sex）长度不能超过1")
    private String sex;

    /**
     * 手机号码
     */
    @JsonProperty("mobile")
    @JSONField(name = "mobile")
    @ApiModelProperty(value = "手机号码")
    @Size(max = 32, message = "手机号码（mobile）长度不能超过32")
    private String mobile;

    /**
     * 员工岗位
     */
    @JsonProperty("staffPost")
    @JSONField(name = "staffPost")
    @ApiModelProperty(value = "员工岗位")
    @Size(max = 64, message = "员工岗位（staffPost）长度不能超过64")
    private String staffPost;

    /**
     * 归属部门ID
     */
    @JsonProperty("departmentId")
    @JSONField(name = "departmentId")
    @ApiModelProperty(value = "归属部门ID")
    @NotNull(message = "归属部门ID（departmentId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "归属部门ID（departmentId）数值过大")
    private Integer departmentId;

    /**
     * 级别 1 经理  2 主管 3 普通员工
     */
    @JsonProperty("grade")
    @JSONField(name = "grade")
    @ApiModelProperty(value = "级别 1 经理  2 主管 3 普通员工")
    @Size(max = 20, message = "卡id（cardId）长度不能超过20")
    private String grade;

    /**
     * 工号
     */
    @JsonProperty("staffNo")
    @JSONField(name = "staffNo")
    @ApiModelProperty(value = "工号")
    @Size(max = 20, message = "工号（staffNo）长度不能超过20")
    private String staffNo;

    /**
     * 用户id，关联sys_user的user_id，冗余项
     */
    @JsonProperty("userId")
    @JSONField(name = "userId")
    @ApiModelProperty(value = "用户id，关联sys_user的user_id，冗余项")
    @Max(value = Integer.MAX_VALUE, message = "用户id（userId）数值过大")
    private Integer userId;

    /**
     * 角色id
     */
    @JsonProperty("roleId")
    @JSONField(name = "roleId")
    @ApiModelProperty(value = "角色id")
    @Max(value = Integer.MAX_VALUE, message = "角色id（roleId）数值过大")
    private Integer roleId;

    /**
     * 入职日期
     */
    @JsonProperty("entryDate")
    @JSONField(name = "entryDate", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "入职日期")
    private LocalDateTime entryDate;

    /**
     * 出生日期
     */
    @JsonProperty("birthDate")
    @JSONField(name = "birthDate", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "出生日期")
    private LocalDateTime birthDate;

    /**
     * 证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他   字典credential_type
     */
    @JsonProperty("credentialType")
    @JSONField(name = "credentialType")
    @ApiModelProperty(value = "证件类型 111: 居民身份证 414  普通护照 554 外国人居留证 990 其他")
    @Size(max = 5, message = "卡id（cardId）长度不能超过5")
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
     * 员工类型  0 试用 1 正式
     */
    @JsonProperty("staffType")
    @JSONField(name = "staffType")
    @ApiModelProperty(value = "员工类型  0 试用 1 正式")
    @Size(max = 5, message = "员工类型（staffType）长度不能超过5")
    private String staffType;

    /**
     * 图片url
     */
    @JsonProperty("picUrl")
    @JSONField(name = "picUrl")
    @ApiModelProperty(value = "图片url")
    @Size(max = 255, message = "图片url（picUrl）长度不能超过255")
    private String picUrl;

    /**
     * 状态 0 禁用 1 启用
     */
    @JsonProperty("staffStatus")
    @JSONField(name = "staffStatus")
    @ApiModelProperty(value = "状态 0 禁用 1 启用")
    @Size(max = 5, message = "状态（staffStatus）长度不能超过5")
    private String staffStatus;

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
     * 街道
     */
    @JsonProperty("streetCode")
    @JSONField(name = "streetCode")
    @ApiModelProperty(value = "街道")
    @Size(max = 32, message = "街道（streetCode）长度不能超过32")
    private String streetCode;

    /**
     * 详细地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    @ApiModelProperty(value = "详细地址")
    @Size(max = 1024, message = "详细地址（address）长度不能超过1024")
    private String address;

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
     * 居住信息
     */
    @JsonProperty("residence")
    @JSONField(name = "residence")
    @ApiModelProperty(value = "居住信息")
    @Size(max = 128, message = "居住信息（residence）长度不能超过128")
    private String residence;

    /**
     * 工作单位
     */
    @JsonProperty("employer")
    @JSONField(name = "employer")
    @ApiModelProperty(value = "工作单位")
    @Size(max = 64, message = "工作单位（employer）长度不能超过64")
    private String employer;

    /**
     * 单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他 字典employer_type
     */
    @JsonProperty("employerType")
    @JSONField(name = "employerType")
    @ApiModelProperty(value = "单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他")
    @Size(max = 1, message = "单位类别（employerType）长度不能超过1")
    private String employerType;

    /**
     * 单位省编码
     */
    @JsonProperty("employerProvinceCode")
    @JSONField(name = "employerProvinceCode")
    @ApiModelProperty(value = "单位省编码")
    @Size(max = 32, message = "单位省编码（employerProvinceCode）长度不能超过32")
    private String employerProvinceCode;

    /**
     * 单位市编码
     */
    @JsonProperty("employerCityCode")
    @JSONField(name = "employerCityCode")
    @ApiModelProperty(value = "单位市编码")
    @Size(max = 32, message = "单位市编码（employerCityCode）长度不能超过32")
    private String employerCityCode;

    /**
     * 单位县(区)编码
     */
    @JsonProperty("employerCountyCode")
    @JSONField(name = "employerCountyCode")
    @ApiModelProperty(value = "单位县(区)编码")
    @Size(max = 32, message = "单位县(区)编码（employerCountyCode）长度不能超过32")
    private String employerCountyCode;

    /**
     * 单位街道编码
     */
    @JsonProperty("employerStreetCode")
    @JSONField(name = "employerStreetCode")
    @ApiModelProperty(value = "单位街道编码")
    @Size(max = 32, message = "单位街道编码（employerStreetCode）长度不能超过32")
    private String employerStreetCode;

    /**
     * 单位详细地址
     */
    @JsonProperty("employerAddress")
    @JSONField(name = "employerAddress")
    @ApiModelProperty(value = "单位详细地址")
    @Size(max = 1024, message = "单位详细地址（employerAddress）长度不能超过1024")
    private String employerAddress;

    /**
     * 组织结构代码
     */
    @JsonProperty("orgCode")
    @JSONField(name = "orgCode")
    @ApiModelProperty(value = "组织结构代码")
    @Size(max = 32, message = "组织结构代码（orgCode）长度不能超过32")
    private String orgCode;

    /**
     * 单位联系电话
     */
    @JsonProperty("employerPhone")
    @JSONField(name = "employerPhone")
    @ApiModelProperty(value = "单位联系电话")
    @Size(max = 20, message = "单位联系电话（employerPhone）长度不能超过20")
    private String employerPhone;

    /**
     * 单位法定代表人
     */
    @JsonProperty("employerOwner")
    @JSONField(name = "employerOwner")
    @ApiModelProperty(value = "单位法定代表人")
    @Size(max = 32, message = "单位法定代表人（employerOwner）长度不能超过32")
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
     * 国家编码
     */
    @JsonProperty("nationalityCode")
    @JSONField(name = "nationalityCode")
    @ApiModelProperty(value = "国家编码")
    @Size(max = 5, message = "国家编码（nationalityCode）长度不能超过5")
    private String nationalityCode;

    /**
     * 民族
     */
    @JsonProperty("nationCode")
    @JSONField(name = "nationCode")
    @ApiModelProperty(value = "民族")
    @Size(max = 5, message = "民族（nationCode）长度不能超过5")
    private String nationCode;

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
     * 生效时间
     */
    @JsonProperty("effDate")
    @JSONField(name = "effDate", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effDate;

    /**
     * 失效时间
     */
    @JsonProperty("expDate")
    @JSONField(name = "expDate", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expDate;

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
     * 图片Base64（自定义，非数据库字段）
     */
    @JsonProperty("picBase64")
    @JSONField(name = "picBase64")
    @ApiModelProperty(value = "图片Base64（自定义，非数据库字段）")
    private String picBase64;

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
