package com.aurine.cloudx.estate.thirdparty.module.device.handler.impl.edge.mili.lift;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.base.BaseParamDataHandler;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>边缘网关电梯控制器参数转换处理类-丽人时段</p>
 *
 * @author : 王良俊
 * @date : 2022/2/7 17:31
 */
@Component
@Slf4j
public class EdgeLiftBeautyPeriodParamsHandler implements BaseParamDataHandler {

    @Override
    public void convertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());
        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fieldIterator = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            while(fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> next = fieldIterator.next();
                String paramName = next.getKey();
                JsonNode value = next.getValue();
                if ("validTime".equals(paramName)) {
                    ArrayNode targetArrayNode = targetParam.putArray(paramName);
                    ArrayNode sourceValidTimeArr = (ArrayNode) value;
                    if (sourceValidTimeArr.size() == 4) {
                        targetArrayNode.add(sourceValidTimeArr.get(0).asText() + ":" + sourceValidTimeArr.get(1).asText());
                        targetArrayNode.add(sourceValidTimeArr.get(2).asText() + ":" + sourceValidTimeArr.get(3).asText());
                    }
                } else {
                    targetParam.set(paramName, value);
                }
            }
        }
    }

    @Override
    public void revertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());
        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fieldIterator = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            while(fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> next = fieldIterator.next();
                String paramName = next.getKey();
                JsonNode value = next.getValue();
                if ("validTime".equals(paramName)) {
                    ArrayNode targetArrayNode = targetParam.putArray(paramName);

                    ArrayNode sourceValidTimeArr = (ArrayNode) value;
                    sourceValidTimeArr.forEach(timeNode -> {
                        String time = timeNode.asText();
                        if (StrUtil.isNotEmpty(time)) {
                            String[] split = time.split(":");
                            targetArrayNode.add(split[0]);
                            targetArrayNode.add(split[1]);
                        }
                    });
                } else {
                    targetParam.set(paramName, value);
                }
            }
        }
    }


    @Override
    public PlatformEnum getPlateForm() {
        return PlatformEnum.AURINE_EDGE_MIDDLE;
    }

    @Override
    public DeviceParamEnum getServiceId() {
        return DeviceParamEnum.EDGE_LIFT_BEAUTY_PERIOD_PARAMS_OBJ;
    }

    /*
     * 测试方法
     * */
/*    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"LiftBeautyPeriodParamsObj\":{\"bswitch\":1,\"validTime\":[\"03:00\",\"05:08\"]}}";
        System.out.println("origin:" + json);
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(json);
        EdgeLiftBeautyPeriodParamsHandler edgeLiftDeviceParamsHandler = new EdgeLiftBeautyPeriodParamsHandler();
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        jsonNode.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            JsonNode value = stringJsonNodeEntry.getValue();
            objectNode1.set(key, value);
        });
        edgeLiftDeviceParamsHandler.revertHandler(jsonNode, objectNode1);
        System.out.println("revert:" + objectNode1);
        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode1.fields().forEachRemaining(stringJsonNodeEntry -> {
            String key = stringJsonNodeEntry.getKey();
            JsonNode value = stringJsonNodeEntry.getValue();
            objectNode2.set(key, value);
        });
        edgeLiftDeviceParamsHandler.convertHandler(objectNode1, objectNode2);
        System.out.println("convert:" + objectNode2);
    }*/

}
