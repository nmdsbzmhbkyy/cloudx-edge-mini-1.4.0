package com.aurine.cloudx.estate.thirdparty.module.device.handler;

import cn.hutool.core.collection.CollUtil;
import com.aurine.cloudx.estate.thirdparty.main.entity.constant.enums.PlatformEnum;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.base.BaseParamDataHandler;
import com.aurine.cloudx.estate.thirdparty.module.device.handler.factory.DeviceParamDataHandlerFactory;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>设备参数数据处理类</p>
 *
 * @author : 王良俊
 * @date : 2022/2/7 16:50
 */
@Component
public class DeviceParamDataHandleChain {

    /**
     * <p>对参数进行调整 数据库取出参数 调整后 输出给前端 或是 前端修改后 下发给设备和保存数据库，这里将参数调整为前端能接受的格式</p>
     * <p>设备端IP地址是存储在数组中，前端是拼接成完整的字符串给前端</p>
     *
     * @param toFrontData   是否是要转换成前端数据格式的数据（否则是要转换成下发到设备上的数据格式）
     * @param serviceIdList 服务ID列表
     * @param paramJson     参数数据 可能带有其他参数
     * @param platform      当前对接的中台
     */
    public JsonNode handle(List<String> serviceIdList, String paramJson, PlatformEnum platform, boolean toFrontData) throws JsonProcessingException {
        List<BaseParamDataHandler> serviceList = DeviceParamDataHandlerFactory.getService(serviceIdList, platform);
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        ObjectNode targetJsonNode = objectMapper.createObjectNode();
        ObjectNode paramNode = (ObjectNode) objectMapper.readTree(paramJson);
        Iterator<Map.Entry<String, JsonNode>> fields = paramNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> param = fields.next();
            String serviceId = param.getKey();
            targetJsonNode.set(serviceId, param.getValue());
        }
        if (CollUtil.isEmpty(serviceList)) {
            return paramNode;
        }
        if (toFrontData) {
            serviceList.forEach(handler -> {
                handler.convertHandler(paramNode, targetJsonNode);
            });
        } else {
            serviceList.forEach(handler -> {
                handler.revertHandler(paramNode, targetJsonNode);

            });
        }
        return targetJsonNode;
    }

/*
    public static void main(String[] args) throws JsonProcessingException {
        String json = "{\"LiftDeviceParamsObj\":{\"building\":1,\"unit\":1,\"stairNoLen\":4,\"roomNoLen\":4,\"cellNoLen\":4,\"useCellNo\":4,\"deviceNo\":4,\"liftNo\":1,\"master\":1,\"secretKey\":\"\"},\"LiftNetworkParamsObj\":{\"mac\":\"12.21.32.14.54.31\",\"ipAddr\":\"10.110.123.1\",\"netMask\":\"255.255.255.0\",\"gateway\":\"10.110.123.254\",\"centerIp\":\"10.110.123.253\",\"dns1\":\"8.8.8.8\",\"dns2\":\"9.9.9.9\",\"exitips\":[\"192.168.1.1\",\"192.168.1.2\",\"192.168.1.3\",\"192.168.1.4\",\"192.168.1.5\",\"192.168.1.6\",\"192.168.1.7\"]},\"LiftHardwareParamsObj\":{\"extCount\":1,\"rs485\":[{\"baudrate\":\"\",\"dataBit\":\"\",\"stopBit\":\"\",\"parity\":\"\"},{\"baudrate\":\"\",\"dataBit\":\"\",\"stopBit\":\"\",\"parity\":\"\"},{\"baudrate\":\"\",\"dataBit\":\"\",\"stopBit\":\"\",\"parity\":\"\"},{\"baudrate\":\"\",\"dataBit\":\"\",\"stopBit\":\"\",\"parity\":\"\"}],\"localBlushCard\":1,\"fireAction\":1,\"readCardSel\":1},\"LiftBaseParamsObj\":{\"floorSet\":\"1~3,5~21\",\"openDoorMode\":1,\"floorOffset\":1,\"autoCall\":1,\"autoCallTm\":1000,\"manualCallTm\":1000,\"visitorCallTm\":1000,\"cardSectorOffset\":1},\"LiftSysOperateObj\":{\"statue\":1},\"LiftFloorObj\":{\"floorSwArray\":[]},\"LiftPeriodManageParamsObj\":{\"periods\":[{\"pswitch\":\"\",\"mode\":\"\",\"validTime\":[],\"validDate\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[\"1\",\"2\",\"4\",\"8\",\"16\",\"32\",\"64\"]},{\"pswitch\":\"\",\"mode\":\"\",\"validTime\":[],\"validDate\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[]},{\"pswitch\":\"\",\"mode\":\"\",\"validTime\":[],\"validDate\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[]},{\"pswitch\":\"\",\"mode\":\"\",\"validTime\":[],\"validDate\":[\"00:00\",\"00:00\"],\"floorArray\":[],\"weekArr\":[]}],\"pswitch\":1,\"mode\":1,\"floorArray\":[],\"validTime\":[],\"validDate\":[]},\"LiftBeautyPeriodParamsObj\":{\"bswitch\":1,\"validTime\":[\"00:00\",\"00:00\"]}}";
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        JsonNode paramNode = objectMapper.readTree(json);
        List<String> serviceIdList = new ArrayList<>();
        serviceIdList.add("LiftDeviceParamsObj");
        ObjectNode targetJsonNode = objectMapper.createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> fields = paramNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> param = fields.next();
            String serviceId = param.getKey();
            if (!serviceIdList.contains(serviceId)) {
                targetJsonNode.set(serviceId, param.getValue());
            } else {
                targetJsonNode.set(serviceId, objectMapper.createObjectNode());
            }
        }

        System.out.println(targetJsonNode);
    }
*/

}
