package com.aurine.cloudx.estate.util.delay;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.aurine.cloudx.estate.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Consumer;


/**
 * <p>
 *
 * </p>
 *
 * @ClassName: DelayTaskLockUtil
 * @author: 王良俊 <>
 * @date: 2020年11月27日 上午08:42:20
 * @Copyright:
 */
@Slf4j
@Deprecated
public class DelayTaskLockUtil {

    private volatile static DelayTaskLockUtil delayTaskLockUtilInstance = null;
    /**
     * 写锁键值
     * */
    public final static String LOCK_KEY = "delayDataWriteLock";
    /**
     * redis缓存的键值
     * */
    public final static String REDIS_DATA_KEY = "delayTaskData";
    /**
     * 用于序列化和反序列化json
     * */
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 线程安全的HashMap 本地缓存{} 延时任务数据
     */
    private static ConcurrentHashMap<String, DelayTask> delayTaskMap;
    /**
     * java线程池
     * */
    private static final ThreadPoolExecutor threadPoolExecutor;

    static {
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        PolymorphicTypeValidator polymorphicTypeValidator = objectMapper.getPolymorphicTypeValidator();
        // 这里在序列化成json字符串的时候会添加对应的对象信息用以在方序列化的时候能够反序列化map中的对象
        objectMapper.activateDefaultTyping(polymorphicTypeValidator, ObjectMapper.DefaultTyping.NON_FINAL);

        BlockingQueue<Runnable> linkedBlockingDeque = new LinkedBlockingDeque<Runnable>(100);
        threadPoolExecutor = new ThreadPoolExecutor(2, 30, 30,
                TimeUnit.SECONDS, linkedBlockingDeque,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private DelayTaskLockUtil() {}

    public static DelayTaskLockUtil instance() {
        if (delayTaskLockUtilInstance == null) {
            synchronized (DelayTaskLockUtil.class) {
                if (delayTaskLockUtilInstance == null) {
                    delayTaskLockUtilInstance = new DelayTaskLockUtil();
                }
            }
        }
        return delayTaskLockUtilInstance;
    }

    /**
     * <p>
     * 执行整个流程的方法（这里用异步执行）
     * </p>
     */
    public void runAsync(Consumer<ConcurrentHashMap<String, DelayTask>> action) {
        threadPoolExecutor.execute(() -> {
            boolean isRecursiveCall = false;
            // 这里是为了避免递归调用 导致一直在等待上锁状态
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            for (StackTraceElement ste : stack) {
                if (ste.getClassName().contains("AbstractDelayTaskLockUtil") && ste.getMethodName().contains("execute")) {
                    isRecursiveCall = true;
                    break;
                }
            }

            if (!isRecursiveCall) {
                lock();
            }
            genDelayTaskMap();
            // 这里执行具体的业务
            action.accept(delayTaskMap);

            if (!isRecursiveCall) {
                updateRedisData();
                log.info("[{} 延时任务] 取消写锁", Thread.currentThread().getId());
                unLock();
            }
        });
    }

    private void updateRedisData() {
        try {
            String data = objectMapper.writeValueAsString(delayTaskMap);
            log.info("[{} 延时任务] 更新redis缓存数据：{}", Thread.currentThread().getId(), data);
            RedisUtil.set(REDIS_DATA_KEY, data);
        } catch (JsonProcessingException e) {
            log.info("[{} 延时任务] redis缓存更新失败", Thread.currentThread().getId());
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 从redis中取出{} 延时任务的备份数据并转换会对应的map数据
     * </p>
     */
    private void genDelayTaskMap() {
        if (delayTaskMap == null) {
            delayTaskMap = new ConcurrentHashMap<>(Maps.newHashMapWithExpectedSize(4000));
        }
        String delayTaskData = (String) RedisUtil.get(REDIS_DATA_KEY);
        log.info("[{} 延时任务] 当前缓存在redis中的数据：{}", Thread.currentThread().getId(), delayTaskData);
        try {
            if (StrUtil.isNotBlank(delayTaskData) && delayTaskData.length() > 2) {
                // 这里合并了redis上和本地的map数据
                delayTaskMap.putAll(objectMapper.readValue(delayTaskData, new TypeReference<ConcurrentHashMap<String, DelayTask>>() {
                }));
            }
        } catch (JsonProcessingException e) {
            delayTaskMap = new ConcurrentHashMap<>();
            e.printStackTrace();
        }
    }

    /**
     * <p>
     * 设置redis{} 延时任务的写锁，过期时间为20秒
     * </p>
     */
    private void lock() {
        log.info("[{} 延时任务] 挂写锁", Thread.currentThread().getId());
        // 这里每过0.1到0.17秒就去尝试上写锁
        Random random = new Random();
        int times = 0;
        do {
            times++;
            if (times >= 200) {
                log.info("[{} 延时任务] 写锁尝试次数超过200次", Thread.currentThread().getId());
                break;
            }
            try {
                Thread.sleep(100 + random.nextInt(70));
                if (RedisUtil.lock(LOCK_KEY, 1, 20)) {
                    log.info("[{} 延时任务] 挂写锁-成功", Thread.currentThread().getId());
                    break;
                }
            } catch (InterruptedException e) {
                log.error("[{} 延时任务] 挂写锁-失败", Thread.currentThread().getId());
                e.printStackTrace();
            }

        } while (true);
    }

    /**
     * <p>
     * 取消写锁
     * </p>
     */
    private void unLock() {
        delayTaskMap.clear();
        RedisUtil.delete(LOCK_KEY);
    }

}
