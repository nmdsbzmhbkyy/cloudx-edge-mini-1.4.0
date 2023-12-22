
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
 * 房屋
 *
 * @author 王伟
 * @date 2020-05-08 16:29:18
 */
@Data
@TableName("project_house_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋")
public class ProjectHouseInfo extends OpenBasePo<ProjectHouseInfo> {
    private static final long serialVersionUID = 1L;


    /**
     * 房屋编号
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "房屋编号")
    private String houseId;
    /**
     * 房屋编码，可传入第三方编码
     */
    @ApiModelProperty(value = "房屋编码，可传入第三方编码")
    private String houseCode;

    /**
     * 单元编号
     */
    @ApiModelProperty(value = "单元编号")
    private String buildingUnit;
    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private Integer floor;
    /**
     * 户型ID
     */
    @ApiModelProperty(value = "户型ID")
    private String houseDesginId;
    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;
    /**
     * 房屋类别编码
     */
    @ApiModelProperty(value = "房屋类别编码")
    private String houseLabelCode;
    /**
     * 房屋类别
     */
    @ApiModelProperty(value = "房屋类别")
    private String houseLabel;
    /**
     * 房屋用途编码
     */
    @ApiModelProperty(value = "房屋用途编码")
    private String housePurposeCode;
    /**
     * 房屋用途
     */
    @ApiModelProperty(value = "房屋用途")
    private String housePurpose;
    /**
     * 房屋面积
     */
    @ApiModelProperty(value = "房屋面积")
    private BigDecimal houseArea;
    /**
     * 最大居住/办公人数
     */
    @ApiModelProperty(value = "最大居住/办公人数")
    private Integer personNumber;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 朝向
     */
    @ApiModelProperty(value = "朝向")
    private String orientation;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;
    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;
    /**
     * 高度
     */
    @ApiModelProperty(value = "高度")
    private BigDecimal alt;
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
     * 坐标系代码
     */
    @ApiModelProperty(value = "实有单位id")
    private String employerId;
    /**
     * 1 自用 2 出租 3 闲置
     */
    @ApiModelProperty(value = "1 自用 2 出租 3 闲置")
    private String usageType;
    /**
     * 房屋产权性质
     */
    @ApiModelProperty(value = "房屋产权性质")
    private String propertyType;
    /**
     * 地址编码
     */
    @ApiModelProperty(value = "地址编码")
    private String locationCode;
    /**
     * 地址名称
     */
    @ApiModelProperty(value = "地址名称")
    private String locationName;

    /**
     * 增值服务到期时间
     */
    @ApiModelProperty(value = "增值服务到期时间")
    private LocalDateTime serviceExpTime;
}
