package com.aurine.cloudx.estate.util.delay.entity;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>任务基本信息</p>
 *
 * @author : 王良俊
 * @date : 2021-08-25 09:34:49
 */
@Data
@Document("DelayTaskCache")
@Component
public class BaseTask {

    public static final String TASK_ID = "taskId";

    public BaseTask() {
        this.taskId = UUID.randomUUID().toString().replaceAll("-", "");
    }

    public BaseTask(String taskId) {
        this();
        this.taskId = taskId;
    }

    /**
     * 项目ID
     */
    private Integer projectId;

    public BaseTask(long offset, LocalDateTime delayTime, DelayTaskTopicEnum delayTaskTopicEnum, Integer projectId) {
        this();
        this.offset = offset;
        this.delayTime = delayTime;
        this.delayTaskTopicEnum = delayTaskTopicEnum;
        this.projectId = projectId;
    }

    /**
     * mongoDB的id
     */
    @MongoId
    @Field("_id")
    protected ObjectId id;

    /**
     * 任务ID
     */
    @Field(BaseTask.TASK_ID)
//    @Indexed
    protected String taskId;

    /**
     * 任务领取数量
     */
    protected int receiveNum = 1;

    /**
     * 获取时间偏移量
     */
    protected long offset;

    /**
     * 什么时间要触发
     */
    private LocalDateTime delayTime;

    /**
     * 是否可以被执行
     */
    private boolean canBeExecuted = true;


    /**
     * 任务要做的事情
     */
    private DelayTaskTopicEnum delayTaskTopicEnum;

    /**
     * 是否是最开始的任务
     */
    private boolean originTask = false;

    /**
     * <p>获取任务要执行的时间</p>
     */
    public LocalDateTime getDelayTime() {
        // 如果是第一个任务这里执行时间不推迟
        if (originTask) {
            return delayTime;
        }
        return delayTime.plusSeconds(offset * receiveNum);
    }

    /**
     * <p>获取任务原先的执行时间</p>
     */
    public LocalDateTime getOriginDelayTime() {
        return delayTime;
    }

    /**
     * <p>返回要创建的索引列表</p>
     *
     * @return 索引列表
     */
    public List<Index> getIndexes() {
        return new ArrayList<>();
    }

    /**
     * <p>获取要取消某个任务所需要的查询条件</p>
     *
     * @return 查询条件对象
     */
    public Query getCancelQuery() {
        return null;
    }

    @Override
    @JsonIgnore
    public String toString() {
        return JSON.toJSONString(this);
    }
}
