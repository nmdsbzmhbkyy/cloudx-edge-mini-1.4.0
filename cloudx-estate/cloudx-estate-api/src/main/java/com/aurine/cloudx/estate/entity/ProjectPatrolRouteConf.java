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
 * 项目巡更路线设置(ProjectPatrolRouteConf)表实体类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-24 12:00:06
 */
@Data
@TableName("project_patrol_route_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目巡更路线设置")
public class ProjectPatrolRouteConf extends Model<ProjectPatrolRouteConf> {

    /**
     *巡更路线id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="巡更路线id")
    private String patrolRouteId;
    /**
     *巡更路线名称
     */
    @ApiModelProperty(value="巡更路线名称")
    private String patrolRouteName;
    /**
     *排班计划id，关联排班计划
     */
    @ApiModelProperty(value="排班计划id，关联排班计划")
    private String schedulePlanId;
    /**
     *提前通知分钟
     */
    @ApiModelProperty(value="提前通知分钟")
    private Integer timeAhead;
    /**
     *分配方式，字典assign_type
     */
    @ApiModelProperty(value="分配方式，字典assign_type")
    private String assignType;
    /**
     *签到方式，字典check_in_type
     */
    @ApiModelProperty(value="签到方式，1.二维码 2.现场拍照 字典check_in_type")
    private String checkInType;
    /**
     *巡更人数
     */
    @ApiModelProperty(value="巡更人数")
    private Integer personNumber;
    /**
     *巡更时长分钟
     */
    @ApiModelProperty(value="巡更时长分钟")
    private Integer patrolDurationLimit;
    /**
     *巡更时间类型 1 每天都相同 2 按周配置
     */
    @ApiModelProperty(value="巡更时间类型 1 每天都相同 2 按周配置")
    private String patrolTimeType;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String regularTime;
    /**
     *周一 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周一 自定义时间区间存储格式")
    private String mon;
    /**
     *周二 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周二 自定义时间区间存储格式")
    private String tue;
    /**
     *周三 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周三 自定义时间区间存储格式")
    private String wed;
    /**
     *周四 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周四 自定义时间区间存储格式")
    private String thu;
    /**
     *周五 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周五 自定义时间区间存储格式")
    private String fri;
    /**
     *周六 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周六 自定义时间区间存储格式")
    private String sat;
    /**
     *周日 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周日 自定义时间区间存储格式")
    private String sun;
    /**
     *是否按顺序巡更 1 是 0 否
     */
    @ApiModelProperty(value="是否按顺序巡更 1 是 0 否")
    private String isSort;
    /**
     *允许签到误差分钟
     */
    @ApiModelProperty(value="允许签到误差分钟")
    private Integer allowGap;
    /**
     *状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value="状态 0 已停用 1 已启用")
    private String status;
    /**
     *创建人
     */
    @ApiModelProperty(value="创建人")
    @TableField(fill = FieldFill.INSERT)
    private String operator;
}