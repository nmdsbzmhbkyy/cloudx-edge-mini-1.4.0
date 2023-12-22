package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * (ProjectDeviceAttrFormVo)
 *
 * @author xull
 * @since 2020/7/6 9:00
 */
@Data
@ApiModel("设备拓展属性表单对象")
public class ProjectDeviceAttrFormVo {
    /**
     * 设备参数key-value视图列表
     */
    @ApiModelProperty(value = "设备采集参数key-value视图列表")
    List<ProjectDeviceAttrListVo> projectDeviceAttrList = new ArrayList<>();
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String deviceId;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private String type;
    /**
     * 项目Id
     */
    @ApiModelProperty(value = "项目Id")
    private Integer projectId;
}
