package com.aurine.cloudx.estate.util.delay.entity;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>媒体广告延时任务类</p>
 * @author : 王良俊
 * @date : 2021-10-14 13:55:12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Component
public class MediaAdDelayTask extends BaseTask {

    /**
     * 单位：秒
     */
    private static final Integer OFFSET = 15;

    /**
     * 广告id(这一批次的广告的自增序列ID)
     * project_media_ad 的 seq字段
     */
    private Long adSeq;

    /**
     * 要进行广告下发的设备ID
     */
    private String deviceId;

    public MediaAdDelayTask() {
        super();
    }

    public MediaAdDelayTask(@Nonnull Integer projectId, @Nonnull Integer adSeq, @Nonnull LocalDateTime delayTime, @Nonnull DelayTaskTopicEnum delayTaskTopicEnum) {
        super(OFFSET, delayTime, delayTaskTopicEnum, projectId);
//        this.adSeq = adSeq;
    }

    @Override
    public List<Index> getIndexes() {
        List<Index> indexList = new ArrayList<>();
        indexList.add(new Index("adId", Sort.Direction.ASC)
                .on("delayTaskTopicEnum", Sort.Direction.ASC)
                .on("delayTime", Sort.Direction.ASC)
                .named("media_ad_index"));
        return indexList;
    }

    @Override
    public Query getCancelQuery() {
        return Query.query(Criteria
                .where("adSeq").is(getAdSeq())
                .and("deviceId").is(getDeviceId())
                .and("delayTaskTopicEnum").is(getDelayTaskTopicEnum())
                .and("delayTime").is(getDelayTime()));
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
