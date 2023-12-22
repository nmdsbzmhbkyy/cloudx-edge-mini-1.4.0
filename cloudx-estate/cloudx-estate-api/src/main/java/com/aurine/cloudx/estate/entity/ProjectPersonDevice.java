

package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * 人员设备权限
 *
 * @author pigx code generator
 * @date 2020-05-22 08:16:11
 */
@Data
@TableName("project_person_device")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "人员设备权限")
public class ProjectPersonDevice extends Model<ProjectPersonDevice> {
private static final long serialVersionUID = 1L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value="序列，自增")
    private Integer seq;
    /**
     * 人员类型 1 住户 2 员工 3 访客
     */
    @ApiModelProperty(value="人员类型 1 住户 2 员工 3 访客")
    private String personType;
    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value="人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String personId;
    /**
     * 通行方案id，为空则为自选权限
     */
    @ApiModelProperty(value="通行方案id，为空则为自选权限")
    private String planId;
    /**
     * 设备id
     */
    @ApiModelProperty(value="设备id")
    private String deviceId;

    /**
     * 是否有效
     */
    @ApiModelProperty(value="状态  是否启用 1 启用 0 禁用")
    private String isActive;

    /**
     * 状态 1 正常 2 失效
     */
    @ApiModelProperty(value="状态 1 正常 2 失效")
    private String status;
    /**
     * 生效时间
     */
    @ApiModelProperty(value="生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value="失效时间")
    private LocalDateTime expTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value="操作时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    }
