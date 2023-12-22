package com.aurine.cloudx.estate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 如下组成：
 * redisKey
 * telephone
 * 人员信息
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/16 10:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfoCacheDto {

    String redisKey;
    String telephone;

    /**
     * 人员ID
     */
    private Integer seq;
    /**
     * 人员ID
     */
    private String personId;

    /**
     * 人员编码，可传入第三方编码
     */
    private String personCode;

    private Integer userId;

    /**
     * 姓名
     */
    private String personName;

    /**
     * 证件类型
     */
    private String credentialType;

    /**
     * 证件号码
     */
    private String credentialNo;

    /**
     * 数据来源 1 人口库 2 门禁系统 3 网络采集
     */
    private String source;

    /**
     * 户籍信息
     */
    private String domiciile;

    /**
     * 民族
     */
    private String nationCode;

    /**
     * 生日
     */
    private LocalDateTime birth;

    /**
     * 性别 1 男 2 女
     */
    private String gender;

    /**
     * 省编码
     */
    private String provinceCode;

    /**
     * 市编码
     */
    private String cityCode;

    /**
     * 县(区)编码
     */
    private String countyCode;

    /**
     * 街道编码
     */
    private String streetCode;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 图片url
     */
    private String picUrl;

    /**
     * 身份证正面照
     */
    private String credentialPicFront;

    /**
     * 身份证反面照
     */
    private String credentialPicBack;

    /**
     * 籍贯
     */
    private String origin;

    /**
     * 人员流动类型 1 户籍人口 2 流动人口
     */
    private String peopleMobileType;

    /**
     * 人员类型 1 业主 2 访客 9 其他
     */
    private String peopleTypeCode;

    /**
     * 人员特征 字典 people_tag
     */
    private String peopleTag;

    /**
     * 人员状态 0 已删除 1 正常
     */
    private String pStatus;

    /**
     * 居住信息
     */
    private String residence;

    private String employer;

    private String employerType;

    private String employerProvinceCode;

    private String employerCityCode;

    private String employerCountyCode;

    private String employerStreetCode;

    private String employerAddress;

    private String orgCode;

    private String employerPhone;

    private String employerOwner;

    /**
     * 学历编码
     */
    private String educationCode;

    /**
     * 婚姻状况
     */
    private String maritalStatusCode;

    /**
     * 配偶姓名
     */
    private String spouseName;

    /**
     * 配偶证件类型
     */
    private String spouseIdType;

    /**
     * 配偶证件号码
     */
    private String spouseIdNo;

    /**
     * 配偶身份证正面照
     */
    private String spousePicFront;

    /**
     * 配偶身份证反面照
     */
    private String spousePicBack;

    /**
     * 配偶联系电话
     */
    private String spousePhone;

    /**
     * 国家编码
     */
    private String nationalityCode;

    /**
     * 入境时间
     */
    private LocalDateTime entryTime;

    /**
     * 外文姓
     */
    private String surnameEng;

    /**
     * 外文名
     */
    private String nameEng;

    /**
     * 归属人姓名
     */
    private String ownName;

    /**
     * 归属人证件类型
     */
    private String ownIdType;

    /**
     * 归属人证件号码
     */
    private String ownIdNo;

    /**
     * 出入类型
     */
    private String entranceTypeCode;

    /**
     * 人员标签
     */
    private String tag;

    /**
     * 通行开始时间
     */
    private LocalDateTime passBeginTime;

    /**
     * 通行结束时间
     */
    private LocalDateTime passEndTime;

    /**
     * 是否重点人员 1 是 0 否
     */
    private String isFocusPerson;

    /**
     * 人口登记类型
     */
    private String registerType;

    /**
     * 曾用名
     */
    private String oldName;

    /**
     * 居住方式
     */
    private String resideWay;

    /**
     * 人员登记类型
     */
    private String personRegType;

    /**
     * 居住事由
     */
    private String resideReason;

    /**
     * 特殊身份
     */
    private String specialIdentity;

    /**
     * 政治面貌
     */
    private String politicalStatus;

    /**
     * 宗教信仰
     */
    private String religiousBelief;

    /**
     * 配偶籍贯省
     */
    private String spouseProvince;

    /**
     * 配偶籍贯市
     */
    private String spouseCity;

    /**
     * 配偶籍贯县
     */
    private String spouseCounty;

    /**
     * 户类型
     */
    private String hlx;

    /**
     * 户口性质
     */
    private String hkxz;

    /**
     * 职业类别
     */
    private String zylb;

    private Integer tenant_id;

    /**
     * 操作人
     */
    private Integer operator;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

//    /**
//     * 更新时间
//     */
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    @ApiModelProperty(value = "更新时间", hidden = true)
//    private LocalDateTime updateTime;

    /**
     * 备注
     */
    private String remark;
}
