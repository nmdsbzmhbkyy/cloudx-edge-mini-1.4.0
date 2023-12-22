

package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p> 楼栋批量增加VO </p>
 * @ClassName: ProjectBuildingInfoBatchVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/6/10 10:47
 * @Copyright:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "楼栋")
public class ProjectBuildingBatchBuildingVo extends Model<ProjectBuildingBatchBuildingVo> {
    private static final long serialVersionUID = 1L;


    /**
     * 单元列表
     */
    @ApiModelProperty(value = "单元列表")
    private List<ProjectBuildingBatchUnitVo> unitList;

    /**
     * 楼栋ID
     */
    @ApiModelProperty(value = "楼栋ID")
    private String buildingId;
    /**
     * 楼栋编码
     */
    @ApiModelProperty(value = "楼栋编码")
    private String buildingCode;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
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
     * 楼层数
     */
    @ApiModelProperty(value = "地面建筑物层数")
    private Integer floorGround;
    /**
     * 户数
     */
    @ApiModelProperty(value = "地下建筑物层数")
    private Integer floorUnderground;



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
    @ApiModelProperty(value="楼栋类别")
    private String buildingType;

    /**
     * 是否架空层
     */
    @ApiModelProperty(value = "是否架空层")
    private String hasStiltFloor;

    /**
     * 公共楼层 注：存储值为楼层编号列表的下标，从0开始
     */
    @ApiModelProperty(value = "公共楼层")
    private String publicFloors;

    /**
     * 楼层编号关系
     */
    @ApiModelProperty(value = "楼层编号关系")
    private String floorNumber;


    /**
     * 组团4
     */
    @ApiModelProperty(value="组团4")
    private String group4;
    /**
     * 组团5
     */
    @ApiModelProperty(value="组团5")
    private String group5;
    /**
     * 组团6
     */
    @ApiModelProperty(value="组团6")
    private String group6;
    /**
     * 组团7
     */
    @ApiModelProperty(value="组团7")
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
     * 地址编码
     */
    @ApiModelProperty(value = "地址编码")
    private String locationCode;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
}
