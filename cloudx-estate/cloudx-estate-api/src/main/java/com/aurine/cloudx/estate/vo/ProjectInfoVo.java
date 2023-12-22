package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectInfo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>比projectInfo少了一些信息加了集团名和组名</p>
 * @author : 王良俊
 * @date : 2021-12-28 15:24:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoVo {

    @ApiModelProperty(value = "项目名称")
    private String projectName;
    /**
     * 项目UUID（可能是级联的第三方项目ID看具体情况）
     */
    private String projectUUID;
    /**
     * 所属集团
     */
    @ApiModelProperty(value = "所属集团")
    private String companyName;
    /**
     * 所属项目组
     */
    @ApiModelProperty(value = "所属项目组")
    private String projectGroupName;

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
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
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
    private String entraExitNum;
}
