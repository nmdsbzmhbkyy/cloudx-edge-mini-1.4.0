package com.aurine.cloudx.open.origin.util;


import com.aurine.cloudx.open.origin.websocket.WebSocketNotify;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class WebSocketNotifyUtil {


    /**
     * WebSocket存储在服务器内存中，未来需考虑集群问题
     */
    private static CopyOnWriteArraySet<WebSocketNotify> webSocketSet = new CopyOnWriteArraySet<WebSocketNotify>();


    public static void add(WebSocketNotify webSocketNotify) {
        WebSocketNotifyUtil.webSocketSet.add(webSocketNotify);
    }

    public static void remove(WebSocketNotify webSocketNotify) {
        WebSocketNotifyUtil.webSocketSet.remove(webSocketNotify);
    }

    public static CopyOnWriteArraySet<WebSocketNotify> getSet() {
        return webSocketSet;
    }


    /**
     * 发送给指定小区
     * @param projectId   项目ID
     */
    public static <T> void sendMessgae(String projectId, String data) {
         log.info("给指定小区发送信息", projectId, projectId);

        for (WebSocketNotify webSocketNotify : WebSocketNotifyUtil.getSet()) {
            try {

                if (webSocketNotify.getProjectId()== null){
                    continue;
                }
                if (webSocketNotify.getProjectId().equals(projectId)) {
                    webSocketNotify.sendMessage(data);
                    log.info("[dashboard] 信息已发送给客户端{}, {}, {}", data);
                }

            } catch (IOException e) {
                continue;
            }
        }
    }
    /**
     * 发送给指定人员
     * @param orderId   人员id
     */
    public static <T> void sendMessagePerson(String orderId, String data) {
        log.info("给指定订单信息", orderId, orderId);

        for (WebSocketNotify webSocketNotify : WebSocketNotifyUtil.getSet()) {
            try {

                if (webSocketNotify.getOrderId()== null){
                    break;
                }
                if (webSocketNotify.getOrderId().equals(orderId)) {
                    webSocketNotify.sendMessage(data);
                    log.info("[dashboard] 信息已发送给客户端{}, {}, {}", webSocketNotify.getOrderId());
                }

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }



}
