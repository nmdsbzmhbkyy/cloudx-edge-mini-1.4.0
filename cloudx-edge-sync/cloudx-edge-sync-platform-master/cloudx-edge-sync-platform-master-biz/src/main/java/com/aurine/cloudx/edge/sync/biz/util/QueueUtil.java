package com.aurine.cloudx.edge.sync.biz.util;


import cn.hutool.core.util.ObjectUtil;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.common.constant.QueueConstant;
import com.aurine.cloudx.edge.sync.common.entity.dto.TaskInfoDto;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class QueueUtil {

    /**
     * 数据入列
     * 30s后过期
     * @param taskInfoDto
     * @return
     */
    public static void inToQueue(String queueKey, TaskInfoDto taskInfoDto) {
        long resultCount = 0;
        if (ObjectUtil.isNotNull(taskInfoDto)) {
            RedisUtil.set(queueKey, taskInfoDto, 30);
            // 添加redis正在发送消息taskId
            RedisUtil.sSet(QueueUtil.getSendingTaskKey(taskInfoDto.getProjectUUID()), taskInfoDto.getTaskId());
            // 重试次数列表添加重试次数记录，从0开始
            RedisUtil.hset(QueueUtil.getRetireCountKey(taskInfoDto.getProjectUUID()), taskInfoDto.getTaskId(), 0);
        }
        log.info("队列已写入{}条，当前队列容量为：{},id为:{}", 1, resultCount, taskInfoDto.getProjectUUID());
    }

    /**
     * 获取队列当前对象
     *
     * @return
     */
    public static TaskInfoDto list(String serviceType, String queueName) {
        return (TaskInfoDto) RedisUtil.get(getQueueKey(serviceType, queueName));
    }


    /**
     * 获取当前队列长度
     *
     * @return
     */
    public static long getQueueSize(String serviceType, String queueName) {
        String queueKey = getQueueKey(serviceType, queueName);
        if (RedisUtil.hasKey(queueKey)) {
            return RedisUtil.lGetListSize(queueKey);
        }
        return 0;
    }

    /**
     * 同步方式获取1条最新的数据
     * 当队列中存在任务，且流量桶中存在token时，获取最新的一条下发请求数据
     *
     * @return
     */
    /**
     * 同步方式获取1条最新的数据
     * 当队列中存在任务，且流量桶中存在token时，获取最新的一条下发请求数据
     *
     * @return
     */
    public static TaskInfoDto getLatestSync(String serviceType, String queueName) {
        TaskInfoDto result = null;
        RedisUtil.syncLock(QueueConstant.QUEUE_LOCK, QueueConstant.QUEUE_LOCK_TTL);


        if (getQueueSize(serviceType, queueName) >= 1) {
            //是否要取这一条数据
            Object taskInfoDto = RedisUtil.lGetIndex(getQueueKey(serviceType, queueName), 0);
            result = (TaskInfoDto) taskInfoDto;
        } else {
            log.debug("队列中无任务可下发");
        }
        RedisUtil.unLock(QueueConstant.QUEUE_LOCK);
        return result;
    }

    public static String getQueueKey(String serviceType, String projectUuid) {
        if (OpenPushSubscribeCallbackTypeEnum.EVENT.name.equals(serviceType)) {
            return Constants.EVENT_QUEUE_KEY_PREFIX + projectUuid;
        } else {
            return Constants.QUEUE_KEY_PREFIX + projectUuid;
        }
    }

    /**
     * 获取redis 发送中id key值
     * @param projectUuid
     * @return
     */
    public static String getUuidMapKey(String projectUuid) {
        return Constants.UUID_MAP_PREFIX + projectUuid;
    }

    /**
     * 获取redis 发送中id key值
     * @param projectUuid
     * @return
     */
    public static String getSendingTaskKey(String projectUuid) {
        return Constants.SENDING_TASKID_LIST_PREFIX + projectUuid;
    }

    /**
     * 获取redis 重试次数 key值
     * @param projectUuid
     * @return
     */
    public static String getRetireCountKey(String projectUuid) {
        return Constants.RETRY_COUNT_KEY_PREFIX + projectUuid;
    }
}
