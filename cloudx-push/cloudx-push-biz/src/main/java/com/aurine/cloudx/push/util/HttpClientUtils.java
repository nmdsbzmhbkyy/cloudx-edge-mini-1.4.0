package com.aurine.cloudx.push.util;

import com.aurine.cloudx.push.factory.MySecureProtocolSocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @ClassName: HttpClientUtils
 * @author: 邹宇
 * @date: 2021-8-24 15:19:18
 * @Copyright:
 */
@Slf4j
public class HttpClientUtils {


    /** post请求，无请求头，请求体参数 */
    public static String doPost(String url) {
        return doPost(url, null, null);
    }

    /** post请求，无请求头，请求体参数 */
    public static String doPost(String url, Map<String, Object> bodyMap) {
        return doPost(url, bodyMap, null);
    }

    /** post请求，无请求头，请求体参数 */
    public static String doPost(String url, Map<String, Object> bodyMap, Map<String, String> headerMap) {
        log.info("postUrl:{}",url);
        // 声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        // 发送请求即可
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        PostMethod httpPost = new PostMethod(url);
        if (headerMap != null && headerMap.size() > 0) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.setRequestHeader("Connection", "close");
        Set<String> keySet = bodyMap.keySet();
        NameValuePair[] postData = new NameValuePair[keySet.size()];
        int index = 0;
        for (String key : keySet) {
            postData[index++] = new NameValuePair(key, bodyMap.get(key).toString());
        }
        httpPost.addParameters(postData);
        try {
            int code = httpclient.executeMethod(httpPost);
            log.info("返回码：{}",code);
            if (code == 200) {
                return httpPost.getResponseBodyAsString();
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }


    /** post请求，无请求头，请求体参数 */
    public static String doJsonPost(String url) {
        return doJsonPost(url, null, null);
    }

    /** post请求，无请求头参数 */
    public static String doJsonPost(String url, String body) {
        return doJsonPost(url, body, null);
    }

    @SuppressWarnings("deprecation")
    public static String doJsonPost(String url, String body, Map<String, String> headerMap) {
        log.info("postUrl:{}",url);
        // 声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        // 发送请求即可
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");

        PostMethod httpPost = new PostMethod(url);
        if (headerMap != null && headerMap.size() > 0) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                httpPost.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        httpPost.addRequestHeader("Content-Type", "application/json");
        httpPost.setRequestBody(body);

        try {
            int code = httpclient.executeMethod(httpPost);
            log.info("返回码:{}",code);
            if (code == 200) {
                return httpPost.getResponseBodyAsString();
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpPost.releaseConnection();
        }
        return null;
    }

    /** get请求，无请求头，请求体参数 */
    public static String doJsonGet(String url) {
        return doJsonGet(url, null, null);
    }

    /** get请求，无请求头参数 */
    public static String doJsonGet(String url, Map<String, Object> params) {
        return doJsonGet(url, params, null);
    }

    @SuppressWarnings("deprecation")
    public static String doJsonGet(String url, Map<String, Object> params, Map<String, String> headerMap) {
        // 声明
        ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
        // 加入相关的https请求方式
        Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
        // 发送请求即可
        HttpClient httpclient = new HttpClient();
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        String appendUrl = url;
        if (params != null) {
            appendUrl = getAppendUrl(url, params);
        }
        log.info("getUrl:{}",appendUrl);
        /**
         * 编辑请求头
         */
        GetMethod httpGet = new GetMethod(appendUrl);
        if (headerMap != null && headerMap.size() > 0) {
            for (Entry<String, String> entry : headerMap.entrySet()) {
                httpGet.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        httpGet.addRequestHeader("Content-Type", "application/json");


        try {
            int code = httpclient.executeMethod(httpGet);
            log.info("返回码:{}",code);
            if (code == 200) {
                return httpGet.getResponseBodyAsString();
            } else {
                return "";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpGet.releaseConnection();
        }
        return null;
    }

    /**
     * @param url 接口地址(无参数)
     * @param map 拼接参数集合
     * @Description get请求URL拼接参数
     */
    private static String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString()) && !url.contains("?")) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }

                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }

    //
}
