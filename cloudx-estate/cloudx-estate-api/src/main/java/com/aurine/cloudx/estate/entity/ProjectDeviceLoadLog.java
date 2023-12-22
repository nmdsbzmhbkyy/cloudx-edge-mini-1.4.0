package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 设备导入日志(ProjectDeviceLoadLog)表实体类
 *
 * @author 王良俊
 * @since 2021-06-03 10:49:35
 */
@Data
@NoArgsConstructor
@TableName("project_device_load_log")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备导入日志(ProjectDeviceLoadLog)")
public class ProjectDeviceLoadLog extends Model<ProjectDeviceLoadLog> {

    private static final long serialVersionUID = -20698466222559515L;

    /**
     * 序列
     */
    @ApiModelProperty(value = "序列")
    private Integer seq;


    /**
     * 导入批次，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "导入批次，uuid")
    private String batchId;


    /**
     * 设备数量
     */
    @ApiModelProperty(value = "设备数量")
    private Integer devCount;


    /**
     * 导入时间
     */
    @ApiModelProperty(value = "导入时间")
    private LocalDateTime loadTime;


    /**
     * 原始文件地址url
     */
    @ApiModelProperty(value = "原始文件地址url")
    private String orginFile;


    /**
     * 临时文件地址url
     */
    @ApiModelProperty(value = "临时文件地址url")
    private String tempFile;

    /**
     * 所导入的设备类型
     */
    @ApiModelProperty(value = "所导入的设备类型")
    private String deviceType;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 租户ID
     */
    @ApiModelProperty("租户ID")
    @TableField("tenant_id")
    private Integer tenantId;


    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField(fill = FieldFill.INSERT)
    private String operator;


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


    public ProjectDeviceLoadLog(String batchId, String deviceType, LocalDateTime loadTime) {
        this.batchId = batchId;
        this.loadTime = loadTime;
        this.deviceType = deviceType;
    }
}
