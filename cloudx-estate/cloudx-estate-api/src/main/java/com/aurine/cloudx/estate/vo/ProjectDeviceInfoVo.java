package com.aurine.cloudx.estate.vo;

import java.util.List;

import com.aurine.cloudx.estate.entity.ProjectDeviceAttr;
import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import com.aurine.cloudx.estate.entity.ProjectDeviceLocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Title: ProjectDeviceInfoVo
 * Description:
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/21 14:22
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备信息视图")
public class ProjectDeviceInfoVo extends ProjectDeviceInfo {
    /**
     * 楼栋名称
     */
    @ApiModelProperty(value = "楼栋名称")
    private String buildingName;
    /**
     * 单元名称
     */
    @ApiModelProperty(value = "单元名称")
    private String unitName;

    /**
     * 房屋名称
     */
    @ApiModelProperty(value = "房屋名称")
    private String houseName;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String deviceRegionName;

    /**
     * 设备描述
     */
    @ApiModelProperty(value = "设备描述")
    private String deviceDesc;
    
    /**
     * 设备定位
     */
    @ApiModelProperty(value = "设备定位")
    private List<ProjectDeviceLocation> locations;

    /**
     * 设备拓展属性
     */
    @ApiModelProperty(value = "设备拓展属性")
    private List<ProjectDeviceAttrListVo> deviceAttrs;

    /**
     * 设备关联控制器
     */
    @ApiModelProperty(value = "设备关联控制器")
    private String controller;

    /**
     * 是否异常
     */
    @ApiModelProperty(value = "是否异常")
    private boolean result;

    @ApiModelProperty(value = "子网掩码")
    private String subnet;

    @ApiModelProperty(value = "默认网关")
    private String gateway;

    @ApiModelProperty(value = "dns1地址")
    private String dns1;

    @ApiModelProperty(value = "dns2地址")
    private String dns2;

    @ApiModelProperty(value = "中心服务器地址")
    private String centerServerIP;

    /**
     * 选中状态 0 全部 1 未被选 2 已被选
     */
    private String selectStatus;

}
