package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceInfo;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
     * 路线选择
     */
    @ApiModelProperty(value = "路线选择")
    private String route;


}
