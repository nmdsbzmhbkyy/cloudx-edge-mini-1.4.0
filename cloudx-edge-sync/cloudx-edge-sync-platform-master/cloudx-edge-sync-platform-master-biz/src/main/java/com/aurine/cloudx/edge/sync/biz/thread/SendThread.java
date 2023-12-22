package com.aurine.cloudx.edge.sync.biz.thread;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
public class SendThread {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private MqttConnector mqttConnector;

    @Resource
    private InitConfig initConfig;

    //private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    @Scheduled(fixedRate = 2000)
    public void dispatchThread() {
        try {
            if (initConfig.isInit()) {
                sendMessage();
            }
        } catch (Exception e) {
            log.error("发送消息异常", e);
        }
    }

    public void sendMessage() {

        Object o = RedisUtil.get(Constants.SEND_PROJECT_UUID);
        String projectUuid;
        if (Objects.isNull(o)) {
            projectUuid = taskInfoService.getSendQueueList();
            if (StrUtil.isEmpty(projectUuid)) {
                return;
            }
            RedisUtil.set(Constants.SEND_PROJECT_UUID, projectUuid,300);
        } else {
            projectUuid = String.valueOf(o);
        }

        //校验线程池是否有空闲线程
        String queueKey = QueueUtil.getQueueKey(null, projectUuid);
        //从任务队列获取任务
        TaskInfoDto request = (TaskInfoDto) RedisUtil.get(queueKey);
        if (request != null) {
            //执行发送数据
            log.info("[{}] 并发执行下发，项目为{}", request.getMsgId(), projectUuid);
            // 调用mqtt接口
            try {
                //log.info("并发执行下发，request：{}", JSON.toJSONString(request));
                mqttConnector.publishRequestMessage(request);
                taskInfoService.updateRedisRetryCount(request.getTaskId(), request.getProjectUUID());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
