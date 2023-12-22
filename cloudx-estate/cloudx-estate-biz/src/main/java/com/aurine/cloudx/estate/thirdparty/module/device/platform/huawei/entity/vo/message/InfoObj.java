package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.entity.vo.message;

import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer.InfoReleaseTimeDeserializer;
import com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.serializer.InfoReleaseTimeSerializer;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <p>设备信息对象</p>
 * @author : 王良俊
 * @date : 2021-09-10 13:49:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InfoObj {

    public InfoObj(Integer msgid, Integer msgType, String title, String content) {
        this.msgid = msgid;
        this.msgType = msgType;
        this.title = title;
        this.content = content;
    }

    /**
     * 信息ID 递增ID？
     */
    private Integer msgid;

    /**
     * 0:纯文本；
     * 1:富文本；
     * 2:图片
     * 3:音乐
     * 4:纯文本&音乐
     * 5:富文本&音乐
     * 6:图片&音乐
     */
    @JsonProperty("msgtype")
    private Integer msgType;

    /**
     * 信息标题 标题最长 1024 字符
     */
    private String title;

    /**
     * 信息内容 内容最长 102400（100k）字符
     */
    private String content;

    /**
     * 发布时间 yyyy-MM-dd-HH:mm:ss（默认为当前时间）
     */
    @JsonDeserialize(using = InfoReleaseTimeDeserializer.class)
    @JsonSerialize(using = InfoReleaseTimeSerializer.class)
    private LocalDateTime time;

    /**
     * 有效天数
     * 默认值为0，不关心该有效时间，由设备端管理，
     * 当指定有效天数，则：有期效=发布时间+有效天数
     */
    @JsonProperty("validity_day")
    private Integer validityDay;

    /**
     * 图片URL地址 只能是png格式
     */
    private String picUrl;

    /**
     * 音乐URL地址 只能是wav格式
     */
    private String voiceUrl;

    public static String getObjName() {
        return "InfoObj";
    }
}
