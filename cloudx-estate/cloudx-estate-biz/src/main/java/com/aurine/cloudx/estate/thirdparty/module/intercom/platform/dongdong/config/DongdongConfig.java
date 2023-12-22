package com.aurine.cloudx.estate.thirdparty.module.intercom.platform.dongdong.config;


import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>咚咚配置类</p>
 *
 * @ClassName: DongdongConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2020-11-13 14:51
 * @Copyright:
 */
@Component
@RefreshScope
public class DongdongConfig {


    @Resource
    private Environment env;

    //    @Value("${thirdparty.intercom.dongdong.appKey}")
    public static String appKey;
    //
//    @Value("${thirdparty.intercom.dongdong.userId}")
    public static String userId;
    //
//    @Value("${thirdparty.intercom.dongdong.appSecrets}")
    public static String secretKey;
    //
//    @Value("${thirdparty.intercom.dongdong.baseUrl}")
    public static String baseUrl;
    //    @Value("${thirdparty.intercom.dongdong.version}")
    public static String version;

    public static final String CONTENT_TYPE = "application/json";

    public static final String USER_AGENT = "WUYE_SDK/2.0 (" + System.getProperty("os.name") + "; " +
            System.getProperty("os.version") + "; " + System.getProperty("os.arch") + ") JAVA/1.7 "
            + "(Wuye Server SDK V3.0.0 and so on..) cli/UnknownZEND/2.6.0";


    /**
     * 遍历属性，并获取配置
     *
     * @throws Exception
     */
    @PostConstruct
    public void readConfig() throws Exception {
        String prefix = "thirdparty.intercom.dongdong.";
        Field[] fields = DongdongConfig.class.getFields();


        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {

            } else {
                field.set(null, getProperty(prefix + field.getName()));
            }
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
