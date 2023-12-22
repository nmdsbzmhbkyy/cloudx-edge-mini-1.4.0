package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.constant.HuaweiErrorEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.dto.HuaweiRespondDTO;

/**
 * 华为中台 数据处理工具类
 *
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-25
 * @Copyright:
 */
public class HuaweiDeviceDataUtil {

    /**
     * 将 respond json转化为数据对象
     *
     * @param respondJson
     * @return
     */
    public static HuaweiRespondDTO handelRespond(JSONObject respondJson) {

        HuaweiRespondDTO respondDTO = new HuaweiRespondDTO();

        respondDTO.setErrorEnum(HuaweiErrorEnum.getByCode(respondJson.getString("errorCode")));
        respondDTO.setErrorMsg(respondJson.getString("errorMsg"));
        try {
            JSONObject respondObj = respondJson.getJSONObject("body");
            respondDTO.setBodyObj(respondObj);
        } catch (Exception e) {
            JSONArray respondArray = respondJson.getJSONArray("body");
            respondDTO.setBodyArray(respondArray);
        }

        return respondDTO;
    }
}
