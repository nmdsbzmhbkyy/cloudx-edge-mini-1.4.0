package com.aurine.cloudx.open.common.entity.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurine.cloudx.open.common.entity.base.OpenBaseVo;
import com.aurine.cloudx.open.common.validate.annotation.SizeCustom;
import com.aurine.cloudx.open.common.validate.group.InsertGroup;
import com.aurine.cloudx.open.common.validate.group.UpdateGroup;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 房屋信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "房屋信息Vo")
public class HouseInfoVo extends OpenBaseVo {

    /**
     * 序列
     */
    @JsonProperty("seq")
    @JSONField(name = "seq")
    @ApiModelProperty(value = "序列", hidden = true)
    @Null(message = "序列（seq）需要为空")
    private Integer seq;

    /**
     * 项目id
     */
    @JsonProperty("projectId")
    @JSONField(name = "projectId")
    @ApiModelProperty(value = "项目id", required = true, position = -1)
    @NotNull(message = "项目id（projectId）不能为空", groups = {InsertGroup.class})
    @Max(value = Integer.MAX_VALUE, message = "项目id（projectId）数值过大")
    private Integer projectId;

    /**
     * 房屋编号
     */
    @JsonProperty("houseId")
    @JSONField(name = "houseId")
    @ApiModelProperty(value = "房屋编号")
    @Null(message = "房屋编号（houseId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "房屋编号（houseId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "房屋编号（houseId）长度不能超过32")
    private String houseId;

    /**
     * 房屋编码，可传入第三方编码
     */
    @JsonProperty("houseId")
    @JSONField(name = "houseId")
    @ApiModelProperty(value = "房屋编码，可传入第三方编码")
    @Size(max = 64, message = "房屋编码（houseCode）长度不能超过64")
    private String houseCode;

    /**
     * 单元编号
     */
    @JsonProperty("buildingUnit")
    @JSONField(name = "buildingUnit")
    @ApiModelProperty(value = "单元编号")
    @Size(max = 32, message = "单元编号（buildingUnit）长度不能超过32")
    private String buildingUnit;

    /**
     * 楼层
     */
    @JsonProperty("floor")
    @JSONField(name = "floor")
    @ApiModelProperty(value = "楼层")
    @Max(value = Integer.MAX_VALUE, message = "楼层（floor）数值过大")
    private Integer floor;

    /**
     * 户型id
     */
    @JsonProperty("houseDesginId")
    @JSONField(name = "houseDesginId")
    @ApiModelProperty(value = "户型id")
    @NotBlank(message = "户型id（houseDesginId）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "户型id（houseDesginId）长度不能小于1")
    @Size(max = 32, message = "户型id（houseDesginId）长度不能超过32")
    private String houseDesginId;

    /**
     * 房屋名称
     */
    @JsonProperty("houseName")
    @JSONField(name = "houseName")
    @ApiModelProperty(value = "房屋名称")
    @NotBlank(message = "房屋名称（houseName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "房屋名称（houseName）长度不能小于1")
    @Size(max = 64, message = "房屋名称（houseName）长度不能超过64")
    private String houseName;

    /**
     * 房屋类别编码
     */
    @JsonProperty("houseLabelCode")
    @JSONField(name = "houseLabelCode")
    @ApiModelProperty(value = "房屋类别编码")
    @Size(max = 5, message = "房屋类别编码（houseLabelCode）长度不能超过5")
    private String houseLabelCode;

    /**
     * 房屋类别
     */
    @JsonProperty("houseLabel")
    @JSONField(name = "houseLabel")
    @ApiModelProperty(value = "房屋类别")
    @Size(max = 64, message = "房屋类别（houseLabel）长度不能超过64")
    private String houseLabel;

    /**
     * 房屋用途编码
     */
    @JsonProperty("housePurposeCode")
    @JSONField(name = "housePurposeCode")
    @ApiModelProperty(value = "房屋用途编码")
    @Size(max = 5, message = "房屋用途编码（housePurposeCode）长度不能超过5")
    private String housePurposeCode;

    /**
     * 房屋用途
     */
    @JsonProperty("housePurpose")
    @JSONField(name = "housePurpose")
    @ApiModelProperty(value = "房屋用途")
    @Size(max = 64, message = "房屋用途（housePurpose）长度不能超过64")
    private String housePurpose;

    /**
     * 房屋面积
     */
    @JsonProperty("houseArea")
    @JSONField(name = "houseArea")
    @ApiModelProperty(value = "房屋面积")
    @Digits(integer = 10, fraction = 2, message = "房屋面积（houseArea）格式不正确，整数最多10位，小数最多2位")
    @DecimalMin(value = "0.00", message = "房屋面积（houseArea）格式不正确，不能小于0")
    private Double houseArea;

    /**
     * 最大居住/办公人数
     */
    @JsonProperty("personNumber")
    @JSONField(name = "personNumber")
    @ApiModelProperty(value = "最大居住/办公人数")
    @Max(value = Integer.MAX_VALUE, message = "最大居住/办公人数（personNumber）数值过大")
    private Integer personNumber;

    /**
     * 备注
     */
    @JsonProperty("note")
    @JSONField(name = "note")
    @ApiModelProperty(value = "备注")
    @Size(max = 128, message = "备注（note）长度不能超过128")
    private String note;

    /**
     * 朝向
     */
    @JsonProperty("orientation")
    @JSONField(name = "orientation")
    @ApiModelProperty(value = "朝向")
    @Size(max = 5, message = "朝向（orientation）长度不能超过5")
    private String orientation;

    /**
     * 经度
     */
    @JsonProperty("lon")
    @JSONField(name = "lon")
    @ApiModelProperty(value = "经度")
    @Digits(integer = 9, fraction = 6, message = "经度（lon）格式不正确，整数最多9位，小数最多6位")
    private Double lon;

    /**
     * 纬度
     */
    @JsonProperty("lat")
    @JSONField(name = "lat")
    @ApiModelProperty(value = "纬度")
    @Digits(integer = 9, fraction = 6, message = "纬度（lat）格式不正确，整数最多9位，小数最多6位")
    private Double lat;

    /**
     * 高度
     */
    @JsonProperty("alt")
    @JSONField(name = "alt")
    @ApiModelProperty(value = "高度")
    @Digits(integer = 6, fraction = 1, message = "高度（alt）格式不正确，整数最多6位，小数最多1位")
    private Double alt;

    /**
     * 坐标
     */
    @JsonProperty("gisArea")
    @JSONField(name = "gisArea")
    @ApiModelProperty(value = "坐标")
    @Size(max = 255, message = "坐标（gisArea）长度不能超过255")
    private String gisArea;

    /**
     * 坐标系代码
     */
    @JsonProperty("gisType")
    @JSONField(name = "gisType")
    @ApiModelProperty(value = "坐标系代码")
    @Size(max = 64, message = "坐标系代码（gisType）长度不能超过64")
    private String gisType;

    /**
     * 实有单位id
     */
    @JsonProperty("employerId")
    @JSONField(name = "employerId")
    @ApiModelProperty(value = "实有单位id")
    @Size(max = 32, message = "实有单位id（employerId）长度不能超过32")
    private String employerId;

    /**
     * 使用类型，1 自用 2 出租 3 闲置
     */
    @JsonProperty("usageType")
    @JSONField(name = "usageType")
    @ApiModelProperty(value = "使用类型，1 自用 2 出租 3 闲置")
    @Size(max = 5, message = "使用类型（usageType）长度不能超过5")
    private String usageType;

    /**
     * 房屋产权性质
     */
    @JsonProperty("propertyType")
    @JSONField(name = "propertyType")
    @ApiModelProperty(value = "房屋产权性质")
    @Size(max = 12, message = "房屋产权性质（propertyType）长度不能超过12")
    private String propertyType;

    /**
     * 地址编码
     */
    @JsonProperty("locationCode")
    @JSONField(name = "locationCode")
    @ApiModelProperty(value = "地址编码")
    @Size(max = 64, message = "地址编码（locationCode）长度不能超过64")
    private String locationCode;

    /**
     * 地址名称
     */
    @JsonProperty("locationName")
    @JSONField(name = "locationName")
    @ApiModelProperty(value = "地址名称")
    @Size(max = 64, message = "地址名称（locationName）长度不能超过64")
    private String locationName;

    /**
     * 增值服务到期时间
     */
    @JsonProperty("serviceExpTime")
    @JSONField(name = "serviceExpTime", format = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "增值服务到期时间")
    private LocalDateTime serviceExpTime;
}

