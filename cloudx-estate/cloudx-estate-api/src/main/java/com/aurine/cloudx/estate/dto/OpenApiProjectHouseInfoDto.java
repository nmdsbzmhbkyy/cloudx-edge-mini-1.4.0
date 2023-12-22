package com.aurine.cloudx.estate.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 开放平台内部项目房屋信息Dto
 *
 * @author : Qiu
 * @date : 2022/7/13 16:31
 */

@Data
@ApiModel(value = "开放平台内部项目房屋信息Dto")
public class OpenApiProjectHouseInfoDto {

    /**
     * 项目ID
     */
    @JSONField(name = "projectId")
    @JsonProperty(value = "projectId")
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 房屋ID
     */
    @JSONField(name = "houseId")
    @JsonProperty(value = "houseId")
    @ApiModelProperty(value = "房屋ID")
    private String houseId;

    /**
     * 房屋编码，可传入第三方编码
     */
    @JSONField(name = "houseCode")
    @JsonProperty(value = "houseCode")
    @ApiModelProperty(value = "房屋编码，可传入第三方编码")
    private String houseCode;

    /**
     * 单元ID
     */
    @JSONField(name = "buildingUnit")
    @JsonProperty(value = "buildingUnit")
    @ApiModelProperty(value = "单元ID")
    private String buildingUnit;

    /**
     * 楼层
     */
    @JSONField(name = "floor")
    @JsonProperty(value = "floor")
    @ApiModelProperty(value = "楼层")
    private Integer floor;

    /**
     * 户型ID
     */
    @JSONField(name = "houseDesignId")
    @JsonProperty(value = "houseDesignId")
    @ApiModelProperty(value = "户型ID")
    private String houseDesignId;

    /**
     * 房屋名称
     */
    @JSONField(name = "houseName")
    @JsonProperty(value = "houseName")
    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    /**
     * 房屋类别编码
     */
    @JSONField(name = "houseLabelCode")
    @JsonProperty(value = "houseLabelCode")
    @ApiModelProperty(value = "房屋类别编码")
    private String houseLabelCode;

    /**
     * 房屋类别
     */
    @JSONField(name = "houseLabel")
    @JsonProperty(value = "houseLabel")
    @ApiModelProperty(value = "房屋类别")
    private String houseLabel;

    /**
     * 房屋用途编码
     */
    @JSONField(name = "housePurposeCode")
    @JsonProperty(value = "housePurposeCode")
    @ApiModelProperty(value = "房屋用途编码")
    private String housePurposeCode;

    /**
     * 房屋用途
     */
    @JSONField(name = "housePurpose")
    @JsonProperty(value = "housePurpose")
    @ApiModelProperty(value = "房屋用途")
    private String housePurpose;

    /**
     * 房屋面积
     */
    @JSONField(name = "houseArea")
    @JsonProperty(value = "houseArea")
    @ApiModelProperty(value = "房屋面积")
    private BigDecimal houseArea;

    /**
     * 最大居住/办公人数
     */
    @JSONField(name = "personNumber")
    @JsonProperty(value = "personNumber")
    @ApiModelProperty(value = "最大居住/办公人数")
    private Integer personNumber;

    /**
     * 备注
     */
    @JSONField(name = "note")
    @JsonProperty(value = "note")
    @ApiModelProperty(value = "备注")
    private String note;

    /**
     * 朝向
     */
    @JSONField(name = "orientation")
    @JsonProperty(value = "orientation")
    @ApiModelProperty(value = "朝向")
    private String orientation;

    /**
     * 经度
     */
    @JSONField(name = "lon")
    @JsonProperty(value = "lon")
    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    /**
     * 纬度
     */
    @JSONField(name = "lat")
    @JsonProperty(value = "lat")
    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    /**
     * 高度
     */
    @JSONField(name = "alt")
    @JsonProperty(value = "alt")
    @ApiModelProperty(value = "高度")
    private BigDecimal alt;

    /**
     * 坐标
     */
    @JSONField(name = "gisArea")
    @JsonProperty(value = "gisArea")
    @ApiModelProperty(value = "坐标")
    private String gisArea;

    /**
     * 坐标系代码
     */
    @JSONField(name = "gisType")
    @JsonProperty(value = "gisType")
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;

    /**
     * 实有单位ID
     */
    @JSONField(name = "employerId")
    @JsonProperty(value = "employerId")
    @ApiModelProperty(value = "实有单位ID")
    private String employerId;

    /**
     * 房屋用途，1-自用；2-出租；3-闲置
     */
    @JSONField(name = "usageType")
    @JsonProperty(value = "usageType")
    @ApiModelProperty(value = "房屋用途，1-自用；2-出租；3-闲置")
    private String usageType;

    /**
     * 房屋产权性质
     */
    @JSONField(name = "propertyType")
    @JsonProperty(value = "propertyType")
    @ApiModelProperty(value = "房屋产权性质")
    private String propertyType;

    /**
     * 地址编码
     */
    @JSONField(name = "locationCode")
    @JsonProperty(value = "locationCode")
    @ApiModelProperty(value = "地址编码")
    private String locationCode;

    /**
     * 地址名称
     */
    @JSONField(name = "locationName")
    @JsonProperty(value = "locationName")
    @ApiModelProperty(value = "地址名称")
    private String locationName;

    /**
     * 增值服务到期时间
     */
    @JSONField(name = "serviceExpTime", format = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "serviceExpTime")
    @ApiModelProperty(value = "增值服务到期时间")
    private LocalDateTime serviceExpTime;

    /**
     * 楼栋ID
     */
    @JSONField(name = "buildingId")
    @JsonProperty(value = "buildingId")
    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;

    /**
     * 楼栋编码
     */
    @JSONField(name = "buildingCode")
    @JsonProperty(value = "buildingCode")
    @ApiModelProperty(value = "楼栋编码")
    private String buildingCode;

    /**
     * 单元编码
     */
    @JSONField(name = "unitCode")
    @JsonProperty(value = "unitCode")
    @ApiModelProperty(value = "单元编码")
    private String unitCode;

    /**
     * 户型描述
     */
    @JSONField(name = "designDesc")
    @JsonProperty(value = "designDesc")
    @ApiModelProperty(value = "户型描述")
    private String designDesc;

    /**
     * 面积
     */
    @JSONField(name = "area")
    @JsonProperty(value = "area")
    @ApiModelProperty(value = "面积")
    private BigDecimal area;

    /**
     * 单元ID（内部字段）
     */
    @JSONField(name = "unitId")
    @JsonProperty(value = "unitId")
    @ApiModelProperty(value = "单元ID", hidden = true)
    private String unitId;
}
