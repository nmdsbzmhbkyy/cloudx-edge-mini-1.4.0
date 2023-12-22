package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.entity.constant.DongDongErrorEnum;
import lombok.Data;

/**
 * @description: 咚咚响应数据对象
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-18
 * @Copyright:
 */
@Data
public class DongDongRespondDTO {

    /**
     * 请求id
     */
    private String requestId;

    /**
     * 响应数据对象
     */
    private JSONObject respondObj;

    /**
     * 响应数据对象
     */
    private JSONArray respondArray;

    /**
     * 响应异常信息
     */
    private DongDongErrorEnum errorEnum;
}
