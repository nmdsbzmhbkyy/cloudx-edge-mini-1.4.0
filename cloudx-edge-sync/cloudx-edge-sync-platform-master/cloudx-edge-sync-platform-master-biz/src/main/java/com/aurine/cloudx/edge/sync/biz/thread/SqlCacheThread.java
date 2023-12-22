package com.aurine.cloudx.edge.sync.biz.thread;

import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.constant.SqlCacheConstants;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import com.aurine.cloudx.edge.sync.common.utils.SqlCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.aurine.cloudx.edge.sync.biz.constant.Constants.SYNC_DISPATCH_TASK_INFO_JOB_LOCK;
import static com.aurine.cloudx.edge.sync.biz.constant.Constants.SYNC_DISPATCH_TASK_INFO_LOCK_PREFIX;


/**
 * @description: Sql缓存处理线程
 * @author: wangwei
 * @date: 2021/12/27 17:14
 **/
@Slf4j
@Component
public class SqlCacheThread {
    private static final int MAX = 200;

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 定时任务处理缓存
     * */
    @Scheduled(fixedRate = 2000)
    public void dispatchCache() {
        RLock lock = redissonClient.getLock(SYNC_DISPATCH_TASK_INFO_JOB_LOCK);
        if (lock!=null) {
            try {
                boolean status = lock.tryLock();
                if (status) {
                    long size = SqlCacheUtil.size();
                    if (size>0) {
                        log.info("[清空SQL缓存] 定期入库SQL {}", size);
                        if (size>MAX) {
                            size = MAX;
                        }
                        List<TaskInfo> list = redisTemplate.opsForList().range(SqlCacheConstants.SQL_CACHE_KEY, 0, size);
                        taskInfoService.saveTaskInfo(list);

                        for (long i=0; i<size; i++) {
                            redisTemplate.opsForList().leftPop(SqlCacheConstants.SQL_CACHE_KEY);
                        }
                    }
                }
            }
            catch (Exception e) {
                log.error("Sql缓存处理定时任务异常", e);
            }
            finally {
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }
        else {
            log.error("获取分布式锁失败");
        }
    }
}
