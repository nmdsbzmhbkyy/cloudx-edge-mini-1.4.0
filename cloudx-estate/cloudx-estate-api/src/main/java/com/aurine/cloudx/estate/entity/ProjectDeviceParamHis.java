package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 记录项目设备参数配置的历史记录(ProjectDeviceParamHis)表实体类
 *
 * @author makejava
 * @since 2020-12-23 09:31:48
 */
@Data
@TableName("project_device_param_his")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "记录项目设备参数配置的历史记录(ProjectDeviceParamHis)")
public class ProjectDeviceParamHis extends Model<ProjectDeviceParamHis> {

    private static final long serialVersionUID = 236532191175962768L;

    /**
     * 序列，自增
     */
    @TableId
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;


    /**
     * 设备id，关联project_device_info.deviceId
     */
    @ApiModelProperty(value = "设备id，关联project_device_info.deviceId")
    private String deviceId;


    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String SN;


    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id")
    private String productId;


    /**
     * 产品型号id
     */
    @ApiModelProperty(value = "产品型号id")
    private String modelId;


    /**
     * 配置时间
     */
    @ApiModelProperty(value = "配置时间")
    private LocalDateTime configTime;


    /**
     * 结果 1 成功 0 失败
     */
    @ApiModelProperty(value = "结果 1 成功 0 失败")
    private String result;


    /**
     * 执行结果 1 已重配 0 未重配
     */
    @ApiModelProperty(value = "执行结果 1 已重配 0 未重配")
    private String execResult;

    /**
     * 设备名称/别名
     */
    @ApiModelProperty(value = "设备名称/别名")
    private String deviceDesc;


    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;


    /**
     * 租户id
     */
    @ApiModelProperty(value = "租户id")
    private Integer tenant_id;


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