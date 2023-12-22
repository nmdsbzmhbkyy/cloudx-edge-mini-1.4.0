

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>车位记录表VO</p>
 * @ClassName: ProjectParkingPlaceRecodeVo
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020/5/11 11:44
 * @Copyright:
 */
@Data
@ApiModel(value = "车位记录表Vo")
public class ProjectParkingPlaceManageRecordVo {
private static final long serialVersionUID = 1L;


    /**
     * 车位ID
     */
    @ApiModelProperty(value="车位ID")
    private String placeId;

    /**
     * 车位区域名
     */
    @ApiModelProperty(value="车位区域名")
    private String parkRegionName;

    /**
     * 车位号
     */
    @ApiModelProperty(value="车位号")
    private String placeName;

//    /**
//     * 停车场+车位号
//     */
//    @ApiModelProperty(value="停车场+车位号")
//    private String parkPlaceName;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value="归属停车场ID")
    private String parkId;

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String personName;
    /**
     * personID
     */
    @ApiModelProperty(value="personID")
    private String personId;

    /**
     * 电话号码
     */
    @ApiModelProperty(value="电话号码")
    private String telephone;

    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value="类型 0 闲置 1 产权 2 租赁")
    private String relType;
    /**
     * 性别
     */
    @ApiModelProperty(value="性别")
    private String gender;
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

    @ApiModelProperty(value="车牌号")
    private String plateNumber;

    }
