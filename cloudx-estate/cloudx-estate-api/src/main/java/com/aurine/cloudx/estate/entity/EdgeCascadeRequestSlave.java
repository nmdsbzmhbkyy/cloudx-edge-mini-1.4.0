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

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("edge_cascade_request_slave")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "级联申请表")
public class EdgeCascadeRequestSlave extends Model<EdgeCascadeRequestSlave> {

    /**
     * 序列
     */
    @ApiModelProperty("序列")
    private Integer seq;
    /**
     * 申请ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("申请ID")
    private String requestId;
    /**
     * 级联码
     */
    @ApiModelProperty("级联码")
    private String connectionCode;
    /**
     * 主边缘网关IP
     */
    @ApiModelProperty("主边缘网关IP")
    private String parentEdgeIp;
    /**
     * 级联状态
     */
    @ApiModelProperty("级联状态")
    private Character cascadeStatus;
    /**
     * 级联同步进度
     */
    @ApiModelProperty("级联同步进度")
    private BigDecimal cascadeSyncProcess;
    /**
     * 配置信息
     */
    @ApiModelProperty("配置信息")
    private String configJson;
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
    /**
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;
    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    @TableField("tenant_id")
    private Integer tenantId;
    /**
     * 操作人员
     */
    @ApiModelProperty("操作人员")
    private Integer operator;
}
