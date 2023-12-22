package com.aurine.cloudx.estate.thirdparty.main.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.estate.entity.IotEventCallback;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Iot设备事件 DTO
 * </p>
 * @author : 王良俊
 * @date : 2021-07-21 13:50:32
 */
@Data
@AllArgsConstructor
public class EventIotDeviceCallbackDTO extends BaseDTO {

    private Set<IotEventCallback> iotEventCallbackSet;
}
