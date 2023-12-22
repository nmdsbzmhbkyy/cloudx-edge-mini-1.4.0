package com.aurine.cloudx.estate.util.delay.entity;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.constant.enums.PersonTypeEnum;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
 * <p>用于住户/员工授权到期</p>
 *
 * @author : 王良俊
 * @date : 2021-08-25 11:10:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class PersonDelayTask extends BaseTask {

    /**
     * 单位：秒
     */
    private static final Integer OFFSET = 15;

    /**
     * 人员ID（住户或员工ID）
     */
    private String personId;

    /**
     * 人员类型枚举（住户或员工）
     */
    private PersonTypeEnum personTypeEnum;

    public PersonDelayTask() {
        super();
    }

    public PersonDelayTask(@Nonnull Integer projectId, @Nonnull String personId, @Nonnull LocalDateTime delayTime,
                           @Nonnull PersonTypeEnum personTypeEnum, @Nonnull DelayTaskTopicEnum delayTaskTopicEnum) {
        super(OFFSET, delayTime, delayTaskTopicEnum, projectId);
        this.personId = personId;
        this.personTypeEnum = personTypeEnum;
    }

    @Override
    public List<Index> getIndexes() {
        List<Index> indexList = new ArrayList<>();
        indexList.add(new Index("personId", Sort.Direction.ASC)
                .on("delayTaskTopicEnum", Sort.Direction.ASC)
                .on("delayTime", Sort.Direction.ASC)
                .named("person_index"));
        return indexList;
    }

    @Override
    public Query getCancelQuery() {
        return Query.query(Criteria
                .where("personId").is(getPersonId())
                .and("delayTaskTopicEnum").is(getDelayTaskTopicEnum())
                .and("delayTime").is(getDelayTime()));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
