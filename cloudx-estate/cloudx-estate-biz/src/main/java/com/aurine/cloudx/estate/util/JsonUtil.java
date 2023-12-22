package com.aurine.cloudx.estate.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-13
 * @Copyright:
 */
public class JsonUtil {

    /**
     * 将JSONObject 转化为 MultiValueMap ，主要用于RestTemplate 的DForm类型传输
     * @param jsonObject
     * @return
     */
    public static MultiValueMap toMultiValueMap(JSONObject jsonObject) {
        Iterator it = jsonObject.keySet().iterator();
        StringBuilder sb = new StringBuilder("{");
        while (it.hasNext()) {
            String key = (String) it.next();
            Object value = jsonObject.getString(key);
            if (value == null) {
                sb.append(JSON.toJSONString(key)).append(":").append(value).append(",");
            } else {
                List<String> list = Collections.singletonList(value.toString());
                sb.append(JSON.toJSONString(key)).append(":").append(JSON.toJSONString(list)).append(",");
            }
        }
        sb.append("}");
        sb.deleteCharAt(sb.length() - 2);
//        System.out.println(sb.toString());
        return JSON.parseObject(sb.toString(), LinkedMultiValueMap.class);
//        System.out.println(params);
    }
}
