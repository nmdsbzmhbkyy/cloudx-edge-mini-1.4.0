package com.aurine.cloudx.estate.thirdparty.module.device.handler.impl.edge.mili.lift;

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

/**
 * <p>边缘网关电梯控制器参数转换处理类-丽人时段</p>
 *
 * @author : 王良俊
 * @date : 2022/2/7 17:31
 */
@Component
@Slf4j
public class EdgeLiftFloorSwitchParamsHandler implements BaseParamDataHandler {

    @Override
    public void convertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());

        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            ArrayNode sourceFloorSwArr = (ArrayNode) sourceParam.path("floorSwArray");

            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            ArrayNode targetFloorSwArr = targetParam.putArray("floorSwArray");
            sourceFloorSwArr.forEach(floorNumNode -> {
                int floorNum = Integer.parseInt(floorNumNode.asText(), 16);
                targetFloorSwArr.add(String.valueOf(floorNum));
            });
            if (targetFloorSwArr.size() < 48) {
                String zero = "0";
                for (int i = targetFloorSwArr.size(); i <= 48; i++) {
                    targetFloorSwArr.add(zero);
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
            ArrayNode sourceFloorSwArr = (ArrayNode) sourceParam.path("floorSwArray");

            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            ArrayNode targetFloorSwArr = targetParam.putArray("floorSwArray");
            sourceFloorSwArr.forEach(floorNumNode -> {
                int floorNum = Integer.parseInt(floorNumNode.asText());
                targetFloorSwArr.add(Integer.toUnsignedString(floorNum, 16));
            });
            if (targetFloorSwArr.size() < 48) {
                String zero = "0";
                for (int i = targetFloorSwArr.size(); i <= 48; i++) {
                    targetFloorSwArr.add(zero);
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
        return DeviceParamEnum.EDGE_LIFT_FLOOR_OBJ;
    }

    /*
     * 测试方法
     * */
/*
    public static void main(String[] args) throws JsonProcessingException {
        int a = 196;

        System.out.println(Integer.toUnsignedString(a, 16));

        String json = "{\"LiftBeautyPeriodParamsObj\":{\"floorSwArray\":[3,\"196\",\"64\",\"8\",\"129\",\"16\",\"146\",\"32\",\"6\",\"2\",\"65\",\"16\",\"32\",\"128\",\"0\",\"72\",\"129\",\"16\",\"112\",\"65\",\"0\",\"4\",\"0\",\"17\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]}}";
        System.out.println("origin:" + json);
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(json);
        EdgeLiftFloorSwitchParamsHandler edgeLiftDeviceParamsHandler = new EdgeLiftFloorSwitchParamsHandler();
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
    }
*/

}
