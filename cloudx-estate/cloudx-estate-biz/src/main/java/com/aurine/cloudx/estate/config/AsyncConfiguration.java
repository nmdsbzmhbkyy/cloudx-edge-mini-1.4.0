package com.aurine.cloudx.estate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * (AsyncConfiguration)
 *
 * @author xull
 * @version 1.0.0
 * @date 2020/11/3 16:13
 */
//@Configuration
public class AsyncConfiguration extends AsyncConfigurerSupport {
//    @Override
//    public Executor getAsyncExecutor() {
//        return new DelegatingSecurityContextExecutorService(Executors.newFixedThreadPool(50));
////        return new DelegatingSecurityContextExecutorService(Executors.newCachedThreadPool());
//    }
}
