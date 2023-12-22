package com.aurine.cloudx.edge.sync.biz.service.parkingremote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaEntryExitLaneService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntryExitLane;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 *  出入口车道信息
 *
 * @author zy
 * @Date 2022-08-15 09:19:47
 */
@Slf4j
@Component
public class EntryExitLaneStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaEntryExitLaneService remoteMetaEntryExitLaneService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("[车场入云] {} add 请求数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectEntryExitLane.class));
        openApiModel.setHeader(requestObj.getHeader());
        requestObj.getData().put("seq", null);
        R r = remoteMetaEntryExitLaneService.save(openApiModel);
        log.info("[车场入云] {} add 响应数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("laneId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) {
        log.info("[车场入云] {} update 请求数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("laneId", uuid);
        data.put("seq", null);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectEntryExitLane.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaEntryExitLaneService.update(openApiModel);
        log.info("[车场入云] {} update 响应数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String uuid) {
        log.info("[车场入云] {} delete 请求数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, uuid);
        R r = remoteMetaEntryExitLaneService.delete(appId, projectUUID, tenantId, uuid);
        log.info("[车场入云] {} delete 响应数据:{}", ServiceNameEnum.ENTRY_EXIT_LANE.name, uuid);
        return new OpenRespVo(null, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.ENTRY_EXIT_LANE.name, this);
    }
}
