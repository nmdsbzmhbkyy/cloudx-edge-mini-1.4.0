package com.aurine.cloudx.estate.entity;

import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备异常记录表，记录设备最新的异常信息，不记录流水(ProjectDeviceAbnormal)表实体类
 *
 * @author 王良俊
 * @since 2021-09-26 13:43:07
 */
@Data
@TableName("project_device_abnormal")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备异常记录表，记录设备最新的异常信息，不记录流水(ProjectDeviceAbnormal)")
public class ProjectDeviceAbnormal extends Model<ProjectDeviceAbnormal> {

    private static final long serialVersionUID = -64447287505233201L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value = "序列")
    private Integer seq;


    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;


    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号")
    private String deviceCode;


    /**
     * 已注册设备编号
     */
    @ApiModelProperty(value = "已注册设备编号")
    private String deviceCodeReg;


    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String deviceType;


    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;


    /**
     * 1 在线 2 离线 3 故障 4 未激活 9 未知
     */
    @ApiModelProperty(value = "1 在线 2 离线 3 故障 4 未激活 9 未知")
    private String dStatus;


    /**
     * 接入方式  1 自动 2 手动 9 未定义
     */
    @ApiModelProperty(value = "接入方式  1 自动 2 手动 9 未定义")
    private String accessMethod;


    /**
     * 字典device_abnormal_code, 多个用逗号分隔
     */
    @ApiModelProperty(value = "字典device_abnormal_code, 多个用逗号分隔")
    private String abnormalCode;


    /**
     * 异常原因描述
     */
    @ApiModelProperty(value = "异常原因描述")
    private String abnormalDesc;


    /**
     * 异常处理方案
     */
    @ApiModelProperty(value = "异常处理方案/处理建议")
    private String advice;


    /**
     * 设备SN
     */
    @ApiModelProperty(value = "设备SN")
    private String sn;


    /**
     * 设备MAC
     */
    @ApiModelProperty(value = "设备MAC")
    private String mac;


    /**
     * ipv4地址
     */
    @ApiModelProperty(value = "ipv4地址")
    private String ipv4;


    /**
     * 设备添加时间
     */
    @ApiModelProperty(value = "设备添加时间")
    private LocalDateTime devAddTime;

    /**
     * 设备第三方编码
     * */
    @ApiModelProperty(value = "设备第三方编码")
    private String thirdpartyCode;

    /**
     * 项目id
     */
    /*@ApiModelProperty(value = "项目id")
    private Integer projectId;*/


    /*@TableField("tenant_id")
    @ApiModelProperty(value = "租户ID")
    private Integer tenantId;*/


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
