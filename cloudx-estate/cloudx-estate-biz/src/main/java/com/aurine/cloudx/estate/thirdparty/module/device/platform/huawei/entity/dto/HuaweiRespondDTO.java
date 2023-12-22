package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import lombok.Data;

/**
 * 华为中台 响应数据 DTO
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-25
 * @Copyright:
 */
@Data
public class HuaweiRespondDTO {

    /**
     * 异常结果
     */
    private HuaweiErrorEnum errorEnum;


    /**
     * 异常信息
     */
    private String errorMsg;


    private JSONArray bodyArray;

    private JSONObject bodyObj;


}
