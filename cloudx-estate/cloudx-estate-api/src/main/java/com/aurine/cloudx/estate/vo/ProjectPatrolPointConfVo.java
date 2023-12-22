package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目巡更点配置(ProjectPatrolPointConf)Vo类
 *
 * @author 黄阳光 huangyg@aurine.cn
 * @since 2020-07-28 08:52:35
 */
@Data
@ApiModel(value = "项目巡更点设置")
public class ProjectPatrolPointConfVo {

    /**
     * 巡更点id，uuid
     */
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
     * 顺序
     */
    @ApiModelProperty(value="顺序")
    private Integer sort;
    /**
     * 状态 0 已停用 1 已启用
     */
    @ApiModelProperty(value="状态 0 已停用 1 已启用")
    private String status;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人Id")
    private Integer operator;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人名称")
    private String operatorName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private String pointCreateTime;
    /**
     * 查询时间开始
     */
    @ApiModelProperty(value = "查询时间开始")
    private LocalDateTime effTime;
    /**
     * 查询时间结束
     */
    @ApiModelProperty(value = "查询时间结束")
    private LocalDateTime expTime;
    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private Integer projectId;

    /**
     *
     */
    @ApiModelProperty(value = "", hidden = true)
    @TableField(value = "tenant_id")
    private Integer tenantId;

    @ApiModelProperty("被N条路线使用")
    private Integer routeCouunt;

    @ApiModelProperty("设置时间范围")
    private String[] dateRange;

}