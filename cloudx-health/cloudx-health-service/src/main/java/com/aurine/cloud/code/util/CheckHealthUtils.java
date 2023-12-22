package com.aurine.cloud.code.util;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class CheckHealthUtils {


    /**
     * 身份证请求码
     */
    public static final String INVOKESERVICE_CODE_ID = "H053";
    /**
     * '
     * 二维码请求码
     */
    public static final String INVOKESERVICE_CODE_QR = "H046";

    /**
     * 授权码(测试)
     */
    public static final String INVOKECALLER_CODE = "402881e47738893b01773961522b000c402881e47738893b";
    /**
     * 密钥
     */
    public static final String SECRET_KEY = "";
    /**
     * 向量
     */
    public static final String IV = "";


    /**
     * kafka 请求主题
     */
    public static final String KAFKA_TOPIC_REQUEST = "aurine_code_health";

    public static final String PLACE_NAMES_FUJIAN = "福建";
    public static final String PLACE_NAMES_JIANGXI = "江西";


    /**
     * POST参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String sendPostParams(String url, Map<String, Object> params,Integer connectTimeout) throws IOException  {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()){

            BasicResponseHandler responseHandler = new BasicResponseHandler();
            // 添加参数
            List<NameValuePair> nameValueList = new ArrayList<>();

            if(params != null) {
                params.forEach((k,v)->{
                    nameValueList.add(new BasicNameValuePair(k,(String) v));
                });
            }

            HttpPost post = new HttpPost(url);
            RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(connectTimeout).build();
            post.setConfig(config);
            post.setEntity(new UrlEncodedFormEntity(nameValueList, Consts.UTF_8));
            return httpClient.execute(post, responseHandler);
        }

    }

    /**
     * @param plainText
     * @param
     * @param
     * @return
     */
    public static String getSm4EncryptByCBC(String plainText) {
        Sm4Utils sm4 = new Sm4Utils(CheckHealthUtils.SECRET_KEY, CheckHealthUtils.IV, false);
        String cipherText = sm4.encryptDataCBC(plainText, "UTF-8");
        return cipherText;
    }
}
