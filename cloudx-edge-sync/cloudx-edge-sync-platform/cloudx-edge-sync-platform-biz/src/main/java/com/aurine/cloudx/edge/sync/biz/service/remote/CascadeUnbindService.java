package com.aurine.cloudx.edge.sync.biz.service.remote;

import com.alibaba.fastjson.JSON;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeCloudUnbindService;
import com.aurine.cloudx.open.api.inner.feign.RemoteCascadeEdgeUnbindService;
import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wrm
 * @Date: 2022/01/20 13:57
 * @Package: com.aurine.cloudx.edge.sync.biz.service.remote
 * @Version: 1.0
 * @Remarks: 级联解绑
 **/
@Slf4j
@Component
public class CascadeUnbindService {

    @Resource
    private RemoteCascadeEdgeUnbindService cascadeEdgeUnbindService;

    @Resource
    private RemoteCascadeCloudUnbindService remoteCascadeCloudUnbindService;

    /**
     * 申请解绑
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R apply(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi applyUnbind req= {}", JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudUnbindService.apply(openApiHeader, projectCode);
        log.info("intoCloud -> openApi applyUnbind res = {}", JSON.toJSONString(r));
        return r;
    }

    /**
     * 同意解绑
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R accept(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi acceptUnbind req= {}", JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudUnbindService.accept(openApiHeader, projectCode);
        log.info("intoCloud -> openApi acceptUnbind res = {}", JSON.toJSONString(r));
        return r;
    }

    /**
     * 同意解绑
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R reject(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi rejectUnbind req= {}", JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudUnbindService.reject(openApiHeader, projectCode);
        log.info("intoCloud -> openApi rejectUnbind res = {}", JSON.toJSONString(r));
        return r;
    }

    /**
     * 撤销解绑
     *
     * @param projectCode
     * @param openApiHeader
     * @return
     */
    public R revoke(String projectCode, OpenApiHeader openApiHeader) {
        log.info("intoCloud -> openApi revokeUnbind req= {}", JSON.toJSONString(openApiHeader));
        R r = remoteCascadeCloudUnbindService.revoke(openApiHeader, projectCode);
        log.info("intoCloud -> openApi revokeUnbind res = {}", JSON.toJSONString(r));
        return r;
    }
}
