package com.aurine.cloudx.estate.thirdparty.module.edge.cascade.remote.impl;

import com.aurine.cloudx.estate.entity.CascadeRequestInfoDTO;
import com.aurine.cloudx.estate.entity.EdgeCascadeResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.MQTTAccount;
import com.aurine.cloudx.estate.thirdparty.module.edge.cascade.remote.CascadeRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.get;
import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.post;


/**
 * <p>级联remote service</p>
 * @author : 王良俊
 * @date : 2021-12-27 08:58:12
 */
@Slf4j
@Service
public class CascadeRemoteServiceImpl extends CascadeRemoteService {

    @Value("${server.edge-center-uri}")
    String edgeCenterUrl;

    @SneakyThrows
    @Override
    public EdgeCascadeResponse requestCascade(String parentIp, CascadeRequestInfoDTO cascadeRequestInfoDTO) {

        EdgeCascadeHttpUtil.RequestResponse response = post("http://" + parentIp + ":9999/estate/edgeCascadeRequestMaster/requestCascade",
                cascadeRequestInfoDTO);
        log.info("申请级联返回结果：{}", response);
        if (response.isSuccess()) {

            String dataStr = response.getData();
            JsonNode jsonNode = objectMapper.readValue(dataStr, JsonNode.class);
            JsonNode dataJsonNode = jsonNode.requiredAt("/data");
            return objectMapper.readValue(dataJsonNode.toString(), EdgeCascadeResponse.class);
        }
        throw new RuntimeException("申请级联失败");
    }

    @SneakyThrows
    @Override
    public boolean createAccount(MQTTAccount mqttAccount) {

        EdgeCascadeHttpUtil.RequestResponse response = post(edgeCenterUrl + "/v1/mqtt/api/acl/update", mqttAccount);
        if (response.isSuccess()) {
            String data = response.getData();
            ObjectNode result = objectMapper.readValue(data, ObjectNode.class);
            int errorCode = result.findPath("errorCode").asInt();
            if (errorCode == 0) {
                log.info("MQTT账号创建成功：{}", mqttAccount.toString());
                return true;
            } else {
                log.error("MQTT账号创建失败：{} 失败原因：{}", mqttAccount.toString(), result.findPath("errorMessage").asText());
            }
        }
        return false;
    }

    @Override
    public EdgeCascadeResponse cancelRequest(String projectCode, String parentIp) throws JsonProcessingException {
        EdgeCascadeHttpUtil.RequestResponse response = get("http://" + parentIp + ":9999/estate/edgeCascadeRequestMaster/cancelRequest/" + projectCode, null);
        if (response.isSuccess()) {
            return objectMapper.readValue(response.getData(), EdgeCascadeResponse.class);
        }
        throw new RuntimeException("撤销级联申请失败");
    }
}
