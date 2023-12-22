package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPatrolDetail;
import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 项目巡更记录(ProjectPatrolInfo)表实体类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-09 08:48:43
 */
@Data
@ApiModel(value = "项目巡更记录")
public class ProjectPatrolInfoVo {
    /*
    巡更记录id
     */
    @ApiModelProperty(value = "巡更记录id")
    private String patrolId;
    /*
    巡更路线id
     */
    @ApiModelProperty(value = "巡更路线id")
    private String patrolRouteId;
    /*
    巡更路线名称
     */
    @ApiModelProperty(value = "巡更路线名称")
    private String patrolRouteName;
    /**
     * 排班计划ID
     */
    @ApiModelProperty(value = "排班计划ID")
    private String schedulePlanId;
    /**
     * 排班计划名称
     */
    @ApiModelProperty(value = "排班计划名称")
    private String planName;
    /*
    巡更状态 参考字典类型 patrol_status
     */
    @ApiModelProperty(value = "巡更状态   0: '待巡更' 1 '巡更中' 2: '已完成' 3: '已过期' 参考字典类型 patrol_status")
    private String status;
    /*
    巡更日期
     */
    @ApiModelProperty(value = "巡更日期")
    private LocalDate patrolDate;
    /*
    巡更时间段
     */
    @ApiModelProperty(value = "巡更时间段")
    private String patrolTime;
    /*
    巡更开始时间
     */
    @ApiModelProperty(value = "巡更开始时间")
    private LocalTime patrolStartTime;
    /*
    巡更结束时间
     */
    @ApiModelProperty(value = "巡更结束时间")
    private LocalTime patrolEndTime;
    /*
    巡更结果 1 正常 2 异常
     */
    @ApiModelProperty(value = "巡更结果 1 正常 2 异常")
    private String result;
    /*
    应巡数量
     */
    @ApiModelProperty(value = "应巡数量")
    private Integer shouldPatrolled;
    /*
    应巡数量
     */
    @ApiModelProperty(value = "未巡数量")
    private Integer notPatrolled;
    /*
    已巡数量
     */
    @ApiModelProperty(value = "已巡数量")
    private Integer alreadyPatrolled;
    /**
     * 签到方式，字典check_in_type
     */
    @ApiModelProperty(value = "签到方式， 1.二维码 2.现场拍照 字典check_in_type")
    private String checkInType;

    /**
     * 分配方式
     */
    @ApiModelProperty(value = "分配方式")
    private String assignType;
    /*
    签到情况 1 正常 2 异常
     */
    @ApiModelProperty(value = "签到情况 0 超时 1 正常 2 异常")
    private String checkInStatus;
    /**
     * 巡更点信息
     */
    @ApiModelProperty(value = "巡更点信息")
    private List<ProjectPatrolDetail> pointList;
    /**
     * 巡更人员信息
     */
    @ApiModelProperty(value = "巡更人员信息")
    private List<ProjectPatrolPerson> personList;


    /**
     * 巡更人名称列表
     */
    @ApiModelProperty(value = "巡更人名称列表")
    private String personListName;
    /**
     * 巡更人id列表
     */
    @ApiModelProperty(value = "巡更人id列表")
    private String personIds;


    /**
     * 计划巡更人名称列表
     */
    @ApiModelProperty(value = "巡更人名称列表")
    private String planPersonListName;
    /**
     * 计划巡更人id列表
     */
    @ApiModelProperty(value = "巡更人id列表")
    private String planPersonIds;

    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间")
    private String doneTime;
}