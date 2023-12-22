package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceLocation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Title: ProjectDeviceCollectListVo
 * Description: 设备平面图属性拓展Vo
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/6/12 14:23
 */
@Data
@ApiModel(value = "设备平面图属性拓展Vo")
public class ProjectDeviceLocationVo extends ProjectDeviceLocation {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    @ApiModelProperty(value = "设备类型")
    private String deviceType;
    @ApiModelProperty(value = "设备状态")
    private String status;
    @ApiModelProperty(value = "设备拓展属性")
    private String deviceFeature;
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionId;
}
