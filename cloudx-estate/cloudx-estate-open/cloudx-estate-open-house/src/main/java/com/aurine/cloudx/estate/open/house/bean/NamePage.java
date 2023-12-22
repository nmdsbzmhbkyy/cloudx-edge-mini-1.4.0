package com.aurine.cloudx.estate.open.house.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("名称")
@Data
public class NamePage extends Page {
    @ApiModelProperty("名称")
    private String name;
}
