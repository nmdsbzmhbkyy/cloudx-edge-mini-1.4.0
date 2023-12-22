package com.aurine.cloudx.push.util;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.push.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @ClassName: WxUtil
 * @author: 邹宇
 * @date: 2021-8-26 09:29:15
 * @Copyright:
 */
@Service
@Slf4j
public class WxUtil {

    /**
     * 获取微信accessToken
     *
     * @return
     */
    public static  String getAccessToken() {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", Constants.APP_ID);
        params.put("secret", Constants.APP_SECRET);
        try {
            String result = HttpClientUtils.doJsonGet(Constants.GET_TOKEN_PATH, params, null);
            JSONObject resultObj = JSONObject.parseObject(result);
            if (resultObj.containsKey("access_token")) {
                Constants.ACCESS_TOKEN = resultObj.getString("access_token");
                return resultObj.getString("access_token");
            }
        }catch (Exception e){
        }
        return null;
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

    /**
     * xml类型数据转map
     */
    public static Map<String, String> xmlToMap(String xml) {
        try {
            InputStream stream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            return parseXmlRequest(stream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析xml数据包
     *
     * @param inputStream
     */
    public static Map<String, String> parseXmlRequest(InputStream inputStream) {
        Map<String, String> map = new HashMap<>(16);
        SAXReader reader = new SAXReader();
        try {
            //读取输入流，获取文档对象
            Document document = reader.read(inputStream);
            //根据文档对象获取根节点
            Element root = document.getRootElement();
            //获取根节点的所有子节点
            List<Element> elements = root.elements();
            for (Element e : elements) {
                map.put(e.getName(), e.getStringValue());
            }
        } catch (DocumentException e) {
            log.info("error read xml，please check xml format");
        }
        return map;
    }
}
