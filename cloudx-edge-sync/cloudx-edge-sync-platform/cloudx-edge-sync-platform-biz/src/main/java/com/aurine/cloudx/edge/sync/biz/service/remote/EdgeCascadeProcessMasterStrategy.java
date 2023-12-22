package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.origin.entity.EdgeCascadeProcessMaster;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeProcessMasterService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 边缘网关对接进度表
 *
 * @author：zouyu
 * @data: 2022/3/24 9:49
 */
@Slf4j
@Component
public class EdgeCascadeProcessMasterStrategy implements BaseStrategy {

    @Resource
    private RemoteCascadeProcessMasterService remoteCascadeProcessMasterService;


    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) throws Exception {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), EdgeCascadeProcessMaster.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteCascadeProcessMasterService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("eventId");
        return new OpenRespVo(id, r);

    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception {
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("eventId", uuid);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), EdgeCascadeProcessMaster.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteCascadeProcessMasterService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.CASCADE_PROCESS_MASTER.name, this);
    }
}
