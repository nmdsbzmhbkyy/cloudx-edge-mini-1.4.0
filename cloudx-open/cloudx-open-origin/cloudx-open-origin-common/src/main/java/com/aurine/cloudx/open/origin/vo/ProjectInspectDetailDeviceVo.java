package com.aurine.cloudx.open.origin.vo;

import com.aurine.cloudx.open.origin.entity.ProjectInspectDetailDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName: ProjectInspectDetailDeviceVo
 * @author: 王良俊 <>
 * @date: 2020年07月31日 上午09:59:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "巡检任务明细设备列表Vo对象")
public class ProjectInspectDetailDeviceVo extends ProjectInspectDetailDevice {

    /**
     * 照片数量
     */
    @ApiModelProperty(value = "照片数量")
    private String picNum;
    /**
     * 设备sn
     */
    @ApiModelProperty("设备sn")
    private String sn;

}