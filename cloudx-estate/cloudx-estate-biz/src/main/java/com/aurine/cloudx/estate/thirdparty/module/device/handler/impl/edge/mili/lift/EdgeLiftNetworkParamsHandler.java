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
public class EdgeLiftNetworkParamsHandler implements BaseParamDataHandler {

    private static final String EXITIPS = "exitips";
    private static final String ETHMODE = "EthMode";

    @Override
    public void convertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());

        System.out.println("转换前：" + source);
        JsonNode sourceParam = source.path(getServiceId().serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fields = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(getServiceId().serviceId);
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> next = fields.next();
                String paramName = next.getKey();
                JsonNode value = next.getValue();
                if (EXITIPS.equals(paramName)) {
                    ArrayNode arrayNode = targetParam.putArray(EXITIPS);
                    ArrayNode ipArrNode = (ArrayNode) value;
                    int startIndex = 0;
                    int endIndex = 3;
                    for (; endIndex <= 27; endIndex += 4, startIndex += 4) {
                        StringBuilder ip = new StringBuilder();
                        for (int curr = startIndex; curr <= endIndex; curr++) {
                            JsonNode jsonNode = ipArrNode.get(curr);
                            if (!jsonNode.isMissingNode() && StrUtil.isNotEmpty(jsonNode.asText())) {
                                ip.append(jsonNode.asText());
                                if (curr != endIndex) {
                                    ip.append(".");
                                }
                            } else {
                                break;
                            }
                        }
                        arrayNode.add(ip.toString());
                    }
                }else if(ETHMODE.equals(paramName)){
                    targetParam.put(paramName, value);
                }
                else {
                    ArrayNode itemArrNode = (ArrayNode) value;
                    StringBuilder ip = new StringBuilder();
                    for (int i = 0; i < itemArrNode.size(); i++) {
                        ip.append(itemArrNode.get(i).asText());
                        if (i != itemArrNode.size() - 1) {
                            ip.append(".");
                        }
                    }
                    targetParam.put(paramName, ip.toString());
                }

            }
        }
        System.out.println("转换后：" + target);
    }

    @Override
    public void revertHandler(ObjectNode source, ObjectNode target) {
        target.remove(getServiceId().serviceId);
        target.set(getServiceId().serviceId, objectMapper.createObjectNode());

        System.out.println("转换前：" + source);
        JsonNode sourceParam = source.path(DeviceParamEnum.EDGE_LIFT_NETWORK_PARAMS_OBJ.serviceId);
        if (!sourceParam.isMissingNode()) {
            Iterator<Map.Entry<String, JsonNode>> fields = sourceParam.fields();
            ObjectNode targetParam = (ObjectNode) target.path(DeviceParamEnum.EDGE_LIFT_NETWORK_PARAMS_OBJ.serviceId);
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> next = fields.next();
                String paramName = next.getKey();
                JsonNode value = next.getValue();
                if (EXITIPS.equals(paramName)) {
                    ArrayNode arrayNode = targetParam.putArray(paramName);
                    ArrayNode ipArrNode = (ArrayNode) value;
                    ipArrNode.forEach(ipNode -> {
                        String ip = ipNode.asText();
                        if (StrUtil.isNotEmpty(ip)) {
                            String[] split = ip.split("\\.");
                            for (int i = 0; i < 4; i++) {
                                if (split.length == 4) {
                                    arrayNode.add(split[i]);
                                } else {
                                    arrayNode.add("");
                                }
                            }
                        }
                    });
                }else if(ETHMODE.equals(paramName)){
                    targetParam.put(paramName, value);
                }else {
                    String address = value.asText();
                    if (StrUtil.isNotEmpty(address)) {
                        String[] split = address.split("\\.");
                        ArrayNode arrayNode = targetParam.putArray(paramName);
                        for (String s : split) {
                            arrayNode.add(s);
                        }
                    }
                }
            }
        }
        System.out.println("转换后：" + target);
    }

    @Override
    public PlatformEnum getPlateForm() {
        return PlatformEnum.AURINE_EDGE_MIDDLE;
    }

    @Override
    public DeviceParamEnum getServiceId() {
        return DeviceParamEnum.EDGE_LIFT_NETWORK_PARAMS_OBJ;
    }

    /*
    * 测试方法
    * */
/*    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"LiftNetworkParamsObj\":{\"mac\":\"12.21.32.14.54.31\",\"ipAddr\":\"10.110.123.1\",\"netMask\":\"255.255.255.0\",\"gateway\":\"10.110.123.254\",\"centerIp\":\"10.110.123.253\",\"dns1\":\"8.8.8.8\",\"dns2\":\"9.9.9.9\",\"exitips\":[\"192.168.1.1\",\"192.168.1.2\",\"192.168.1.3\",\"192.168.1.4\",\"192.168.1.5\",\"192.168.1.6\",\"192.168.1.7\"]}}";
        System.out.println("origin:" + json);
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        JsonNode jsonNode = objectMapper.readTree(json);
        EdgeLiftDeviceParamsHandler edgeLiftDeviceParamsHandler = new EdgeLiftDeviceParamsHandler();
        ObjectNode objectNode1 = objectMapper.createObjectNode();
        objectNode1.set("LiftNetworkParamsObj", objectMapper.createObjectNode());
        edgeLiftDeviceParamsHandler.revertHandler(jsonNode, objectNode1);
        System.out.println("convert:" + objectNode1);
        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.set("LiftNetworkParamsObj", objectMapper.createObjectNode());
        edgeLiftDeviceParamsHandler.convertHandler(objectNode1, objectNode2);
        System.out.println("revert:" + objectNode2);
    }*/

}
