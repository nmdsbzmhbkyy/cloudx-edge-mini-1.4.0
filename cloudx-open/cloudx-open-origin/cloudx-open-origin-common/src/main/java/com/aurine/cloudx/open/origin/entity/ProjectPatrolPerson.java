package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡更人员分配表(ProjectPatrolPerson)表实体类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 16:01:11
 */
@Data
@TableName("project_patrol_person")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡更人员分配表")
public class  ProjectPatrolPerson extends OpenBasePo<ProjectPatrolPerson> {

    /*
    * 人员分配ID
    * */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="人员分配ID")
    private String patrolPersonId;

    /*
    巡更记录id
     */
    @ApiModelProperty(value="巡更记录id")
    private String patrolId;
    /*
    人员id
     */
    @ApiModelProperty(value="人员id")
    private String staffId;
    /*
    姓名
     */
    @ApiModelProperty(value="姓名")
    private String staffName;
    /**
    类型 类型 1 计划人员 2 执行人员
     */
    @ApiModelProperty(value="类型 1 计划人员 2 执行人员")
    private String staffType;
//    /*
//    项目id
//     */
//    @ApiModelProperty(value="项目id")
//    private Integer projectId;
}