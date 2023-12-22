package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */
@Data
@TableName("project_card_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "卡操作记录")
public class ProjectCardHis extends OpenBasePo<ProjectCardHis> {
    private static final long serialVersionUID = 1L;

    /**
     * uid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uid")
    private String uid;

    /**
     * 人员id
     */
    @ApiModelProperty(value = "人员id")
    private String personId;

    /**
     * 卡号
     */
    @ApiModelProperty(value = "卡号")
    private String cardNo;


    /**
     * 状态 0成功 1进行中 2失败
     */
    @ApiModelProperty(value = "状态 0成功 1进行中 2失败")
    private String state;

    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量")
    private String deviceCount;

    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value = "人员类型 1 住户 2 员工 3 访客")
    private String personType;

    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    private String personName;


    /**
     * 操作类型 1 下发 2 挂失 3 解挂 4 注销
     */
    @ApiModelProperty(value = "操作类型 1 下发 2 挂失 3 解挂 4 注销")
    private String operationType;

    /*
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

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
