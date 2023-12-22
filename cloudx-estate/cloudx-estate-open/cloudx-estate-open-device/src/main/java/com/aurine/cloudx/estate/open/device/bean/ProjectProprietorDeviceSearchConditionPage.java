package com.aurine.cloudx.estate.open.device.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>住户 权限  查询条件 VO</p>
 */
@Data
@ApiModel(value = "住户 权限 查询条件")
public class ProjectProprietorDeviceSearchConditionPage extends Page {

    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value = "人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String buildingName;

    @ApiModelProperty("建筑id")
    private String buildingId;

    @ApiModelProperty("单元名")
    private String unitName;
    @ApiModelProperty("单元id")
    private String unitId;
    @ApiModelProperty("房屋名")
    private String houseName;
    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("车位区域")
    private String parkRegionName;
    @ApiModelProperty("车位区域id")
    private String parkRegionId;

    @ApiModelProperty("停车场名字")
    private String parkName;
    @ApiModelProperty("停车场id")
    private String parkId;

    @ApiModelProperty("车位号名")
    private String placeName;
    @ApiModelProperty("车位号id")
    private String placeId;
    @ApiModelProperty("人员名")
    private String personName;
    @ApiModelProperty("是否房屋搜索")
    boolean houseSearch;
    @ApiModelProperty("是否停车场搜索")
    boolean parkSearch;

    @ApiModelProperty(value = "通行状态，''全部，0，未配置，1，正常，2 已过期 ")
    private String rightStatus;

}
