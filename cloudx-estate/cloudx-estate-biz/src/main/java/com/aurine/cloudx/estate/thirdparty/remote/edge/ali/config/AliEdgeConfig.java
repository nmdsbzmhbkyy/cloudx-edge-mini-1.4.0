package com.aurine.cloudx.estate.thirdparty.remote.edge.ali.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * 配置信息
 *
 * @ClassName: AliEdgeConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-06-17 9:07
 * @Copyright:
 */

@Component
public class AliEdgeConfig {
    @Resource
    private Environment env;

    public static String apiEndPoint;
    public static String apiVersion;
    public static String apiResUrl;
    public static String appKey;
    public static String appSecret;

    public static String tokenEndPoint;
    public static String tokenVersion;
    public static String tokenResUrl;
    public static String tokenKey;
    public static String tokenSecret;




    /**
     * 遍历属性，并获取配置
     *
     * @throws Exception
     */
    @PostConstruct
    public void readConfig() throws Exception {
        String prefix = "thirdparty.edge-gateway.ali.";
        Field[] fields = AliEdgeConfig.class.getFields();
        for (Field field : fields) {
            field.set(null, getProperty(prefix + field.getName()));
        }
    }

    /**
     * 从配置文件获取属性
     *
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getProperty(String key) throws UnsupportedEncodingException {
        return new String(env.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
    }
}

