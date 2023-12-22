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
public class ShieldCardVo {

    @ApiModelProperty("屏蔽卡类型")
    private Integer shieldCardType;
    @ApiModelProperty("屏蔽卡号")
    private String shieldCardNo;
}
