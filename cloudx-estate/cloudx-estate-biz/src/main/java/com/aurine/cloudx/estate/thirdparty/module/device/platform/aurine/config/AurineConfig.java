package com.aurine.cloudx.estate.thirdparty.module.device.platform.aurine.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * aurine配置信息
 *
 * @ClassName: AurineConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-08-11 10:52
 * @Copyright:
 */

@Component
@RefreshScope
public class AurineConfig {
    @Resource
    private Environment env;

//    /**
//     * 服务器地址
//     */
//    public static String apiEndPoint;
//    /**
//     * 版本
//     */
//    public static String apiVersion;
//    /**
//     * 基础连接地址
//     */
//    public static String appKey;
//    public static String appSecret;
//    public static String appType;
//    public static String userName;
    /**
     * kafka 客户端ID
     */
    public static String kafkaCliendId;

    /**
     * 遍历属性，并获取配置
     *
     * @throws Exception
     */
    @PostConstruct
    public void readConfig() throws Exception {
        String prefix = "thirdparty.middleplatform.aurine.";
        Field[] fields = AurineConfig.class.getFields();
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

