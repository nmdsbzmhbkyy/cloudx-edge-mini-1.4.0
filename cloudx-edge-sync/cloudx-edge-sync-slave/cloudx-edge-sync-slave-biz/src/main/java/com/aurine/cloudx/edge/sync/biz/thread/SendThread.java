package com.aurine.cloudx.edge.sync.biz.thread;

import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.biz.transfer.mqtt.MqttConnector;
import com.aurine.cloudx.edge.sync.biz.util.QueueUtil;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.edge.sync.common.config.QueueConfig;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    public void startSendThread() {
        fixedThreadPool.execute(() -> {
            log.info("下发线程已启动");
            while (true) {
                //  redis查询的时候开始根据project_relation表查询项目uuid，根据这个uuid去做队列
                try {
                    sendMessage();
                    Thread.sleep(QueueConfig.getAdownThreadRate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void sendMessage() {
        List<String> uuidList = taskInfoService.getSendQueueList();
        //校验线程池是否有空闲线程
        for (String projectUuid : uuidList) {
            String queueKey = QueueUtil.getQueueKey(null, projectUuid);
            //从任务队列获取任务
            TaskInfoDto request = (TaskInfoDto) RedisUtil.get(queueKey);
            if (request != null) {
                //执行发送数据
                log.info("并发执行下发，项目为{}", projectUuid);
                // 调用mqtt接口
                mqttConnector.publishRequestMessage(request);
                this.updateRedisTaskInfo(projectUuid);
            }
        }

    };

    /**
     * 根据项目UUID更新redis数据
     *
     * @param queueKey redis主键
     */
    private void updateRedisTaskInfo(String queueKey) {
        if (RedisUtil.hasKey(queueKey)) {
            TaskInfoDto taskInfoDto = (TaskInfoDto) RedisUtil.get(queueKey);
            taskInfoDto.setRetriesCount(taskInfoDto.getRetriesCount() + 1);
            RedisUtil.set(queueKey, taskInfoDto);
        }
    }
}
