package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.respond;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-07-13
 * @Copyright:
 */
@Data
public class CallBackData {

    @ApiModelProperty("订阅的资源名称")
    private String resource;

    @ApiModelProperty("订阅的资源事件")
    private String event;

    @ApiModelProperty("订阅id")
    private String subscriptionId;

    @ApiModelProperty("消息上报回调数据内容")
    private DevMessageData onNotifyData;

}
