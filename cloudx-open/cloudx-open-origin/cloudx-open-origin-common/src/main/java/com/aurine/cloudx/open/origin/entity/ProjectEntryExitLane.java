package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 出入口车道信息(ProjectEntryExitLane)表实体类
 *
 * @author 王良俊
 * @since 2020-08-17 11:58:42
 */
@Data
@TableName(value = "project_entry_exit_lane", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "出入口车道信息(ProjectEntryExitLane)")
public class ProjectEntryExitLane extends OpenBasePo<ProjectEntryExitLane> {

    private static final long serialVersionUID = 743899167372761125L;

    /**
     * 自增主键
     */
    @ApiModelProperty(value = "自增主键")
    private Integer seq;

    /**
     * 车道id
     */
    @TableId(type = IdType.ASSIGN_UUID)
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}