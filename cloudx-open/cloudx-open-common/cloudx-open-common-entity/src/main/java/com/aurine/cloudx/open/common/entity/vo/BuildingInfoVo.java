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

/**
 * 楼栋信息Vo
 *
 * @author : Qiu
 * @date : 2021 12 16 17:16
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "楼栋信息Vo")
public class BuildingInfoVo extends OpenBaseVo {

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
     * 楼栋id
     */
    @JsonProperty("buildingId")
    @JSONField(name = "buildingId")
    @ApiModelProperty(value = "楼栋id")
    @Null(message = "楼栋id（buildingId）新增时需要为空", groups = {InsertGroup.class})
    @NotBlank(message = "楼栋id（buildingId）不能为空", groups = {UpdateGroup.class})
    @Size(max = 32, message = "楼栋id（buildingId）长度不能超过32")
    private String buildingId;

    /**
     * 楼栋编码
     */
    @JsonProperty("buildingCode")
    @JSONField(name = "buildingCode")
    @ApiModelProperty(value = "楼栋编码")
    @Size(max = 64, message = "楼栋编码（buildingCode）长度不能超过64")
    private String buildingCode;

    /**
     * 框架编码
     */
    @JsonProperty("frameNo")
    @JSONField(name = "frameNo")
    @ApiModelProperty(value = "框架编码")
    @Size(max = 20, message = "楼栋编码（buildingCode）长度不能超过20")
    private String frameNo;

    /**
     * 楼栋名称
     */
    @JsonProperty("buildingName")
    @JSONField(name = "buildingName")
    @ApiModelProperty(value = "楼栋名称")
    @NotBlank(message = "楼栋名称（buildingName）不能为空", groups = {InsertGroup.class})
    @SizeCustom(message = "楼栋名称（buildingName）长度不能小于1")
    @Size(max = 32, message = "楼栋名称（buildingName）长度不能超过32")
    private String buildingName;

    /**
     * 楼层数
     */
    @JsonProperty("floorTotal")
    @JSONField(name = "floorTotal")
    @ApiModelProperty(value = "楼层数")
    @Max(value = Integer.MAX_VALUE, message = "楼层数（floorTotal）数值过大")
    private Integer floorTotal;

    /**
     * 户数
     */
    @JsonProperty("houseTotal")
    @JSONField(name = "houseTotal")
    @ApiModelProperty(value = "户数")
    @Max(value = Integer.MAX_VALUE, message = "户数（houseTotal）数值过大")
    private Integer houseTotal;

    /**
     * 单元数
     */
    @JsonProperty("unitTotal")
    @JSONField(name = "unitTotal")
    @ApiModelProperty(value = "单元数")
    @Max(value = Integer.MAX_VALUE, message = "单元数（unitTotal）数值过大")
    private Integer unitTotal;

    /**
     * 单元名称类型 1 数字 2 字母
     */
    @JsonProperty("unitNameType")
    @JSONField(name = "unitNameType")
    @ApiModelProperty(value = "单元名称类型 1 数字 2 字母")
    @Size(max = 5, message = "单元名称类型（unitNameType）长度不能超过5")
    private String unitNameType;

    /**
     * 楼栋面积
     */
    @JsonProperty("buildingArea")
    @JSONField(name = "buildingArea")
    @ApiModelProperty(value = "楼栋面积")
    @Digits(integer = 12, fraction = 2, message = "楼栋面积（buildingArea）格式不正确，整数最多12位，小数最多2位")
    @DecimalMin(value = "0.00", message = "楼栋面积（buildingArea）格式不正确，不能小于0")
    private Double buildingArea;

    /**
     * 楼栋年代,YYYY
     */
    @JsonProperty("buildingEra")
    @JSONField(name = "buildingEra")
    @ApiModelProperty(value = "楼栋年代,YYYY")
    @Size(max = 4, message = "楼栋年代（buildingEra）长度不能超过4")
    private String buildingEra;

    /**
     * 楼栋类别
     */
    @JsonProperty("buildingType")
    @JSONField(name = "buildingType")
    @ApiModelProperty(value = "楼栋类别")
    @Size(max = 5, message = "楼栋类别（buildingType）长度不能超过5")
    private String buildingType;

    /**
     * 组团4
     */
    @JsonProperty("group4")
    @JSONField(name = "group4")
    @ApiModelProperty(value = "组团4")
    @Size(max = 32, message = "组团4（group4）长度不能超过32")
    private String group4;

    /**
     * 组团5
     */
    @JsonProperty("group5")
    @JSONField(name = "group5")
    @ApiModelProperty(value = "组团5")
    @Size(max = 32, message = "组团5（group5）长度不能超过32")
    private String group5;

    /**
     * 组团6
     */
    @JsonProperty("group6")
    @JSONField(name = "group6")
    @ApiModelProperty(value = "组团6")
    @Size(max = 32, message = "组团6（group6）长度不能超过32")
    private String group6;

    /**
     * 组团7
     */
    @JsonProperty("group7")
    @JSONField(name = "group7")
    @ApiModelProperty(value = "组团7")
    @Size(max = 32, message = "组团7（group7）长度不能超过32")
    private String group7;

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
    @Size(max = 5, message = "坐标系代码（gisType）长度不能超过5")
    private String gisType;

    /**
     * 区域id
     */
    @JsonProperty("regionId")
    @JSONField(name = "regionId")
    @ApiModelProperty(value = "区域id")
    @Size(max = 32, message = "区域id（regionId）长度不能超过32")
    private String regionId;

    /**
     * 员工id
     */
    @JsonProperty("staffId")
    @JSONField(name = "staffId")
    @ApiModelProperty(value = "员工id")
    @Size(max = 32, message = "员工id（staffId）长度不能超过32")
    private String staffId;

    /**
     * 省
     */
    @JsonProperty("provinceCode")
    @JSONField(name = "provinceCode")
    @ApiModelProperty(value = "省")
    @Size(max = 32, message = "省（provinceCode）长度不能超过32")
    private String provinceCode;

    /**
     * 市
     */
    @JsonProperty("cityCode")
    @JSONField(name = "cityCode")
    @ApiModelProperty(value = "市")
    @Size(max = 32, message = "市（cityCode）长度不能超过32")
    private String cityCode;

    /**
     * 县
     */
    @JsonProperty("countyCode")
    @JSONField(name = "countyCode")
    @ApiModelProperty(value = "县")
    @Size(max = 32, message = "县（countyCode）长度不能超过32")
    private String countyCode;

    /**
     * 街道
     */
    @JsonProperty("streetCode")
    @JSONField(name = "streetCode")
    @ApiModelProperty(value = "街道")
    @Size(max = 32, message = "街道（streetCode）长度不能超过32")
    private String streetCode;

    /**
     * 地址编码
     */
    @JsonProperty("locationCode")
    @JSONField(name = "locationCode")
    @ApiModelProperty(value = "地址编码")
    @Size(max = 64, message = "地址编码（locationCode）长度不能超过64")
    private String locationCode;

    /**
     * 地址
     */
    @JsonProperty("address")
    @JSONField(name = "address")
    @ApiModelProperty(value = "地址")
    @Size(max = 255, message = "地址（address）长度不能超过255")
    private String address;

    /**
     * 地面建筑物层数
     */
    @JsonProperty("floorGround")
    @JSONField(name = "floorGround")
    @ApiModelProperty(value = "地面建筑物层数")
    @Max(value = Integer.MAX_VALUE, message = "地面建筑物层数（floorGround）数值过大")
    private Integer floorGround;

    /**
     * 地下建筑物层数
     */
    @JsonProperty("floorUnderground")
    @JSONField(name = "floorUnderground")
    @ApiModelProperty(value = "地下建筑物层数")
    @Max(value = Integer.MAX_VALUE, message = "地下建筑物层数（floorUnderground）数值过大")
    private Integer floorUnderground;

    /**
     * 地下居住物层数
     */
    @JsonProperty("liveUnderground")
    @JSONField(name = "liveUnderground")
    @ApiModelProperty(value = "地下居住物层数")
    @Max(value = Integer.MAX_VALUE, message = "地下居住物层数（liveUnderground）数值过大")
    private Integer liveUnderground;

    /**
     * 图片1
     */
    @JsonProperty("picUrl1")
    @JSONField(name = "picUrl1")
    @ApiModelProperty(value = "图片1")
    @Size(max = 128, message = "图片1（picUrl1）长度不能超过128")
    private String picUrl1;

    /**
     * 图片2
     */
    @JsonProperty("picUrl2")
    @JSONField(name = "picUrl2")
    @ApiModelProperty(value = "图片2")
    @Size(max = 128, message = "图片2（picUrl2）长度不能超过128")
    private String picUrl2;

    /**
     * 图片3
     */
    @JsonProperty("picUrl3")
    @JSONField(name = "picUrl3")
    @ApiModelProperty(value = "图片3")
    @Size(max = 128, message = "图片3（picUrl3）长度不能超过128")
    private String picUrl3;

    /**
     * 图片1Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic1Base64")
    @JSONField(name = "pic1Base64")
    @ApiModelProperty(value = "图片1Base64（自定义，非数据库字段）")
    private String pic1Base64;

    /**
     * 图片2Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic2Base64")
    @JSONField(name = "pic2Base64")
    @ApiModelProperty(value = "图片2Base64（自定义，非数据库字段）")
    private String pic2Base64;

    /**
     * 图片3Base64（自定义，非数据库字段）
     */
    @JsonProperty("pic3Base64")
    @JSONField(name = "pic3Base64")
    @ApiModelProperty(value = "图片3Base64（自定义，非数据库字段）")
    private String pic3Base64;
}

