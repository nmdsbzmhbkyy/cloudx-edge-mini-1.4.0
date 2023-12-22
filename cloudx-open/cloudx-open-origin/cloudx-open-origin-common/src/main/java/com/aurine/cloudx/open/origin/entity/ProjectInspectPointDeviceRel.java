package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备巡检点与设备关联表(ProjectInspectPointDeviceRel)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:26:37
 */
@Data
@TableName("project_inspect_point_device_rel")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检点与设备关联表(ProjectInspectPointDeviceRel)")
public class ProjectInspectPointDeviceRel extends OpenBasePo<ProjectInspectPointDeviceRel> {

    private static final long serialVersionUID = 272180068903922391L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Object seq;

    /**
     * 关系id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "关系id，uuid")
    private String relId;

    /**
     * 巡检点id，uuid
     */
    @ApiModelProperty(value = "巡检点id，uuid")
    private String pointId;

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    
    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String deviceSn;
    
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;

}