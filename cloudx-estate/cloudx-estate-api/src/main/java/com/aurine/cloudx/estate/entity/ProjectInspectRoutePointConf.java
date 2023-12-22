package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备巡检路线与巡更点关系表(ProjectInspectRoutePointConf)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:32:49
 */
@Data
@TableName("project_inspect_route_point_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检路线与巡更点关系表(ProjectInspectRoutePointConf)")
public class ProjectInspectRoutePointConf extends Model<ProjectInspectRoutePointConf> {

    private static final long serialVersionUID = -96836712772091951L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 记录id,uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "记录id,uuid")
    private String recordId;

    /**
     * 巡检路线id，uuid
     */
    @ApiModelProperty(value = "巡检路线id，uuid")
    private String inspectRouteId;

    /**
     * 巡检点id
     */
    @ApiModelProperty(value = "巡检点id")
    private String inspectPointId;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序")
    private Integer sort;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
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

}