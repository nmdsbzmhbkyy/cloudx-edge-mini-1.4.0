package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 优惠查询表单(ProjectPromotionConfFormVo)视图表
 *
 * @author xull@aurine.cn
 * @date 2020-07-24 09:52:28
 */
@Data
public class ProjectPromotionConfFormVo {
    /**
     * 开始时间字符串
     */
    @ApiModelProperty(value = "生效时间")
    private String beginTimeString;


    /**
     * 结束时间字符串
     */
    @ApiModelProperty(value = "失效时间")
    private String endTimeString;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "生效时间")
    private LocalDate beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "失效时间")
    private LocalDate endTime;
    /**
     * 是否有效
     */
    @ApiModelProperty("是否有效")
    private String valid;

    /**
     * 优惠活动名称
     */
    @ApiModelProperty(value = "优惠活动名称")
    private String promotionName;
}
