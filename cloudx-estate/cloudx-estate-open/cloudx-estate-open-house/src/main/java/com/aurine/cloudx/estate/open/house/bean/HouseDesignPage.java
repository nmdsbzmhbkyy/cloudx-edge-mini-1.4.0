package com.aurine.cloudx.estate.open.house.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("户型分页条件")
@Data
public class HouseDesignPage extends Page {
    @ApiModelProperty("户型描述")
    private String desginDesc;
}
