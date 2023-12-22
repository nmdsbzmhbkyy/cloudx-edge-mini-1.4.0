package com.aurine.cloudx.estate.component.chain.annotation;


import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.component.chain.AbstractHandleChain;
import com.aurine.cloudx.estate.component.chain.AbstractHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 自动注册定义的Chain、处理链工厂
 * @Auther: 王伟 <wangwei@aurine.cn>
 * @create 2021/09/17 8:55
 */
@Component
@Slf4j
public class ChainManager implements ApplicationContextAware {

    private static ChainManager managerInstance;//工厂单例

    private Map<Class, AbstractHandleChain> chainBeanMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取工厂单例
        ChainManager.managerInstance = applicationContext.getBean(ChainManager.class);


        //获取所有带有@Chain注解的Chain对象单例
        Map<String, AbstractHandleChain> beanMap = applicationContext.getBeansOfType(AbstractHandleChain.class);

        if (CollUtil.isNotEmpty(beanMap)) {

            beanMap.forEach((key, instance) -> {
                if (instance.getClass().getAnnotation(Chain.class) != null) {
                    managerInstance.chainBeanMap.put(instance.getClass(), instance);
                }
            });


            //获取所有handler对象
            Map<String, AbstractHandler> allHandlerMap = applicationContext.getBeansOfType(AbstractHandler.class);

            //将合法的Handler对象自动注册到Chain中
            if (CollUtil.isNotEmpty(managerInstance.chainBeanMap) && CollUtil.isNotEmpty(allHandlerMap)) {
                log.info("开始注册处理链");

                allHandlerMap.forEach((handlerName, handlerInstance) -> {
                    if (handlerInstance.getClass().getAnnotation(ChainHandler.class) != null) {

                        AbstractHandleChain chain = managerInstance.chainBeanMap.get(handlerInstance.getClass().getAnnotation(ChainHandler.class).chainClass());
                        if (chain != null) {
                            chain.addHandler(handlerInstance);
                            log.info("处理器： {} 被注册到处理链 {} 中", handlerInstance.getClass().getSimpleName(), chain.getClass().getSimpleName());
                        }
                    }
                });
                log.info("处理链注册完成");

//                for (Class chainClass : managerInstance.chainBeanMap.keySet()) {
//
//                    Map<String, AbstractHandler> handlerMap = new HashMap<>();
//
//                    //过滤所有使用了@ChainHandler注解，并且继承了AbstractHandler的处理器对象，根据注解属性chainName，注册到chain实例中
//                    allHandlerMap.forEach((handlerName, instance) -> {
//                        if (instance.getClass().getAnnotation(ChainHandler.class) != null) {
//                            if (instance.getClass().getAnnotation(ChainHandler.class).chainClass() == chainClass) {
//                                handlerMap.put(handlerName, instance);
//                            }
//                        }
//                    });
//
//                    if (CollUtil.isNotEmpty(handlerMap)) {
//                        managerInstance.initChainHandle(chainClass, managerInstance.chainBeanMap.get(chainClass), handlerMap);
//                    }
//                }

            }
        }
    }


//    /**
//     * 自动注册chain
//     *
//     * @param chainClass
//     * @param chain
//     * @param handlerMap
//     */
//    private void initChainHandle(Class chainClass, AbstractHandleChain chain, Map<String, AbstractHandler> handlerMap) {
//
//        log.info("初始化处理链{}", chainClass);
//        List<AbstractHandler<T>> list = new ArrayList<>();
//
//        handlerMap.values().forEach(e -> {
//            list.add(e);
//        });
//
//        chain.setHandlerList(list);
//        log.info("初始化处理链{} 完成，共{}个handler", chainClass, list.size());
//    }

    /**
     * 获取处理链
     *
     * @param chainClass
     * @return
     */
    public static AbstractHandleChain getChain(Class chainClass) {
        if (managerInstance.chainBeanMap.get(chainClass) == null) {
            throw new NullPointerException(chainClass + " 处理链未被定义");
        }
        return managerInstance.chainBeanMap.get(chainClass);
    }

}