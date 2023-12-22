package com.aurine.cloudx.estate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: hjj
 * @Date: 2022/4/1 09:26
 * @Description: 屏蔽房间VO
 */
@Data
@ApiModel(value = "屏蔽房间VO")
public class ShieldRoomVo {

    @ApiModelProperty("是否屏蔽")
    private Integer shield;
    @ApiModelProperty("房间编号")
    private String roomCode;
}
