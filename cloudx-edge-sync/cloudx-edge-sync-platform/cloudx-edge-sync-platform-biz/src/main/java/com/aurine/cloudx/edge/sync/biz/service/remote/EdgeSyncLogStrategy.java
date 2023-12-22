package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.origin.entity.EdgeSyncLog;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeSyncLogService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 边缘网关同步日志，存储手动同步历史记录
 *
 * @author：zouyu
 * @data: 2022/3/24 9:56
 */
@Slf4j
@Component
public class EdgeSyncLogStrategy implements BaseStrategy {

    @Resource
    private RemoteCascadeSyncLogService remoteCascadeSyncLogService;


    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) throws Exception {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), EdgeSyncLog.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteCascadeSyncLogService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("syncId");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception {
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("syncId", uuid);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), EdgeSyncLog.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteCascadeSyncLogService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.CASCADE_SYNC_LOG.name, this);
    }
}
