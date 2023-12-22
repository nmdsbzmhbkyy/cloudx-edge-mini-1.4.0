package com.aurine.cloudx.estate.open.house.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("房屋分页条件")
@Data
public class HousePage extends Page {
    @ApiModelProperty("房屋ID")
    private String houseId;
}
