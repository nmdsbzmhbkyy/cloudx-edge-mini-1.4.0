package com.aurine.cloudx.edge.sync.biz.config;

import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.mqtt.MqttClient;
import com.aurine.cloudx.edge.sync.biz.service.ProjectRelationService;
import com.aurine.cloudx.edge.sync.biz.service.ScribeCallbackService;
import com.aurine.cloudx.edge.sync.biz.thread.DispatchThread;
import com.aurine.cloudx.edge.sync.biz.thread.EventDispatchThread;
import com.aurine.cloudx.edge.sync.biz.thread.EventSendThread;
import com.aurine.cloudx.edge.sync.biz.thread.SendThread;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.entity.po.ProjectRelation;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: wrm
 * @Date: 2021/12/24 15:35
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks: 初始化数据，按顺序执行
 **/
@Component
@Slf4j
public class InitConfig implements ApplicationRunner {

    @Resource
    private MqttClient mqttClient;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private DispatchThread dispatchThread;

    @Resource
    private EventDispatchThread eventDispatchThread;

    @Resource
    private SendThread sendThread;

    @Resource
    private EventSendThread eventSendThread;

    @Resource
    private ProjectRelationService projectRelationService;

    @Resource
    private ScribeCallbackService scribeCallbackService;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    /**
     * 初始化Constants
     * 向open平台注册回调地址
     * 查询一次入云级联数据，有数据则触发级联操作
     *
     * @param args
     */
    @Override
    public void run(ApplicationArguments args) {
        // 初始化constants
        initConstants();
        // 初始化mqttClient
        mqttClient.initMqttClient();
        //监听mqtt连接状态
        mqttClient.mqttListener();
        // 获取projectRelation所有uuid，mqtt订阅消息
        subscribeProjectRelationTopics();
        // 调用projectInfo接口获取并初始化项目关系,mqtt，open订阅数据
//        initBaseProjectInfo();
        // 初始化 mqtt发送队列服务
        initQueue();

        log.info("--------- init config success------------");
    }

    /**
     * 初始化constants数据
     */
    private void initConstants() {
        //初始化Constants
        Constants.edgeSyncTransferUrl = Constants.TRANSFER_IP + ":" + globalVariable.getEdgeSyncTransferPort();
        Constants.topicVersion = globalVariable.getVersion();
        Constants.localHostPort = globalVariable.getPort();
        Constants.appId = globalVariable.getAppId();
        Constants.imgUriPrefix = globalVariable.getImgUri();
    }

    /**
     * 获取正在级联或入云的项目
     * 查询数据库新增则保存relation关系
     *
     * @return uuid列表
     */
    @SneakyThrows
    public void initBaseProjectInfo() {
        try {
            // 初始化添加config订阅
            if (!RedisUtil.hasKey(Constants.UUID_SUBSCRIBE_CONFIG)) {
                R r = scribeCallbackService.addConfigScribeCallback();
                if (r.getCode() == 0) {
                    RedisUtil.set(Constants.UUID_SUBSCRIBE_CONFIG, true);
                }
            }
            // 获取级联项目列表
            List<ProjectRelation> projectInfoList = projectRelationService.getProjectInfoList();
            if (projectInfoList != null && projectInfoList.size() > 0) {
                for (ProjectRelation projectRelation : projectInfoList) {
                    // 新增关系
                    projectRelationService.addProjectRelation(projectRelation);

                    // 查询现有订阅信息
                    List<OpenPushSubscribeCallbackVo> scribeCallback = scribeCallbackService.getScribeCallback(projectRelation.getProjectUUID());
                    if (CollectionUtils.isEmpty(scribeCallback)) {
                        // 新增 OpenApi 订阅回调地址
                        scribeCallbackService.addScribeCallback(projectRelation.getProjectUUID());
                    }
                }
            }
        } catch (RuntimeException e) {
            log.info(e.getMessage());
            Thread.sleep(5000);
            initBaseProjectInfo();
        }
    }

    /**
     * 初始化消息发送队列
     */
    public void initQueue() {
        // 初始化通用消息发送队列
        dispatchThread.startDispatchThread();
        sendThread.startSendThread();
        // 初始化事件类型消息发送队列
        eventDispatchThread.startEventDispatchThread();
        eventSendThread.startEventSendThread();
    }

    /**
     * 获取projectRelation所有uuid
     * mqtt订阅消息
     */
    public void subscribeProjectRelationTopics() {
        mqttClient.addSubscribeTopics();
    }
}
