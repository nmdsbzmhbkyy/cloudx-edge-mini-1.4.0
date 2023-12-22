package com.aurine.cloudx.estate.thirdparty.module.device.platform.edge.entity.vo.respond;

import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;

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

    @SneakyThrows
    @JsonIgnore
    @Override
    public String toString() {
        return ObjectMapperUtil.instance().writeValueAsString(this);
    }
}
