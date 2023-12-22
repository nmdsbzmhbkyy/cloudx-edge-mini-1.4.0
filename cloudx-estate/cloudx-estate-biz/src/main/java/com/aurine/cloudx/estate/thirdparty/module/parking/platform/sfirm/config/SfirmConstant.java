package com.aurine.cloudx.estate.thirdparty.module.parking.platform.sfirm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

/**
 * 赛菲姆配置信息
 *
 * @ClassName: SfirmConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-07-24 10:52
 * @Copyright:
 */

@Component
public class SfirmConstant {
    @Autowired
    private Environment env;

    public static String appId;
    public static String appSecrets;
    public static String baseUrl;
    public static String version;

    /**
     * 遍历属性，并获取配置
     * @throws Exception
     */
    @PostConstruct
    public void readConfig() throws Exception {
        String prefix = "thirdparty.parking.sfirm.";
        Field[] fields = SfirmConstant.class.getFields();
        for (Field field : fields) {
            field.set(null, getProperty(prefix + field.getName()));
        }
    }

    /**
     * 从配置文件获取属性
     * @param key
     * @return
     * @throws UnsupportedEncodingException
     */
    private String getProperty(String key) throws UnsupportedEncodingException {
        return new String(env.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");
    }
}

