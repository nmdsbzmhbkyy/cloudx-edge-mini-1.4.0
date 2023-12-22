package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/4/15
 * @description： 楼栋
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "楼栋")
public class BuildingVo {
    /**
     * 长度50，名称,必填
     */
    @ApiModelProperty(value = "名称")
    private String name;
    /**
     * 长度50，编码，必填
     */
    @ApiModelProperty(value = "编码")
    private String number;
    /**
     * int，长度11，地上楼层数（正数，必填
     */
    @ApiModelProperty(value = "地上楼层数")
    private Integer overgroundCount;
    /**
     * int，长度11，地下楼层数（非负数），必填
     */
    @ApiModelProperty(value = "地下楼层数")
    private Integer undergroundCount;
    /**
     * 长度32，源ID，必填
     */
    @ApiModelProperty(value = "源ID")
    private String sourceID;
    /**
     * 长度32，来源系统，必填
     */
    @ApiModelProperty(value = "来源系统")
    private String sourceSystem;
    /**
     * 长度50，面积（非负数）
     */
    @ApiModelProperty(value = "面积")
    private String area;
    /**
     * 长度200，项目组团名称
     */
    @ApiModelProperty(value = "项目组团名称")
    private String projectGroupName;
    /**
     * 长度200，单元数（非负数
     */
    @ApiModelProperty(value = "单元数")
    private String unitCount;
    /**
     * 单元，当单元数大于0时，单元列表必须有值
     */
    @ApiModelProperty(value = "单元")
    private List<UnitVo> buildingUnitVoList;
    /**
     * 长度256，楼栋图
     */
    @ApiModelProperty(value = "楼栋图")
    private String imageUrl;
    /**
     * int，长度2，资源分类 1房源 2场地 3广告位 4车位
     */
    @ApiModelProperty(value = "资源分类 1房源 2场地 3广告位 4车位")
    private Integer catalog;
    /**
     * 长度64，位置
     */
    @ApiModelProperty(value = "位置")
    private String position;
    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;
}