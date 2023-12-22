package com.aurine.cloudx.open.origin.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectNoticeObjectVo {
    @ApiModelProperty("消息id")
    private String noticeId;

    @ApiModelProperty("房屋id")
    private String houseId;
    @ApiModelProperty("房屋名称")
    private String houseName;
    @ApiModelProperty("单元名称")
    private String unitName;
    @ApiModelProperty("楼栋名称")
    private String buildName;
}
