package com.aurine.cloudx.estate.util.delay.dao;

import com.aurine.cloudx.estate.util.delay.entity.BaseTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

/**
 * <p>延时任务的mongoDB服务类</p>
 *
 * @author : 王良俊
 * @date : 2021-08-25 09:41:07
 */
@Service
public interface DelayTaskRepository extends MongoRepository<BaseTask, String> {

    /**
     * <p>
     * 根据任务ID删除任务
     * </p>
     *
     * @param taskId 任务ID
     */
    void removeByTaskId(String taskId);

    /**
     * <p>
     * 根据任务ID获取任务信息
     * </p>
     *
     * @param taskId 任务ID
     * @return 任务数据
     */
    BaseTask getBaseTaskByTaskId(String taskId);

    /**
     * <p>
     * 根据可被执行状态和任务ID获取任务
     * </p>
     *
     * @param taskId        任务ID
     * @param canBeExecuted 是否可被执行
     * @return 任务基类对象
     */
    BaseTask getBaseTaskByTaskIdAndCanBeExecuted(String taskId, boolean canBeExecuted);

    /**
     * <p>
     * 根据任务ID进行判断任务是否存在
     * </p>
     *
     * @param taskId 任务ID
     * @return 是否存在这个任务
     */
    boolean existsByTaskId(String taskId);

}
