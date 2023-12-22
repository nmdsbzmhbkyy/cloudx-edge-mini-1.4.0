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
public class CommandStrategyFactory {

    private static Map<String, CommandStrategy> commandStrategyMap = new HashMap<>();

    public static CommandStrategy getStrategy(String name) {
        CommandStrategy commandStrategy = commandStrategyMap.get(name);
        if (commandStrategy == null) {
            // 没有对应实现，执行默认实现类,暂未实现
            return commandStrategyMap.get("default");
        } else {
            return commandStrategy;
        }
    }

    public static void registerStrategy(String name, CommandStrategy commandStrategy) {
        if (StringUtils.isEmpty(name) || commandStrategy == null) {
            return;
        }
        commandStrategyMap.put(name, commandStrategy);
    }
}