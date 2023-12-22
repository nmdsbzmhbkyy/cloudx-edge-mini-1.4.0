package com.aurine.cloudx.edge.sync.common.config;

import com.alibaba.fastjson.JSONObject;
import com.aurine.cloudx.edge.sync.common.entity.vo.OpenRespVo;
import com.aurine.cloudx.open.common.entity.model.OpenApiModel;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: wrm
 * @Date: 2022/01/20 11:55
 * @Package: com.aurine.cloudx.edge.sync.biz.config
 * @Version: 1.0
 * @Remarks: 策略接口，业务类实现继承此接口根据
 **/
public interface BaseStrategy extends InitializingBean {
    /**
     * 新增操作
     *
     * @param requestObj
     * @return r 返回请求结果；id 返回新增操作后返回的主键id
     */
    OpenRespVo doAdd(OpenApiModel<JSONObject> requestObj) throws Exception;

    /**
     * 修改操作
     *
     * @param requestObj
     * @param uuid
     * @return r 返回请求结果；
     */
    OpenRespVo doUpdate(OpenApiModel<JSONObject> requestObj, String uuid) throws Exception;

    /**
     * 删除操作
     *
     * @param id          业务主键
     * @param tenantId    租户id
     * @param appId       appId
     * @param projectUUID 项目UUid
     * @return r 返回请求结果；
     */
    OpenRespVo doDelete(String appId, String projectUUID, Integer tenantId, String id) throws Exception;

}
