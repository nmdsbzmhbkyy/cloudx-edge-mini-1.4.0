package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class ProjectInspectRouteConf extends OpenBasePo<ProjectInspectRouteConf> {

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

}