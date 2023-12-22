package com.aurine.cloudx.edge.sync.common.config;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wrm
 * @Date: 2022/01/19 19:31
 * @Package: com.example.aoptest.usertaskstrategy
 * @Version: 1.0
 * @Remarks: 策略工厂
 **/
public class BaseStrategyFactory {

    private static Map<String, BaseStrategy> strategyMap = new HashMap<>();

    public static BaseStrategy getStrategy(String name) {
        BaseStrategy baseStrategy = strategyMap.get(name);
        if (baseStrategy == null) {
            // 没有对应实现，执行默认实现类,暂未实现
            return strategyMap.get("default");
        } else {
            return baseStrategy;
        }
    }

    public static void registerStrategy(String name, BaseStrategy baseStrategy) {
        if (StringUtils.isEmpty(name) || baseStrategy == null) {
            return;
        }
        strategyMap.put(name, baseStrategy);
    }
}