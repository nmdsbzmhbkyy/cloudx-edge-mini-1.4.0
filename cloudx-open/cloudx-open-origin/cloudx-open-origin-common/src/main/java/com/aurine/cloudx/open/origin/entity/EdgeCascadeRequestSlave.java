package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>级联申请</p>
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@TableName("edge_cascade_request_slave")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "级联申请表")
public class EdgeCascadeRequestSlave extends OpenBasePo<EdgeCascadeRequestSlave> {

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
     * 项目ID
     */
    @ApiModelProperty("项目ID")
    private Integer projectId;
}
