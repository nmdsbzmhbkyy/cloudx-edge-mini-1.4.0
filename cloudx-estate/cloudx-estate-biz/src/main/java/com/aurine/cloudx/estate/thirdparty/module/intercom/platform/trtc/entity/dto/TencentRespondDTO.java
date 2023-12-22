package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.trtc.entity.constant.TencentErrorEnum;
import lombok.Data;

/**
 * @program: cloudx
 * @description: 腾讯响应数据对象
 * @author: 谢泽毅
 * @create: 2021-08-11 16:35
 **/
@Data
public class TencentRespondDTO {
    public TencentRespondDTO() {
    }

    public TencentRespondDTO(JSONObject respondObj, JSONArray respondArray) {
        this.respondObj = respondObj;
        this.respondArray = respondArray;
    }

    /**
     * 应答码
     */
    private String code;

    /**
     * 响应数据对象
     */
    private JSONObject respondObj;

    /**
     * 响应数据对象
     */
    private JSONArray respondArray;

    /**
     * 应答消息
     */
    private String msg;
}
