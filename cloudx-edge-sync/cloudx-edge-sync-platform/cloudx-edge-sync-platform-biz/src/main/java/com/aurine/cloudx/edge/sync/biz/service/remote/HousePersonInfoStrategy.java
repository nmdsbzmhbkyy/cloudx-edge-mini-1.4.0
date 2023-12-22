package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.biz.constant.Constants;
import com.aurine.cloudx.edge.sync.biz.constant.KafkaConstant;
import com.aurine.cloudx.edge.sync.biz.mq.enums.TypeEnum;
import com.aurine.cloudx.edge.sync.biz.mq.kafka.KafkaProducer;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaHousePersonInfoService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectHousePersonRel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/02/10 12:05
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 住户信息管理
 **/
@Slf4j
@Component
public class HousePersonInfoStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaHousePersonInfoService remoteMetaHousePersonInfoService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        ProjectHousePersonRel projectHousePersonRel = JSONObject.toJavaObject(requestObj.getData(), ProjectHousePersonRel.class);
        openApiModel.setData(projectHousePersonRel);
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaHousePersonInfoService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("relaId");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", TypeEnum.PERSON_ADD.value);
        jsonObject.put("data",projectHousePersonRel);
        jsonObject.put("projectUUID",requestObj.getHeader().getProjectUUID());
        KafkaProducer.sendMessage(KafkaConstant.EDGE_INTERCOM_TOPIC,jsonObject);

        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) {
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("relaId", uuid);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectHousePersonRel.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaHousePersonInfoService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String uuid) {
        log.info("intoCloud -> openApi delete req= {}", uuid);
        R r = remoteMetaHousePersonInfoService.delete(appId, projectUUID, tenantId, uuid);
        log.info("intoCloud -> openApi delete res = {}", JSON.toJSONString(r));


        return new OpenRespVo(null, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.HOUSE_PERSON_INFO.name, this);
    }
}
