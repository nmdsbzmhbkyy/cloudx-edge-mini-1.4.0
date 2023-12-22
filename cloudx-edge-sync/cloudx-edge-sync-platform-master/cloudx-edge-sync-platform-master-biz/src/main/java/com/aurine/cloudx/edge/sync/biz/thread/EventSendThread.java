package com.aurine.cloudx.edge.sync.biz.thread;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @Author: wrm
 * @Date: 2022/01/20 8:51
 * @Package: com.aurine.cloudx.edge.sync.biz.thread
 * @Version: 1.0
 * @Remarks:
 **/
@Component
@Slf4j
public class EventSendThread {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private MqttConnector mqttConnector;

    @Resource
    private InitConfig initConfig;

    //private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    @Scheduled(fixedRate = 2000)
    public void dispatchThread() {
        //  redis查询的时候开始根据project_relation表查询项目uuid，根据这个uuid去做队列
        if (initConfig.isInit()) {
            try {
                eventSendMessage();
            } catch (Exception e) {
                log.error("发送事件异常", e);
            }
        }
    }

    public void eventSendMessage() {
        Object o = RedisUtil.get(Constants.EVENT_SNED_PROJECT_UUID);
        String projectUuid;
        if (Objects.isNull(o)) {
            projectUuid = taskInfoService.getEventSendQueueList();
            if (StrUtil.isEmpty(projectUuid)) {
                return;
            }
            RedisUtil.set(Constants.EVENT_DISPATCH_PROJECT_UUID, projectUuid,300);
        } else {
            projectUuid = String.valueOf(o);
        }

        //校验线程池是否有空闲线程
        String queueKdy = QueueUtil.getQueueKey(OpenPushSubscribeCallbackTypeEnum.EVENT.name, projectUuid);
        //从任务队列获取任务
        TaskInfoDto request = (TaskInfoDto) RedisUtil.get(queueKdy);
        if (request != null) {
            //执行发送数据
            log.info("[{}] 事件消息 并发执行下发，项目为{}", request.getMsgId(), projectUuid);
            // 调用mqtt接口
            try {
                mqttConnector.publishRequestMessage(request);
                taskInfoService.updateRedisRetryCount(request.getTaskId(), request.getProjectUUID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
