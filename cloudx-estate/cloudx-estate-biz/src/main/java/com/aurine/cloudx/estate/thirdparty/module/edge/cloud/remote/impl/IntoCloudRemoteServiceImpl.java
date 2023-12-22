package com.aurine.cloudx.estate.thirdparty.module.edge.cloud.remote.impl;

import com.aurine.cloudx.common.core.util.StringUtil;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.IntoCloudRequestInfoDTO;
import com.aurine.cloudx.estate.entity.IntoCloudResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.cloud.remote.IntoCloudRemoteService;
import com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil;
import com.aurine.cloudx.estate.util.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.get;
import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.post;

@Slf4j
@Service
@RefreshScope
public class IntoCloudRemoteServiceImpl extends IntoCloudRemoteService {

    @Value("${server.cloud-uri}")
    String cloudUrl;
    @Value("${server.edge-center-uri}")
    String edgeCenterUrl;

    @Override
    public IntoCloudResponse requestIntoCloud(IntoCloudRequestInfoDTO intoCloudRequestInfoDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperUtil.instance();

        EdgeCascadeHttpUtil.RequestResponse response = post(cloudUrl + "/estate/projectConnectRequest/requestIntoCloud", intoCloudRequestInfoDTO);
        if (response.isSuccess()) {

            String dataStr = response.getData();

            JsonNode jsonNode = objectMapper.readValue(dataStr, JsonNode.class);
            JsonNode dataJsonNode = jsonNode.requiredAt("/data");
            return objectMapper.readValue(dataJsonNode.toString(), IntoCloudResponse.class);
        }
        throw new RuntimeException("申请入云失败");
    }

    @Override
    public IntoCloudResponse cancelRequest(String projectCode, String connectionCode) throws JsonProcessingException {
        ObjectMapper objectMapper = ObjectMapperUtil.instance();
        EdgeCascadeHttpUtil.RequestResponse response = get(cloudUrl + "/estate/projectConnectRequest/cancelRequest/" + projectCode + "/" + connectionCode, null);
        if (response.isSuccess()) {
            String data = response.getData();
            if (StringUtil.isNotEmpty(data)) {
                JsonNode dataJsonNode = objectMapper.readValue(data, JsonNode.class);
                return objectMapper.readValue(dataJsonNode.toString(), IntoCloudResponse.class);
            }
        }
        throw new RuntimeException("撤销入云申请失败");
    }

}
