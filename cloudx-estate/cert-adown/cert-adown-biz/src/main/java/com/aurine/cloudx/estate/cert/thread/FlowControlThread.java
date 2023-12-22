package com.aurine.cloudx.estate.cert.thread;

import com.aurine.cloudx.estate.cert.config.CertAdownConfig;
import com.aurine.cloudx.estate.cert.util.TokenBucketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * 流量控制线程
 *
 * @description:
 * @author: wangwei
 * @date: 2021/12/14 18:05
 **/
@Slf4j
@Component
@Order(3)
public class FlowControlThread implements ApplicationRunner {


    @Override
    public void run(ApplicationArguments args) throws Exception {
        //以固定速率生成令牌，生成速率为上游限速速率，桶容量为1时，为全局限速速率
//		String tokenBucketName = TokenBucketConstant.TOKEN_BUCKET_REDIS_PREFIX + "";
        Thread thread = new Thread() {
            public void run() {
                log.info("控流线程已启动");
                while (true) {
                    try {
                        TokenBucketUtil.generateToken();
                        Thread.sleep(CertAdownConfig.getGenerateTokenRate());
                    } catch (Exception e) {
                        log.error("定时生成令牌执行异常：", e);
                    }
                }
            }
        };
        thread.start();
    }
}
