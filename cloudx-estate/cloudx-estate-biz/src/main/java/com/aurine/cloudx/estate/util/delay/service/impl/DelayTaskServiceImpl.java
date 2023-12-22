package com.aurine.cloudx.estate.util.delay.service.impl;

import com.aurine.cloudx.estate.entity.ProjectBillingInfo;
import com.aurine.cloudx.estate.util.bean.BeanPropertyUtil;
import com.aurine.cloudx.estate.util.bean.FieldMapping;
import com.aurine.cloudx.estate.util.delay.dao.DelayTaskRepository;
import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import com.aurine.cloudx.estate.util.delay.service.DelayTaskService;
import com.aurine.cloudx.estate.vo.ProjectBillingInfoVo;
import com.mongodb.client.result.DeleteResult;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>延时任务服务实现类</p>
 *
 * @author : 王良俊
 * @date : 2021-08-25 09:57:00
 */
@Service
public class DelayTaskServiceImpl implements DelayTaskService {

    @Resource
    MongoTemplate mongoTemplate;

    @Resource
    DelayTaskRepository delayTaskRepository;

    @Override
    public BaseTask incrReceiveNumByTaskId(String taskId) {
        return mongoTemplate.findAndModify(
                Query.query(Criteria.where("taskId").is(taskId)),
                new Update().inc("receiveNum", 1),
                BaseTask.class
        );
    }

    @Override
    public BaseTask markTaskAsExecuted(String taskId) {
        return mongoTemplate.findAndModify(
                Query.query(Criteria.where("taskId").is(taskId).and("canBeExecuted").is(true)),
                new Update().set("canBeExecuted", false),
                BaseTask.class
        );
    }

    @Override
    public BaseTask markTaskAsNotExecuted(String taskId) {
        return mongoTemplate.findAndModify(
                Query.query(Criteria.where("taskId").is(taskId)),
                new Update().set("canBeExecuted", true),
                BaseTask.class
        );
    }

    @Override
    public void removeByTaskId(String taskId) {
        delayTaskRepository.removeByTaskId(taskId);
    }

    @Override
    public boolean existsByTaskId(String taskId) {
        return delayTaskRepository.existsByTaskId(taskId);
    }

    @Override
    public BaseTask getByTaskId(String taskId) {
        return delayTaskRepository.getBaseTaskByTaskIdAndCanBeExecuted(taskId, true);
    }

    @Override
    public List<BaseTask> findAll() {
        return mongoTemplate.find(Query.query(Criteria.where("canBeExecuted").is(true)), BaseTask.class);
    }

    @Override
    public BaseTask recordTask(BaseTask task) {
        return delayTaskRepository.save(task);
    }

    @Override
    public boolean removeByQuery(Query query) {
        DeleteResult remove = mongoTemplate.remove(query, BaseTask.class);
        return remove.getDeletedCount() != 0;
    }


}
