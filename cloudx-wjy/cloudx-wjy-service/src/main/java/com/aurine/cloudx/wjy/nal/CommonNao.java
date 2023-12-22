package com.aurine.cloudx.wjy.nal;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aurine.cloudx.wjy.pojo.R;

import java.util.Map;

/**
 * 网络访问API基本接口，负责向第三方发送请求，并响应转换成所需的java对象
 *  附：这里本意设计成网络访问层（符合多层结构设计原则），基于项目小，功能少，
 *      先抽象出网络访问基础，请求得到响应转成对象
 */
public class CommonNao {
    public static <T> R<T> doGet(String url, Map<String, String> header, Map<String, Object> params, TypeReference<R<T>> typeReference){
        System.out.println("Request GET: " + url);
        if(header != null && header.size() > 0){
            System.out.println("Header: " + header);
        }
        System.out.println("Body: " + params);
        return doInner(url, Method.GET, header, params, typeReference);
    }

    public static <T> R<T> doPost(String url, Map<String, String> header, Map<String, Object> params, TypeReference<R<T>> typeReference){
        System.out.println("Request POST: " + url);
        System.out.println("Body: " + params);
        return doInner(url, Method.POST, header, params, typeReference);
    }

    private static <T> R<T> doInner(String url, Method method, Map<String, String> header, Map<String, Object> params, TypeReference<R<T>> typeReference){
        R<T> responseObj = null;
        do {
            String body;
            JSONObject jobj;
            try {
//                if(method.equals("GET")){
//                    body = HttpUtil.createGet(url).form(params).execute().body();
//                }else{// else if(method.equals("POST")){
//                    body = HttpUtil.createPost(url).form(params).execute().body();
//                }
                body = HttpUtil.createRequest(method, url).headerMap(header, false).form(params).execute().body();
            }catch(Exception ex){
                ex.printStackTrace();
                break;
            }
            System.out.println("Response: " + body);
            if(! JSONUtil.isJson(body)){
                break;
            }
            jobj = JSONUtil.parseObj(body);
            if(! (jobj.containsKey("success") && jobj.containsKey("code") && jobj.containsKey("msg"))){
                break;
            }
            responseObj = JSONUtil.toBean(body, typeReference, false);
        }while(false);

        return responseObj;
    }
}
