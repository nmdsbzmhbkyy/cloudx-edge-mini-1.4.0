package com.aurine.cloudx.estate.util.delay;

import cn.hutool.core.date.DatePattern;
import com.aurine.cloudx.estate.util.delay.constants.DelayTaskTopicEnum;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName: DelayTask
 * @author: 王良俊 <>
 * @date:  2020年11月02日 上午09:17:05
 * @Copyright:
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Deprecated
public class DelayTask {

    private LocalDateTime delayTime;


    private int projectId;

    private DelayTaskTopicEnum delayTaskTopicEnum;

    public String getTaskTopic() {
        return delayTaskTopicEnum.topic;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_MINUTE_PATTERN);
        return "DelayTask{" +
                "topic=" + delayTaskTopicEnum.getTopic() +
                ", delayTime=" + delayTime.format(dateTimeFormatter) +
                ", projectId=" + projectId +
                '}';
    }
}
