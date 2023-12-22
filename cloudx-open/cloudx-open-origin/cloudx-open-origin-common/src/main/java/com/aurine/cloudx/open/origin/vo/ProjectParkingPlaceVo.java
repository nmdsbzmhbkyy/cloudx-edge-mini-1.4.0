

package com.aurine.cloudx.open.origin.vo;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车位
 *
 * @author 王伟
 * @date 2020-05-08 15:24:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "车位")
public class ProjectParkingPlaceVo extends Model<ProjectParkingPlaceVo> {
    private static final long serialVersionUID = 1L;


    /**
     * 车位ID
     */
    @ApiModelProperty(value = "车位ID")
    private String placeId;
    /**
     * 车位编号，如A-2201。可用于第三方对接
     */
    @ApiModelProperty(value = "车位编号，如A-2201。可用于第三方对接")
    private String placeCode;
    /**
     * 车位号
     */
    @ApiModelProperty(value = "车位号")
    private String placeName;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "归属停车场ID")
    private String parkId;
    /**
     * 归属停车区域Id
     */
    @ApiModelProperty(value = "归属停车区域Id")
    private String parkRegionId;
    /**
     * 归属停车场ID
     */
    @ApiModelProperty(value = "归属停车场名称")
    private String parkRegionName;

    /**
     * 归属人
     */
    @ApiModelProperty(value = "是否被使用")
    private Integer isUse;

    /**
     * 归属人
     */
    @ApiModelProperty(value = "是否被使用")
    private String isUseText;

    /**
     * 车位类型
     */
    @ApiModelProperty(value = "车位类型")
    private String placeType;

    /**
     * 关系类型 0 闲置 1 产权 2 租赁
     */
    @ApiModelProperty(value = "关系类型 0 闲置 1 产权 2 租赁")
    private String relType;

    /**
     * 车位号位数
     */
    @ApiModelProperty(value = "车位号位数")
    private Integer numberOfParkingSpaces;

    /**
     * 车位起始编号
     */
    @ApiModelProperty(value = "车位起始编号")
    private Integer beginNum;

    /**
     * 车位结束编号
     */
    @ApiModelProperty(value = "车位结束编号")
    private Integer endNum;

    /**
     * 车位号列表
     */
    @ApiModelProperty(value = "车位号列表")
    private List<String> placeNameList;

    /**
     * 启用时间
     */
    @ApiModelProperty(value = "启用时间")
    private LocalDateTime checkInTime;
    /**
     * 生效时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDateTime effTime;
    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDateTime expTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private Integer operator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
