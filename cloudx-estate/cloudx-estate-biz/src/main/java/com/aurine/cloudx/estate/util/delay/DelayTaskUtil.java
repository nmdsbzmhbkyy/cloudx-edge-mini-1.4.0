package com.aurine.cloudx.estate.util.delay;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.common.data.project.ProjectContextHolder;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: DelayTaskUtil
 * @author: 王良俊
 * @date: 2020年10月09日 上午08:40:45
 * @Copyright:
 */
@Slf4j
@Component
@Deprecated
public class DelayTaskUtil
//        implements ApplicationRunner
{

    /**
     * netty的时间轮 实现延时任务的主要工具
     * */
    public static final HashedWheelTimer hashedWheelTimer;

    /**
     * 存储了延时任务的对象 用来取消延时任务
     * */
    private static final ConcurrentHashMap<String, Timeout> timeoutMap;

    /**
     * 用于序列化和反序列化json
     * */
    private static final ObjectMapper objectMapper;

    private static KafkaTemplate<String, String> kafkaTemplate;

    private volatile static DelayTaskUtil delayTaskUtilInstance = null;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateAutowired;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    static {
        hashedWheelTimer = new HashedWheelTimer();
        timeoutMap = new ConcurrentHashMap<>();
        objectMapper = ObjectMapperUtil.instance();
    }

    private DelayTaskUtil() {}

    public static DelayTaskUtil instance() {
        if (delayTaskUtilInstance == null) {
            synchronized (DelayTaskUtil.class) {
                if (delayTaskUtilInstance == null) {
                    delayTaskUtilInstance = new DelayTaskUtil();
                }
            }
        }
        return delayTaskUtilInstance;
    }

    /**
     * <p>
     * 判断是否是今天
     * </p>
     *
     * @param dateTime 要进行判断的日期
     */
    public static boolean isToday(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
        return LocalDateTime.now().format(dateTimeFormatter).equals(dateTime.format(dateTimeFormatter));
    }

    public static Set<String> getAllTask() {
        return timeoutMap.keySet();
    }

    public static String genKey(DelayTask delayTask) {
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return delayTask.getTaskTopic() + "," + delayTask.getDelayTime().format(timeFormat) + "," + delayTask.getProjectId();
    }

    /**
     * <p>
     * 服务器启动完成后根据redis缓存中的延时任务对延时任务进行生成
     * </p>
     */
    /*@Override
    public void run(ApplicationArguments args) {
        log.info("[{} 延时任务] 初始化", Thread.currentThread().getId());
        DelayTaskUtil.kafkaTemplate = kafkaTemplateAutowired;
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PolymorphicTypeValidator polymorphicTypeValidator = objectMapper.getPolymorphicTypeValidator();
        // 这里在序列化成json字符串的时候会添加对应的对象信息用以在方序列化的时候能够反序列化map中的对象
        objectMapper.activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL);

        syncData();
    }*/

    public void addDelayTaskList(List<DelayTask> taskList) {
        DelayTaskLockUtil.instance().runAsync(delayTaskMap -> {
            if (CollUtil.isNotEmpty(taskList)) {
                taskList.forEach(delayTask -> {
                    // 如果之前没有添加过这个延时任务则添加
                    if (!timeoutMap.containsKey(genKey(delayTask)) && delayTaskMap.get(genKey(delayTask)) == null) {
                        addDelayTask(delayTask, delayTaskMap);
                    }
                });
            }
        });
    }

    public void addDelayTask(DelayTask delayTask) {
        DelayTaskLockUtil.instance().runAsync(delayTaskMap -> {
            if (delayTask.getProjectId() == 0) {
                delayTask.setProjectId(ProjectContextHolder.getProjectId());
            }
            addDelayTask(delayTask, delayTaskMap);
        });

    }

    /**
     * <p>
     * 延时任务数据同步任务
     * </p>
     */
    private void syncData() {
        threadPoolTaskExecutor.execute(() -> {
            log.info("[{} 延时任务] 开始同步任务", Thread.currentThread().getId());
            if (RedisUtil.hasKey(DelayTaskLockUtil.REDIS_DATA_KEY)) {
                String delayTaskData = (String) RedisUtil.get(DelayTaskLockUtil.REDIS_DATA_KEY);
                log.info("[{} 延时任务] redis中缓存的数据：{}", Thread.currentThread().getId(), delayTaskData);
                try {
                    if (!StrUtil.isEmpty(delayTaskData) && delayTaskData.length() > 2) {
                        ConcurrentHashMap<String, DelayTask> remoteDelayTaskMap = objectMapper.readValue(delayTaskData,
                                new TypeReference<ConcurrentHashMap<String, DelayTask>>() {
                                });
                        // 获取本地内存中的延时任务key集合
                        ConcurrentHashMap.KeySetView<String, Timeout> localKey = timeoutMap.keySet();
                        // 获取redis缓存中备份的延时任务key集合
                        ConcurrentHashMap.KeySetView<String, DelayTask> remoteKey = remoteDelayTaskMap.keySet();
                        // 获取到本地没有的延时任务用于任务的添加
                        remoteKey.removeAll(localKey);
                        remoteKey.forEach(key -> {
                            DelayTask delayTask = remoteDelayTaskMap.get(key);
                            if (delayTask.getProjectId() == 0) {
                                delayTask.setProjectId(ProjectContextHolder.getProjectId());
                            }
                            log.info("[{} 延时任务] 添加了一个任务：{}", Thread.currentThread().getId(), delayTask.toString());
                            // 如果是重复任务这里先取消原来的任务
                            Duration between = Duration.between(LocalDateTime.now(), delayTask.getDelayTime());
                            long seconds = between.toMillis() / 1000;
                            log.info("[{} 延时任务] 当前距任务执行秒数：{}", Thread.currentThread().getId(), seconds);
                            Timeout currentTimeout = hashedWheelTimer.newTimeout(timeout -> {
                                // 判断当前下发任务是否被取消(可能在别的服务器上取消的)
                                log.info("[{} 延时任务] 发送消息，主题：{}", Thread.currentThread().getId(), delayTask.getTaskTopic());
                                kafkaTemplate.send(delayTask.getTaskTopic(), objectMapper.writeValueAsString(delayTask));
                            }, seconds, TimeUnit.SECONDS);
                            if (seconds > 0) {
                                timeoutMap.put(genKey(delayTask), currentTimeout);
                            } else {
                                timeoutMap.remove(genKey(delayTask));
                            }
                        });
                    }
                } catch (JsonProcessingException e) {
                    log.info("[{} 延时任务] 同步失败：{}", Thread.currentThread().getId(), e.getMessage());
                    e.printStackTrace();
                }
            }
            log.info("[{} 延时任务] 同步完成", Thread.currentThread().getId());
        });

        // 这里每30分钟获取一次最新的数据
        hashedWheelTimer.newTimeout(timeout -> {
            syncData();
        }, 30, TimeUnit.MINUTES);
    }

    /**
     * <p>
     * 生成延时任务
     * </p>
     *
     * @param delayTaskMap 要跟新的延时任务map
     * @param delayTask    延时任务
     */
    private void addDelayTask(DelayTask delayTask, Map<String, DelayTask> delayTaskMap) {
        String key = genKey(delayTask);
        log.info("[{} 延时任务] 添加任务：{}", Thread.currentThread().getId(), delayTask.toString());
        Duration between = Duration.between(LocalDateTime.now(), delayTask.getDelayTime());
        long seconds = between.toMillis() / 1000;
        log.info("[{} 延时任务] 当前距任务执行秒数：{}", Thread.currentThread().getId(), seconds);
        Timeout currentTimeout = hashedWheelTimer.newTimeout(timeout -> {
            log.info("[{} 延时任务] 发送消息，主题：{}", Thread.currentThread().getId(), delayTask.getTaskTopic());
            kafkaTemplate.send(delayTask.getTaskTopic(), objectMapper.writeValueAsString(delayTask));
        }, seconds, TimeUnit.SECONDS);
        if (seconds <= 0) {
            timeoutMap.remove(key);
            delayTaskMap.remove(key);
        } else {
            timeoutMap.put(key, currentTimeout);
            delayTaskMap.put(key, delayTask);
        }
    }

    /**
     * <p>
     * 取消延时任务
     * </p>
     *
     * @param delayTaskList 要取消的延时任务列表
    */
    public void cancelTask(List<DelayTask> delayTaskList) {
        log.info("[{} 延时任务] 取消任务：{}", Thread.currentThread().getId(), delayTaskList.toString());
        DelayTaskLockUtil.instance().runAsync(delayTaskMap -> {
            delayTaskList.forEach(delayTask -> {
                String key = genKey(delayTask);
                Timeout timeout = timeoutMap.get(key);
                if (timeout != null) {
                    timeout.cancel();
                }
                delayTaskMap.remove(key);
            });
        });
    }

    /**
     * <p>
     * 从延时任务缓存中删除某个延时任务
     * </p>
     *
     * @param keyList 所要删除的延时任务缓存的key列表
    */
    public void removeTaskMap(List<String> keyList) {
        DelayTaskLockUtil.instance().runAsync(delayTaskMap -> {
            keyList.forEach(delayTaskMap::remove);
        });
    }

}