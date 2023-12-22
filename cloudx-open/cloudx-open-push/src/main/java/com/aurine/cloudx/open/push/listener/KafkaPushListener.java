package com.aurine.cloudx.open.push.listener;

import com.aurine.cloudx.open.common.core.exception.CloudxOpenException;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.push.constant.KafkaConstant;
import com.aurine.cloudx.open.push.handler.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 开放平台Kafka推送监听器
 *
 * @author : Qiu
 * @date : 2022/7/25 14:32
 */

@Slf4j
@Component
public class KafkaPushListener {

    @Resource
    private PushSendConfigHandler pushSendConfigHandler;

    @Resource
    private PushSendCascadeHandler pushSendCascadeHandler;

    @Resource
    private PushSendOperateHandler pushSendOperateHandler;

    @Resource
    private PushSendCommandHandler pushSendCommandHandler;

    @Resource
    private PushSendEventHandler pushSendEventHandler;

    @Resource
    private PushSendOtherHandler pushSendOtherHandler;


    /**
     * 发送配置类消息
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_CONFIG, errorHandler = "")
    void config(Message<?> message) {
        log.info("[KafkaPushListener - config]: 发送配置类消息, message={}", JSONConvertUtils.objectToString(message));

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendConfigHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - config]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - config]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送级联入云类消息
     * （申请apply、撤销revoke、同意accept、拒绝reject）
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_CASCADE, errorHandler = "")
    void cascade(Message<?> message) {
        log.info("[KafkaPushListener - cascade]: 发送级联入云类消息, message={}", JSONConvertUtils.objectToString(message));

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendCascadeHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - cascade]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - cascade]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送操作类消息
     * （添加add、修改update、删除delete、同步sync）
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_OPERATE, errorHandler = "")
    void operate(Message<?> message) {
        log.info("[KafkaPushListener - config]: 发送操作类消息, message={}", message);

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendOperateHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - operate]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - operate]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送指令类消息
     * （关闭close、打开open、改变change、清空empty）
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_COMMAND, errorHandler = "")
    void command(Message<?> message) {
        log.info("[KafkaPushListener - command]: 发送指令类消息, message={}", JSONConvertUtils.objectToString(message));

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendCommandHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - command]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - command]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送事件类消息
     * （人行、车行、告警）
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_EVENT, errorHandler = "")
    void event(Message<?> message) {
        log.info("[KafkaPushListener - event]: 发送事件类消息, message={}", JSONConvertUtils.objectToString(message));

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendEventHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - event]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - event]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 发送其他消息
     *
     * @param message 接收到的message
     * @return
     */
    @KafkaListener(topics = KafkaConstant.TOPIC_PUSH_OTHER, errorHandler = "")
    void other(Message<?> message) {
        log.info("[KafkaPushListener - other]: 发送其他消息, message={}", JSONConvertUtils.objectToString(message));

        OpenPushModel model = JSONConvertUtils.stringToObject((String) message.getPayload(), OpenPushModel.class);
        try {
            pushSendOtherHandler.handle(model);
//            PushSendHandlerFactory.getHandler(model).handle(model);
        } catch (CloudxOpenException coe) {
            log.error("[KafkaPushListener - other]: CloudxOpenException, coe={}", coe.getMessage());
            coe.printStackTrace();
        } catch (Exception e) {
            log.error("[KafkaPushListener - other]: Exception, e={}", e.getMessage());
            e.printStackTrace();
        }
    }
}
