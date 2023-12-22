/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车辆信息VO
 *
 * @author 王伟
 * @date 2020-07-08 14:10:32
 */
@Data
@ApiModel(value = "车辆信息VO")
public class ProjectCarInfoVo {


    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String telephone;

    /**
     * 车主名
     */
    @ApiModelProperty(value = "车主名")
    private String personName;



    /**
     * 车辆id，uuid
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty(value = "车辆id，uuid")
    private String carUid;

    /**
     * 车主
     */
    @ApiModelProperty(value = "车主")
    private String personId;
    /**
     * 车辆信息标识
     */
    @ApiModelProperty(value = "车辆信息标识")
    private String carVinInfo;
    /**
     * 字典值 plate_type
     */
    @ApiModelProperty(value = "字典值 plate_type")
    private String plateType;
    /**
     * 字典值 plate_color
     */
    @ApiModelProperty(value = "字典值 plate_color")
    private String plateColor;
    /**
     * 字典值 vehicle_type
     */
    @ApiModelProperty(value = "字典值 vehicle_type")
    private String vehicleType;
    /**
     * 车辆中文品牌名称
     */
    @ApiModelProperty(value = "车辆中文品牌名称")
    private String brandName;
    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆型号")
    private String vehicleModel;
    /**
     * 车辆长度(毫米)
     */
    @ApiModelProperty(value = "车辆长度(毫米)")
    private Integer length;
    /**
     * 车辆宽度(毫米)
     */
    @ApiModelProperty(value = "车辆宽度(毫米)")
    private Integer width;
    /**
     * 车辆高度(毫米)
     */
    @ApiModelProperty(value = "车辆高度(毫米)")
    private Integer height;
    /**
     * 字典值 vehicle_color
     */
    @ApiModelProperty(value = "A.白 B.灰 C.黄 D.粉 E.红 F.紫 G.绿 H.蓝 I.棕 J.黑 Z.其他 字典值 vehicle_color")
    private String vehicleColor;
    /**
     * 车辆简要情况
     */
    @ApiModelProperty(value = "车辆简要情况")
    private String remark;


}
