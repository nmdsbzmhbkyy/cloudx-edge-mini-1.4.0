package com.aurine.cloudx.push.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.push.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: 邹宇
 * @Date: 2021-8-24 15:26:49
 * 第一次启动读取
 * 每天固定时间从头读取 暂定4点，具体查看RefreshMapTimer 内定义
 * 小程序调用查不到unionId重新读取map，依旧读不到返回错误结果
 **/
@Service
@Slf4j
public class UserUtil {

    private static Map<String, String> userMap = new ConcurrentHashMap<>();

    /**
     * 根据用户openID获得用户详细信息
     *
     * @param openId
     * @return
     */
    public static String getOnceUserInfo(String openId) {
        String url = String.format(Constants.USER_GET_INFO, Constants.ACCESS_TOKEN);
        Map<String, Object> params = new HashMap<>(16);
        params.put("access_token", Constants.ACCESS_TOKEN);
        params.put("openid", openId);
        params.put("lang", "zh_CN");
        return HttpClientUtils.doJsonGet(url, params, null);
    }

    /**
     * 获取用户openID列表
     *
     * @param next_openid
     * @return
     */
    public static String getUserList(String next_openid) {
        String url = String.format(Constants.USER_GET_LIST, Constants.ACCESS_TOKEN);
        Map<String, Object> params = new HashMap<>(16);
        if (next_openid != null) {
            params.put("next_openid", next_openid);
        }
        return HttpClientUtils.doJsonGet(url, params, null);
    }

    /**
     * 批量获取用户基本信息
     *
     * @param openIds
     * @return
     */
    public static String batchGetUserList(JSONArray openIds) {
        String url = String.format(Constants.USER_BATCH_GET_LIST, Constants.ACCESS_TOKEN);
        Map<String, Object> bodyMap = new HashMap<>(16);
        Object[] user_list = new Object[openIds.size()];
        for (int i = 0; i < openIds.size(); i++) {
            Map<String, String> obj = new HashMap<>();
            obj.put("openid", openIds.getString(i));
            obj.put("lang", "zh_CN");
            user_list[i] = obj;
        }
        bodyMap.put("user_list", user_list);
        return HttpClientUtils.doJsonPost(url, JSON.toJSONString(bodyMap), null);
    }

    /**
     * 初始化获取用户数据保存到Map缓存中
     */
    public static void updateUserInfoToMap() {
        //初始化map
        log.info("重新拉取用户数据");
        userMap = new HashMap<>();

        String userList = getUserList(null);
        //判断nextOpenId,为空则重新生成map
        JSONObject userListObj = JSONObject.parseObject(userList);
        System.out.println(userList);
        if (userListObj.containsKey("data")) {
            JSONArray array = userListObj.getJSONObject("data").getJSONArray("openid");
            //判断数组长度是否大于100，最多100每次分次发送
            for (int i = 0; i < Math.ceil(array.size() / 100.0); i++) {
                List<Object> objects = array.subList(i * 100,
                        (i + 1) * 100 < array.size() ? (i + 1) * 100 : array.size());
                //发送批量获取用户信息，将UnionId作为key，OpenId作为Value保存到map中
                String userInfos = batchGetUserList(JSONArray.parseArray(JSON.toJSONString(objects)));
                JSONArray userInfoList = JSONObject.parseObject(userInfos).getJSONArray("user_info_list");
                userInfoList.stream().forEach(userInfo -> {
                    JSONObject obj = (JSONObject) JSONObject.toJSON(userInfo);;
                    //System.out.println(userInfo);
                    //System.err.println(obj.getString("openid") + " = " + obj.getString("unionid") + " = " + obj.getString("nickname"));
                    userMap.put(obj.getString("unionid"), obj.getString("openid"));
                });
            }
        }
        //打开可以看到所有用户的openId 和 unionId
        //log.info("userMap:{}",JSON.toJSONString(userMap));

        log.info("userMap长度:{}",UserUtil.getUserMap().size());
    }


    public static Map<String, String> getUserMap() {
        return userMap;
    }

    /**
     * 很久value删除map中对应数据
     *
     * @param delValue
     */
    public synchronized static void delMapValue(String delValue) {
        synchronized (userMap) {
            AtomicReference<String> deleteKey = new AtomicReference<>("");
            userMap.forEach((key, value) -> {
                if (value.equals(delValue)) {
                    deleteKey.set(key);
                }
            });
            if (deleteKey != null && !StringUtils.isEmpty(deleteKey)) {
                userMap.remove(deleteKey.get());
            }
        }
    }

    /**
     * 往数组中添加数据
     *
     * @param key
     * @param value
     */
    public synchronized static void putMapValue(String key, String value) {
        synchronized (userMap) {
            userMap.put(key, value);
        }
    }
}
