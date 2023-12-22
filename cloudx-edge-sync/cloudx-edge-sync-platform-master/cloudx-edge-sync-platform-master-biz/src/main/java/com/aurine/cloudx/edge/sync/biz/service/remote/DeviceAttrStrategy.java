package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaDeviceAttrService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectDeviceAttr;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 设备拓展属性管理
 *
 * @author：zouyu
 * @data: 2023-05-04 14:00:55
 */
@Slf4j
@Component
public class DeviceAttrStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaDeviceAttrService remoteMetaDeviceAttrService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectDeviceAttr.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaDeviceAttrService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("attrId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) {
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("attrId", uuid);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectDeviceAttr.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaDeviceAttrService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String uuid) {
        log.info("intoCloud -> openApi delete req= {}", uuid);
        R r = remoteMetaDeviceAttrService.delete(appId, projectUUID, tenantId, uuid);
        log.info("intoCloud -> openApi delete res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.DEVICE_ATTR.name, this);
    }
}
