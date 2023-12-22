

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>车位归属 更多信息 VO</p>
 *
 * @ClassName: ProjectParkingPlaceManageVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 13:09
 * @Copyright:
 */
@Data
@ApiModel(value = "车位")
public class ProjectParkingPlaceManageVoMore {
    private static final long serialVersionUID = 1L;


    /**
     * 车位ID
     */
    @ApiModelProperty(value = "车位ID")
    private String placeId;




    /********************人员部分***********************/


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
     * 籍贯
     */
    @ApiModelProperty(value = "籍贯")
    private String origin;
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
     * 居住信息
     */
    @ApiModelProperty(value = "居住信息")
    private String residence;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employer;
    /**
     *
     */
    @ApiModelProperty(value = "单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他  字典employer_type")
    private String employerType;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerProvinceCode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerCityCode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerCountyCode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerStreetCode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerAddress;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String orgCode;
    /**
     *
     */
    @ApiModelProperty(value = "")
    private String employerPhone;
    /**
     *
     */
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
     * 归属人姓名
     */
    @ApiModelProperty(value = "归属人姓名")
    private String ownName;

    /**
     * 人员类别
     */
    @ApiModelProperty(value="人员类别")
    private String peopleMobileType;

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
