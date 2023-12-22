package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>边缘网关对接进度表</p>
 *
 * @author : 邹宇
 * @date : 2022-3-22 09:39:24
 */
@Data
@TableName("edge_cascade_process_master")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘网关对接进度表")
public class EdgeCascadeProcessMaster extends Model<EdgeCascadeProcessMaster> {

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    /**
     * 事件ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "事件ID")
    private String eventId;

    /**
     * 同步id
     */
    @ApiModelProperty(value = "同步id")
    private String syncId;

    /**
     * 执行业务名称
     */
    @ApiModelProperty(value = "执行业务名称")
    private String serviceName;

    /**
     * 0未完成 1已完成
     */
    @ApiModelProperty(value = "0未完成 1已完成")
    private String status;

    /**
     * 预留字段
     */
    @ApiModelProperty(value = "预留字段")
    private String type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;



}
