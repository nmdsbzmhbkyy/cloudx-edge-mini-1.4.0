package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Data
@ApiModel("报修登记表单")
public class AppRepairRecordRequestFormVo {
    /**
     * 房间ID
     */
    @ApiModelProperty(value = "房间ID")
    private String houseId;
    /**
     * 报修类型 参考字典类型 repair_type
     */
    @ApiModelProperty(value = "报修类型（0 水电煤气 1 家电家具 2 电梯 3 门禁 4 公共设施 5 物业设备 6 其他报修）")
    private String repairType;
    /**
     * 报修内容
     */
    @ApiModelProperty(value = "报修内容")
    private String content;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片列表 Base64")
    private List<String> picBase64List;
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
