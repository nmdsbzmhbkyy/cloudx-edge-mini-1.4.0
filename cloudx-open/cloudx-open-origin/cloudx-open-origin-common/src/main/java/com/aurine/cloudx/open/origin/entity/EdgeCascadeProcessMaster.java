package com.aurine.cloudx.open.origin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>边缘网关对接进度表</p>
 *
 * @author : 邹宇
 * @date : 2022-3-22 09:39:24
 */
@Data
@TableName("edge_cascade_process_master")
@ApiModel(value = "边缘网关对接进度表")
public class EdgeCascadeProcessMaster  {
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;

    /**
     * 事件ID
     */
    @ApiModelProperty("事件ID")
    @TableId(type = IdType.ASSIGN_UUID)
    private String eventId;

    /**
     * 执行业务
     */
    @ApiModelProperty("执行业务")
    private String serviceName;

    /**
     * 状态 0未完成 1已完成
     */
    @ApiModelProperty("状态 0未完成 1已完成")
    private Character status;

    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;

    /**
     * 同步ID
     */
    @ApiModelProperty("同步Id")
    private String syncId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
}
