package com.aurine.cloudx.estate.cert.service;

 
import com.aurine.cloudx.estate.cert.config.CertAdownConfig;
import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.util.CertRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 凭证下发服务
 * @author zengxn
 * */
@Slf4j
@Service
public class CertAdwonService {
    @Autowired
    private CertAdownQueueService certAdownQueueService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定时凭证下发调度
     * */
    @Scheduled(fixedDelay = 2000)
    public void dispatchTask() {
        try {
            boolean locked = CertRedisUtil.lock(AdownConstant.CERT_ADOWN_DISPATCH_TASK_LOCK, CertAdownConfig.adownTimeoutValue);
            if (locked) {
                try{
                    Set<String> set = redisTemplate.opsForSet().members(AdownConstant.DEVICE_DISPATCH_QUEUE_KEY);
                    for (String deviceId : set) {
                        try {
                            certAdownQueueService.adown(deviceId);
                        } catch (Exception e) {
                            log.error("凭证下发调度任务执行异常：", e);
                        }
                    }
                }
                catch (Exception e) {
                    log.error("凭证下发调度任务执行异常：", e);
                }
                CertRedisUtil.unLock(AdownConstant.CERT_ADOWN_DISPATCH_TASK_LOCK);
            }
        }
        catch (Exception e) {
            log.error("定时凭证下发调度执行异常：", e);
        }
    }

    /**
     * 定时超时检查
     * */
    @Scheduled(fixedDelay = 1000)
    public void timeoutMonitorTask() {
        try {
            //获取当前时间
            LocalDateTime now = LocalDateTime.now();
            //添加分布式锁，开始超时遍历
            boolean result = CertRedisUtil.lock(AdownConstant.CERT_ADOWN_TIMEOUT_CHECK_LOCK,  10);
            if (result) {
                //加锁成功，执行超时检查
                //获取到期时间
                LocalDateTime dueTime = now.minusSeconds(CertAdownConfig.getAdownTimeoutValue());
                Set<String> keys = redisTemplate.keys(AdownConstant.CERT_ADOWN_PROCESSING_REQUEST_PREFIX + "*");
                for (String key : keys) {
                    Object obj = redisTemplate.opsForValue().get(key);
                    if (obj!=null) {
                        SysCertAdownRequest request = (SysCertAdownRequest) obj;
                        if (request.getDownloadingTime().isBefore(dueTime)) {
                            //凭证下发超时，从处理队列移除
                            redisTemplate.delete(key);
                            if (request.getRetry()<CertAdownConfig.adownTimeoutRetryTime) {
                                certAdownQueueService.retryAdown(request);
                            }
                            else{
                                certAdownQueueService.retryOverTimes(request);
                            }
                            //分布式锁续约
                            redisTemplate.expire(AdownConstant.CERT_ADOWN_TIMEOUT_CHECK_LOCK, 10, TimeUnit.SECONDS);
                        }
                    }
                }
            }
            //释放锁
            redisTemplate.delete(AdownConstant.CERT_ADOWN_TIMEOUT_CHECK_LOCK);
        } catch (Exception e) {
            log.error("执行凭证下发超时检查异常：", e);
        }
    }
}
