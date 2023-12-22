package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: ProjectWaterMeterVo
 * @author: 邹宇 <>
 * @date:  2021年7月06日 下午15:35:36
 * @Copyright:
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "水表vo对象")
public class ProjectWaterMeterVo {
    /**
     * 抄表日期
     */
    @ApiModelProperty("抄表日期")
    private String dataValue;
    /**
     * 抄表时间
     */
    @ApiModelProperty("抄表时间")
    private String timeValue;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private String[] deviceIds;

}