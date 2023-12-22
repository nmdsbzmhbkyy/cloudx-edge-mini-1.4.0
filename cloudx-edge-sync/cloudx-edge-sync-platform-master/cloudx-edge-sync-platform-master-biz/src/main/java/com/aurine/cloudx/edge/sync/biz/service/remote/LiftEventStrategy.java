package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.origin.entity.ProjectLiftEvent;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaLiftEventService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 乘梯事件记录
 *
 * @author：zouyu
 * @data: 2022-07-18 10:46:57
 */
@Slf4j
@Component
public class LiftEventStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaLiftEventService remoteMetaLiftEventService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("intoCloud -> openApi add req= " + JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectLiftEvent.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaLiftEventService.save(openApiModel);
        log.info("intoCloud -> openApi add res = " + JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("eventId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception {
        return null;
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception {
        return null;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.LIFT_EVENT.name, this);
    }
}
