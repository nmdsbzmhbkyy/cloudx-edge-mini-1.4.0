package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 车辆登记记录表
 *
 */
@Data
@TableName("project_car_pre_register")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车辆登记记录表")
public class ProjectCarPreRegister extends Model<ProjectCarPreRegister> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;
    /**
     * 预登记id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "预登记id")
    private String preRegId;

    /**
     * 车牌号
     */
    @ApiModelProperty(value = "车牌号")
    private String plateNumber;
    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;

    /**
     * 提交时间
     */
    @ApiModelProperty(value = "提交时间")
    private LocalDateTime commitTime;
    /**
     * 审核状态 1 认证中 2 已登记 9 已拒绝
     */
    @ApiModelProperty(value = "审核状态 1 认证中 2 已登记 9 已拒绝")
    private String auditStatus;
    /**
     * 审核意见
     */
    @ApiModelProperty(value = "审核意见")
    private String auditRemark;

    /**
     * 审核人(userId)
     */
    @ApiModelProperty(value = "审核人(userId)")
    private Integer auditor;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
