package com.aurine.cloudx.estate.util.delay;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import com.aurine.cloudx.estate.util.delay.service.DelayTaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.HashedWheelTimer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>延时任务工具类</p>
 *
 * @author : 王良俊
 * @date : 2021-08-28 10:30:58
 */
@Component
@Slf4j
public class TaskUtil implements ApplicationRunner {

    @Resource
    DelayTaskService delayTaskService;

    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    private static final ObjectMapper objectMapper = ObjectMapperUtil.instance();

    private static final CopyOnWriteArraySet<String> taskIdSet = new CopyOnWriteArraySet<>();

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    /**
     * 时间轮工具类
     */
    public static final HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();

    @Override
    public void run(ApplicationArguments args) {
        try {
            this.syncDelayTask();
        } catch (Exception e) {
            log.info("[延时任务] 任务初始化失败");
            e.printStackTrace();
        }
    }

    public void addDelayTaskAsync(BaseTask task) {
        fixedThreadPool.execute(() -> {
            this.addDelayTask(task);
        });
    }

    public void addDelayTask(BaseTask task) {
        try {
            task = delayTaskService.recordTask(task);
            task.setOriginTask(true);
            executeTask(task);
            taskIdSet.add(task.getTaskId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addDelayTaskNotRecord(BaseTask task) {
        try {
            task = delayTaskService.incrReceiveNumByTaskId(task.getTaskId());
            if (task == null) {
                return;
            }
            executeTask(task);
            taskIdSet.add(task.getTaskId());
        } catch (Exception e) {
            log.error("[延时任务] 任务同步失败：{}", task);
            e.printStackTrace();
        }
    }

    private void executeTask(BaseTask task) {
        log.info("[延时任务] 添加任务：{}", task);
        hashedWheelTimer.newTimeout(timeout -> {
            BaseTask taskId = delayTaskService.markTaskAsExecuted(task.getTaskId());
            // 任务未被删除且没有正在被执行
            if (taskId != null) {
                log.info("[延时任务] 任务即将执行：{}", task.toString());
                kafkaTemplate.send(task.getDelayTaskTopicEnum().getTopic(), objectMapper.writeValueAsString(task));
                log.info("[延时任务] 已发送到消息队列中");
            }
        }, getDelayTime(task), TimeUnit.SECONDS);
    }

    /**
     * <p>取消任务执行的方法</p>
     *
     * @param baseTask 延时任务具体实现类
     */
    private void cancelTask(BaseTask baseTask) {
        if (baseTask != null) {
            delayTaskService.removeByQuery(baseTask.getCancelQuery());
        }
    }

    public void removeDelayTask(BaseTask task) {
        taskIdSet.remove(task.getTaskId());
        delayTaskService.removeByTaskId(task.getTaskId());
    }

    public void resetTaskStatus(BaseTask task) {
        delayTaskService.markTaskAsNotExecuted(task.getTaskId());
    }

    public void syncDelayTask() {
        List<BaseTask> taskList = delayTaskService.findAll();
        log.info("[延时任务] 获取到任务总数：{}", taskList.size());
        if (CollUtil.isNotEmpty(taskList)) {
            taskList.forEach(task -> {
                if (!taskIdSet.contains(task.getTaskId())) {
                    fixedThreadPool.execute(() -> {
                        this.addDelayTaskNotRecord(task);
                    });
                }
            });
        }
        hashedWheelTimer.newTimeout(timeout -> {
            syncDelayTask();
        }, 30, TimeUnit.MINUTES);
    }

    public static Long getDelayTime(BaseTask task) {
        return Duration.between(LocalDateTime.now(), task.getDelayTime()).toMillis() / 1000;
    }

    public static boolean isToday(LocalDateTime dateTime) {
        return LocalDate.now().isEqual(dateTime.toLocalDate());
    }

    public static boolean isToday(LocalDate date) {
        return LocalDate.now().isEqual(date);
    }

}
