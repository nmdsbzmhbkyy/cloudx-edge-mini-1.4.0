package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@TableName("project_thirdparty_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘侧云端或级联项目信息表")
public class ProjectThirdpartyInfo extends Model<ProjectThirdpartyInfo> {

    private static final long serialVersionUID = -11374331561034291L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列")
    private Object seq;

    @ApiModelProperty(value = "入云申请ID")
    private String requestId;

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
    private String adminName;


    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String adminPhone;


    @ApiModelProperty(value = "")
    private String adminGender;


    @ApiModelProperty(value = "")
    private String adminIdNumber;


    @ApiModelProperty(value = "")
    private String adminPicPath;


    @ApiModelProperty(value = "")
    private String adminRoleName;


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
     * 地址编码，公安标准编码
     */
    @ApiModelProperty(value = "地址编码，公安标准编码")
    private String locationCode;


    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积")
    private Double acreage;


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
     * 出入口数量
     */
    @ApiModelProperty(value = "出入口数量")
    private Integer entraExitNum;


    /**
     * 项目ID,关联pgixx.sys_dept.dept_id
     */
    @ApiModelProperty(value = "项目ID,关联pgixx.sys_dept.dept_id")
    private Integer projectId;


    @TableField(value = "tenant_id")
    private Integer tenantId;


    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "操作人")
    private Integer operator;


    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
