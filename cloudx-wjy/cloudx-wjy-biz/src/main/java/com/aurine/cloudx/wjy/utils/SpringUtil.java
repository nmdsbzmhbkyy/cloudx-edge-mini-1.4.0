package com.aurine.cloudx.wjy.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext = null;
	public static ApplicationContext getApplicationContext(){return applicationContext;}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// TODO Auto-generated method stub
        System.out.println("==========applicationContext===========");
		if(SpringUtil.applicationContext == null){
			SpringUtil.applicationContext = applicationContext;
		}
	}
	
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }
 
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }
    
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
