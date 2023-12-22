package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目员工信息表
 *
 * @author lingang
 * @date 2020-05-11 13:38:09
 */
@Data
@TableName("project_staff")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目员工信息表")
public class ProjectStaff extends OpenBasePo<ProjectStaff> {
    private static final long serialVersionUID = 1L;
    /**
     * uid，可作为项目员工的唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uid，可作为项目员工的唯一标识")
    private String staffId;
    /**
     * 员工编码，可传入第三方编码
     */
    @ApiModelProperty(value = "员工编码，可传入第三方编码")
    private String staffCode;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名")
    private String staffName;
    /**
     * 性别 1 男 2 女
     */
    @ApiModelProperty(value = "性别 1 男 2 女")
    private String sex;
    /**
     * 手机号码
     */
    @ApiModelProperty(value = "手机号码")
    private String mobile;
    /**
     * 员工岗位
     */
    @ApiModelProperty(value = "员工岗位")
    private String staffPost;
    /**
     * 归属部门ID
     */
    @ApiModelProperty(value = "归属部门ID")
    private Integer departmentId;
    /**
     * 级别 1 经理  2 主管 3 普通员工
     */
    @ApiModelProperty(value = "级别 1 经理  2 主管 3 普通员工")
    private String grade;
    /**
     * 工号
     */
    @ApiModelProperty(value = "工号")
    private String staffNo;
    /**
     * 用户id，关联sys_user的user_id，冗余项
     */
    @ApiModelProperty(value = "用户id，关联sys_user的user_id，冗余项")
    private Integer userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;
    /**
     * 入职日期
     */
    @ApiModelProperty(value = "入职日期")
    private LocalDateTime entryDate;
    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期")
    private LocalDateTime birthDate;
    /**
     * 证件类型
     */
    @ApiModelProperty(value = "证件类型 111.居民身份证 414.普通护照 554.外国人居留证 990.其他  字典credential_type")
    private String credentialType;
    /**
     * 证件号码
     */
    @ApiModelProperty(value = "证件号码")
    private String credentialNo;
    /**
     * 员工类型  0 试用 1 正式
     */
    @ApiModelProperty(value = "员工类型  0 试用 1 正式")
    private String staffType;
    /**
     * 照片url
     */
    @ApiModelProperty(value = "照片url")
    private String picUrl;
    /**
     * 状态 0 禁用 1 启用
     */
    @ApiModelProperty(value = "状态 0 禁用 1 启用")
    private String staffStatus;
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
    @ApiModelProperty(value = "人员流动类型 1 户籍人口 2 流动人口  字典people_mobie_type")
    private String peopleMobileType;
    /**
     * 居住信息
     */
    @ApiModelProperty(value = "居住信息")
    private String residence;
    /**
     * 工作单位
     */
    @ApiModelProperty(value = "工作单位")
    private String employer;
    /**
     * 单位类别
     */
    @ApiModelProperty(value = "单位类别  1 机关团体 2 事业单位 3 企业单位 9 其他 字典employer_type")
    private String employerType;
    /**
     * 单位省编码
     */
    @ApiModelProperty(value = "单位省编码")
    private String employerProvinceCode;
    /**
     * 单位市编码
     */
    @ApiModelProperty(value = "单位市编码")
    private String employerCityCode;
    /**
     * 单位县(区)编码
     */
    @ApiModelProperty(value = "单位县(区)编码")
    private String employerCountyCode;
    /**
     * 单位街道编码
     */
    @ApiModelProperty(value = "单位街道编码")
    private String employerStreetCode;
    /**
     * 单位详细地址
     */
    @ApiModelProperty(value = "单位详细地址")
    private String employerAddress;
    /**
     * 组织结构代码
     */
    @ApiModelProperty(value = "组织结构代码")
    private String orgCode;
    /**
     * 单位联系电话
     */
    @ApiModelProperty(value = "单位联系电话")
    private String employerPhone;
    /**
     * 单位法定代表人
     */
    @ApiModelProperty(value = "单位法定代表人")
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
     * 民族
     */
    @ApiModelProperty(value = "民族")
    private String nationCode;
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
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effDate;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expDate;
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
}
