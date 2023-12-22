package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * (ProjectRepairRecordRequestFormVo)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/9/2 15:25
 */
@ApiModel("报修登记表单视图")
@Data
public class ProjectRepairRecordRequestFormVo {
    /**
     * 房号
     */
    @ApiModelProperty(value = "房号")
    private String houseId;
    /**
     * 报修类型 参考字典类型 repair_type
     */
    @ApiModelProperty(value = "报修类型 0.水电煤气 1.家电家具 2.电梯 3.门禁 4.公共设施 5.物业设备 6.其他报修  参考字典类型 repair_type")
    private String repairType;
    /**
     * 报修内容
     */
    @ApiModelProperty(value = "报修内容")
    private String content;
    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picPath1;
    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picPath2;
    /**
     * 图片3
     */
    @ApiModelProperty(value = "图片3")
    private String picPath3;
    /**
     * 图片4
     */
    @ApiModelProperty(value = "图片4")
    private String picPath4;
    /**
     * 图片5
     */
    @ApiModelProperty(value = "图片5")
    private String picPath5;
    /**
     * 图片6
     */
    @ApiModelProperty(value = "图片6")
    private String picPath6;
    /**
     * 是否尽快 0 否 1 是
     */
    @ApiModelProperty(value = "是否尽快 0 否 1 是")
    private String isASAP;
    /**
     * 预约时间段起始
     */
    @ApiModelProperty(value = "预约时间段起始")
    private LocalDateTime reserveTimeBegin;
    /**
     * 预约时间段结束
     */
    @ApiModelProperty(value = "预约时间段结束")
    private LocalDateTime reserveTimeEnd;
}
