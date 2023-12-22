package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Classname ProjectParkCarTypeVo
 * @Description 车辆类型
 * @Date 2022/5/10 14:28
 * @Created by linlx
 */
@Data
@ApiModel(value = "车辆类型")
public class ProjectParkCarTypeVo {

    @ApiModelProperty(value = "类型ID")
    private String typeId;

    /**
     * 车辆类型 0 免费车 1月租车
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
     * 是否默认 0 否 1 是
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
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
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
