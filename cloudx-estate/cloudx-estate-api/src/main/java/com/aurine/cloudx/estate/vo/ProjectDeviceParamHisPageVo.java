package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.entity.ProjectDeviceParamHis;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * (ProjectDeviceParamHisPageVo) 设备参数配置历史分页vo
 *
 * @author guhl
 * @since 2020/12/23 10:16
 */
@Data
@ApiModel("设备参数配置历史分页vo")
public class ProjectDeviceParamHisPageVo extends ProjectDeviceParamHis {
    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    String deviceName;

    /**
     * 设备描述
     */
    @ApiModelProperty("设备描述")
    String deviceAlias;
}
