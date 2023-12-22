package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 级联申请
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@FeignClient(contextId = "remoteCascadeEdgeApply", value = "cloudx-open-biz")
public interface RemoteCascadeEdgeApplyService {

    /**
     * 撤销级联申请
     * （从网关 -> 主网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-apply/revoke/{projectCode}")
    R<Boolean> revoke(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 同意级联申请
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-apply/accept/{projectCode}")
    R<Boolean> accept(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 拒绝级联申请
     * （主网关 -> 从网关）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/edge-apply/reject/{projectCode}")
    R<Boolean> reject(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);
}
