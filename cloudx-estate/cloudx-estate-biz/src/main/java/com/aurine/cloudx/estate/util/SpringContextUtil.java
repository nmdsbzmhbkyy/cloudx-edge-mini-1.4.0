package com.aurine.cloudx.estate.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author ds
 * @Date 2022-05-05
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    /**
     * spring的应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * 初始化时将应用上下文设置进applicationContext
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext=applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 根据bean名称获取某个bean对象
     *
     * @param name bean名称
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据bean的class获取某个bean对象
     * @param beanClass
     * @param <T>
     * @return
     * @throws BeansException
     */
    public static <T> T getBean(Class<T> beanClass) throws BeansException {
        return applicationContext.getBean(beanClass);
    }

    /**
     * 获取spring.profiles.active
     * 返回应用程序上下文（getApplicationContext()）的环境（getEnvironment()）中激活的配置文件（getActiveProfiles()）的第一个元素（[0]）。
     * 这个方法的主要目的是获取当前激活的配置文件，通常在应用程序的配置文件（如application.properties或application.yml）中定义。这个配置文件通常用于设置应用程序的运行环境，例如开发环境、测试环境或生产环境。
     * 这个方法返回的是一个字符串，因为配置文件中的值可能是一个字符串。如果配置文件中的值不是字符串，那么这个方法将返回一个字符串类型的默认值，例如空字符串（""）。
     * @return
     */
    public static String getProfile(){
        return getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }
}