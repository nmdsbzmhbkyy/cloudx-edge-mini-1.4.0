package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 车辆出入口信息表(ProjectVehiclesEntryExit)表实体类
 *
 * @author 王良俊
 * @since 2020-08-17 10:08:52
 */
@Data
@ApiModel(value = "车辆出入口信息表Vo对象")
public class ProjectVehiclesEntryExitVo  {

    /**
     * uuid
     */
    private String entryId;

    /**
     * 第三方编码
     */
    @ApiModelProperty(value = "第三方编码")
    private String entryCode;

    /**
     * 出入口名称
     */
    @ApiModelProperty(value = "出入口名称")
    private String entryName;

    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Double lon;

    /**
     * 纬度
     */
    @ApiModelProperty(value = "纬度")
    private Double lat;

    /**
     * 坐标，面、多点、线
     */
    @ApiModelProperty(value = "坐标，面、多点、线")
    private String gisArea;

    /**
     * 坐标系代码
     */
    @ApiModelProperty(value = "坐标系代码")
    private String gisType;

    @ApiModelProperty(value = "")
    private String entryType;

    /**
     * 使用状态 1 在用 0 禁用
     */
    @ApiModelProperty(value = "使用状态 1 在用 0 禁用")
    private String status;

    /**
     * 行进方向
     */
    @ApiModelProperty(value = "行进方向")
    private String direction;

    /**
     * 车道数
     */
    @ApiModelProperty(value = "车道数")
    private Integer laneNumber;

    /**
     * 停车场id
     */
    @ApiModelProperty(value = "停车场id")
    private String parkId;

    @ApiModelProperty(value = "")
    private String remark;

    /**
     * 图片路径
     */
    @ApiModelProperty(value = "图片路径")
    private String picUrl;

    /**
     * 楼层号
     */
    @ApiModelProperty(value = "楼层号")
    private String floor;

    /**
     * 车道ID列表
     * */
    @ApiModelProperty("车道ID列表")
    String[] laneIdArr;

    /**
     * 图片文件列表
     * */
    @ApiModelProperty("图片url对象集合")
    List<ProjectUnitFileVo> fileList;

    /**
     * 图片url数组
     * */
    @ApiModelProperty("图片url数组")
    String[] picUrlArr;
}
