package com.aurine.cloudx.open.common.entity.entity;

import com.aurine.cloudx.open.common.entity.base.OpenBasePo;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 开放平台推送订阅回调信息
 *
 * @author : Qiu
 * @date : 2021 12 09 10:49
 */

@Data
@TableName("open_push_subscribe_callback")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "开放平台推送订阅回调信息")
public class OpenPushSubscribeCallback extends OpenBasePo<OpenPushSubscribeCallback> {

    /**
     * 序列号，自增
     */
    @ApiModelProperty(value = "序列号，自增")
    private Integer seq;

    /**
     * 回调ID，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "回调ID，uuid")
    private String callbackId;

    /**
     * 回调类型，0-配置类；1-级联入云类；2-操作类；3-指令类；4-事件类；5-反馈类；9-其他
     */
    @ApiModelProperty(value = "回调类型，0-配置类；1-级联入云类；2-操作类；3-指令类；4-事件类；5-反馈类；9-其他")
    private String callbackType;

    /**
     * 回调方式，0-url方式；1-topic方式，默认为0
     */
    @ApiModelProperty(value = "回调方式，0-url方式；1-topic方式，默认为0")
    private String callbackMode;

    /**
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址")
    private String callbackUrl;

    /**
     * 回调主题
     */
    @ApiModelProperty(value = "回调主题")
    private String callbackTopic;

    /**
     * 回调请求头参数，参数名为Param（预留字段）
     */
    @ApiModelProperty(value = "回调请求头参数，参数名为Param")
    private String callbackHeaderParam;

    /**
     * 回调说明
     */
    @ApiModelProperty(value = "回调说明")
    private String callbackDesc;

    /**
     * 项目UUID；如果回调类型是配置类可为空
     */
    @ApiModelProperty(value = "项目UUID；如果回调类型是配置类可为空")
    private String projectUUID;

    /**
     * 应用ID
     */
    @ApiModelProperty(value = "应用ID")
    private String appId;

    /**
     * 项目类型，0-云平台端；1-边缘网关；2-WR2X（预留字段）
     */
    @ApiModelProperty(value = "项目类型，0-云平台端；1-边缘网关；2-WR2X（预留字段）")
    private String projectType;

    /**
     * 隐藏projectId在swagger和MybatisPlus上的映射
     * 目的是为了覆盖父类（OpenBasePo）对象的projectId属性
     * 因为本Po对象对应的数据库表（open_push_subscribe_callback）没有projectId这个字段
     */
    @ApiModelProperty(value = "项目id", hidden = true)
    @TableField(exist = false)
    private Integer projectId;

}
