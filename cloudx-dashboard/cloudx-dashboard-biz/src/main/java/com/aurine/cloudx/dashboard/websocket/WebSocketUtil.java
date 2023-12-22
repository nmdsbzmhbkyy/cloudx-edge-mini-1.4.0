package com.aurine.cloudx.dashboard.websocket;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.dashboard.constant.VersionEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description: WebScoket Session 管理工具
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24
 * @Copyright:
 */
@Slf4j
public class WebSocketUtil {
    /**
     * WebSocket存储在服务器内存中，未来需考虑集群问题
     */
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();


    public static void add(WebSocketServer webSocketServer) {
        WebSocketUtil.webSocketSet.add(webSocketServer);
    }

    public static void remove(WebSocketServer webSocketServer) {
        WebSocketUtil.webSocketSet.remove(webSocketServer);
    }

    public static CopyOnWriteArraySet<WebSocketServer> getSet() {
        return webSocketSet;
    }

    /**
     * 给订阅客户端推送信息
     *
     * @param message     推送的信息
     * @param projectId   项目ID
     * @param serviceName 服务名称
     * @param version     服务版本
     */
    public static <T> void sendMessgae(T message, String projectId, String serviceName, String version) {
        log.info("[dashboard] 给订阅了 {}-{}-{}的 客户端 推送消息{}", projectId, serviceName, version, message);

        for (WebSocketServer webSocketServer : WebSocketUtil.getSet()) {
            try {

                if (serviceFilter(webSocketServer, projectId, serviceName, version)) {
                    webSocketServer.sendMessage(JSONObject.toJSONString(message));
                    log.info("[dashboard] 信息已发送给客户端{},{},{}", webSocketServer.getProjectId(), webSocketServer.getServiceName(), webSocketServer.getVersion());
                }

            } catch (IOException e) {
                continue;
            }
        }
    }


    private static boolean serviceFilter(WebSocketServer webSocketServer, String projectId, String serviceName, String version) {
        boolean projectFlag = serviceFilter(webSocketServer.getProjectId(), projectId);//项目是否匹配
        boolean serviceNameFlag = false;//服务名是否匹配
        boolean versionFlag = false;//版本号是否匹配

        //如果未定义服务名，默认给所有订阅版本为V1的客户端推送信息
        if (StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(webSocketServer.getServiceName())) {
            serviceNameFlag = true;
            versionFlag = StringUtils.equals(VersionEnum.V1.number, version);
        } else {
            /**
             * 如果服务名存在定义，找到对应服务名设定的版本号
             * :serviceName1-serviceName2
             * :version1-version2
             */
            String[] serviceNameArray = webSocketServer.getServiceName().split("-");
            int serviceNameIndex = ArrayUtil.indexOf(serviceNameArray, serviceName);


            if (serviceNameIndex == -1) {
                return false;//服务不匹配
            } else {
                serviceNameFlag = true;
            }

            //匹配版本号
            if (StringUtils.isNotEmpty(webSocketServer.getVersion())) {
                //会话定义了版本信息
                String[] versionArray = webSocketServer.getServiceName().split("-");

                boolean useUniformVerson = versionArray.length == 1; //是否使用统一版本，当版本的设置值仅为1个参数时，认为所有的服务均使用该版本

                if (useUniformVerson) {
                    //所有的服务均使用该版本
                    versionFlag = StringUtils.equals(versionArray[0], version);
                } else {
                    if (serviceNameIndex >= versionArray.length) {
                        //如果对应服务为设置版本号，则默认为版本1
                        versionFlag = StringUtils.equals(VersionEnum.V1.number, version);
                    } else {
                        versionFlag = StringUtils.equals(versionArray[serviceNameIndex], version);
                    }
                }

            } else {//会话未定义版本信息，默认为版本1
                versionFlag = StringUtils.equals(VersionEnum.V1.number, version);
            }
        }

        return projectFlag && serviceNameFlag && versionFlag;
    }

    /**
     * 消息发送过滤器，根据websocket的参数和过滤参数，对群发客户端会话进行过滤
     *
     * @param websocketParam
     * @param filterParam
     * @return
     */
    private static boolean serviceFilter(String websocketParam, String filterParam) {

        //会话参数为空，表示当前会话接收所有该参数信息
        if (StringUtils.isEmpty(websocketParam)) {
            return true;
        }

        //过滤参数为空，表示当前推送信息可以发送给所有会话
        if (StringUtils.isEmpty(filterParam)) {
            return true;
        }

        /**
         * 当过滤参数匹配会话参数时，允许发送
         * websocketParam 格式为 value 或 value-value-value3
         */
        if (websocketParam.indexOf(filterParam) >= 0) {
            return true;
        } else {
            return false;
        }

    }


}
