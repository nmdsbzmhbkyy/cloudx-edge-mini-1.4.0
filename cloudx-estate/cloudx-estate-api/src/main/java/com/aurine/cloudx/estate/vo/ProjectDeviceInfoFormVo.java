package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ProjectDeviceInfoFormVo
 * Description:  根据设备类型列表及楼栋单元查询设备
 *
 * @author xull@aurine.cn
 * @version 1.0.0
 * @date 2020/5/21 9:18
 */
@Data
@ApiModel("根据设备类型列表及楼栋单元查询设备传参表单")
public class ProjectDeviceInfoFormVo {
    /**
     * 设备类型列表
     */
    @ApiModelProperty("设备类型列表")
    private List<String> types;
    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id")
    private String buildingId;
    /**
     * 单元id
     */
    @ApiModelProperty(value = "单元id")
    private String unitId;
    /**
     * 设备区域
     */
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionId;
    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
}
