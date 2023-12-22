package com.aurine.cloudx.push.util;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author: wrm
 * @Date: 2020/12/02 9:06
 * @Package: com.wxgzh.api
 * @Version: 1.0
 * @Remarks: 处理从微信收到的消息
 **/
@Service
public class MessageUtil {

    /**
     * 处理所有的事件和消息的回复
     *
     * @param requestMap
     * @return xml数据包
     */
    public static String getResponse(Map<String, String> requestMap) {
        System.out.println(requestMap);
        String msgType = requestMap.get("MsgType");
        //判断接收消息的类型并作出回复
        if ("event".equals(msgType)) {
            MenuUtil.dealEvent(requestMap);
        }
        return "success";
    }
}
