package com.aurine.cloudx.open.common.data.config;

import com.aurine.cloudx.open.common.data.injector.CustomerSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置
 *
 * @author : Qiu
 * @date : 2022 04 22 8:51
 */

//@Configuration
//@MapperScan("com.aurine.cloudx.open.common.data.mapper")
public class CustomMybatisPlusConfig {

    @Bean
    public ISqlInjector iSqlInjector() {
        return new CustomerSqlInjector();
    }
}
