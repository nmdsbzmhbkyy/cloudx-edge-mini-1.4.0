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
 * 周界报警防区信息表
 *
 * @author 邹宇
 * @date 2021-6-15 14:45:18
 */
@Data
@TableName("project_perimeter_alarm_area")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "周界报警防区信息表")
public class ProjectPerimeterAlarmArea extends Model<ProjectPerimeterAlarmArea> {

    private static final long serialVersionUID = -8627654375966425732L;

    /**
     * 自增序列
     */
    @TableId
    @ApiModelProperty(value = "自增序列")
    private Integer seq;

    /**
     * 信息uuid
     */
    @ApiModelProperty(value = "信息uuid")
    private String infoUid;

    /**
     * 报警主机设备id
     */
    @ApiModelProperty(value = "报警主机设备id")
    private String deviceId;

    /**
     * 报警模块设备号
     */
    @ApiModelProperty(value = "报警模块设备号")
    private String moduleNo;


    /**
     * 防区号
     */
    @ApiModelProperty(value = "防区号")
    private String channelNo;

    /**
     * 防区名称
     */
    @ApiModelProperty(value = "防区名称")
    private String channelName;

    /**
     * 防区类型 1 周界 9 其他
     */
    @ApiModelProperty(value = "防区类型")
    private String channelType;

    /**
     * 布防状态 0 撤防 1 布防 2 旁路
     */
    @ApiModelProperty(value = "布防状态")
    private String armedStatus;


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
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "区域id")
    private String deviceRegionId;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "trsp地址")
    private String rtspUrl;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     * 区域id
     */
    @ApiModelProperty(value = "密码")
    private String password;


}
