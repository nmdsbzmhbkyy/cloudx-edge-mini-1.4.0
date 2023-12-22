package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectPatrolPerson;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: ProjectPatrolPersonVo
 * @author: 王良俊 <>
 * @date:  2020年09月17日 下午12:34:52
 * @Copyright:
*/
@Data
@ApiModel(value = "巡更人员分配表Vo")
public class ProjectPatrolPersonVo extends ProjectPatrolPerson {

    /**
     * 巡更状态 1 已巡 0 未巡
     */
    @ApiModelProperty(value = "巡更状态 1 已巡 0 未巡")
    private String partrolStatus;


    /**
     * 人员分配id
     */
    @ApiModelProperty(value = "人员分配id")
    private String patrolPersonId;


    /**
     * 巡更明细id
     */
    @ApiModelProperty(value = "巡更明细id")
    private String patrolDetailId;




    /**
     * 签到情况 0 超时 1 正常
     */
    @ApiModelProperty(value = "签到情况 0 超时 1 正常")
    private String checkInStatus;

    /**
     * 签到图片路径
     */
    @ApiModelProperty(value = "签到图片路径")
    private String checkInPic;

    /**
     * 巡更结果 1 正常 2 异常 0 未巡
     */
    @ApiModelProperty(value = "巡更结果 1 正常 2 异常 0 未巡")
    private String patrolResult;
    /**
     * 结果描述
     */
    @ApiModelProperty(value = "结果描述")
    private String resultDesc;
    /**
     * 巡更结果图片1
     */
    @ApiModelProperty(value = "巡更结果图片1")
    private String patrolResultPic1;

    /**
     * 巡更结果图片2
     */
    @ApiModelProperty(value = "巡更结果图片2")
    private String patrolResultPic2;
    /**
     * 巡更结果图片3
     */
    @ApiModelProperty(value = "巡更结果图片3")
    private String patrolResultPic3;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenant_id;


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