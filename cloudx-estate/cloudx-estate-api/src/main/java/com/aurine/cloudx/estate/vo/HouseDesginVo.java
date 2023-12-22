package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * (HouseDesginVo)
 *
 * @author guhl
 * @version 1.0.0
 * @date 2021/3/10 8:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "用于批量设置户型vo")
public class HouseDesginVo {
    @ApiModelProperty("房屋id")
    List<String> houseIds;
    @ApiModelProperty("户型id")
    String houseDesginId;
}
