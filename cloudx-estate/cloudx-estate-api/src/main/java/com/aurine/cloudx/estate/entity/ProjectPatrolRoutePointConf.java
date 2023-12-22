package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 巡更路线点关联配置(ProjectPatrolRoutePointConf)表实体类
 *
 * @author 黄阳光
 * @since 2020-07-28 16:34:36
 */
@SuppressWarnings("serial")
@Data
@TableName("project_patrol_route_point_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡更路线点关联配置")
public class ProjectPatrolRoutePointConf extends Model<ProjectPatrolRoutePointConf> {

    /**
     *记录id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="记录id，uuid")
    private String recordId;
    /**
     *巡更路线id
     */
    @ApiModelProperty(value="巡更路线id")
    private String patrolRouteId;
    /**
     *巡更点id
     */
    @ApiModelProperty(value="巡更点id")
    private String patrolPointId;
    /**
     *间隔上次签到分钟
     */
    @ApiModelProperty(value="间隔上次签到分钟")
    private Integer gapTime;
    /**
     *顺序
     */
    @ApiModelProperty(value="顺序")
    private Integer sort;
    /**
     *创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

}