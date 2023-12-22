

package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>车位归属VO</p>
 *
 * @ClassName: ProjectParkingPlaceManageVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 13:09
 * @Copyright:
 */
@Data
@ApiModel(value = "车位")
public class ProjectParkingPlaceManageVo {
    private static final long serialVersionUID = 1L;


    /**
     * 车位ID
     */
    @ApiModelProperty(value = "车位ID")
    private String placeId;
    /**
     * 车位编号，如A-2201。可用于第三方对接
     */
    @ApiModelProperty(value = "车位编号，如A-2201。可用于第三方对接")
    private String placeCode;
    /**
     * 车位号
     */
    @ApiModelProperty(value = "车位号")
    private String placeName;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "归属停车场ID")
    private String parkId;
    /**
     * 停车区域id
     */
    @ApiModelProperty(value = "停车区域id")
    private String parkRegionId;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "归属停车场名称")
    private String parkName;

    /**
     * 人员编码
     */
    @ApiModelProperty(value = "人员编码")
    private String personId;

    /**
     * 归属房屋ID
     */
    @ApiModelProperty(value = "归属房屋")
    private String houseId;
    /**
     * 归属单元ID
     */
    @ApiModelProperty(value = "归属单元")
    private String unitId;

    /**
     * 归属楼栋ID
     */
    @ApiModelProperty(value = "归属楼栋")
    private String buildingId;


    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置 1 产权 2 租赁")
    private String relType;

    /**
     * 启用时间
     */
    @ApiModelProperty(value="启用时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


    /********************人员部分***********************/

    /**
     * 人员编码，可传入第三方编码
     */
    @ApiModelProperty(value = "人员编码，可传入第三方编码")
    private String personCode;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
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
    private String pstatus;
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
    @ApiModelProperty(value = "单位类别 1 机关团体 2 事业单位 3 企业单位 9 其他 字典employer_type")
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
     * 配偶证件正面
     */
    @ApiModelProperty(value="配偶证件正面")
    private String spousePicFront;
    /**
     * 配偶证件反面
     */
    @ApiModelProperty(value="配偶证件反面")
    private String spousePicBack;

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
     * 人员标签
     */
    @ApiModelProperty(value = "人员标签")
    private String[] tagArray;

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
     * 住户拓展属性
     */
    @ApiModelProperty(value = "住户拓展属性")
    private List<ProjectPersonAttrListVo> projectPersonAttrList;
}
