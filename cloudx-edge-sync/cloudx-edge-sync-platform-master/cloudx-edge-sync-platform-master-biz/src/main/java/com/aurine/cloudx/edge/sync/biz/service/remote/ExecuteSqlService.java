package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeCloudApplyService;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeEdgeApplyService;
import com.aurine.cloudx.open.api.inner.feign.RemoteMetaExecuteSqlService;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import com.aurine.cloudx.open.origin.entity.ProjectEntranceAlarmEvent;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 13:57
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 级联绑定
 **/
@Slf4j
@Service
public class ExecuteSqlService {

    @Resource
    private RemoteMetaExecuteSqlService remoteMetaExecuteSqlService;

    public OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) throws Exception {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));

        R r = remoteMetaExecuteSqlService.save(requestObj);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));

        return new OpenRespVo(null, r);
    }


    public OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj) throws Exception {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));

        R r = remoteMetaExecuteSqlService.update(requestObj);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));

        return new OpenRespVo(null, r);
    }


    public OpenRespVo doDelete(OpenApiModel<JSONObject> requestObj) throws Exception {
        log.info("intoCloud -> openApi add req= {}", JSON.toJSONString(requestObj));

        R r = remoteMetaExecuteSqlService.delete(requestObj);
        log.info("intoCloud -> openApi add res = {}", JSON.toJSONString(r));

        return new OpenRespVo(null, r);
    }

}
