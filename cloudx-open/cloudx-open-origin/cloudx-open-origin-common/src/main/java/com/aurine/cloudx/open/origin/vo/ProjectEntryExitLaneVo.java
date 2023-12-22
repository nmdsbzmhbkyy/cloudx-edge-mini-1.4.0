package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @ClassName ProjectEntryExitLaneVo
 * @Description 出入口信息
 * @Author linlx
 * @Date 2022/5/20 15:04
 **/
@Data
@ApiModel(value = "出入口信息")
public class ProjectEntryExitLaneVo {

    /**
     * 车道id
     */
    @ApiModelProperty(value = "车道id")
    private String laneId;

    @ApiModelProperty(value = "")
    private String laneCode;

    /**
     * 车道名称
     */
    @ApiModelProperty(value = "车道名称")
    private String laneName;

    /**
     * 关联project_vehicles_entry_exit.entryId
     */
    @ApiModelProperty(value = "关联project_vehicles_entry_exit.entryId")
    private String entryId;

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
     * 控制机机号
     */
    @ApiModelProperty(value = "控制机机号")
    private String machineNo;

    /**
     * 车辆道闸一体机ID
     */
    @ApiModelProperty(value = "车辆道闸一体机ID")
    private String deviceId;

    /**
     * 车辆道闸一体机设备名称
     */
    @ApiModelProperty(value = "车辆道闸一体机设备名称")
    private String deviceName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
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
     * 车场ID
     */
    @ApiModelProperty(value = "车场ID")
    private String parkId;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    private String videoUrl;

}
