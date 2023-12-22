package com.aurine.cloudx.estate.cert.service;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.cert.config.CertAdownConfig;
import com.aurine.cloudx.estate.cert.constant.ADownStateEnum;
import com.aurine.cloudx.estate.cert.constant.AdownConstant;
import com.aurine.cloudx.estate.cert.entity.SysAppConfig;
import com.aurine.cloudx.estate.cert.entity.SysCertAdownRequest;
import com.aurine.cloudx.estate.cert.exception.BusinessException;
import com.aurine.cloudx.estate.cert.exception.code.ErrorCode;
import com.aurine.cloudx.estate.cert.transfer.http.HttpConnector;
import com.aurine.cloudx.estate.cert.util.CertRedisUtil;
import com.aurine.cloudx.estate.cert.util.TokenBucketUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 凭证下发队列服务
 * @author zengxn
 * */
@Slf4j
@Service
public class CertAdownQueueService {
    /**凭证下发设备调度队列*/
    private static final String CERT_MD5_CACHE_NAME = "cert-adown:CERT_MD5_CACHE";
    /**下发凭证到单个设备的凭证队列Key*/
    private static final String DEVICE_CERT_QUEUE_NAME_PREFIX = "cert-adown:DEVICE_CERT_ADOWN_QUEUE:";

    private static final String DEVICE_CERT_ADOWN_INSERT_LIST_KEY = "cert-adown:REQUEST_INSERT_QUEUE";

    private static final String DEVICE_CERT_ADOWN_UPDATE_LIST_KEY = "cert-adown:REQUEST_UPDATE_QUEUE";

    private static final String DEVICE_CERT_ADOWN_FLUSH_DB_LOCK_KEY = "cert-adown:REQUEST_FLUSH_DB_LOCK";
    private static final String TOPIC_ADOWN_SEND = "ADOWN_SEND";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysCertAdownRequestService sysCertAdownRequestService;
    @Autowired
    private HttpConnector httpConnector;
    @Autowired
    private SysAppConfigService sysAppConfigService;

    @Resource
    KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 加入队列
     * */
    public void add(SysCertAdownRequest request) {
        if (md5Check(request)) {
            request.setRequestTime(LocalDateTime.now());
            String deviceQueueKey = DEVICE_CERT_QUEUE_NAME_PREFIX + request.getDeviceId();
            //加入设备凭证队列
            redisTemplate.opsForList().rightPush(deviceQueueKey, request);
            boolean exists = redisTemplate.opsForSet().isMember(AdownConstant.DEVICE_DISPATCH_QUEUE_KEY, request.getDeviceId());
            if (!exists) {
                redisTemplate.opsForSet().add(AdownConstant.DEVICE_DISPATCH_QUEUE_KEY, request.getDeviceId());
            }
            if (request.getRetry()==0) {
                //第一次请求进来，添加到插入缓冲队列
                insertToCache(request);
            }
        }
        else {
            log.info("丢弃重复下发凭证请求，请求：", request);
        }
    }

    /**
     * 校验是否存在待下发的设备凭证
     * */
    private boolean md5Check(SysCertAdownRequest request) {
        String md5 =md5(request);
        boolean has = redisTemplate.opsForSet().isMember(CERT_MD5_CACHE_NAME, md5);
        if (!has) {
            redisTemplate.opsForSet().add(CERT_MD5_CACHE_NAME, md5);
            return true;
        }
        return false;
    }

    /**
     * 移除md5校验缓存
     * */
    private void removeMd5Cache(SysCertAdownRequest request) {
        String md5 =md5(request);
        redisTemplate.opsForSet().remove(CERT_MD5_CACHE_NAME, md5);
    }

    /**
     * 获取设备凭证md5值
     * */
    private String md5(SysCertAdownRequest request) {
        String md5 = DigestUtils.md5DigestAsHex(request.getBody().getBytes());
        return request.getDeviceId() + md5;
    }

    /**
     * 弹出队列
     * */
    private SysCertAdownRequest popCertAdownRequest(String deviceId) {
        String deviceQueueKey = DEVICE_CERT_QUEUE_NAME_PREFIX + deviceId;
        Object certAdownRequest = redisTemplate.opsForList().leftPop(deviceQueueKey);
        if (certAdownRequest!=null) {
            long size = redisTemplate.opsForList().size(deviceQueueKey);
            if (size==0) {
                //删除设备队列
                redisTemplate.delete(deviceQueueKey);
            }
            return (SysCertAdownRequest) certAdownRequest;
        }
        return null;
    }

    private void insertToCache(SysCertAdownRequest request) {
        redisTemplate.opsForList().rightPush(DEVICE_CERT_ADOWN_INSERT_LIST_KEY, request);
    }

    private void updateToCache(SysCertAdownRequest request) {
        redisTemplate.opsForList().rightPush(DEVICE_CERT_ADOWN_UPDATE_LIST_KEY, request);
        removeMd5Cache(request);
    }

    /**
     * 定时将缓存数据刷入数据库
     * */
    @Scheduled(fixedDelay = 3000)
    public void flushToDB() {
        try {
            CertRedisUtil.lock(DEVICE_CERT_ADOWN_FLUSH_DB_LOCK_KEY, 30);
            List<SysCertAdownRequest> insertList = redisTemplate.opsForList().range(DEVICE_CERT_ADOWN_INSERT_LIST_KEY, 0, -1);
            if (insertList!=null && insertList.size()>0) {
                try{
                    sysCertAdownRequestService.saveBatch(insertList);
                }catch (DuplicateKeyException de) {
                    for(int i=0; i<insertList.size(); i++) {
                        try{
                            sysCertAdownRequestService.save(insertList.get(i));
                        }catch (Exception e) {
                            log.warn("插入凭证调度请求记录异常", e);
                        }
                    }
                }
                for(int i=0; i<insertList.size(); i++) {
                    redisTemplate.opsForList().leftPop(DEVICE_CERT_ADOWN_INSERT_LIST_KEY);
                }
            }

            List<SysCertAdownRequest> updateList = redisTemplate.opsForList().range(DEVICE_CERT_ADOWN_UPDATE_LIST_KEY, 0, -1);
            if (updateList!=null && updateList.size()>0) {
                sysCertAdownRequestService.updateBatchById(updateList);
                for(int i=0; i<updateList.size(); i++) {
                    redisTemplate.opsForList().leftPop(DEVICE_CERT_ADOWN_UPDATE_LIST_KEY);
                }
            }
            CertRedisUtil.unLock(DEVICE_CERT_ADOWN_FLUSH_DB_LOCK_KEY);
        }
        catch (Exception e) {
            log.error("定时将缓存数据刷入数据库执行异常：", e);
        }
    }

    /**
     * 下发凭证到设备
     * */
    @Async("adownExecutor")
    public void adown(String deviceId) {
        boolean exists = redisTemplate.hasKey(AdownConstant.CERT_ADOWN_DEVICE_LOCK_PREFIX+deviceId);
        if (exists) {
            return;
        }
        boolean locked = CertRedisUtil.lock(AdownConstant.CERT_ADOWN_DEVICE_LOCK_PREFIX+deviceId, CertAdownConfig.adownTimeoutValue);
        if (locked) {
            if (isNotEmptyCertAdownQueue(deviceId)) {
                log.info("开始执行设备凭证下发，设备ID：{}", deviceId);
                while(true) {
                    try {
                        if (TokenBucketUtil.getToken()) {
                            //判断设备是否正在下发凭证
                            boolean hasKey = redisTemplate.hasKey(AdownConstant.DEVICE_CERT_ADOWN_PROCESSING_PREFIX + deviceId);
                            if (!hasKey) {
                                //设备没有凭证在下发了
                                SysCertAdownRequest request = popCertAdownRequest(deviceId);
                                if (request !=null) {
                                    //下发凭证
                                    try {
                                        adown(request);
                                    }
                                    catch (Exception e) {
                                        log.error("下发凭证异常：{}", e);
                                        log.info("下发凭证异常，重新加入队列：{}", request);
                                        //add(request);
                                    }
                                    //凭证没有下发完，锁续约
                                    redisTemplate.expire(AdownConstant.CERT_ADOWN_DEVICE_LOCK_PREFIX+deviceId, CertAdownConfig.adownTimeoutValue, TimeUnit.SECONDS);
                                }
                                else {
                                    //凭证下发完成，释放锁
                                    redisTemplate.delete(AdownConstant.CERT_ADOWN_DEVICE_LOCK_PREFIX+deviceId);
                                    redisTemplate.opsForSet().remove(AdownConstant.DEVICE_DISPATCH_QUEUE_KEY, deviceId);
                                    break;
                                }

                            }
                        }
                        else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } catch (Exception e) {
                        log.error("下发凭证异常：{}", e);
                    }
                }
                log.info("设备凭证下发结束，设备ID：{}", deviceId);
            }
            else {
                redisTemplate.opsForSet().remove(AdownConstant.DEVICE_DISPATCH_QUEUE_KEY, deviceId);
            }
        }
    }

    /**
     * 下发凭证
     * */
    private void adown(SysCertAdownRequest request) {
        log.info("[CERT-ADOWN]下发凭证，APPID：{}，设备ID：{}，请求ID：{}", request.getAppId(), request.getDeviceId(), request.getRequestId());
        request.setDownloadingTime(LocalDateTime.now());
        request.setState("2");
        //加入发送处理队列
        redisTemplate.opsForValue().set(AdownConstant.CERT_ADOWN_PROCESSING_REQUEST_PREFIX + request.getRequestId(), request, CertAdownConfig.adownTimeoutValue * 2, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(AdownConstant.DEVICE_CERT_ADOWN_PROCESSING_PREFIX+request.getDeviceId(), request.getRequestId(), CertAdownConfig.adownTimeoutValue, TimeUnit.SECONDS);
        updateToCache(request);
        //调用远程接口，下发凭证
//        httpConnector.post(request, getUrl(request, true));
        kafkaTemplate.send(TOPIC_ADOWN_SEND, JSONObject.toJSONString(httpConnector.toDto(request)));
    }

    /**
     * 判断下发队列是否为空
     * */
    public boolean isNotEmptyCertAdownQueue(String deviceId) {
        long size = redisTemplate.opsForList().size(DEVICE_CERT_QUEUE_NAME_PREFIX + deviceId);
        return size > 0;
    }

    /**
     * 获取下发凭证请求地址
     * */
    private String getUrl(SysCertAdownRequest request, boolean getReqUrl) {
        SysAppConfig appConfig = sysAppConfigService.getOne(new QueryWrapper<SysAppConfig>().lambda().eq(SysAppConfig::getAppId, request.getAppId()));
        if (appConfig != null) {
            if (getReqUrl) {
                return appConfig.getReqCallBackUrl();
            } else {
                return appConfig.getRespCallBackUrl();
            }
        } else {
            log.error("未找到APP：{} 的配置信息,{}", request.getAppId(), request);
            throw new BusinessException(ErrorCode.CONFIG_NO_CONFIG);
        }
    }

    /**
     * 下载结果变更回调
     * */
    public void update(SysCertAdownRequest request) {
        //获取缓存中的凭证下发请求
        SysCertAdownRequest certAdownRequest = (SysCertAdownRequest) redisTemplate.opsForValue().get(AdownConstant.CERT_ADOWN_PROCESSING_REQUEST_PREFIX + request.getRequestId());
        if (certAdownRequest==null) {
            //解决超时回调后，收到凭证下发成功的请求的异常
            certAdownRequest = sysCertAdownRequestService.getById(request.getRequestId());
            if (certAdownRequest == null) {
                log.warn("未找到凭证，请求ID：{}", request.getRequestId());
                return;
            }
        }
        else {
            redisTemplate.delete(AdownConstant.DEVICE_CERT_ADOWN_PROCESSING_PREFIX + certAdownRequest.getDeviceId());
            redisTemplate.delete(AdownConstant.CERT_ADOWN_PROCESSING_REQUEST_PREFIX + certAdownRequest.getRequestId());
            removeMd5Cache(certAdownRequest);
        }
        certAdownRequest.setState(request.getState());
        switch (ADownStateEnum.getEnum(request.getState())) {
            case SUCCESS:
                certAdownRequest.setState("3");
                certAdownRequest.setRequestTime(LocalDateTime.now());
                updateToCache(certAdownRequest);
                log.info("凭证下发成功，设备ID：{}，请求ID：{}", certAdownRequest.getDeviceId(), certAdownRequest.getRequestId());
                break;
            case FAIL:
                certAdownRequest.setState("4");
                certAdownRequest.setRequestTime(LocalDateTime.now());
                updateToCache(certAdownRequest);
                log.error("凭证下发失败，设备ID：{}", certAdownRequest.getDeviceId());
                break;
            default:
                log.error("错误的状态类型：{}", request);
        }
//        httpConnector.post(certAdownRequest, getUrl(certAdownRequest, false));
    }

    /**
     * 重试下发凭证
     * */
    public void retryAdown(SysCertAdownRequest request) {
        //重新加入下发请求调度队列
        request.setRetry(request.getRetry() + 1);
        removeMd5Cache(request);
        add(request);
        redisTemplate.delete(AdownConstant.DEVICE_CERT_ADOWN_PROCESSING_PREFIX + request.getDeviceId());
        log.info("凭证下发超时重试，请求ID：{}，重试次数：{}", request.getRequestId(), request.getRetry());
    }

    /**
     * 超过重试次数
     * */
    public void retryOverTimes(SysCertAdownRequest request) {
        redisTemplate.delete(AdownConstant.DEVICE_CERT_ADOWN_PROCESSING_PREFIX + request.getDeviceId());
        //超过超时重试次数，超时回调通知
        request.setState("5");
        request.setRequestTime(LocalDateTime.now());
        removeMd5Cache(request);
        updateToCache(request);
        httpConnector.asyncPost(request, getUrl(request, false));
        log.warn("凭证下发超时重试，超过最大超时重试次数，丢弃：{}", request);
    }
}
