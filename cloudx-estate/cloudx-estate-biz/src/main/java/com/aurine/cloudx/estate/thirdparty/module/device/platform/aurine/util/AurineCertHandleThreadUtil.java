package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.util;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.config.kafka.KafkaConsumerConfig;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config.AurineConfigDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.constant.AurineCacheConstant;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.entity.dto.AurineCertSendKafkaDTO;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.handle.AbstractAurineCertHandle;
import com.aurine.cloudx.estate.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * <p>冠林中台 下发业务处理线程工具类</p>
 * 用于生成并维护处理线程
 *
 * @ClassName: AurineCertHandleThreadUtil
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-12-07 9:40
 * @Copyright:
 */
@Component
@Slf4j
public class AurineCertHandleThreadUtil {

    private static HashMap<String, Thread> threadMap = new HashMap<>();

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Resource
    KafkaConsumerConfig kafkaConsumerConfig;

    /**
     * 创建线程
     *
     * @param aurineConfigDTO
     */
    public boolean initThread(AurineConfigDTO aurineConfigDTO, AbstractAurineCertHandle aurineCertHandle) {
        String threadName = "handle";
        if (threadMap.get(threadName) != null) {
            return false;
        }

        log.info("[冠林中台] 开始创建凭证传输处理线程");

        Thread thread = threadPoolTaskExecutor.createThread(() -> {
            List<String> snlist = null;
            List<Object> doneList = null;
            List<Object> deviceList = null;
            String doneSn = "";
            String doneSnMsg = "";

            String[] doneStrArray = null;
            String certSendDtoStr = "";
            AurineCertSendKafkaDTO certSendDto;
            boolean isLock = false;
            long cacheListSize = 0;
            Set<Object> deviceSnSet = null;

            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                /**
                 * 监听完成逻辑
                 */
                //检查完成队列，如果存在新的完成请求，对该请求进行解锁，并下发新的请求。
                long doneSize = RedisUtil.lGetListSize(AurineCacheConstant.AURINE_CERT_DONE_LIST);
                if (doneSize >= 1) {

                    log.info("[冠林中台][凭证处理线程] 监听到 {} 条凭证处理已完成", doneSize);
//                    doneList = RedisUtil.lGet(AurineCacheConstant.AURINE_CERT_DONE_LIST, 0, doneSize - 1);

                    while (RedisUtil.lGetListSize(AurineCacheConstant.AURINE_CERT_DONE_LIST) >= 1) {
                       //处理完成队列
                        doneSnMsg = String.valueOf(RedisUtil.lGetIndex(AurineCacheConstant.AURINE_CERT_DONE_LIST, 0));
                        doneStrArray = StringUtils.split(doneSnMsg.toString(), ":");
                        doneSn = doneStrArray[1];

                        RedisUtil.lRemove(AurineCacheConstant.AURINE_CERT_DONE_LIST, 1, doneSnMsg);

                        //从请求队列中删除处理完的数据
                        certSendDtoStr = String.valueOf(RedisUtil.lGetIndex(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, 0));
                        RedisUtil.lRemove(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, 1, certSendDtoStr);
                        RedisUtil.expire(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, AurineCacheConstant.AURINE_CERT_COMMAND_TTL);//消费成功后，队列续约，如果长时间没有消费完成，则认为设备无响应，销毁队列

                        //解锁
                        RedisUtil.deleteLock(AurineCacheConstant.AURINE_CERT_LOCK_PER + doneSn);
                        log.info("[冠林中台][凭证处理线程] {} 凭证处理完成, 剩余长度{}", AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, RedisUtil.lGetListSize(AurineCacheConstant.AURINE_CERT_DONE_LIST));

                    }


//                    RedisUtil.expire(AurineCacheConstant.AURINE_CERT_DONE_LIST, AurineCacheConstant.AURINE_CERT_COMMAND_TTL);//消费成功后，队列续约，如果长时间没有消费完成，则认为设备无响应，销毁队列


//                    for (Object msgIdObj : doneList) {
//
//                        doneStrArray = StringUtils.split(msgIdObj.toString(), ":");
//                        doneSn = doneStrArray[1];
//
//                        //从队列中删除处理完的数据
//                        certSendDtoStr = String.valueOf(RedisUtil.lGetIndex(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, 0));
//                        RedisUtil.lRemove(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, 1, certSendDtoStr);
//                        RedisUtil.expire(AurineCacheConstant.AURINE_CERT_COMMAND_PER + doneSn, AurineCacheConstant.AURINE_CERT_COMMAND_TTL);//消费成功后，队列续约，如果长时间没有消费完成，则认为设备无响应，销毁队列
//
//                        //解锁
//                        RedisUtil.deleteLock(AurineCacheConstant.AURINE_CERT_LOCK_PER + doneSn);
//                        RedisUtil.lRemove(AurineCacheConstant.AURINE_CERT_DONE_LIST, 1, msgIdObj);
//                    }

                }


                /**
                 * 监听等待发送队列逻辑
                 */
                //从redis中获取设备下发列表
                deviceSnSet = RedisUtil.sGet(AurineCacheConstant.AURINE_CERT_DEVICE_SET);

                if (CollUtil.isNotEmpty(deviceSnSet)) {
                    //遍历设备下发列表，获取每个设备列表的末端请求
                    for (Object sn : deviceSnSet) {

                        //检查是否存在锁，如果存在跳过。否则执行异步下发
                        //创建锁，锁的超时时间略高于消息处理的超时时间。当超时后自动重试当前请求。如果达到队列超时时间，则全部队列请求被销毁
                        isLock = RedisUtil.lock(AurineCacheConstant.AURINE_CERT_LOCK_PER + sn.toString(), 1, AurineCacheConstant.AURINE_MESSAGE_CACHE_TTL + 5);

                        // 当前队列被其他线程锁定
                        if (!isLock) {
                            continue;
                        }


                        //获取到待下发指令队列的一条数据
                        cacheListSize = RedisUtil.lGetListSize(AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn.toString());

                        if (cacheListSize == 0) {
                            RedisUtil.setRemove(AurineCacheConstant.AURINE_CERT_DEVICE_SET, sn.toString());
                            continue;
                        }

                        certSendDtoStr = String.valueOf(RedisUtil.lGetIndex(AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn.toString(), 0));

                        log.info("[冠林中台][凭证处理线程] 获取到队列 {} ，开始执行下发，剩余长度{}", AurineCacheConstant.AURINE_CERT_COMMAND_PER + sn.toString(), cacheListSize);
                        aurineCertHandle.readActiveQueue(certSendDtoStr);
                    }
                }


            }

        });

        threadPoolTaskExecutor.execute(thread);
        threadMap.put(threadName, thread);
        return true;
    }

}
