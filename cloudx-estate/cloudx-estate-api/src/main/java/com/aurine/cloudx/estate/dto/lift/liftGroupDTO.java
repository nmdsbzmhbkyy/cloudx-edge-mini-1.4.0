package com.aurine.cloudx.estate.dto.lift;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 10:08
 * @Description:
 */
@Data
@ApiModel(value = "电梯业主卡电梯组数据DTO")
public class liftGroupDTO {
    //楼栋号
    @ApiModelProperty("楼栋号")
    private Integer liftBuilding;
    //单元号
    @ApiModelProperty("单元号")
    private Integer liftUnit;
    //房间编号，房间编号3A02（带字母），表示成0x003A02；房间编号12A08（带字母），表示成012A08；房间编号0601，表示成000601
    @ApiModelProperty("房间编号")
    private Integer liftRoom;
    //电梯组电梯数量
    @ApiModelProperty("电梯组电梯数量")
    private Integer liftCountsInGroup;
    //组内电梯编号（最多8位）
    @ApiModelProperty("组内电梯编号（最多8位）")
    private List<Integer> liftsNo;
    //可用楼层每一位代表一层，=1 可用，=0 不可用（单开门192，双开门96）
    @ApiModelProperty("可用楼层每一位代表一层，=1 可用，=0 不可用（单开门192，双开门96）")
    private List<Integer> layersEnable;
}
