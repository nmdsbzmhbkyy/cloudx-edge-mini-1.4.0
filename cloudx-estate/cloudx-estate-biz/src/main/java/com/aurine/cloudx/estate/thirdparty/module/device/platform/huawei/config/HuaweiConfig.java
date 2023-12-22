package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * huawei配置信息
 *
 * @ClassName: SfirmConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-24 10:52
 * @Copyright:
 */

@Component
@RefreshScope
@Deprecated
public class HuaweiConfig {
    @Resource
    private Environment env;

    /**
     * 服务器地址
     */
    public static String apiEndPoint;
    /**
     * 版本
     */
    public static String apiVersion;
    /**
     * 基础连接地址
     */
    public static String apiResUrl;
    public static String appKey;
    public static String appSecret;
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
        String prefix = "thirdparty.middleplatform.huawei.";
        Field[] fields = HuaweiConfig.class.getFields();
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

