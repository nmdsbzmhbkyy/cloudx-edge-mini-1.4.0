

package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 项目
 *
 * @author xull@aurine.cn
 * @date 2020-05-06 19:14:05
 */
@Data
@TableName("project_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目")
public class ProjectInfo extends OpenBasePo<ProjectInfo> {
    private static final long serialVersionUID = 1L;

    private Integer seq;

    /**
     * 项目ID,关联pigxx.sys_dept.dept_id
     */
    @ApiModelProperty(value = "项目ID,关联pigxx.sys_dept.dept_id")
    @TableId(type = IdType.INPUT)
    private Integer projectId;
    /**
     * 项目UUID
     */
    private String projectUUID;
    /**
     * 第三方项目UUID，如果是入云要取这个字段的数据作为项目UUID
     */
    private String projectCode;
    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 项目简称
     */
    @ApiModelProperty(value = "项目简称")
    private String shortName;
    /**
     * 项目类型 1 正式 2 测试 3 演示
     */
    @ApiModelProperty(value = "项目类型 1 正式 2 测试 3 演示")
    private String projectType;
    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactPerson;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactPhone;
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
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private String streetCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String address;
    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积")
    private BigDecimal acreage;
    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String picPath;
    /**
     * 经度，保留小数点后6位
     */
    @ApiModelProperty(value = "经度，保留小数点后6位")
    private Double lon;
    /**
     * 纬度，保留小数点后6位
     */
    @ApiModelProperty(value = "纬度，保留小数点后6位")
    private Double lat;
    /**
     * 高度，保留小数点后1位
     */
    @ApiModelProperty(value = "高度，保留小数点后1位")
    private Double alt;
    /**
     * 坐标
     */
    @ApiModelProperty(value = "坐标")
    private String gisArea;
    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;
    /**
     * 所属派出所
     */
    @ApiModelProperty(value = "所属派出所")
    private String policeStation;
    /**
     * 物业公司
     */
    @ApiModelProperty(value = "物业公司")
    private String propertyCompany;
    /**
     * 物业负责人
     */
    @ApiModelProperty(value = "物业负责人")
    private String propertyPrincipal;
    /**
     * 物业组织机构代码
     */
    @ApiModelProperty(value = "物业组织机构代码")
    private String propertyOrgCode;
    /**
     * 物业公司电话
     */
    @ApiModelProperty(value = "物业公司电话")
    private String propertyPhone;
    /**
     * 物业公司地址
     */
    @ApiModelProperty(value = "物业公司地址")
    private String propertyAddress;
    /**
     * 公安机关编码
     */
    @ApiModelProperty(value = "公安机关编码")
    private String policeCode;
    /**
     * 审核状态
     */
    @ApiModelProperty(value = "审核状态 1 待审核 2 已通过 9 未通过 字典wechat_audit_status")
    private String auditStatus;
    /**
     * 第三方项目编号
     */
    @ApiModelProperty(value = "第三方项目编号")
    private String thirdPartyNo;
    /**
     * 接口版本号
     */
    @ApiModelProperty(value = "接口版本号")
    private String appVersion;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 所属集团
     */
    @ApiModelProperty(value = "所属集团")
    private Integer companyId;

    /**
     * 所属项目组
     */
    @ApiModelProperty(value = "所属项目组")
    private Integer projectGroupId;

    /**
     * 过期时间
     */
    @ApiModelProperty(value = "过期时间")
    private LocalDateTime expTime;
    /**
     * 审核不通过原因
     */
    @ApiModelProperty(value = "审核不通过原因")
    private String auditReason;
    /**
     * 出入口数量
     */
    @ApiModelProperty("出入口数量")
    private Integer entraExitNum;
    /**
     * 是否启用
     */
    @ApiModelProperty("是否启用:0否1是")
    private String status;
    /**
     * 地址编码:公安标准编码
     */
    @ApiModelProperty("地址编码:公安标准编码")
    private String locationCode;
}
