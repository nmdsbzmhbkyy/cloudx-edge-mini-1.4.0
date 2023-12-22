package com.aurine.cloudx.open.origin.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 项目参数设置
 *
 * @author guhl.@aurine.cn
 * @date 2020-07-10 10:06:39
 */
@Data
@TableName("project_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "项目参数设置")
public class ProjectConfig extends OpenBasePo<ProjectConfig> {
    private static final long serialVersionUID = 1L;

    /**
     * 序列
     */
    @TableId
    @ApiModelProperty(value="序列")
    private Integer seq;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
    /**
     * 警情超限时间(分钟)
     */
    @ApiModelProperty(value = "警情超限时间(分钟)")
    private Integer alarmTimeLimit;
    /**
     * 增值服务初始状态 1 默认开 0 默认关
     */
    @ApiModelProperty(value = "增值服务初始状态 1 默认开 0 默认关")
    private String serviceInitalStatus;
    /**
     * 访客管理审核初始状态 1 默认开 0 默认关
     */
    @ApiModelProperty(value = "访客管理审核初始状态 1 系统审核 2 物业审核")
    private String visitorAudit;
    /**
     * 身份认证审核初始状态 1 默认开 0 默认关
     */
    @ApiModelProperty(value = "身份认证审核初始状态 1 系统审核 2 物业审核")
    private String authAudit;
    /**
     * 身份证采集器选择
     */
    @ApiModelProperty(value = "身份证采集器选择")
    private String cardCollector;
    /**
     * 楼层号长度
     */
    @ApiModelProperty(value = "楼层号长度")
    private Integer floorNoLen;
    /**
     * 一车多位 1 开 0 默认关
     */
    @ApiModelProperty(value = "一车多位 1开 0关")
    private String multiCarsPerPlace;

    @ApiModelProperty(value = "健康码开关  0 关闭 1 开启")
    private String healthCode;

    @ApiModelProperty(value = "自动催缴开关 0 关闭 1 开启")
    private String isAutoDC;

    @ApiModelProperty(value = "微信公众号催缴间隔（天）")
    private Integer wechatDCInterval;

    @ApiModelProperty(value = "app催缴间隔（天）")
    private Integer appDCInterval;

    @ApiModelProperty(value = "阿里接入设置")
    private String aliProjectCode;

    @ApiModelProperty(value = "模板id")
    private String dcTemplateId;

    @ApiModelProperty(value = "可接入监控数")
    private Integer totalMonitorDevNo;

    @ApiModelProperty(value = "操作人")
    @TableField(fill = FieldFill.INSERT)
    private Integer operator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "开放平台关联clientId")
    private String clientId;

    @ApiModelProperty(value = "开放平台关联clientSecret")
    private String clientSecret;

    @ApiModelProperty(value = "梯控功能项目加密key")
    private String liftEncryptKey;

    @ApiModelProperty(value = "是否自动延期 1 是 0 否")
    private String isAutoDelay;

}
