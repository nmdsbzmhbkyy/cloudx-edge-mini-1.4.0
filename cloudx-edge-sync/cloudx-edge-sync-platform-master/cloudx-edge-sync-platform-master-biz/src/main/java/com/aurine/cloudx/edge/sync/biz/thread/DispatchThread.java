package com.aurine.cloudx.edge.sync.biz.thread;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.edge.sync.biz.config.InitConfig;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.service.DispatchService;
import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

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

    @Resource
    private InitConfig initConfig;

    @Scheduled(fixedRate = 2000)
    public void dispatchThread() {
        //验证可调度的情况下，执行调度，生成下发请求数据
        try {
            if (initConfig.isInit()) {
                Object o = RedisUtil.get(Constants.DISPATCH_PROJECT_UUID);
                String projectUuid;
                if (Objects.isNull(o)) {
                    projectUuid = taskInfoService.getDispatchQueueList();
                    if (StrUtil.isEmpty(projectUuid)) {
                        return;
                    }
                    RedisUtil.set(Constants.DISPATCH_PROJECT_UUID,projectUuid,300);
                } else {
                    projectUuid = String.valueOf(o);
                }
                try {
                    dispatchService.dispatchQueue(null, projectUuid);
                } catch (Exception e) {
                    log.error("生成下发请求数据异常, projectUuid:{}", projectUuid, e);
                }
            }
        } catch (Exception e) {
            log.error("调度队列异常", e);
        }
    }
}
