package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <p>
 * 开关闸记录对象
 * </p>
 *
 * @author 王良俊
 * @since 2022/6/27 10:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenLaneHisQueryVo {

    /**
     * 行进方向
     */
    @ApiModelProperty(value = "车道出入方向")
    private String direction;

    /*
     * 开闸时间范围
     **/
    @ApiModelProperty("开闸时间范围")
    private LocalDateTime[] openTimeRange;

    /*
     * 关闸时间范围
     **/
    @ApiModelProperty("关闸时间范围")
    private LocalDateTime[] closeTimeRange;

    /*
     * 车道ID
     **/
    @ApiModelProperty("车道名称")
    private String laneName;

    /*
     * 车场ID
     **/
    @ApiModelProperty("车场ID")
    private String parkId;
}
