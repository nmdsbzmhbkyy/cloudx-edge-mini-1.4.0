package com.aurine.cloudx.estate.open.house.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("楼栋分页条件")
@Data
public class BuildingPage extends Page {
    @ApiModelProperty("每页显示条数，默认 20")
    private String buildingName;
}
