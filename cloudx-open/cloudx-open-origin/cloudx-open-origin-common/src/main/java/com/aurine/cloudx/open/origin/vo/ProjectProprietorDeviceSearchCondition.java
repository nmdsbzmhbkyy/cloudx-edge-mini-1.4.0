

package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>住户 权限  查询条件 VO</p>
 *
 */
@Data
@ApiModel(value = "住户 权限 查询条件")
public class ProjectProprietorDeviceSearchCondition extends ProjectPersonDeviceVo {

    /**
     * 人员id，根据人员类型取相应表id，注意访客应取当前的来访id
     */
    @ApiModelProperty(value = "人员id，根据人员类型取相应表id，注意访客应取当前的来访id")
    private String buildingName;
    private String buildingId;

    private String unitName;
    private String unitId;

    private String houseName;
    private String houseId;

    private String parkRegionName;
    private String parkRegionId;

    private String parkName;
    private String parkId;

    private String placeName;
    private String placeId;

    private String personName;

    boolean houseSearch;

    boolean parkSearch;

    @ApiModelProperty(value = "通行状态，''全部，0，未配置，1，正常，2 已过期 ")
    private String rightStatus;

}
