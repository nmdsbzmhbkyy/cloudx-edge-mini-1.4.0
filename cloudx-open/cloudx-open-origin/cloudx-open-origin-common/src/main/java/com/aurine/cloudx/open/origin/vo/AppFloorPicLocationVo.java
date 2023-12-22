package com.aurine.cloudx.open.origin.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppFloorPicLocationVo extends Page {

    @ApiModelProperty(value = "节点ID deviceRegionId")
    private String deviceId;
}
