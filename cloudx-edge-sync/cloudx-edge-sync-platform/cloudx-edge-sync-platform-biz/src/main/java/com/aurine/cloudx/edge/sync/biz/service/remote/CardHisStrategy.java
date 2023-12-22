package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategy;
import com.aurine.cloudx.edge.sync.common.config.BaseStrategyFactory;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaCardHisService;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaCardInfoService;
import com.aurine.cloudx.open.common.core.constant.enums.ServiceNameEnum;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectCard;
import com.aurine.cloudx.open.origin.entity.ProjectCardHis;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 卡操作记录
 *
 * @author zy
 * @date 2022-10-18 08:40:49
 */

@Slf4j
@Component
public class CardHisStrategy implements BaseStrategy {

    @Resource
    private RemoteMetaCardHisService remoteMetaCardHisService;

    @Override
    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));
        requestObj.getData().put("seq", null);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectCardHis.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaCardHisService.save(openApiModel);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));
        if (r.getCode() != 0) {
            return new OpenRespVo(null, r);
        }
        JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(r.getData()));
        String id = object.getString("uid");
        return new OpenRespVo(id, r);
    }

    @Override
    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception {
        log.info("intoCloud -> openApi update req= {}", JSON.toJSONString(requestObj));
        JSONObject data = requestObj.getData();
        data.put("uid", uuid);
        data.put("seq", null);
        OpenApiModel openApiModel = new OpenApiModel();
        openApiModel.setData(JSONObject.toJavaObject(requestObj.getData(), ProjectCardHis.class));
        openApiModel.setHeader(requestObj.getHeader());
        R r = remoteMetaCardHisService.update(openApiModel);
        log.info("intoCloud -> openApi update res = {}", JSON.toJSONString(r));
        return new OpenRespVo(null, r);
    }

    @Override
    public OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        BaseStrategyFactory.registerStrategy(ServiceNameEnum.CARD_HIS.name, this);
    }
}
