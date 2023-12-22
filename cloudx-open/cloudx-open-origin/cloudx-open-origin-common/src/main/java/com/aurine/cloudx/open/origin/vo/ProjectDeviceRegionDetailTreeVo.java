package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (ProjectDeviceRegionDetailTreeVo)
 * 设备区域树(含设备数量)
 *
 * @author xull
 * @since 2020/7/8 14:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("设备区域树(含设备数量)")
public class ProjectDeviceRegionDetailTreeVo extends ProjectDeviceRegionTreeVo{
    /**
     * 区域内包含设备数量
     */
    private Integer num;
}
