package com.aurine.cloudx.estate.thirdparty.module.device.handler.impl.edge.mili.lift;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.constant.enums.DeviceParamEnum;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.base.BaseParamDataHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>边缘网关电梯控制器参数转换处理类</p>
 *
 * @author : 王良俊
 * @date : 2022/2/7 17:31
 */
@Component
@Slf4j
public class EdgeLiftPeriodParamsHandler implements BaseParamDataHandler {

    private static final String PERIODS = "periods";

    private static final String VALID_TIME = "validTime";
    private static final String VALID_TIME_END_TIME = "validTimeEndTime";
    private static final String FLOOR_ARRAY = "floorArray";

    //TODO: 这里后面换成对应的对象来处理现在先这么写
    @Override
    public void convertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());

        //TODO: 还少个楼层数组的设置
//        System.out.println("转换前：" + source);
        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fields = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            ArrayNode sourcePeriodArrNode = (ArrayNode) sourceParam.path(PERIODS);
            ArrayNode targetPeriodArrNode = targetParam.putArray(PERIODS);

            sourcePeriodArrNode.forEach(period -> {
                // 转换后的参数都放到这个对象里面
                ObjectNode targetPeriodItem = objectMapper.createObjectNode();

                // 获取到每一个时段的迭代器
                Iterator<Map.Entry<String, JsonNode>> periodFieldIterator = period.fields();

                // 遍历每个时段的参数
                while (periodFieldIterator.hasNext()) {
                    Map.Entry<String, JsonNode> periodField = periodFieldIterator.next();
                    String paramName = periodField.getKey();
                    // 找出要转换的参数
                    if (VALID_TIME.equals(paramName)) {
                        ArrayNode sourceValidTimeArrNode = (ArrayNode) periodField.getValue();

                        ArrayNode targetValidTimeArrNode = targetPeriodItem.putArray(VALID_TIME);
                        if (sourceValidTimeArrNode.size() == 4) {
                            targetValidTimeArrNode.add(sourceValidTimeArrNode.get(0).asText() + ":" + sourceValidTimeArrNode.get(1).asText());
                            targetValidTimeArrNode.add(sourceValidTimeArrNode.get(2).asText() + ":" + sourceValidTimeArrNode.get(3).asText());
                        }

                        targetPeriodItem.set(VALID_TIME, targetValidTimeArrNode);

                    } else if (VALID_TIME_END_TIME.equals(paramName)) {
                        ArrayNode targetValidTimeEndTimeArrNode = targetPeriodItem.putArray(VALID_TIME_END_TIME);

                        ArrayNode sourceValidTimeEndTimeArrNode = (ArrayNode) periodField.getValue();
                        if (sourceValidTimeEndTimeArrNode.size() >= 6) {
                            targetValidTimeEndTimeArrNode.add(sourceValidTimeEndTimeArrNode.get(0).asText() + "-" + sourceValidTimeEndTimeArrNode.get(1).asText() + "-" + sourceValidTimeEndTimeArrNode.get(2).asText());
                            targetValidTimeEndTimeArrNode.add(sourceValidTimeEndTimeArrNode.get(3).asText() + "-" + sourceValidTimeEndTimeArrNode.get(4).asText() + "-" + sourceValidTimeEndTimeArrNode.get(5).asText());
                        }
                        if (sourceValidTimeEndTimeArrNode.size() == 7) {
                            ArrayNode targetWeekArr = targetPeriodItem.putArray("weekArr");
                            JsonNode weekNumNode = sourceValidTimeEndTimeArrNode.get(6);
                            if (StrUtil.isNotEmpty(weekNumNode.asText())) {
                                int weekNum = Integer.parseInt(weekNumNode.asText());
                                for (int i = 0; i < 8; i++) {
                                    int result = (weekNum >> i) & 1;
                                    if (result == 1) {
                                        targetWeekArr.add(String.valueOf((int) Math.pow(2, i)));
                                    }
                                }
                            }
                        }
                    } else if (FLOOR_ARRAY.equals(paramName)) {
                        ArrayNode sourceFloorArrayNode = (ArrayNode) periodField.getValue();
                        ArrayNode targetFloorArrayNode = targetPeriodItem.putArray(paramName);
                        sourceFloorArrayNode.forEach(floorNumNode -> {
                            int floorNum = Integer.parseInt(floorNumNode.asText(), 16);
                            targetFloorArrayNode.add(String.valueOf(floorNum));
                        });
                        if (targetFloorArrayNode.size() < 24) {
                            String zero = "0";
                            for (int i = targetFloorArrayNode.size(); i <= 24; i++) {
                                targetFloorArrayNode.add(zero);
                            }
                        }
                    } else {
                        // 不需要转换的参数
                        String key = periodField.getKey();
                        if (!"weekArr".equals(key)) {
                            targetPeriodItem.set(key, periodField.getValue());
                        }
                    }
                }
                targetPeriodArrNode.add(targetPeriodItem);
            });
        }
//        System.out.println("转换后：" + target);
    }

    @Override
    public void revertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());

        //TODO: 还少个楼层数组的设置
//        System.out.println("转换前：" + source);
        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fields = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            ArrayNode sourcePeriodArrNode = (ArrayNode) sourceParam.path(PERIODS);
            ArrayNode targetPeriodArrNode = targetParam.putArray(PERIODS);

            sourcePeriodArrNode.forEach(period -> {
                // 转换后的参数都放到这个对象里面
                ObjectNode targetPeriodItem = objectMapper.createObjectNode();

                // 获取到每一个时段的迭代器
                Iterator<Map.Entry<String, JsonNode>> periodFieldIterator = period.fields();

                // 遍历每个时段的参数
                while (periodFieldIterator.hasNext()) {
                    Map.Entry<String, JsonNode> periodField = periodFieldIterator.next();
                    String paramName = periodField.getKey();
                    // 找出要转换的参数
                    if (VALID_TIME.equals(paramName)) {
                        ArrayNode sourceValidTimeArrNode = (ArrayNode) periodField.getValue();

                        ArrayNode targetValidTimeArrNode = targetPeriodItem.putArray(VALID_TIME);

                        sourceValidTimeArrNode.forEach(timeNode -> {
                            String time = timeNode.asText();
                            if (StrUtil.isNotEmpty(time)) {
                                String[] split = time.split(":");
                                targetValidTimeArrNode.add(split[0]);
                                targetValidTimeArrNode.add(split[1]);
                            }
                        });
                        targetPeriodItem.set(VALID_TIME, targetValidTimeArrNode);
                    } else if (VALID_TIME_END_TIME.equals(paramName)) {
                        ArrayNode targetValidTimeEndTimeArrNode = targetPeriodItem.putArray(VALID_TIME_END_TIME);

                        ArrayNode sourceValidTimeEndTimeArrNode = (ArrayNode) periodField.getValue();

                        sourceValidTimeEndTimeArrNode.forEach(dateNode -> {
                            String date = dateNode.asText();
                            if (StrUtil.isNotEmpty(date)) {
                                String[] split = date.split("-");
                                for (String s : split) {
                                    targetValidTimeEndTimeArrNode.add(s);
                                }
                            }
                        });
                        ArrayNode weekArr = (ArrayNode) period.path("weekArr");
                        int weekNum = 0;
                        for (int i = 0; i < weekArr.size(); i++) {
                            weekNum += Integer.parseInt(weekArr.get(i).asText());
                        }
                        targetValidTimeEndTimeArrNode.add(weekNum);

                    } else if (FLOOR_ARRAY.equals(paramName)) {
                        ArrayNode sourceFloorArrayNode = (ArrayNode) periodField.getValue();
                        ArrayNode targetFloorArrayNode = targetPeriodItem.putArray(paramName);
                        sourceFloorArrayNode.forEach(floorNumNode -> {
                            int floorNum = Integer.parseInt(floorNumNode.asText());
                            targetFloorArrayNode.add(Integer.toUnsignedString(floorNum, 16));
                        });
                        if (targetFloorArrayNode.size() < 24) {
                            String zero = "0";
                            for (int i = targetFloorArrayNode.size(); i <= 24; i++) {
                                targetFloorArrayNode.add(zero);
                            }
                        }
                    } else {
                        // 不需要转换的参数
                        String key = periodField.getKey();
                        if (!"weekArr".equals(key)) {
                            targetPeriodItem.set(key, periodField.getValue());
                        }
                    }
                }
                targetPeriodArrNode.add(targetPeriodItem);
            });
        }
//        System.out.println("转换后：" + target);
    }

    @Override
    public PlatformEnum getPlateForm() {
        return PlatformEnum.AURINE_EDGE_MIDDLE;
    }

    @Override
    public DeviceParamEnum getServiceId() {
        return DeviceParamEnum.EDGE_LIFT_PERIOD_MANAGE_PARAMS_OBJ;
    }

    /*
     * 测试方法
     * */
  /*  public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"LiftPeriodManageParamsObj\":{\"periods\":[{\"pswitch\":\"1\",\"mode\":\"1\",\"validTimeEndTime\":[\"2022-03-23\",\"2022-04-13\"],\"validTime\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[\"1\",\"2\",\"4\",\"8\",\"16\",\"32\",\"64\"]},{\"pswitch\":\"1\",\"mode\":\"1\",\"validTimeEndTime\":[\"2022-03-16\",\"2022-04-14\"],\"validTime\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[\"2\",\"4\",\"16\"]},{\"pswitch\":\"0\",\"mode\":\"1\",\"validTimeEndTime\":[\"2022-03-16\",\"2022-04-14\"],\"validTime\":[\"02:04\",\"08:12\"],\"floorArray\":[],\"weekArr\":[\"1\",\"16\"]},{\"pswitch\":\"0\",\"mode\":\"0\",\"validTimeEndTime\":[\"2022-03-22\",\"2022-04-19\"],\"validTime\":[\"00:00\",\"00:08\"],\"floorArray\":[],\"weekArr\":[\"1\",\"2\",\"4\",\"8\",\"16\",\"32\",\"64\"]}]}}";
        System.out.println("origin:" + json);
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        JsonNode jsonNode = objectMapper.readTree(json);
        EdgeLiftPeriodParamsHandler edgeLiftDeviceParamsHandler = new EdgeLiftPeriodParamsHandler();
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
