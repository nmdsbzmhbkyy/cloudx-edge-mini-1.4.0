package com.aurine.cloudx.wjy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目映射实体
 * @author ： huangjj
 * @date ： 2021/4/14
 * @description： 我家云项目配置
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "我家云项目配置")
public class ProjectVo {

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id")
    private Integer projectId;

    /**
     * 组织名称
     */
    @ApiModelProperty(value = "组织名称")
    private String orgName;
    /**
     * 我家云项目id
     */
    @ApiModelProperty(value = "我家云项目id")
    private String pid;
    /**
     * 我家云后台物业appkey
     */
    @ApiModelProperty(value = "我家云后台物业appkey")
    private String wyAppkey;
    /**
     * 我家云后台物业appid
     */
    @ApiModelProperty(value = "我家云后台物业appid")
    private String wyAppid;
    /**
     * 我家云后台物业wyAppsecret
     */
    @ApiModelProperty(value = "我家云后台物业wyAppsecret")
    private String wyAppsecret;
    /**
     * 我家云我家appkey
     */
    @ApiModelProperty(value = "我家云我家appkey")
    private String wjAppkey;
    /**
     * 我家云我家appid
     */
    @ApiModelProperty(value = "我家云我家appid")
    private String wjAppid;
    /**
     * 我家云我家appsecret
     */
    @ApiModelProperty(value = "我家云我家appsecret")
    private String wjAppsecret;
    /**
     * 我家云管家appkey
     */
    @ApiModelProperty(value = "我家云管家appkey")
    private String gjAppkey;
    /**
     * 我家云管家appid
     */
    @ApiModelProperty(value = "我家云管家appid")
    private String gjAppid;
    /**
     * 我家云管家appsecret
     */
    @ApiModelProperty(value = "我家云管家appsecret")
    private String gjAppsecret;
    /**
     * 管理员手机号码
     */
    @ApiModelProperty(value = "管理员手机号码")
    private String phone;

    @ApiModelProperty(value = "是否启用 1:启用;0:未启用")
    private Integer enable;
}