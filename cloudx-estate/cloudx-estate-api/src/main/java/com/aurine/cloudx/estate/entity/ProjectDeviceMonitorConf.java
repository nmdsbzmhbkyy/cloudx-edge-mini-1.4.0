package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 设备监控项配置(ProjectDeviceMonitorConf)表实体类
 *
 * @author 王良俊
 * @since 2020-07-23 18:30:45
 */
@Data
@TableName("project_device_monitor_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备监控项配置(ProjectDeviceMonitorConf)")
public class ProjectDeviceMonitorConf extends Model<ProjectDeviceMonitorConf> {

    private static final long serialVersionUID = -88281977933974642L;

    /**
     * 自增序列
     */
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 监控项id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "监控项id")
    private String monitorId;

    /**
     * 设备类型id
     */
    @ApiModelProperty(value = "设备类型id")
    private String deviceTypeId;


    /**
     * 监控项编码
     */
    @ApiModelProperty(value = "监控项编码")
    private String monitorCode;


    /**
     * 监控项名称
     */
    @ApiModelProperty(value = "监控项名称")
    private String monitorName;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String remark;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


}