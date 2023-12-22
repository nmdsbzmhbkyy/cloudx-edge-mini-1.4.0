package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡更明细表(ProjectPatrolDetail)表实体类
 *
 * @author 黄阳光 <huangyg@aurine.cn>
 * @since 2020-09-11 11:51:51
 */
@Data
@TableName("project_patrol_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡更明细表")
public class ProjectPatrolDetail extends Model<ProjectPatrolDetail> {
    /**
     * 巡更明细id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "巡更明细id，uuid")
    private String patrolDetailId;
    /**
     * 巡更记录id
     */
    @ApiModelProperty(value = "巡更记录id")
    private String patrolId;
    /**
     * 巡更点id
     */
    @ApiModelProperty(value = "巡更点id")
    private String pointId;
    /**
     * 巡更点位置
     */
    @ApiModelProperty(value = "巡更点位置")
    private String pointName;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
}