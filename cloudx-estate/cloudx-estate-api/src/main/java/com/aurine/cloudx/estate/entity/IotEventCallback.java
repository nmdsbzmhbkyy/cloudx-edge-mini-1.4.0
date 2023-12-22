package com.aurine.cloudx.estate.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.annotation.OrderByDesc;
import com.aurine.cloudx.estate.convert.LocalDateTimeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * Iot设备事件回调对象基类
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-15 15:53:51
 */
@Data
@NoArgsConstructor
@HeadRowHeight(20)
@Document("IotEventCallback")
public class IotEventCallback implements Serializable {

    @Id
    @Field("_id")
    private String callbackId;

    /**
     * 回调日志对应设备的ID
     */
    private String deviceId;

    @ExcelProperty("序号")
    @ColumnWidth(20)
    private Integer seq;

    @ExcelProperty("事件")
    @ColumnWidth(40)
    private String event;

    @ExcelProperty("状态")
    @ColumnWidth(40)
    private String status;

//    @OrderByDesc
    @ColumnWidth(40)
    @ExcelProperty(value = "上报时间", converter = LocalDateTimeConverter.class)
    @DateTimeFormat("yyyy年MM月dd日HH时mm分ss秒")
    private LocalDateTime eventTime;

    public IotEventCallback(String deviceId, String event, String status, LocalDateTime eventTime) {
        this.deviceId = deviceId;
        this.event = event;
        this.status = status;
        this.eventTime = eventTime;
    }

    public IotEventCallback(String deviceId, String event, LocalDateTime eventTime) {
        this.deviceId = deviceId;
        this.event = event;
        this.eventTime = eventTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IotEventCallback that = (IotEventCallback) o;
        return StringUtil.equals(event, that.event) && StringUtil.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, status);
    }

}
