package com.aurine.cloudx.dashboard.websocket;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 信息发送类
 * 请求参数为：queryParam
 *
 * @param projectId   监听的项目id，如果要查询多个项目，以‘-’为分隔符，进行多项目监听
 * @param serviceName 监听的的服务，不输入默认监听所有服务
 * @param version     监听的的服务版本
 * @description:
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-21
 * @Copyright:
 */
@Slf4j
@Component
@ServerEndpoint("/dashboard")
@Data
public class WebSocketServer {

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
//    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();


    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 服务名
     */
    private String serviceName = "";
    /**
     * 项目ID
     */
    private String projectId = null;
    /**
     * 版本号
     */
    private String version = "";

//    /**
//     * 连接建立成功调用的方法
//     */
//    @OnOpen
//    public void onOpen(Session session, @PathParam("projectId") Integer projectId, @PathParam("serviceName") String serviceName, @PathParam("version") String version) {
//        this.session = session;
////        webSocketSet.add(this);
//        WebSocketUtil.add(this);   //加入set中
//        addOnlineCount();           //在线数加1
//        log.info("[dashboard][websocket] 有新窗口开始监听:projectId = {},serviceName = {},version = {},当前在线客户端为" + getOnlineCount(), projectId, serviceName, version);
//        this.serviceName = serviceName;
//        this.projectId = projectId;
//        this.version = version;
//
//        log.info("获取到的参数：{}", queryParam);
//    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
//        webSocketSet.add(this);
        addOnlineCount();           //在线数加1

        Map<String, String> queryParamMap = this.getQueryParam(session.getQueryString());

        log.info("[dashboard][websocket] 有新窗口开始监听:{},当前在线客户端为" + getOnlineCount(), queryParamMap);
        this.serviceName = queryParamMap.get("serviceName");
        this.projectId = queryParamMap.get("projectId");
        this.version = queryParamMap.get("version");

        WebSocketUtil.add(this);   //加入set中
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WebSocketUtil.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("[dashboard][websocket] 有一连接关闭！当前在线客户端为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("[dashboard][websocket] 收到来自窗口" + serviceName + "的信息:" + message);
        //群发消息
        for (WebSocketServer item : WebSocketUtil.getSet()) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("[dashboard][websocket] 非正常关闭,发生错误!====>" + error.toString() + "当前在线客户端为" + getOnlineCount());
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("serviceName") String serviceName) throws IOException {
        log.info("[dashboard][websocket] 推送消息到窗口" + serviceName + "，推送内容:" + message);
        for (WebSocketServer item : WebSocketUtil.getSet()) {
            try {
                //这里可以设定只推送给这个serviceName的，为null则全部推送
                if (serviceName == null) {
                    item.sendMessage(message);
                } else if (item.serviceName.equals(serviceName)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    /**
     * 获取query参数
     *
     * @param queryParamStr
     * @return
     */
    private Map<String, String> getQueryParam(String queryParamStr) {
        Map<String, String> queryParamMap = new HashMap<>();
        String[] queryParamArray;

        if (StringUtils.isNotEmpty(queryParamStr)) {

            if (queryParamStr.indexOf("&") >= 0) {
                queryParamArray = queryParamStr.split("&");
            } else {
                queryParamArray = new String[1];
                queryParamArray[0] = queryParamStr;
            }

            String[] paramArray;
            for (String param : queryParamArray) {
                if (param.indexOf("=") >= 0) {
                    paramArray = param.split("=");
                    queryParamMap.put(paramArray[0], paramArray[1]);
                }
            }
        }

        return queryParamMap;
    }
}
