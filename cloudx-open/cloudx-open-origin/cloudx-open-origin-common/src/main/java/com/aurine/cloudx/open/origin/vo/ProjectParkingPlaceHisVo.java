package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectParkingPlaceHisVo {


    @ApiModelProperty(value="")
    private Integer seq;

    /**
     * 车位ID
     */
    @ApiModelProperty(value="车位ID")
    private String placeId;
    /**
     * 车位编号，如A-2201。可用于第三方对接
     */
    @ApiModelProperty(value="车位编号，如A-2201。可用于第三方对接")
    private String placeCode;
    /**
     * 车位号
     */
    @ApiModelProperty(value="车位号")
    private String placeName;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value="归属停车场ID")
    private String parkId;
    /**
     * 归属人
     */
    @ApiModelProperty(value="归属人")
    private String personId;
    /**
     * 归属人姓名
     */
    @ApiModelProperty(value="归属人姓名")
    private String personName;
    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value="关系类型 0 闲置 1 产权 2 租赁")
    private String relType;

    /**
     * 启用时间
     */
    @ApiModelProperty(value="启用时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value="生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value="失效时间")
    private LocalDateTime expTime;
    /**
     * 迁入迁出类型 1 迁入 0 迁出
     */
    @ApiModelProperty(value="迁入迁出类型 1 迁入 0 迁出")
    private String action;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private Integer operator;




    @ApiModelProperty(value = "该条记录的创建时间")
    private String createTime;
}
