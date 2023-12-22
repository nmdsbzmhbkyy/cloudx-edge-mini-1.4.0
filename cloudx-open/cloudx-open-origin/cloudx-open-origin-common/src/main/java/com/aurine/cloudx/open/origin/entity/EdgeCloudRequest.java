package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 边缘网关入云申请
 *
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
@Data
@TableName("edge_cloud_request")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "边缘网关入云申请")
public class EdgeCloudRequest extends OpenBasePo<EdgeCloudRequest> {

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
     * 入云方式
     */
    @ApiModelProperty("入云方式")
    private Character syncType;

    /**
     * syncType选择2使用云端数据时启用，0未删除，1已删除，2删除中
     */
    @ApiModelProperty("syncType选择2使用云端数据时启用，0未删除，1已删除，2删除中")
    private Character delStatus;

    /**
     * 入云申请状态
     */
    @ApiModelProperty("入云申请状态 0 未入云 1 待审核 2  已拒绝 3 已入云 4 解绑中")
    private Character cloudStatus;
    /**
     * 入云码
     */
    @ApiModelProperty("入云码")
    private String connectionCode;
    /**
     * 入云同步进度
     */
    @ApiModelProperty("入云同步进度")
    private BigDecimal cloudSyncProcess;
    /**
     * 入云配置信息
     */
    @ApiModelProperty("入云配置信息")
    private String configJson;
    /**
     * 边缘网关项目UID（主和从都是一样的）
     */
    @ApiModelProperty("第三方项目ID")
    private String projectCode;
    /**
     * 云端项目ID
     */
    @ApiModelProperty("云端项目ID")
    private String cloudProjectUid;

    /**
     * 0 未同步 1 已同步
     */
    @ApiModelProperty("同步状态")
    private Character isSync;

    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private LocalDateTime requestTime;

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
     * tenantId
     */
    @ApiModelProperty("tenantId")
    @TableField("tenant_id")
    private Integer tenantId = 1;


    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private Integer operator;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
