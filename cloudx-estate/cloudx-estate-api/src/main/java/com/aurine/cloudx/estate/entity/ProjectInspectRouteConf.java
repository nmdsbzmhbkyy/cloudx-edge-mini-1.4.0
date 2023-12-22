package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备巡检路线设置(ProjectInspectRouteConf)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:33:18
 */
@Data
@TableName("project_inspect_route_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备巡检路线设置(ProjectInspectRouteConf)")
public class ProjectInspectRouteConf extends Model<ProjectInspectRouteConf> {

    private static final long serialVersionUID = -42160999923825331L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 巡检路线id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "巡检路线id，uuid")
    private String inspectRouteId;

    /**
     * 巡检路线名称
     */
    @ApiModelProperty(value = "巡检路线名称")
    private String inspectRouteName;

    /**
     * 是否按顺序 1 是 0 否
     */
    @ApiModelProperty(value = "是否按顺序 1 是 0 否")
    private String isSort;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value = "状态 0 已停用 1 已启用")
    private char status;

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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}