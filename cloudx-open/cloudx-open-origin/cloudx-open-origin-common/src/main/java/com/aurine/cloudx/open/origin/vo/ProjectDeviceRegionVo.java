package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectDeviceRegion;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectDeviceRegionVo)
 *
 * @author xull
 * @since 2020/7/8 15:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "设备区域属性拓展Vo")
public class ProjectDeviceRegionVo extends ProjectDeviceRegion {
    /**
     * 设备数量
     */
    @ApiModelProperty("设备数量")
    private Integer num;
}
