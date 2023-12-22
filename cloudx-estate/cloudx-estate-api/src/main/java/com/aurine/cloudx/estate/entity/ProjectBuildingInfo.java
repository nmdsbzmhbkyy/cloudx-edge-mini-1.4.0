

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 楼栋
 *
 * @author 王伟
 * @date 2020-05-07 16:52:22
 */
@Data
@TableName("project_building_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "楼栋")
public class ProjectBuildingInfo extends Model<ProjectBuildingInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * 楼栋ID
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;
    /**
     * 楼栋编码
     */
    @ApiModelProperty(value = "楼栋编码")
    private String buildingCode;

    /**
     * 框架编码
     */
    @ApiModelProperty(value = "框架编码")
    private String frameNo;

    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 楼层数
     */
    @ApiModelProperty(value = "楼层数")
    private Integer floorTotal;
    /**
     * 户数
     */
    @ApiModelProperty(value = "户数")
    private Integer houseTotal;
    /**
     * 单元数
     */
    @ApiModelProperty(value = "单元数")
    private Integer unitTotal;
    /**
     * 单元名称类型 1 数字 2 字母
     */
    @ApiModelProperty(value = "单元名称类型 1 数字 2 字母")
    private String unitNameType;
    /**
     * 楼栋面积
     */
    @ApiModelProperty(value = "楼栋面积")
    private BigDecimal buildingArea;
    /**
     * 楼栋年代,YYYY
     */
    @ApiModelProperty(value = "楼栋年代,YYYY")
    private String buildingEra;
    /**
     * 楼栋类别
     */
    @ApiModelProperty(value = "楼栋类别")
    private String buildingType;
    /**
     * 组团4
     */
    @ApiModelProperty(value = "组团4")
    private String group4;
    /**
     * 组团5
     */
    @ApiModelProperty(value = "组团5")
    private String group5;
    /**
     * 组团6
     */
    @ApiModelProperty(value = "组团6")
    private String group6;
    /**
     * 组团7
     */
    @ApiModelProperty(value = "组团7")
    private String group7;
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
     * 设备区域ID
     */
    @ApiModelProperty(value = "区域ID")
    private String regionId;

    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    private String staffId;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceCode;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityCode;
    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    private String countyCode;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private String streetCode;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址")
    private String address;

    /**
     * 地面建筑物层数
     */
    @ApiModelProperty(value = "地面建筑物层数")
    private Integer floorGround;

    /**
     * 地下建筑物层数
     */
    @ApiModelProperty(value = "地下建筑物层数")
    private Integer floorUnderground;

    /**
     * 地下居住物层数
     */
    @ApiModelProperty(value = "地下居住物层数")
    private Integer liveUnderground;

    /**
     * 地址编码
     */
    @ApiModelProperty(value = "地址编码")
    private String locationCode;

    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picUrl1;

    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picUrl2;

    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picUrl3;


    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间，东八区
     */
    @ApiModelProperty(value = "操作时间，东八区")
    private LocalDateTime createTime;
    /**
     * 更新时间，东八区
     */
    @ApiModelProperty(value = "更新时间，东八区")
    private LocalDateTime updateTime;
    /**
     * 是否架空层
     */
    @ApiModelProperty(value = "是否架空层")
    private String hasStiltFloor;
    /**
     * 公共楼层
     */
    @ApiModelProperty(value = "公共楼层")
    private String publicFloors;
    /**
     * 楼层编号关系
     */
    @ApiModelProperty(value = "楼层编号关系")
    private String floorNumber;
}
