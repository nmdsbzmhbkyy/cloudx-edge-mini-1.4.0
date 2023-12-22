package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 屏蔽房间VO
 */
@Data
@ApiModel(value = "屏蔽房间VO")
public class LiftDurationVo {

    @ApiModelProperty("设置时段卡开关 =1开启,=0取消")
    private Integer durationSwitch;
    @ApiModelProperty("=1开放使用电梯时段,=0刷卡使用时段")
    private Integer durationType;
    @ApiModelProperty(value = "楼层列表")
    private String[] checkedFloors;
    //通行时间
    @ApiModelProperty("通行时间")
    private String[] effDate;
    //通行开始时间
    @ApiModelProperty("通行开始时间")
    private String effTime;
    //通行结束时间
    @ApiModelProperty("通行结束时间")
    private String expTime;
    //特点时段起始时间
    @ApiModelProperty("特点时段起始时间")
    private String startTime;
    //特点时段结束时间
    @ApiModelProperty("特点时段结束时间")
    private String endTime;
    //周期 [1,2,3,4,5,6,7]
    @ApiModelProperty("周期")
    private List<String> checkedDays;
}
