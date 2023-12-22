package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 人员巡更巡点记录(ProjectPatrolPersonPoint)表实体类
 *
 * @author 王良俊
 * @since 2020-09-17 10:29:21
 */
@Data
@TableName("project_patrol_person_point")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员巡更巡点记录(ProjectPatrolPersonPoint)")
public class ProjectPatrolPersonPoint extends OpenBasePo<ProjectPatrolPersonPoint> {

    private static final long serialVersionUID = -15226605110343198L;


    /**
     * 主键，自增
     */
    @TableId
    @ApiModelProperty(value="主键，自增")
    private Integer seq;

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


}