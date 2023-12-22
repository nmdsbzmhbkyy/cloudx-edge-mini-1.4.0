package com.aurine.cloudx.open.origin.websocket;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.open.origin.feign.RemoteUmsTokenService;
import com.aurine.cloudx.open.origin.service.ProjectWebSocketService;
import com.aurine.cloudx.open.origin.util.WebSocketNotifyUtil;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@ServerEndpoint("/notify")
@Slf4j
@Data
@Component
public class WebSocketNotify {


    public static ProjectWebSocketService projectWebSocketService;
    public static RemoteUmsTokenService remoteUmsTokenService;


    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 项目ID
     */
    private String projectId = null;

    /**
     * 用户id
     */
    private String orderId = null;
    /**
     * token
     */
    private String token = null;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
//        webSocketSet.add(this);
        addOnlineCount();           //在线数加1
        Map<String, String> queryParamMap = this.getQueryParam(session.getQueryString());
        this.token = queryParamMap.get("token")==null? "123":queryParamMap.get("token");
        //this.token = "8a77b3b7-0a5d-4a67-a599-83271a31118b";
        R<Boolean> judgment = remoteUmsTokenService.judgment(token, SecurityConstants.FROM_IN);
        if (!judgment.getData()) {
            try {
                sendMessage(JSONObject.toJSONString(R.ok(Boolean.FALSE,"token无效")));
                this.onClose();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {

            try {
                log.info("[dashboard][websocket] 有新窗口开始监听:{},当前在线客户端为" + getOnlineCount(), queryParamMap);
                this.projectId = queryParamMap.get("projectId");
                this.orderId = queryParamMap.get("orderId");
                if (projectId!=null){
                    ProjectContextHolder.setProjectId(Integer.parseInt(projectId));
                    sendMessage(JSONObject.toJSONString(projectWebSocketService.findNumByProjectId()));
                }else {
                    sendMessage("小伙子 你这不是个项目");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        WebSocketNotifyUtil.add(this);   //加入set中
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        WebSocketNotifyUtil.remove(this);  //从set中删除

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
        log.info("[dashboard][websocket] 收到来自窗口" + projectId + "的信息:" + message);
            try {
                sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
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
        log.info("[dashboard] 信息已发送给客户端{}, {}, {}",message);
        this.session.getBasicRemote().sendText(message);
    }

    /*
     */

    /**
     * 群发自定义消息
     *//*
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
    }*/
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketNotify.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketNotify.onlineCount==0){
            WebSocketNotify.onlineCount=1;
        }
        WebSocketNotify.onlineCount--;
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
