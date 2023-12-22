package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目巡更路线设置(ProjectPatrolRouteConf)表Vo类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-24 12:00:06
 */
@Data
@ApiModel(value = "项目巡更路线设置")
public class ProjectPatrolRouteConfVo {

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
     *巡更点数量
     */
    @ApiModelProperty(value="巡更点数量")
    private String patrolRouteNum;
    /**
     *排班计划id，关联排班计划
     */
    @ApiModelProperty(value="排班计划id，关联排班计划")
    private String schedulePlanId;
    /**
     *排班计划id，关联排班计划
     */
    @ApiModelProperty(value="排班计划名称")
    private String planName;
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
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private List<String> regularTimeList;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String monTime;
    /**
     *周一 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周一 自定义时间区间存储格式")
    private List<String> mon;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String tueTime;
    /**
     *周二 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周二 自定义时间区间存储格式")
    private List<String> tue;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String wedTime;
    /**
     *周三 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周三 自定义时间区间存储格式")
    private List<String> wed;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String thuTime;
    /**
     *周四 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周四 自定义时间区间存储格式")
    private List<String> thu;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String friTime;
    /**
     *周五 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周五 自定义时间区间存储格式")
    private List<String> fri;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String satTime;
    /**
     *周六 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周六 自定义时间区间存储格式")
    private List<String> sat;
    /**
     *常规时间配置 自定义时间区间存储格式
     */
    @ApiModelProperty(value="常规时间配置 自定义时间区间存储格式")
    private String sunTime;
    /**
     *周日 自定义时间区间存储格式
     */
    @ApiModelProperty(value="周日 自定义时间区间存储格式")
    private List<String> sun;
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
     *巡更路线点关联配置
     */
    @ApiModelProperty(value="巡更路线点关联配置")
    private List<ProjectPatrolRoutePointConfVo> routePointList;
    /**
     *巡更点信息
     */
    @ApiModelProperty(value="巡更点信息")
    private List<ProjectPatrolPointConfVo> pointList;
    /**
     *状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value="状态 0 已停用 1 已启用")
    private String status;
    /**
     *查询开始时间
     */
    @ApiModelProperty(value="查询开始时间")
    private LocalDateTime effTime;
    /**
     *查询结束时间
     */
    @ApiModelProperty(value="查询结束时间")
    private LocalDateTime expTime;
    /**
     *创建人Id
     */
    @ApiModelProperty(value="创建人Id")
    private String operator;
    /**
     *创建人
     */
    @ApiModelProperty(value="创建人名称")
    private String operatorName;
    /**
     *创建时间
     */
    @ApiModelProperty(value="创建时间")
    private String RouteCreateTime;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     * 巡更参与人
     */
    @ApiModelProperty("巡更参与人数组")
    private String[] patrolRoutePersonArray;

    /**
     * 巡更参与人姓名
     */
    @ApiModelProperty("巡更参与人数组")
    private String[] patrolRoutePersonName;

    /**
     * 查询时间范围
     */
    @ApiModelProperty("查询时间范围")
    private String[] dateRange;
}