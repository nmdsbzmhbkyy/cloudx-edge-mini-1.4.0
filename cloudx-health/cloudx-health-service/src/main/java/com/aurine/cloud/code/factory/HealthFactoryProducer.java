package com.aurine.cloud.code.factory;


import com.aurine.cloud.code.entity.enums.CityEnum;
import com.aurine.cloud.code.util.CheckHealthUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 工厂生成器,根据设备ID选择工厂实例
 *
 * @ClassName: HealthFactoryProducer
 * @author: yz
 * @date: 2021-06-10 9:01
 * @Copyright:
 */

@Component
@Slf4j
public class HealthFactoryProducer{



    /**
     * 根据地名获取工厂
     *
     * @param placeName 地名
     * @return
     */
    public static AbstractHealthFactory getFactory(String placeName) {

         if (placeName.equals(CityEnum.FUJIAN_CODE.value)){
             return CheckFujianQrCodeFactory.getInstance();
         }
        throw new RuntimeException("未找到该版本实例:" + placeName);
    }

}
