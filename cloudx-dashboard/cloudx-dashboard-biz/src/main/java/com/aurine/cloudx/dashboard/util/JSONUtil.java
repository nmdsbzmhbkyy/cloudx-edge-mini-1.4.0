package com.aurine.cloudx.dashboard.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-25
 * @Copyright:
 */
public class JSONUtil {

    /**
     * 将类转换为JSONObject
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> JSONObject toJSONObject(T t) {
        return JSONObject.parseObject(JSONObject.toJSONString(t));
    }
}
