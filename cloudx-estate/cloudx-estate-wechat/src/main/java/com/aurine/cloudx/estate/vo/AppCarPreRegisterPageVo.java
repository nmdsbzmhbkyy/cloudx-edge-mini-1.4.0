package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆登记记录表
 *
 */
@Data
@ApiModel(value = "车辆列表")
public class AppCarPreRegisterPageVo {
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
}
