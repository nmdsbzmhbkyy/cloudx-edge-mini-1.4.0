package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 级联解绑
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@FeignClient(contextId = "remoteCascadeEdgeUnbind", value = "cloudx-open-biz")
public interface RemoteCascadeEdgeUnbindService {

    /**
     * 申请级联解绑
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-unbind/apply/{projectCode}")
    R<Boolean> apply(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 撤销级联解绑
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-unbind/revoke/{projectCode}")
    R<Boolean> revoke(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 同意级联解绑
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-unbind/accept/{projectCode}")
    R<Boolean> accept(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 拒绝级联解绑
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-unbind/reject/{projectCode}")
    R<Boolean> reject(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);
}
