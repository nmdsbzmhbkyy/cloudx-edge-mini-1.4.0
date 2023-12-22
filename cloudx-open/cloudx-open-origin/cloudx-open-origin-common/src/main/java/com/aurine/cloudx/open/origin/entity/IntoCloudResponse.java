package com.aurine.cloudx.open.origin.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : 王良俊
 * @date : 2021-12-17 09:25:12
 */
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
