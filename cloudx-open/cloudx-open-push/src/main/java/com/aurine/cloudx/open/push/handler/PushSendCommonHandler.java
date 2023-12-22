package com.aurine.cloudx.open.push.handler;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.open.common.core.constant.RedisConstant;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackModeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.OpenPushSubscribeCallbackTypeEnum;
import com.aurine.cloudx.open.common.core.constant.enums.RedisKeySuffixEnum;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.core.exception.CloudxOpenErrorEnum;
import com.aurine.cloudx.open.common.core.util.JSONConvertUtils;
import com.aurine.cloudx.open.common.core.util.RedisUtils;
import com.aurine.cloudx.open.common.entity.model.OpenPushModel;
import com.aurine.cloudx.open.common.entity.util.HandleResult;
import com.aurine.cloudx.open.common.entity.vo.OpenPushSubscribeCallbackVo;
import com.aurine.cloudx.open.push.constant.PushSendCallbackHeaderConstant;
import com.aurine.cloudx.open.push.constant.PushSendKeyConstant;
import com.aurine.cloudx.open.push.service.PushSubscribeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 推送模块发送公共处理类
 *
 * @author : Qiu
 * @date : 2021 12 13 19:33
 */

@Slf4j
@Component
public class PushSendCommonHandler {

    /*
    // 本地缓存的方式

    // 回调列表Map，根据回调类型和项目UUID做映射
    private static final Map<String, List<OpenPushSubscribeCallbackVo>> callbackListMap = new HashMap<>();
    */

    // 回调类型Map，根据业务类型（serviceType）做映射
    private static final Map<String, String> callbackTypeMap = new HashMap<>();
    // 业务类型Map，根据回调类型（callbackType）做映射
    private static final Map<String, String> serviceTypeMap = new HashMap<>();

    private static final String REDIS_KEY_CALLBACK_LIST = RedisConstant.getRedisKey(RedisKeySuffixEnum.CALLBACK_LIST.suffix);

    private static final String FORMAT_CALLBACK_LIST_MAP_KEY = "%s_%s";
    private static final String FORMAT_HANDLE_MESSAGE = "%s: %s";

    @Resource
    private PushSubscribeService pushSubscribeService;

    @Resource
    private KafkaTemplate kafkaTemplate;


    /**
     * 过滤方法
     *
     * @param model 推送模型类
     * @return
     */
    protected HandleResult<OpenPushModel> filter(OpenPushModel model) {
        HandleResult<OpenPushModel> result = new HandleResult(model);
        if (model == null) {
            result.failed(CloudxOpenErrorEnum.ARGUMENT_FORMAT_INVALID.getMsg());
            return result;
        }

        // 过滤的Set集合
        Set<String> filterSet = new HashSet<>();

        if (model.getTenantId() == null) {
            filterSet.add(PushSendKeyConstant.TENANT_ID);
        }
        if (StringUtils.isBlank(model.getProjectUUID())) {
            filterSet.add(PushSendKeyConstant.PROJECT_UUID);
        }
        if (StringUtils.isBlank(model.getServiceType())) {
            filterSet.add(PushSendKeyConstant.SERVICE_TYPE);
        }
        if (StringUtils.isBlank(model.getServiceName())) {
            filterSet.add(PushSendKeyConstant.SERVICE_NAME);
        }
        if (model.getData() == null) {
            filterSet.add(PushSendKeyConstant.DATA);
        }

        // 如果Set集合长度大于0，则表示有检测到问题
        if (filterSet.size() > 0) {
            result.failed(this.buildHandleMessage(CloudxOpenErrorEnum.MISSING_REQUIRED_PARAMETERS.getMsg(), String.join(",", filterSet)));
        } else if (!ServiceNameEnum.existName(model.getServiceName())) {
            result.failed(this.buildHandleMessage(CloudxOpenErrorEnum.ARGUMENT_INVALID.getMsg(), PushSendKeyConstant.SERVICE_NAME));
        }

        return result;
    }

    /**
     * 获取回调列表
     *
     * @param model 推送模型类
     * @return 回调列表
     */
    protected List<OpenPushSubscribeCallbackVo> getCallbackList(OpenPushModel model) {
        String serviceType = model.getServiceType();
        String projectUUID = model.getProjectUUID();
        List<OpenPushSubscribeCallbackVo> callbackList = null;

        // 获取回调类型
        String callbackType = this.getCallbackTypeByServiceType(serviceType);
        if (OpenPushSubscribeCallbackTypeEnum.CONFIG.code.equals(callbackType)) {
            projectUUID = null;
        }

        Map<String, ArrayList> callbackListMap = RedisUtils.hmget(REDIS_KEY_CALLBACK_LIST, String.class, ArrayList.class);
        String key = this.buildCallbackListMapKey(serviceType, projectUUID);

        // 判断回调列表map中是否已存在回调列表
        if (callbackListMap.containsKey(key)) {
            // 若存在则直接取出并赋值
            callbackList = callbackListMap.get(key);
        } else {
            // 若不存在则重新查询一次，如果不为空就保存到map
            // 根据回调类型和项目UUID查找回调列表，并存入map
            if (StringUtils.isNotEmpty(callbackType)) {
                callbackList = pushSubscribeService.getListByServiceTypeAndProjectUUID(callbackType, projectUUID).getData();
                callbackListMap.put(key, (ArrayList<OpenPushSubscribeCallbackVo>) callbackList);

                RedisUtils.hmsetBySerializable(REDIS_KEY_CALLBACK_LIST, callbackListMap);
            }
        }

        return callbackList;
    }

//    /**
//     * 获取回调列表
//     * 本地缓存的方式
//     *
//     * @param model 推送模型类
//     * @return
//     */
//    protected List<OpenPushSubscribeCallbackVo> getCallbackList(OpenPushModel model) {
//        String serviceType = model.getServiceType();
//        String projectUUID = model.getProjectUUID();
//
//        // 判断回调列表map中是否已存在回调列表，若存在则直接返回
//        String key = this.buildCallbackListMapKey(serviceType, projectUUID);
//        if (callbackListMap.containsKey(key)) return callbackListMap.get(key);
//
//        // 获取回调类型
//        String callbackType = this.getCallbackTypeByServiceType(serviceType);
//
//        // 根据回调类型和项目UUID查找回调列表，并存入map
//        if (StringUtils.isNotEmpty(callbackType)) {
//            List<OpenPushSubscribeCallbackVo> callbackList = pushSubscribeService.getListByServiceTypeAndProjectUUID(callbackType, projectUUID).getData();
//            callbackListMap.put(key, callbackList);
//            return callbackList;
//        }
//
//        return null;
//    }

    /**
     * 回调
     *
     * @param model 推送模型类
     * @return
     */
    protected JSONObject callback(OpenPushModel model) {
//        Integer callbackMode = model.getCallbackMode();
//
//        if (callbackMode != null && callbackMode == 1) {
//            return this.callbackByKafka(this.getCallbackList(model), model);
//        }
//
//        return this.callbackByUrl(this.getCallbackList(model), model);

        return this.callback(this.getCallbackList(model), model);
    }

    /**
     * 回调
     *
     * @param callbackList 回调列表
     * @param model        推送模型类
     * @return
     */
    protected JSONObject callback(List<OpenPushSubscribeCallbackVo> callbackList, OpenPushModel model) {
        if (callbackList == null || callbackList.size() <= 0) return null;
        log.info("[PushSendCommonHandler - callbackByUrl]: 回调地址列表, callbackList={}", JSONConvertUtils.listToJSONArray(callbackList));

        JSONObject object = new JSONObject();
        callbackList.forEach(callback -> {
            String callbackId = callback.getCallbackId();
            String callbackMode = callback.getCallbackMode();

            // 根据不同的回调方式进行回调，将返回的结果赋值给result，并存入object中
            // 注：使用topic方式回调是异步方式，所以result会是空字符串
            Object result;
            if (StringUtil.isNotBlank(callbackMode) && OpenPushSubscribeCallbackModeEnum.TOPIC.code.equals(callbackMode)) {
                result = this.callbackByTopic(callback, model);
            } else {
                result = this.callbackByUrl(callback, model);
            }

            object.put(callbackId, result);
        });
        return object;
    }

    /**
     * url方式回调
     *
     * @param callback 回调列表
     * @param model    推送模型类
     * @return
     */
    protected Object callbackByUrl(OpenPushSubscribeCallbackVo callback, OpenPushModel model) {
        String callbackUrl = callback.getCallbackUrl();
        String callbackHeaderParam = callback.getCallbackHeaderParam();

        if (StringUtil.isBlank(callbackUrl)) {
            log.error("[PushSendCommonHandler - callbackByUrl]: url为空，Http请求失败，callback={}", JSONConvertUtils.objectToString(callback));
            return CloudxOpenErrorEnum.CALLBACK_ERROR.getMsg();
        }

        // 获取Http请求对象，使用GET方式请求
        HttpRequest request = HttpRequest.get(callbackUrl).body(JSONConvertUtils.objectToString(model));
        // 如果回调请求头参数（callbackHeaderParam）不为空，就在请求头中加上该内容
        if (StringUtils.isNotEmpty(callbackHeaderParam)) {
            request.header(PushSendCallbackHeaderConstant.PARAM, callbackHeaderParam);
        }
        // 回调，把结果返回
        Object result;
        try {
            String resultString = request.execute().body();
            result = JSONConvertUtils.stringToJSONObject(resultString);
        } catch (Exception e) {
            log.error("[PushSendCommonHandler - callbackByUrl]: Http请求错误", e);
            return CloudxOpenErrorEnum.CALLBACK_ERROR.getMsg();
        }
        return result;
    }

    /**
     * topic方式回调
     * 使用Kafka发送
     *
     * @param callback 回调列表
     * @param model    推送模型类
     * @return
     */
    protected Object callbackByTopic(OpenPushSubscribeCallbackVo callback, OpenPushModel model) {
        String callbackTopic = callback.getCallbackTopic();
        String callbackHeaderParam = callback.getCallbackHeaderParam();

        if (StringUtil.isBlank(callbackTopic)) {
            log.error("[PushSendCommonHandler - callbackByTopic]: topic为空，Kafka发送失败，callback={}", JSONConvertUtils.objectToString(callback));
            return CloudxOpenErrorEnum.CALLBACK_ERROR.getMsg();
        }

        ProducerRecord producerRecord = new ProducerRecord(callbackTopic, JSONConvertUtils.objectToString(model));
        if (StringUtil.isNotBlank(callbackHeaderParam)) {
            producerRecord.headers().add(PushSendCallbackHeaderConstant.PARAM, callbackHeaderParam.getBytes(StandardCharsets.UTF_8));
        }

        // 因为发送是异步操作，所以这里发送出去之后只打印日志
        ListenableFuture listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("[PushSendCommonHandler - callbackByTopic]: Kafka发送失败, topic={}, ex={}", callbackTopic, ex.toString());
            }

            @Override
            public void onSuccess(Object result) {
                log.info("[PushSendCommonHandler - callbackByTopic]: Kafka发送成功, topic={}, sendResult={}", callbackTopic, JSONConvertUtils.objectToString(result));
            }
        });

        // 将发送结果放入object中
        JSONObject object = new JSONObject();
        try {
            SendResult sendResult = (SendResult) listenableFuture.get();
            ProducerRecord pr = sendResult.getProducerRecord();

            object.put("topic", pr.topic());
            object.put("headers", pr.headers());
            object.put("value", pr.value());
            if (pr.key() != null) object.put("key", pr.key());
            if (pr.partition() != null) object.put("partition", pr.partition());
            if (pr.timestamp() != null) object.put("timestamp", pr.timestamp());
        } catch (Exception e) {
            log.error("[PushSendCommonHandler - callbackByTopic]: Kafka发送错误", e);
            return CloudxOpenErrorEnum.CALLBACK_ERROR.getMsg();
        }

        return object;
    }

    /**
     * 生成处理信息
     *
     * @param messageType 消息类型（错误类型）
     * @param desc        消息描述（错误描述）
     * @return
     */
    protected String buildHandleMessage(String messageType, String desc) {
        String format = FORMAT_HANDLE_MESSAGE;
        if (StringUtil.isBlank(desc)) {
            format = format.replace(": %s", "");
            return String.format(format, messageType);
        }
        return String.format(format, messageType, desc);
    }

    /**
     * 生成回调地址Map的key值
     *
     * @param serviceType 业务类型
     * @param projectUUID 项目UUID
     * @return
     */
    protected String buildCallbackListMapKey(String serviceType, String projectUUID) {
        String format = FORMAT_CALLBACK_LIST_MAP_KEY;
        if (StringUtil.isBlank(projectUUID)) {
            format = format.replace("_%s", "");
            return String.format(format, serviceType);
        }
        return String.format(format, serviceType, projectUUID);
    }

    /**
     * 根据serviceType（业务类型）获取callbackType（回调类型）
     *
     * @param serviceType
     * @return
     */
    protected String getCallbackTypeByServiceType(String serviceType) {
        String callbackType = null;

        // 判断回调类型map中是否已存在对应的业务类型
        if (callbackTypeMap.containsKey(serviceType)) {
            // 若存在则直接取出并赋值
            callbackType = callbackTypeMap.get(serviceType);
        } else {
            // 若不存在则重新对应一次
            OpenPushSubscribeCallbackTypeEnum callbackTypeEnum = OpenPushSubscribeCallbackTypeEnum.getByName(serviceType);
            if (callbackTypeEnum != null) {
                callbackType = callbackTypeEnum.code;
                callbackTypeMap.put(serviceType, callbackType);
                serviceTypeMap.put(callbackType, serviceType);
            }
        }

        return callbackType;
    }

    /**
     * 根据callbackType（回调类型）获取serviceType（业务类型）
     *
     * @param callbackType
     * @return
     */
    protected String getServiceTypeByCallbackType(String callbackType) {
        String serviceType = null;

        // 判断业务类型map中是否已存在对应的回调类型
        if (serviceTypeMap.containsKey(callbackType)) {
            // 若存在则直接取出并赋值
            serviceType = serviceTypeMap.get(callbackType);
        } else {
            // 若不存在则重新对应一次
            OpenPushSubscribeCallbackTypeEnum callbackTypeEnum = OpenPushSubscribeCallbackTypeEnum.getByCode(callbackType);
            if (callbackTypeEnum != null) {
                serviceType = callbackTypeEnum.name;
                serviceTypeMap.put(callbackType, serviceType);
                callbackTypeMap.put(serviceType, callbackType);
            }
        }

        return serviceType;
    }

    /**
     * 判断回调列表Map的key值是否存在
     *
     * @param serviceType 业务类型
     * @param projectUUID 项目UUID
     * @return
     */
    public boolean existCallbackListMapKey(String serviceType, String projectUUID) {
        return this.existCallbackListMapKey(this.buildCallbackListMapKey(serviceType, projectUUID));
    }

    /**
     * 判断回调列表Map的key值是否存在
     *
     * @param key 回调列表Map的key值
     * @return
     */
    public boolean existCallbackListMapKey(String key) {
        Map<String, ArrayList> callbackListMap = RedisUtils.hmget(REDIS_KEY_CALLBACK_LIST, String.class, ArrayList.class);
        return callbackListMap.containsKey(key);
    }

    /**
     * 如果回调列表Map如果存在key值就删除
     *
     * @param callbackType 回调类型类型
     * @param projectUUID  项目UUID
     * @return
     */
    public boolean removeCallbackListMapIfExistKey(String callbackType, String projectUUID) {
        String serviceType = this.getServiceTypeByCallbackType(callbackType);
        if (StringUtils.isBlank(serviceType)) serviceType = callbackType;

        String key = this.buildCallbackListMapKey(serviceType, projectUUID);
        if (this.existCallbackListMapKey(key)) {
            log.info("[PushSendCommonHandler - removeCallbackListMapIfExistKey]: 删除回调列表Map值, key={}", key);

            RedisUtils.hdel(REDIS_KEY_CALLBACK_LIST, key);
            return true;
        }
        return false;
    }

    /**
     * 清空回调列表缓存
     *
     * @return
     */
    public boolean clearCallbackListCache() {
        try {
            RedisUtils.delete(REDIS_KEY_CALLBACK_LIST);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
