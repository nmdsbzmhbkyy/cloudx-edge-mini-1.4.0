package com.aurine.cloudx.estate.thirdparty.module.device.platform.huawei.jackson.deserializer;


import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * <p>
 * Iot设备电量反序列化器（设备有可能给的不是电量百分比而是3.20v，这里要进行转换）
 * </p>
 *
 * @author : 王良俊
 * @date : 2021-07-16 16:17:51
 */
public class BatteryVoltageDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String batteryVoltageStr = jsonParser.getText();
        String[] split = batteryVoltageStr.split("\\.");
        if (ArrayUtil.isNotEmpty(split) && split.length == 2) {
            if (Double.parseDouble(batteryVoltageStr) <= 3) {
                return 0;
            } else if (Double.parseDouble(batteryVoltageStr) >= 3.66) {
                return 100;
            } else {
                return (int) ((Integer.parseInt(split[1]) / 66.0) * 100);
            }
        }
        return Integer.parseInt(batteryVoltageStr);

    }
}
