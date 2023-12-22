package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)实体类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-28 08:52:35
 */
@Data
@TableName("project_patrol_point_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目巡更点设置")
public class ProjectPatrolPointConf extends OpenBasePo<ProjectPatrolPointConf> {

    /**
     * 巡更点id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value="巡更点id")
    private String pointId;
    /**
     * 巡更点位置
     */
    @ApiModelProperty(value="巡更点位置")
    private String pointName;
    /**
     * 设备编号
     */
    @ApiModelProperty(value="设备编号")
    private String deviceId;
    /**
     * 注意事项
     */
    @ApiModelProperty(value="注意事项")
    private String remark;
    /**
     * 状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value="状态 0 已停用 1 已启用")
    private String status;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

}