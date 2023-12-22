package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("车辆出入记录搜索视图")
public class AppSearchParkEntranceHisVo {

    @ApiModelProperty(value = "车场Id")
    private String parkId;
    @ApiModelProperty(value = "车牌号", required = true)
    private String plateNumber;
    @ApiModelProperty(value = "出入状态(1 入场 2 出场)")
    private String status;

}
