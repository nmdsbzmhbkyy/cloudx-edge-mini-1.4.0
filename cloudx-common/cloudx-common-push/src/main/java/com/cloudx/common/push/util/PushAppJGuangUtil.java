package com.cloudx.common.push.util;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class PushAppJGuangUtil {

    /**
     * 极光   推送信息
     * @param content
     * @param idList
     * @param extras
     * @param deployStatus      环境 1：开发环境 其他：生产环境
     * @param masterSecret
     * @param appKey
     * @return
     */
    public static boolean JGuangPush(String content, List<String> idList, Map<String, String> extras, Integer deployStatus, String masterSecret, String appKey) {
        JPushClient jpushClient = new JPushClient(masterSecret, appKey);
        PushPayload payload = BuildPushObject_id_message(content, idList, extras, deployStatus);
        try {
            jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            log.error("极光服务器异常");
            return false;
        } catch (APIRequestException e) {
            // System.out.println("Error response from JPush server. Should
            // review and fix it. " + e);
            // System.out.println("HTTP Status: " + e.getStatus());
            // System.out.println("Error Code: " + e.getErrorCode());
            // System.out.println("Error Message: " + e.getErrorMessage());
            // System.out.println("Msg ID: " + e.getMsgId());
            log.error(e.getErrorMessage());
            return false;
        }
        return true;
    }

    private static PushPayload BuildPushObject_id_message(String message, List<String> idList, Map<String, String> extras, Integer deployStatus) {
        if (deployStatus == null) {
            return PushPayload.newBuilder().setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(idList))
//                    .setAudience(Audience.all())
                    .setMessage(Message.newBuilder().setMsgContent(message).addExtras(extras).build()).build();
        } else {
            if (deployStatus == 1) {
                return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(idList))
                        .setMessage(Message.newBuilder().setMsgContent(message).addExtras(extras).build())
                        .setOptions(Options.newBuilder().setApnsProduction(false).build()).build();
            } else {
                return PushPayload.newBuilder().setPlatform(Platform.all()).setAudience(Audience.registrationId(idList))
                        .setMessage(Message.newBuilder().setMsgContent(message).addExtras(extras).build())
                        .setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
            } // 设置ios平台环境 True 表示推送生产环境，False 表示要推送开发环境 默认是开发
        }
    }

    public static void main(String[] args) {
        String master="9c2f79a4c9e2000e054e5e60";
        String appKey = "69b98a47a059d2d3a7b1dcb5";
        List<String> list = new ArrayList<>();
        list.add("2f7f808e22919e687ac4426a5960e18a");
        Map<String,String> exMap = new HashMap<>();
        exMap.put("msg","1111");
        boolean result = PushAppJGuangUtil.JGuangPush("推送内容",list,exMap,null,master,appKey);
        System.out.println(result);
    }

}
