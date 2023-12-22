package com.aurine.cloudx.estate.util.delay.service;

import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface DelayTaskService {

    /**
     * <p>原子增加任务领取数，并获取属于当前领取者的偏移量</p>
     *
     * @param taskId 任务ID
     * @return 任务领取数量
     */
    BaseTask incrReceiveNumByTaskId(String taskId);

    /**
     * <p>任务变更为已执行</p>
     *
     * @param taskId 任务ID
     */
    BaseTask markTaskAsExecuted(String taskId);

    /**
     * <p>任务变更为已执行</p>
     *
     * @param taskId 任务ID
     */
    BaseTask markTaskAsNotExecuted(String taskId);

    /**
     * <p>根据任务ID删除任务</p>
     *
     * @param taskId 任务ID
     */
    void removeByTaskId(String taskId);

    /**
     * <p>根据任务ID进行判断任务是否存在</p>
     *
     * @param taskId 任务ID
     * @return 是否存在这个任务
     */
    boolean existsByTaskId(String taskId);

    /**
     * <p>根据任务ID进行判断任务是否存在</p>
     *
     * @param taskId 任务ID
     * @return 是否存在这个任务
     */
    BaseTask getByTaskId(String taskId);

    /**
     * <p>获取存在mongoDB中的所有延时任务</p>
     *
     * @return 是否存在这个任务
     */
    List<BaseTask> findAll();

    /**
     * <p>记录已添加的任务</p>
     *
     * @param task 任务信息
     * @return 是否保存成功
     */
    BaseTask recordTask(BaseTask task);

    /**
     * <p>记录已添加的任务</p>
     *
     * @param query 所要删除任务的查询条件
     * @return 是否有根据条件删除了集合中的某条记录
     */
    boolean removeByQuery(Query query);


}
