package com.aurine.cloudx.estate.thirdparty.module.edge;

import cn.hutool.core.util.StrUtil;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.DriverManagerReq;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.BridgingConfInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.EdgeIaasResponse;
import com.aurine.cloudx.estate.thirdparty.module.edge.entity.SubCfgInfo;
import com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.get;
import static com.aurine.cloudx.estate.thirdparty.module.edge.util.EdgeCascadeHttpUtil.post;

/**
 * <p>入云级联remote抽象类</p>
 * @author : 王良俊
 * @date : 2021-12-27 13:59:45
 */

public abstract class CascadeCloudRemoteServiceAbstract implements CascadeCloudRemoteService{

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${server.edge-center-uri}")
    String edgeCenterUrl;

    @Override
    public String getDeviceSN() {
        EdgeCascadeHttpUtil.RequestResponse response = get(edgeCenterUrl + "/v1/mqtt/api/sn", new HashMap<>());
        return response.getData();
    }

    @SneakyThrows
    @Override
    public boolean configBridging(BridgingConfInfo bridgingConfInfo) {
        log.info("准备配置桥接：{}", bridgingConfInfo);
        EdgeCascadeHttpUtil.RequestResponse response = post(edgeCenterUrl + "/v1/mqtt/api/mosquitto/update", bridgingConfInfo);
        if (response.isSuccess()) {
            JsonNode result = objectMapper.readValue(response.getData(), JsonNode.class);
            if (result.findPath("errorCode").asInt() != 0) {
                throw new RuntimeException("桥接配置失败");
            }
        } else {
            throw new RuntimeException("桥接配置失败，网络问题");
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean pubSubCfg(SubCfgInfo subCfgInfo) {
        log.info("准备新增订阅配置：{}", subCfgInfo);
        if (subCfgInfo.getCommunityId() == null) {
            subCfgInfo.setCommunityId("");
        }
        if (subCfgInfo.getMachineSN() == null) {
            subCfgInfo.setMachineSN("");
        }
        EdgeCascadeHttpUtil.RequestResponse response = post(edgeCenterUrl + "/v1/mqtt/api/mosquitto/pubSubCfg", subCfgInfo);
        if (response.isSuccess()) {
            EdgeIaasResponse edgeIaasResponse = objectMapper.readValue(response.getData(), EdgeIaasResponse.class);
//            JsonNode result = objectMapper.readValue(response.getData(), JsonNode.class);
            if (edgeIaasResponse.getErrorCode() != 0) {
                throw new RuntimeException("订阅发布失败");
            }
        } else {
            throw new RuntimeException("订阅发布失败，网络问题");
        }
        return true;
    }

    @SneakyThrows
    @Override
    public boolean confDriverCommunityId(DriverManagerReq req) {
        log.info("准备配置驱动社区ID：{}", req);
        if (StrUtil.isEmpty(req.getCommunityId())) {
            log.info("配置失败-社区ID不能为空");
        }
        EdgeCascadeHttpUtil.RequestResponse response = post(edgeCenterUrl + "/v1/mqtt/api/driver/communityId", req);
        if (response.isSuccess()) {
            EdgeIaasResponse edgeIaasResponse = objectMapper.readValue(response.getData(), EdgeIaasResponse.class);
            return edgeIaasResponse.getErrorCode() == 0;
        }
        return false;
    }
}
