package com.aurine.cloudx.edge.sync.biz.thread;

import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.service.DispatchService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.config.QueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Author: wrm
 * @Date: 2022/01/20 8:51
 * @Package: com.aurine.cloudx.edge.sync.biz.thread
 * @Version: 1.0
 * @Remarks: 分发业务线程，使用信号量，避免修改操作过期旧数据导致脏读影响正常业务
 **/
@Component
@Slf4j
public class DispatchThread {
    @Resource
    private DispatchService dispatchService;

    @Resource
    private TaskInfoService taskInfoService;

    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(50);

    public void startDispatchThread() {
        fixedThreadPool.execute(() -> {
            log.info("调度线程已启动");
            while (true) {

                //验证可调度的情况下，执行调度，生成下发请求数据
                try {
                    List<String> uuidList = taskInfoService.getDispatchQueueList();

                    for (String projectUuid : uuidList) {
                        //TODO 信号量改分布式锁
                        // 获取信号量对象
                        Semaphore semaphore = InitConfig.getSemaphore(projectUuid);
                        // 尝试获取信号量
                        semaphore.acquire();
                        try {
                            dispatchService.dispatchQueue(null, projectUuid);
                        } catch (Exception e) {
                            log.error("生成下发请求数据异常", e);
                        }
                        // 释放信号量
                        semaphore.release();
                    }
                    Thread.sleep(QueueConfig.getDispatchThreadRate());
                } catch (Exception e) {
                    //e.printStackTrace();
                    log.error("调度队列异常", e);
                }
            }
        });
    }
}
