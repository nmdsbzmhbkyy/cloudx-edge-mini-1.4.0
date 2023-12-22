package com.aurine.cloudx.edge.sync.biz.listener;

import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *  redis是失效监听
 *
 * @author zouyu
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Resource
    private InitConfig initConfig;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /**
     * Redis-key失效监听事件
     *
     * @param message
     * @param pattern
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取失效的key
        String expiredKey = message.toString();
        //mqtt连接状态
        if (expiredKey.contains(Constants.CONNECTION_STATUS)) {
            log.info("监听到连接状态失效，开始重试");
            // 获取projectRelation所有uuid，mqtt订阅消息
            initConfig.subscribeProjectRelationTopics();
            // 调用projectInfo接口获取并初始化项目关系,mqtt，open订阅数据
            initConfig.initBaseProjectInfo();
            // 初始化 mqtt发送队列服务
            initConfig.initQueue();
        }
    }

}
