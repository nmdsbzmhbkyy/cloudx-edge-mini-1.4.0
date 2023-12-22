package com.aurine.cloudx.push.util;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.push.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @ClassName: MenuUtil
 * @author: 邹宇
 * @date: 2021-8-26 09:29:15
 * @Copyright:
 */
@Service
@Slf4j
public class MenuUtil {


    /**
     * 处理事件推送的事件
     *
     * @param requestMap
     */
    public static void dealEvent(Map<String, String> requestMap) {
        String event = requestMap.get("Event");
        switch (event) {
            //用户关注事件
            case "subscribe":
                dealSubscribe(requestMap);
                break;
            //用户取消关注事件
            case "unsubscribe":
                dealUnSubscribe(requestMap);
                break;
            default:
                break;
        }
    }


    /**
     * 处理用户关注事件
     * 关注后将用户信息保存到OpenId和UnionId关系map中
     * @param requestMap
     */
    private static void dealSubscribe(Map<String, String> requestMap) {
        log.info("有用户关注了公众号:{}",requestMap.get("FromUserName"));
        String userInfo = UserUtil.getOnceUserInfo(requestMap.get("FromUserName"));
        JSONObject userObj = JSONObject.parseObject(userInfo);
        if(userObj.containsKey("unionid")){
            UserUtil.putMapValue(userObj.getString("unionid"),requestMap.get("FromUserName"));
        }
        log.info("userObj:{}",UserUtil.getUserMap().size());
    }
    private static  void dealUnSubscribe(Map<String, String> requestMap) {
        log.info("有用户取关了公众号:{}",requestMap.get("FromUserName"));
        //删除用户关系
        UserUtil.delMapValue(requestMap.get("FromUserName"));
        log.info("userObj:{}",UserUtil.getUserMap().size());
    }

    /**
     * 检验发送者是否为微信
     *
     * @param timeStamp
     * @param nonce
     * @param signature
     * @return
     */
    public static boolean check(String timeStamp, String nonce, String signature) {
        //判断传入的参数是否为空，为空直接返回false
        if (timeStamp == null || nonce == null || signature == null) {
            return false;
        }
        // 1）将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{Constants.TOKEN, timeStamp, nonce};
        Arrays.sort(arr);
        // 2）将三个参数字符串拼接成一个字符串进行sha1加密
        String str = arr[0] + arr[1] + arr[2];
        String sha1Hex = DigestUtils.sha1Hex(str);
        // 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (sha1Hex.equals(signature)) {
            return true;
        }
        return false;
    }
}
