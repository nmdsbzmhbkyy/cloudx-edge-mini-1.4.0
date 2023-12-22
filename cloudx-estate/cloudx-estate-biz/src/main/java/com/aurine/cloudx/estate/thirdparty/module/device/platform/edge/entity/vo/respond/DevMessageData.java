package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 设备消息对象数据 <devMessageData>
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-13
 * @Copyright:
 */
@Data
public class DevMessageData {
    @ApiModelProperty("消息ID")
    private String msgId;

    @ApiModelProperty("消息名称")
    private String msgName;

    @ApiModelProperty("产品ID")
    private String productId;

    @ApiModelProperty("设备ID")
    private String devId;

    @ApiModelProperty("网关ID")
    private String gatewayId;

    @ApiModelProperty("消息更新事件 UTC 时间字符串")
    private String changeTime;

    @ApiModelProperty("对象管理消息数据")
    private ObjManagerData objManagerData;

}
