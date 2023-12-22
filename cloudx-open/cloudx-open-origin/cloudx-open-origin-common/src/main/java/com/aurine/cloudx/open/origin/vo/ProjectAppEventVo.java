package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 人员信息Vo(继承了重点人员信息的数据)
 *
 * @author pigx code generator
 * @date 2020-05-12 13:37:22
 */
@Data
@ApiModel(value = "开门记录")
public class ProjectAppEventVo {


    /**
     * 开门时间
     */
    @ApiModelProperty(value = "开门时间")
    private String eventTime;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称")
    private String deviceName;
    /**
     * 出入方向 1 入  2 出
     */
    @ApiModelProperty(value = "出入方向 1 入  2 出")
    private String entranceType;
    /**
     * 认证介质 1 指纹 2 人脸 3 卡
     */
    @ApiModelProperty(value = "认证介质 1 指纹 2 人脸 3 卡")
    private String certMedia;
    /**
     * 设备区域
     */
    @ApiModelProperty(value = "设备区域")
    private String deviceRegionName;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String dAddress;

}
