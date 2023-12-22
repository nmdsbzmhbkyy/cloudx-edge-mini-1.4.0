

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 人员信息
 *
 * @author pigx code generator
 * @date 2020-05-12 13:37:22
 */
@Data
@TableName("project_person_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员信息")
public class ProjectPersonInfo extends OpenBasePo<ProjectPersonInfo> {

    private static final long serialVersionUID = 1L;

    /**
     * 人员ID
     */
    @ApiModelProperty(value = "人员seq")
    private Integer seq;
    /**
     * 人员ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "人员ID", required = true)
    private String personId;

    /**
     * 人员编码，可传入第三方编码
     */
    @ApiModelProperty(value = "人员编码，可传入第三方编码")
    private String personCode;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", required = true)
    private String personName;

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
     * 数据来源 1 人口库 2 门禁系统 3 网络采集
     */
    @ApiModelProperty(value = "数据来源 1 人口库 2 门禁系统 3 网络采集 ")
    private String source;

    /**
     * 户籍信息
     */
    @ApiModelProperty(value = "户籍信息")
    private String domiciile;

    /**
     * 民族
     */
    @ApiModelProperty(value = "民族")
    private String nationCode;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    private LocalDateTime birth;

    /**
     * 性别 1 男 2 女
     */
    @ApiModelProperty(value = "性别 1 男 2 女")
    private String gender;

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
     * 图片url
     */
    @ApiModelProperty(value = "图片url")
    private String picUrl;

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

    /**
     * 籍贯
     */
    @ApiModelProperty(value = "籍贯")
    private String origin;

    /**
     * 人员流动类型 1 户籍人口 2 流动人口
     */
    @ApiModelProperty(value = "人员流动类型 1 户籍人口 2 流动人口 字典people_mobie_type")
    private String peopleMobileType;

    /**
     * 人员类型 1 业主 2 访客 9 其他
     */
    @ApiModelProperty(value = "人员类型 1 业主 2 访客 9 其他")
    private String peopleTypeCode;

    /**
     * 人员特征 字典 people_tag
     */
    @ApiModelProperty(value = "人员特征 1.普通人员 2.知名人员 3.残疾人员 4.独居老人 5.重点治疗人员 6.安置帮教人员 7.重点关注人员 8.其他关注人员 字典 people_tag")
    private String peopleTag;

    /**
     * 人员状态 0 已删除 1 正常
     */
    @ApiModelProperty(value = "人员状态 0 已删除 1 正常")
    private String pStatus;

    /**
     * 居住信息
     */
    @ApiModelProperty(value = "居住信息")
    private String residence;

    @ApiModelProperty(value = "")
    private String employer;

    @ApiModelProperty(value = "单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他 字典employer_type")
    private String employerType;

    @ApiModelProperty(value = "")
    private String employerProvinceCode;

    @ApiModelProperty(value = "")
    private String employerCityCode;

    @ApiModelProperty(value = "")
    private String employerCountyCode;

    @ApiModelProperty(value = "")
    private String employerStreetCode;

    @ApiModelProperty(value = "")
    private String employerAddress;

    @ApiModelProperty(value = "")
    private String orgCode;

    @ApiModelProperty(value = "")
    private String employerPhone;

    @ApiModelProperty(value = "")
    private String employerOwner;

    /**
     * 学历编码
     */
    @ApiModelProperty(value = "学历编码")
    private String educationCode;

    /**
     * 婚姻状况
     */
    @ApiModelProperty(value = "婚姻状况 10 未婚 20 已婚 21 初婚 22 再婚 23 复婚 30 丧偶 40 离婚 90 未说明的婚姻状况 通用字典项 marital_status")
    private String maritalStatusCode;

    /**
     * 配偶姓名
     */
    @ApiModelProperty(value = "配偶姓名")
    private String spouseName;

    /**
     * 配偶证件类型
     */
    @ApiModelProperty(value = "配偶证件类型")
    private String spouseIdType;

    /**
     * 配偶证件号码
     */
    @ApiModelProperty(value = "配偶证件号码")
    private String spouseIdNo;

    /**
     * 配偶身份证正面照
     */
    @ApiModelProperty(value = "配偶身份证正面照")
    private String spousePicFront;

    /**
     * 配偶身份证反面照
     */
    @ApiModelProperty(value = "配偶身份证反面照")
    private String spousePicBack;

    /**
     * 配偶联系电话
     */
    @ApiModelProperty(value = "配偶联系电话")
    private String spousePhone;

    /**
     * 国家编码
     */
    @ApiModelProperty(value = "国家编码")
    private String nationalityCode;

    /**
     * 入境时间
     */
    @ApiModelProperty(value = "入境时间")
    private LocalDateTime entryTime;

    /**
     * 外文姓
     */
    @ApiModelProperty(value = "外文姓")
    private String surnameEng;

    /**
     * 外文名
     */
    @ApiModelProperty(value = "外文名")
    private String nameEng;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码")
    private String telephone;

    /**
     * 归属人姓名
     */
    @ApiModelProperty(value = "归属人姓名")
    private String ownName;

    /**
     * 归属人证件类型
     */
    @ApiModelProperty(value = "归属人证件类型")
    private String ownIdType;

    /**
     * 归属人证件号码
     */
    @ApiModelProperty(value = "归属人证件号码")
    private String ownIdNo;

    /**
     * 出入类型
     */
    @ApiModelProperty(value = "出入类型")
    private String entranceTypeCode;

    /**
     * 人员标签
     */
    @ApiModelProperty(value = "人员标签")
    private String tag;

    /**
     * 通行开始时间
     */
    @ApiModelProperty(value = "通行开始时间")
    private LocalDateTime passBeginTime;

    /**
     * 通行结束时间
     */
    @ApiModelProperty(value = "通行结束时间")
    private LocalDateTime passEndTime;

    /**
     * 是否重点人员 1 是 0 否
     */
    @ApiModelProperty(value = "是否重点人员 1 是 0 否")
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
     * 居住方式
     */
    @ApiModelProperty(value = "居住方式")
    private String resideWay;

    /**
     * 人员登记类型
     */
    @ApiModelProperty(value = "人员登记类型")
    private String personRegType;

    /**
     * 居住事由
     */
    @ApiModelProperty(value = "居住事由")
    private String resideReason;

    /**
     * 特殊身份
     */
    @ApiModelProperty(value = "特殊身份")
    private String specialIdentity;

    /**
     * 政治面貌
     */
    @ApiModelProperty(value = "政治面貌")
    private String politicalStatus;

    /**
     * 宗教信仰
     */
    @ApiModelProperty(value = "宗教信仰")
    private String religiousBelief;

    /**
     * 配偶籍贯省
     */
    @ApiModelProperty(value = "配偶籍贯省")
    private String spouseProvince;

    /**
     * 配偶籍贯市
     */
    @ApiModelProperty(value = "配偶籍贯市")
    private String spouseCity;

    /**
     * 配偶籍贯县
     */
    @ApiModelProperty(value = "配偶籍贯县")
    private String spouseCounty;

    /**
     * 户类型
     */
    @ApiModelProperty(value = "户类型")
    private String hlx;

    /**
     * 户口性质
     */
    @ApiModelProperty(value = "户口性质")
    private String hkxz;

    /**
     * 职业类别
     */
    @ApiModelProperty(value = "职业类别")
    private String zylb;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", hidden = true)
    private String remark;


}
