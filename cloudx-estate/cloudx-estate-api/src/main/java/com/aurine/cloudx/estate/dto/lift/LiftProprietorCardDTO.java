package com.aurine.cloudx.estate.dto.lift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 电梯业主卡数据DTO
 */
@Data
@ApiModel(value = "电梯业主卡数据DTO")
public class LiftProprietorCardDTO {
    //1 时段卡；2收费卡
    @ApiModelProperty("1 时段卡；2收费卡")
    private String cardType;
    //卡号
    @ApiModelProperty("卡号")
    private String cardNo;
    //用户名
    @ApiModelProperty("用户名")
    private String personName;
    //用户ID
    @ApiModelProperty("用户ID")
    private String personId;
    //手机号码
    @ApiModelProperty("手机号码")
    private String mobile;
    //召梯方式  =0 手动；=1 自动；=2  手动+自动(预留)
    @ApiModelProperty("召梯方式  =0 手动；=1 自动；=2  手动+自动(预留)")
    private Integer callLiftType;
    //=0 一卡一梯； =1 一卡多梯
    @ApiModelProperty("=0 一卡一梯； =1 一卡多梯")
    private Integer cardLiftRel;
    //用户特权  =0 普通卡；=1贵宾卡；=2 丽人卡
    @ApiModelProperty("用户特权  =0 普通卡；=1贵宾卡；=2 丽人卡")
    private Integer prerogative;
    //电梯组数量
    @ApiModelProperty("电梯组数量")
    private Integer liftCounts;
    //=0  无扩展电梯组； =1扩展电梯组
    @ApiModelProperty("=0  无扩展电梯组； =1扩展电梯组")
    private Integer hasLiftGroup;
    //特定时段开始时间-小时
    @ApiModelProperty("特定时段开始时间-小时")
    private Integer periodStartTimeHour;
    //特定时段开始时间-分钟
    @ApiModelProperty("特定时段开始时间-分钟")
    private Integer periodStartTimeMinutes;
    //特定时段结束时间-小时
    @ApiModelProperty("特定时段结束时间-小时")
    private Integer periodEndTimeHour;
    //特定时段结束时间-分钟
    @ApiModelProperty("特定时段结束时间-分钟")
    private Integer periodEndTimeMinutes;
    //时段周期，二进制位表示，bit7：一整周，bit6：周日，bit5：周六……，0为禁用1为启用
    @ApiModelProperty("时段周期，二进制位表示，bit7：一整周，bit6：周日，bit5：周六……，0为禁用1为启用")
    private Integer period;
    //卡有效期开始日期-年
    @ApiModelProperty("卡有效期开始日期-年")
    private Integer cardUsefulLifeStartYear;
    //卡有效期开始日期-月
    @ApiModelProperty("卡有效期开始日期-月")
    private Integer cardUsefulLifeStartMouth;
    //卡有效期开始日期-日
    @ApiModelProperty("卡有效期开始日期-日")
    private Integer cardUsefulLifeStartDay;
    //卡有效期开始日期-小时
    @ApiModelProperty("卡有效期开始日期-小时")
    private Integer cardUsefulLifeStartHour;
    //卡有效期开始日期-分钟
    @ApiModelProperty("卡有效期开始日期-分钟")
    private Integer cardUsefulLifeStartMinutes;
    //卡有效期开始日期-年
    @ApiModelProperty("卡有效期开始日期-年")
    private Integer cardUsefulLifeEndYear;
    //卡有效期开始日期-月
    @ApiModelProperty("卡有效期开始日期-月")
    private Integer cardUsefulLifeEndMouth;
    //卡有效期开始日期-日
    @ApiModelProperty("卡有效期开始日期-日")
    private Integer cardUsefulLifeEndDay;
    //卡有效期开始日期-小时
    @ApiModelProperty("卡有效期开始日期-小时")
    private Integer cardUsefulLifeEndHour;
    //卡有效期开始日期-分钟
    @ApiModelProperty("卡有效期开始日期-分钟")
    private Integer cardUsefulLifeEndMinutes;
    //和liftCounts数量对应
    @ApiModelProperty("和liftCounts数量对应")
    private List<liftGroupDTO> liftGroup;
    //最多16位，没有填0，和hasLiftGroup对应
    @ApiModelProperty("最多16位，没有填0，和hasLiftGroup对应")
    private List<Integer> liftGroupExt;
}
