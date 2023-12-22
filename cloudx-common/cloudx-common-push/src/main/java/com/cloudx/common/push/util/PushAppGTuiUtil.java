package com.cloudx.common.push.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.cloudx.common.push.constant.OSTypeEnum;
import com.gexin.rp.sdk.base.IBatch;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.notify.Notify;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.dto.GtReq.NotifyInfo.Type;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PushAppGTuiUtil {

    /**
     * 个推 推送单条信息
     *
     * @param content      推送的内容
     * @param customs      自定义推送的内容，和content二选一
     * @param cId          客户端id
     * @param type         客户手机类型
     * @param serverUrl    服务地址
     * @param appId
     * @param appKey
     * @param masterSecret
     * @param intent
     * @return
     */
    public static boolean GTuiPush(String content, Map<String, String> customs, String cId, OSTypeEnum type, String serverUrl, String appId, String appKey, String masterSecret, String intent) {
        System.out.println("单推开始：" + JSON.toJSONString(customs));
        IGtPush push = new IGtPush(serverUrl, appKey, masterSecret);
        TransmissionTemplate template = null;
        String customstr = JSONObject.toJSONString(customs);
        switch (type) {
            case ios:
                APNPayload apnPayload = new APNPayload();
                // apnPayload.setContentAvailable(1);
                apnPayload.setSound("default");
                apnPayload.setAutoBadge("1");
                apnPayload.setAlertMsg(new APNPayload.SimpleAlertMsg(content));
                if (customs != null) {
                    for (Map.Entry<String, String> custom : customs.entrySet()) {
                        apnPayload.addCustomMsg(custom.getKey(), custom.getValue());
                    }
                }
                template = IosTransmissionTemplate(customstr, appId, appKey, apnPayload);
                break;
            case android:

                template = AndroidTransmissionTemplate(content, customstr, appId, appKey, intent);
                break;

        }

        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(3600 * 1000);
        message.setData(template);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);
        Target target = new Target();
        target.setAppId(appId);
        target.setClientId(cId);
        IPushResult ret = null;
        try {
            ret = push.pushMessageToSingle(message, target);
        } catch (RequestException e) {
            e.printStackTrace();
            // ret = push.pushMessageToSingle(message, target,
            // e.getRequestId());
        }
        if (ret != null) {
            log.info(ret.getResponse().toString());
            if ("ok".equals(ret.getResponse().get("result").toString())) {
                return true;
            } else {
                return false;
            }
        } else {
            log.error("个推服务器异常");
            return false;
        }
    }

    // 群推

    /**
     * @param content      发送的内容
     * @param customs      定制化内容，与content 二选一
     * @param infos        发送的目标组合["",3,cid1],["",4,cid2],其中，3 = 安卓，4 = IOS
     * @param serverUrl
     * @param appId
     * @param appKey
     * @param masterSecret
     * @param intent
     * @return
     */
    public static boolean GTuiBatchPush(String content, Map<String, String> customs, List<Object[]> infos, String serverUrl, String appId, String appKey, String masterSecret, String intent) {
        System.out.println("群推开始：" + JSON.toJSONString(customs) + "|" + JSON.toJSONString(infos));
        IGtPush push = new IGtPush(serverUrl, appKey, masterSecret);
        IBatch batch = push.getBatch();
        TransmissionTemplate template = null;
        SingleMessage message = new SingleMessage();
        message.setOffline(true);
        String customstr = JSONObject.toJSONString(customs);
        APNPayload apnPayload = new APNPayload();
        // apnPayload.setContentAvailable(1);
        apnPayload.setSound("default");
        apnPayload.setAutoBadge("1");
        apnPayload.setAlertMsg(new APNPayload.SimpleAlertMsg(content));
        if (customs != null) {
            for (Map.Entry<String, String> custom : customs.entrySet()) {
                apnPayload.addCustomMsg(custom.getKey(), custom.getValue());
            }
        }
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(3600 * 1000);
        // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
        message.setPushNetWorkType(0);

        try {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i)[2] != null) {

                    Target target = new Target();
                    target.setAppId(appId);
                    target.setClientId(infos.get(i)[2].toString());

                    switch (Integer.valueOf(infos.get(i)[1].toString())) {
                        case 3:
                            template = AndroidTransmissionTemplate(content, customstr, appId, appKey, intent);
                            break;
                        case 4:

                            template = IosTransmissionTemplate(customstr, appId, appKey, apnPayload);
                            break;
                    }
                    message.setData(template);
                    batch.add(message, target);
                }
            }
            IPushResult ret = batch.submit();
            log.info(ret.getResponse().toString());
            if ("ok".equals(ret.getResponse().get("result"))) {
                return true;
            } else {
                log.info("群推错误");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("群退异常");
            return false;
        }

    }

    // 透传
    private static TransmissionTemplate AndroidTransmissionTemplate(String content, String message, String appid, String appkey, String intent) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appid);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(message);
        if (StringUtils.isNotBlank(intent)) {
            Notify notify = new Notify();
            notify.setTitle("消息通知");
            notify.setContent(content);
            notify.setIntent(intent);
            notify.setType(Type._intent);
            template.set3rdNotifyInfo(notify);// 设置第三方通知
        }
        return template;
    }

    private static TransmissionTemplate IosTransmissionTemplate(String content, String appid, String appkey, APNPayload apnPayload) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appid);
        template.setAppkey(appkey);
        // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        template.setTransmissionType(2);
        template.setTransmissionContent(content);
        template.setAPNInfo(apnPayload);
        return template;
    }

    public static void main(String[] args) {
        String appId = "peQYk8SdKv9ctlMlzFKl85";
        String appKey = "JoF779Dj2Z73fXf9XRRfA9";
        String masterSecret = "86tzefEYMSAqRBZE7Uu6h";
        String url = "http://sdk.open.api.igexin.com/apiex.htm";

        String cid = "2fa9b5b4300273dc41dcdb25aff81daa";
        String cid2 = "a22b1890ff486f6b31daa7bbad7574ab";

        // GTuiPush("5432112345", "x2lZ6UlepK9DkHalvGVW1",
        // "30kl8erX2k5lpkRgYbmn35", "t7fUk3xezU51SKXToGNDG4",
        // PUSH_SYSTEM.android, "2ddd518f31dfc4e79c9f3020fa5c97ba");
        /*
         * List<PushInfo> infos = new ArrayList<PushInfo>(); PushInfo info = new
         * PushInfo(); info.setClientId("e29e1f1e042a8eee18fb5d4164eb1861");
         * info.setClientType(3); infos.add(info);
         */
        /*
         * PushInfo info2 = new PushInfo();
         * info2.setClientId("e29e1f1e042a8eee18fb5d4164eb1861");
         * info2.setClientType(3); infos.add(info2);
         */
        List<Object[]> infos = new ArrayList<Object[]>();


        Map<String, String> map = new HashMap<String, String>();
        map.put("content", "这是内容");
        map.put("time", (System.currentTimeMillis() / 1000L) + "");
        map.put("push_type", "C1");
        map.put("message_type", "8");
        map.put("community_id", "12");
        map.put("item_id", "2314");
        map.put("app_type", "MLink");

        infos.add(new Object[]{"", 3, cid});
        infos.add(new Object[]{"", 3, cid2});
//        GTuiBatchPush("234567", map, infos, url, appId, appKey, masterSecret, "intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");

        GTuiPush("这是内容2", map, cid, OSTypeEnum.android, url, appId, appKey, masterSecret,"intent:#Intent;action=android.intent.action.oppopush;package=com.smarthome.oem.nb;component=com.smarthome.oem.nb/.MainActivity;end");
    }

}
