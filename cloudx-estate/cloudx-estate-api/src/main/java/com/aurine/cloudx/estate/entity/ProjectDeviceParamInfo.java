package com.aurine.cloudx.estate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)表实体类
 *
 * @author makejava
 * @since 2020-12-15 10:24:15
 */
@Data
@TableName("project_device_param_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "设备参数信息表，存储设备的参数信息(ProjectDeviceParamInfo)")
public class ProjectDeviceParamInfo extends Model<ProjectDeviceParamInfo> {

    private static final long serialVersionUID = -21389244856811550L;

    /**
     * 序列，自增
     */
    @ApiModelProperty(value = "序列，自增")
    private Integer seq;

    /**
     * uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "uuid")
    private String uuid;


    /**
     * 设备id，关联project_device_info.deviceId
     */
    @ApiModelProperty(value = "设备id，关联project_device_info.deviceId")
    private String deviceId;

    /**
     * 服务ID
     */
    @ApiModelProperty(value = "服务ID")
    private String serviceId;


    /**
     * json字符串
     */
    @ApiModelProperty(value = "json字符串")
    private String deviceParam;


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