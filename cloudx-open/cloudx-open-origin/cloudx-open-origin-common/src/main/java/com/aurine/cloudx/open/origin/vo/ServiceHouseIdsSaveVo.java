package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Title: ServiceHouseSaveVo
 * Description:房屋批量新增增值服务时，用于接收前端传来的参数
 *
 * @author guhl@aurine.cn
 * @version 1.0.0
 * @date 2020/6/5 11:54
 */

@Data
@ApiModel(value = "批量新增房屋增值服务对象")
public class ServiceHouseIdsSaveVo {
    @ApiModelProperty("房屋id列表")
    List <String> houseIds;
    @ApiModelProperty("增值服务id列表")
    List <String> serviceIds;
    @ApiModelProperty("截止时间")
    private String expTime;
}
