package com.aurine.cloudx.estate.util.delay.init;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>用来自动创建延时任务所需要的组合索引和主键索引（spring不推荐使用@Index方式自动创建索引）</p>
 * @author : 王良俊
 * @date : 2021-08-30 11:20:17
 */
@Component
@Slf4j
public class TaskMongoDBIndexInit implements ApplicationContextAware {

    @Resource
    MongoTemplate mongoTemplate;

    AtomicInteger atomicInteger = new AtomicInteger();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        int i = atomicInteger.incrementAndGet();
        log.info("[MongoDB-延时任务] 尝试创建索引次数{}", i);
        try {
            mongoTemplate.indexOps(BaseTask.class).ensureIndex(new Index(BaseTask.TASK_ID, Sort.Direction.ASC).unique());
        } catch (UncategorizedMongoDbException e) {
            e.printStackTrace();
            if (i < 5) {
                // 删除所有索引重新创建
                log.warn("[MongoDB-延时任务] 索引变更需要删除原有索引重新创建");
                mongoTemplate.indexOps(BaseTask.class).dropAllIndexes();
                this.setApplicationContext(applicationContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, BaseTask> beansOfType = applicationContext.getBeansOfType(BaseTask.class);
        if (MapUtil.isNotEmpty(beansOfType)) {
            beansOfType.values().forEach(baseTask -> {
                try {
                    if (CollUtil.isNotEmpty(baseTask.getIndexes())) {
                        baseTask.getIndexes().forEach(index -> {
                            mongoTemplate.indexOps(BaseTask.class).ensureIndex(index);
                        });
                    }
                } catch (UncategorizedMongoDbException e) {
                    e.printStackTrace();
                    if (i < 5) {
                        // 删除所有索引重新创建
                        log.warn("[MongoDB-延时任务] 索引变更需要删除原有索引重新创建");
                        mongoTemplate.indexOps(BaseTask.class).dropAllIndexes();
                        this.setApplicationContext(applicationContext);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
