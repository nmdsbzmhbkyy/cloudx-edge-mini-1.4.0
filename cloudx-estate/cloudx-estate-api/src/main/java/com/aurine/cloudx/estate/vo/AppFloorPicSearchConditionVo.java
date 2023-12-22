package com.aurine.cloudx.estate.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppFloorPicSearchConditionVo extends Page {


    @ApiModelProperty(value = "节点ID deviceRegionId")
    private String regionId;

    @ApiModelProperty(value = "设备ID")
    private String deviceId;

}
