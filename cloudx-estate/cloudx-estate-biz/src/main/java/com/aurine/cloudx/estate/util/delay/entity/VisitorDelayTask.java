package com.aurine.cloudx.estate.util.delay.entity;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import lombok.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>用于访客签离</p>
 *
 * @author : 王良俊
 * @date : 2021-08-25 11:10:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class VisitorDelayTask extends BaseTask {



    /**
     * 单位：秒
     */
    private static final Integer OFFSET = 15;

    /**
     * 访问记录ID
     */
    private String visitId;

    public VisitorDelayTask() {
        super();
    }

    public VisitorDelayTask(@Nonnull Integer projectId, @Nonnull String visitId, @Nonnull LocalDateTime delayTime, @Nonnull DelayTaskTopicEnum delayTaskTopicEnum) {
        super(OFFSET, delayTime, delayTaskTopicEnum, projectId);
        this.visitId = visitId;
    }

    @Override
    public List<Index> getIndexes() {
        List<Index> indexList = new ArrayList<>();
        indexList.add(new Index("visitId", Sort.Direction.ASC)
                .on("delayTaskTopicEnum", Sort.Direction.ASC)
                .on("delayTime", Sort.Direction.ASC)
                .named("visitor_index"));
        return indexList;
    }

    @Override
    public Query getCancelQuery() {
        return Query.query(Criteria
                .where("visitId").is(getVisitId())
                .and("delayTaskTopicEnum").is(getDelayTaskTopicEnum())
                .and("delayTime").is(getDelayTime()));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
