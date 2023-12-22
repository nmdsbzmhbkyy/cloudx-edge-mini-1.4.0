package com.aurine.cloudx.edge.sync.biz.service.parkingremote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaParkingPlaceService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectParkingPlace;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liuzhiming
 * @date 2022/8/16
 */
@Slf4j
@Component
public class ParkingPlaceStrategy implements BaseStrategy{
    @Resource
    private RemoteMetaParkingPlaceService remoteMetaParkingPlaceService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("[车场入云] {} add 请求数据:{}", ServiceNameEnum.PARKING_PLACE.name, JSON.toJSONString(requestObj));
        requestObj.getData().put("seq", null);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectParkingPlace.class));
        openApiModel.setHeader(requestObj.getHeader());

        R r = remoteMetaParkingPlaceService.save(openApiModel);
        log.info("[车场入云] {} add 响应数据:{}", ServiceNameEnum.PARKING_PLACE.name, JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("placeId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) {
        log.info("[车场入云] {} update 请求数据:{}", ServiceNameEnum.PARKING_PLACE.name, JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("placeId", uuid);
        data.put("seq", null);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectParkingPlace.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaParkingPlaceService.update(openApiModel);
        log.info("[车场入云] {} update 响应数据:{}", ServiceNameEnum.PARKING_PLACE.name, JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String uuid) {
        log.info("[车场入云] {} delete 请求数据:{}", ServiceNameEnum.PARKING_PLACE.name, uuid);
        R r = remoteMetaParkingPlaceService.delete(appId, projectUUID, tenantId, uuid);
        log.info("[车场入云] {} delete 响应数据:{}", ServiceNameEnum.PARKING_PLACE.name, uuid);
        return new OpenRespVo(null, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.PARKING_PLACE.name, this);
    }
}
