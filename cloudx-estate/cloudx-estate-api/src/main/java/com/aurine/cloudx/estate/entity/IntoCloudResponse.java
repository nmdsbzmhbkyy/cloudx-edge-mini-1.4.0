package com.aurine.cloudx.estate.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IntoCloudResponse {

    /**
     * 服务ID：比如入云申请服务
     */
    private String serviceId;

    /**
     * 对应的结果
     */
    private String resultCode;

    /**
     * 消息
     */
    private String msg;

    /**
     * 存放各种数据
     */
    private String data;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
