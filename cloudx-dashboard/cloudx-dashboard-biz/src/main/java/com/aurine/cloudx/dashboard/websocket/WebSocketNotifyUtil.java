package com.aurine.cloudx.dashboard.websocket;


import com.alibaba.fastjson.JSONObject;
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
    public static <T> void sendMessgae(String projectId) {
        log.info("给指定小区发送信息", projectId, projectId);

        for (WebSocketNotify webSocketNotify : WebSocketNotifyUtil.getSet()) {
            try {

                if (webSocketNotify.getProjectId().equals(projectId)) {
                    webSocketNotify.sendMessage(projectId);
                    log.info("[dashboard] 信息已发送给客户端{},{},{}", webSocketNotify.getProjectId());
                }

            } catch (IOException e) {
                continue;
            }
        }
    }
}
