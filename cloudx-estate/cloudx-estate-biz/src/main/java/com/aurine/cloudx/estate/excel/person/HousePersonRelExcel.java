package com.aurine.cloudx.estate.excel.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * <p>
 *  迁入住户-系统标准版
 *  所有地址都是用-分隔
 * </p>
 *
 * @ClassName: HousePersonRelExcel
 * @author: 王良俊 <>
 * @date: 2020年08月19日 下午12:57:45
 * @Copyright:
 */
@Data
public class HousePersonRelExcel {

    /**
     * 楼栋 *
     * */
    @Length(min = 1,max = 32,message = "楼栋应在32字内")
    @ApiModelProperty(value = "楼栋 *")
    private String buildingName;

    /**
     * 单元 *
     * */
    @Length(min = 1,max = 32,message = "单元应在32字内")
    @ApiModelProperty(value = "单元 *")
    private String unitName;

    /**
     *房号 *
     * */
    @Length(min = 1,max = 32,message = "房号应在32字内")
    @ApiModelProperty(value = "房号 *")
    private String houseNo;

    /**
     *是否重点人员*中文
     * */
    @ApiModelProperty(value = "是否重点人员*中文")
    private String isFocusPersonCh;

    /**
     *人口登记类型 (公安必填)
     * */
    @Length(min = 0,max = 20,message = "人口登记类型应在20字内")
    @ApiModelProperty(value = "人口登记类型")
    private String registerType;

    /**
     *姓名 *
     * */
    @Length(min = 1,max = 64,message = "姓名应在64字内")
    @ApiModelProperty(value = "姓名 *")
    private String personName;

    /**
     *手机号 *
     * */
    @ApiModelProperty(value = "手机号*")
    private String telephone;

    /**
     *曾用名
     * */
    @Length(min = 0,max = 64,message = "曾用名应在64字内")
    @ApiModelProperty(value = "曾用名")
    private String oldName;

    /**
     *性别  (公安必填) 中文
     * */
    @ApiModelProperty(value = "性别*中文")
    private String genderCh;

    /**
     *出生日期
     * */
    @ApiModelProperty(value = "出生日期")
    private String birthStr;

    /**
     *国籍 中文  (公安必填)
     * */
    @ApiModelProperty(value = "国籍中文")
    private String nationalityNameCh;

    /**
     *证件类型 中文  (公安必填)
     * */
    @ApiModelProperty(value = "证件类型中文")
    private String credentialTypeCh;

    /**
     *证件号  (公安必填)
     * */
    @ApiModelProperty(value = "证件号")
    private String credentialNo;

    /**
     *住户类型 * 中文
     * */
    @ApiModelProperty(value = "住户类型*中文")
    private String householdTypeCh;

    /**
     * 家庭成员关系 * 中文
     */
    @ApiModelProperty(value = "关系*中文")
    private String memberTypeCh;

    /**
     * 租赁日期 *（只有是访客的时候才是必填项） 2020-08-03 至 2020-08-06
     */
    @ApiModelProperty(value = "租赁日期*")
    private String rentTimeRange;

    /**
     *入住时间
     * */
    @ApiModelProperty(value = "入住时间")
    private String checkInTimeStr;

    /**
     * 户籍信息（这里是中文要拆分）  (公安必填) provinceCode，cityCode，countyCode，streetCode
     */
    @ApiModelProperty(value = "户籍信息")
    private String domicileInfo;

    /**
     * 户籍详细地址  (公安必填)
     */
    @Length(min = 0,max = 1024,message = "户籍详细地址应在1024字内")
    @ApiModelProperty(value = "户籍详细地址")
    private String address;

    /**
     *居住方式
     * */
    @Length(min = 0,max = 20,message = "居住方式应在20字内")
    @ApiModelProperty(value = "居住方式")
    private String resideWay;

    /**
     *人员登记类型
     * */
    @Length(min = 0,max = 20,message = "人员登记类型应在20字内")
    @ApiModelProperty(value = "人员登记类型")
    private String personRegType;

    /**
     *居住事由
     * */
    @Length(min = 0,max = 64,message = "居住事由应在64字内")
    @ApiModelProperty(value = "居住事由")
    private String resideReason;

    /**
     *外文名
     * */
    @Length(min = 0,max = 64,message = "外文名应在64字内")
    @ApiModelProperty(value = "外文名")
    private String nameEng;

    /**
     *外文姓
     * */
    @Length(min = 0,max = 32,message = "外文姓应在32字内")
    @ApiModelProperty(value = "外文姓")
    private String surnameEng;

    /**
     *入境时间
     * */
    @ApiModelProperty(value = "入境时间")
    private String entryTimeStr;

    /**
     *民族中文
     * */
    @ApiModelProperty(value = "民族中文")
    private String nationCh;

    /**
     *文化程度中文
     * */
    @ApiModelProperty(value = "文化程度中文")
    private String educationCh;

    /**
     *特殊身份中文
     * */
    @ApiModelProperty(value = "特殊身份中文")
    private String specialIdentityCh;

    /**
     *政治面貌中文
     * */
    @ApiModelProperty(value = "政治面貌中文")
    private String politicalStatusCh;

    /**
     *宗教信仰中文
     * */
    @ApiModelProperty(value = "宗教信仰中文")
    private String religiousBeliefCh;

    /**
     *婚姻状况中文
     * */
    @ApiModelProperty(value = "婚姻状况中文")
    private String maritalStatusCodeCh;

    /**
     *配偶姓名
     * */
    @Length(min = 0,max = 32,message = "配偶姓名应在32字内")
    @ApiModelProperty(value = "配偶姓名")
    private String spouseName;

    /**
     *配偶联系电话
     * */
    @ApiModelProperty(value = "配偶联系电话")
    private String spousePhone;

    /**
     *配偶证件类型中文
     * */
    @ApiModelProperty(value = "配偶证件类型中文")
    private String spouseIdTypeCh;

    /**
     *配偶证件号码
     * */
    @ApiModelProperty(value = "配偶证件号码")
    private String spouseIdNo;

    /**
     * 配偶籍贯 spouseProvince，spouseCity，spouseCounty
     */
    @ApiModelProperty(value = "籍贯")
    private String spouseDomicile;

    /**
     *人员类别中文
     * */
    @ApiModelProperty(value = "人员类别中文")
    private String peopleTypeCh;

    /**
     *户类型中文
     * */
    @ApiModelProperty(value = "户类型中文")
    private String hlxCh;

    /**
     *户口性质分类中文
     * */
    @ApiModelProperty(value = "户口性质分类中文")
    private String hkxzflCh;

    /**
     *单位名称
     * */
    @Length(min = 0,max = 64,message = "单位名称应在64字内")
    @ApiModelProperty(value = "单位名称")
    private String employer;

    /**
     *职业类别
     * */
    @Length(min = 0,max = 20,message = "职业类别应在20字内")
    @ApiModelProperty(value = "职业类别")
    private String zylb;

    /**
     *单位类别中文
     * */
    @ApiModelProperty(value = "单位类别中文")
    private String employerTypeCh;

    /**
     *组织结构代码
     * */
    @Length(min = 0,max = 32,message = "组织结构代码应在32字符内")
    @ApiModelProperty(value = "组织结构代码")
    private String orgCode;

    /**
     *单位联系电话
     * */
    @Length(min = 0,max = 20,message = "单位联系电话长度不应超过20")
    @ApiModelProperty(value = "单位联系电话")
    private String employerPhone;

    /**
     *单位法定代表人
     * */
    @Length(min = 0,max = 32,message = "单位法定代表人应在32字内")
    @ApiModelProperty(value = "单位法定代表人")
    private String employerOwner;

    /**
     * 单位地址 employerProvinceCode，employerCityCode，employerCountyCode，employerStreetCode
     */
    @ApiModelProperty(value = "单位地址")
    private String employerLocation;

    /**
     * 单位详细地址
     */
    @Length(min = 0,max = 1024,message = "单位详细地址应在1024字内")
    @ApiModelProperty(value = "单位详细地址")
    private String employerAddress;

    /**
     *房屋产权证号
     * */
    @Length(min = 0,max = 32,message = "房屋产权证号长度不超过32")
    @ApiModelProperty(value = "房屋产权证号")
    private String fwcqzh;

    /**
     *委托代理人姓名
     * */
    @Length(min = 0,max = 32,message = "委托代理人姓名不应超过32个字")
    @ApiModelProperty(value = "委托代理人姓名")
    private String wtdlrxm;

    /**
     *代理人联系电话
     * */
    @Length(min = 0,max = 20,message = "代理人联系电话长度不应超过20")
    @ApiModelProperty(value = "代理人联系电话")
    private String dlrlxdh;

    /**
     *代理人证件类型中文
     * */
    @ApiModelProperty(value = "代理人证件类型中文")
    private String dlrzjlxCh;

    /**
     *代理人证件号码
     * */
    @ApiModelProperty(value = "代理人证件号码")
    private String dlrzjhm;

    /**
     * 这里应该转换成数组focusCategoryArr
     */
    @ApiModelProperty(value = "重点人员管理类别中文逗号分割")
    private String focusCategoryCh;

    /**
     * 管理地一地址 province1，city1，county1，street1
     */
    @ApiModelProperty(value = "管理地一地址")
    private String manage1Location;

    /**
     * 管理地一详细地址
     */
    @Length(min = 0,max = 128,message = "管理地一详细地址不应超过128个字")
    @ApiModelProperty(value = "管理地一详细地址")
    private String address1;

    /**
     * 治安重点人员管理类别
     */
    @ApiModelProperty(value = "治安重点人员管理类别")
    private String focusCategory1Ch;

    /**
     *管控状态中文
     * */
    @ApiModelProperty(value = "管控状态")
    private String status1Ch;

    /**
     *管控地联系方式
     * */
    @Length(min = 0,max = 20,message = "管控地一联系方式长度不应超过20")
    @ApiModelProperty(value = "管控地联系方式")
    private String phone1;

    /**
     *管控事由
     * */
    @Length(min = 0,max = 128,message = "管理地一管控事由不应超过128个字")
    @ApiModelProperty(value = "管控事由")
    private String reason1;

    /**
     *管控民警姓名
     * */
    @Length(min = 0,max = 20,message = "管控民警1姓名不应超过20个字")
    @ApiModelProperty(value = "管控民警姓名")
    private String policeName1;

    /**
     *管控民警联系电话
     * */
    @Length(min = 0,max = 20,message = "管控民警一联系电话长度不应超过20")
    @ApiModelProperty(value = "管控民警联系电话")
    private String policePhone1;

    /**
     *管控民警证件类型中文
     * */
    @ApiModelProperty(value = "管控民警证件类型中文")
    private String policeIdType1Ch;

    /**
     *管控民警证件号码
     * */
    @ApiModelProperty(value = "管控民警证件号码")
    private String policeIdNo1;

    /**
     * 管理地二地址 province2,city2,county2,street2
     */
    @ApiModelProperty(value = "管理地二地址")
    private String manage2Location;

    /**
     *管理地二详细地址
     * */
    @Length(min = 0,max = 128,message = "管理地二详细地址不应超过128个字")
    @ApiModelProperty(value = "管理地二详细地址")
    private String address2;

    /**
     *治安重点人员管理类别中文
     * */
    @ApiModelProperty(value = "治安重点人员管理类别中文")
    private String focusCategory2Ch;

    /**
     * 管理地二管控状态中文
     */
    @ApiModelProperty(value = "管控状态中文")
    private String status2Ch;

    /**
     *管控地联系方式
     * */
    @Length(min = 0,max = 20,message = "管控地二联系方式长度不应超过20")
    @ApiModelProperty(value = "管控地联系方式")
    private String phone2;

    /**
     *管控事由
     * */
    @Length(min = 0,max = 128,message = "管理地二管控事由不应超过128个字")
    @ApiModelProperty(value = "管控事由")
    private String reason2;

    /**
     * 管理地二管控民警姓名
     */
    @Length(min = 0,max = 20,message = "管控民警2姓名不应超过20个字")
    @ApiModelProperty(value = "管控民警姓名")
    private String policeName2;

    /**
     * 管理地二管控民警联系电话
     */
    @Length(min = 0,max = 20,message = "管控民警二联系电话长度不应超过20")
    @ApiModelProperty(value = "管控民警联系电话")
    private String policePhone2;

    /**
     * 管理地二管控民警证件类型中文
     */
    @ApiModelProperty(value = "管控民警证件类型中文")
    private String policeIdType2Ch;

    /**
     * 管理地二管控民警证件号码
     */
    @ApiModelProperty(value = "管控民警证件号码")
    private String policeIdNo2;

}
