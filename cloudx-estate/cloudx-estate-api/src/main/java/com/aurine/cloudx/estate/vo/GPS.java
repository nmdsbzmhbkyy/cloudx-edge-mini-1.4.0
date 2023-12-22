package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("经纬度")
public class GPS {
    public GPS () {

    }
    public GPS (Double lan, Double lon) {
        this.lat = lan;
        this.lon = lon;
    }
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("经度")
    private Double lon;
}
