package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Classname ProjectParkCarType
 * @Description 车辆类型表
 * @Date 2022/5/10 14:06
 * @Created by linlx
 */
@Data
@TableName(value = "project_park_car_type", schema = "aurine_parking")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆类型表")
public class ProjectParkCarType extends OpenBasePo<ProjectParkCarType> {
    private static final long serialVersionUID = 1L;

    /**
     * 类型ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "类型ID")
    private String typeId;

    /**
     * 车辆类型 1 免费车 2 月租车
     */
    @ApiModelProperty(value = "车辆类型")
    private String carType;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "类型名称")
    private String typeName;

    /**
     * 是否禁用 0 启用 1 禁用
     */
    @ApiModelProperty(value = "是否禁用 0 启用 1 禁用")
    private String isDisable;

    /**
     * 是否默认
     */
    @ApiModelProperty(value = "是否默认 0 否 1 是")
    private Integer isDefault;

    /**
     * 车场ID
     */
    @ApiModelProperty(value = "车场ID")
    private String parkId;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 租户id
     */
    @TableField(value = "tenant_id")
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
