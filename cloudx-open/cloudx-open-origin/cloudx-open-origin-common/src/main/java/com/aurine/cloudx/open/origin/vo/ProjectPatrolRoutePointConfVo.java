package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡更路线点关联配置(ProjectPatrolRoutePointConf)表实体类Vo
 *
 * @author makejava
 * @since 2020-07-28 16:34:36
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡更路线点关联配置")
public class ProjectPatrolRoutePointConfVo extends Model<ProjectPatrolRoutePointConfVo> {

    /**
     *记录id，uuid
     */
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
    @ApiModelProperty(value="后端保存巡更点id")
    private String patrolPointId;
    /**
     *巡更点id
     */
    @ApiModelProperty(value="前端接收巡更点id")
    private String pointId;
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
    private Integer operator;

}