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

import java.io.Serializable;
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
public class ProjectConfig extends Model<ProjectConfig> {
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
     * 一车多位 1 开 0 默认关
     */
    @ApiModelProperty(value = "一车多位 1开 0关")
    private String multiCarsPerPlace;

    @ApiModelProperty(value = "项目增值服务到期时间")
    private String serviceExpTime;

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
    /**
     * 楼层号长度
     */
    @ApiModelProperty(value = "楼层号长度")
    private Integer floorNoLen;
    /**
     * 开放平台关联clientId
     */
    @ApiModelProperty(value = "ID")
    private String clientId;
    /**
     * 开放平台关联clientSecret
     */
    @ApiModelProperty(value = "密钥")
    private String clientSecret;

    /**
     * 是否启用梯控功能
     */
    @ApiModelProperty(value = "否启用梯控功能")
    private String liftEnable;
    /**
     * 梯控功能项目加密key
     */
    //@ApiModelProperty(value = "梯控功能项目加密key")
    private String liftEncryptKey;

    /**
     * 是否自动延期 1 是 0 否
     */
    @ApiModelProperty(value = "是否自动延期 1 是 0 否")
    private String isAutoDelay;

    @ApiModelProperty(value = "二维码识别类型：0->冠林自有算法，1->表结构识别,2->透传识别")
    private Integer qrReconWay;

    @ApiModelProperty(value = "黑名单是否开门：0->关，1->开")
    private Integer blackOpen;
}
