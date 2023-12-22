package com.aurine.cloudx.estate.vo;

import com.aurine.cloudx.estate.dto.lift.liftGroupDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 电梯业主卡数据VO
 */
@Data
@ApiModel(value = "电梯业主卡数据DTO")
public class LiftProprietorCardVo {
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
    private String personId;//用户ID
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("房号")
    private String houseCode;
    @ApiModelProperty("规则")
    private String houseRule;
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
    //电梯组
    @ApiModelProperty("电梯组")
    private List<ProjectDeviceLiftVo> liftList;
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
    private List<String> days;
    @ApiModelProperty("单/双开门 =0 单开门；=2 双开门")
    private Integer openDoorMode;
    @ApiModelProperty(value = "楼层列表")
    private List<ProjectFloorVo> floors;

}
