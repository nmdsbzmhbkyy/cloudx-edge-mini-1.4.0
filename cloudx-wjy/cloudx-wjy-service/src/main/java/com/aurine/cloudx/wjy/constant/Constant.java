package com.aurine.cloudx.wjy.constant;

import com.aurine.cloudx.wjy.entity.Project;
import com.aurine.cloudx.wjy.pojo.AccessToken;
import com.aurine.cloudx.wjy.pojo.Prams;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ： huangjj
 * @date ： 2021/4/22
 * @description： 公共变量
 */
public class Constant {
    public static Map<String, AccessToken> bgTokenMap = new HashMap<>();//后台接口请求凭证集合
    public static Map<String, AccessToken> wjTokenMap = new HashMap<>();//用户接口请求凭证集合
    public static Map<String, AccessToken> gjTokenMap = new HashMap<>();//管家接口请求凭证集合

    //开发地址
    public static String baseUrl = "http://mhyy.kingdee.com";
    //public static final String wyWebUrl = "http://mhwy.kingdee.com/thirdUser/auth2";
    //public static final String wjWebUrl = "http://mhsj.kingdee.com/users/login";
    //public static final String gjWebUrl = "http://mhgj.kingdee.com/vue/index.html";
    //正式地址
    //public static String baseUrl = "https://api.wojiacloud.com";


    public static final String ver = "V1.0";
    public static final String source = "aurine";
    //开发
    public static String productName = "门禁系统";
    //正式
    //public static final String productName = "门禁产品";


    public static Integer wyWebType = 2;
    public static Integer wjWebType = 1;
    public static Integer gjWebType = 3;

    public static Prams getConfig(Project project, Integer type){
        String appKey = "";
        String appSecret = "";
        //根据类型获取appkey,appsecret
        if(type == Constant.wyWebType){
            appKey = project.getWyAppkey();
            appSecret = project.getWyAppsecret();
        }else if(type == Constant.wjWebType){
            appKey = project.getWjAppkey();
            appSecret = project.getWjAppsecret();
        }else if(type == Constant.gjWebType){
            appKey = project.getGjAppkey();
            appSecret = project.getGjAppsecret();
        }
        Prams p = new Prams();
        p.setBaseUrl(baseUrl);
        p.setAppKey(appKey);
        p.setAppSecret(appSecret);
        return p;
    }
    public static AccessToken getToken(String pid, Integer type){
        if(Constant.wyWebType == type){
            return  bgTokenMap.get(pid);
        }else if(Constant.wjWebType == type){
            return wjTokenMap.get(pid);
        }else if(Constant.gjWebType == type){
            return gjTokenMap.get(pid);
        }
        return null;
    }
    public static void setToken(String pid, AccessToken token, Integer type){
        if(Constant.wyWebType == type){
            bgTokenMap.put(pid,token);
        }else if(Constant.wjWebType == type){
            wjTokenMap.put(pid,token);
        }else if(Constant.gjWebType == type){
            gjTokenMap.put(pid,token);
        }
    }
}