package com.aurine.cloudx.wjy.canal.config;

import com.aurine.cloudx.wjy.canal.CanalClient;
import com.aurine.cloudx.wjy.canal.handle.DealCanalHandleChain;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @ClassName: CanalConfig
 * @author: 王伟 <wangwei@aurine.cn>
 * @date: 2021-03-24 8:37
 * @Copyright:
 */
@Component
public class CanalBoot{

    @Resource
    private CanalClient canalClient;
    @Resource
    private DealCanalHandleChain dealCanalHandleChain;

    public void start(){
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                canalClient.connection(dealCanalHandleChain);
            }
        });

    }
}
