package com.aurine.cloudx.edge.sync.biz.thread;

import com.aurine.cloudx.edge.sync.biz.service.TaskInfoService;
import com.aurine.cloudx.edge.sync.common.config.GlobalVariable;
import com.aurine.cloudx.edge.sync.common.constant.SqlCacheConstants;
import com.aurine.cloudx.edge.sync.common.entity.po.TaskInfo;
import com.aurine.cloudx.edge.sync.common.utils.SqlCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @description: Sql缓存处理线程
 * @author: wangwei
 * @date: 2021/12/27 17:14
 **/
@Slf4j
@Component
public class SqlCacheThread {

    @Resource
    private TaskInfoService taskInfoService;

    @Resource
    private GlobalVariable globalVariable;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 定时任务处理缓存
     * */
    @Scheduled(fixedRate = 1000)
    public void dispatchCache() {
        int max = 60;
        try {
            long size = SqlCacheUtil.size();
            if (size>0) {
                log.info("[清空SQL缓存] 定期入库SQL {}", size);
                List<TaskInfo> list = new ArrayList<>();
                for (long i=0; i<size; i++) {
                    if (i>max) {
                        log.warn("超过最大批量入库限制");
                        break;
                    }
                    Object obj = redisTemplate.opsForList().leftPop(SqlCacheConstants.SQL_CACHE_KEY);
                    if (obj!=null) {
                        list.add((TaskInfo) obj);
                    }
                    else {
                        break;
                    }
                }
                if (list.size()>0) {
                    taskInfoService.saveTaskInfo(list);
                }
            }
        }
        catch (Exception e) {
            log.error("Sql缓存处理定时任务异常", e);
        }
    }
}
