package com.aurine.cloudx.open.api.inner.feign;

import com.aurine.cloudx.open.common.entity.base.OpenApiHeader;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 入云解绑
 *
 * @author : Qiu
 * @date : 2021 12 24 16:35
 */

@FeignClient(contextId = "remoteCascadeCloudUnbind", value = "cloudx-open-biz")
public interface RemoteCascadeCloudUnbindService {

    /**
     * 申请入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/cloud-unbind/apply/{projectCode}")
    R<Boolean> apply(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 撤销入云解绑
     * （边缘侧 -> 平台侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/cloud-unbind/revoke/{projectCode}")
    R<Boolean> revoke(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 同意入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/cloud-unbind/accept/{projectCode}")
    R<Boolean> accept(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);

    /**
     * 拒绝入云解绑
     * （平台侧 -> 边缘侧）
     *
     * @param header      请求头信息
     * @param projectCode 项目第三方code
     * @return R 返回结果
     */
    @PutMapping("/v1/cascade/cloud-unbind/reject/{projectCode}")
    R<Boolean> reject(@RequestBody OpenApiHeader header, @PathVariable("projectCode") String projectCode);
}
