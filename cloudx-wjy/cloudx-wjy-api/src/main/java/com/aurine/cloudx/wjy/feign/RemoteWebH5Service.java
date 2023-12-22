package com.aurine.cloudx.wjy.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ： huangjj
 * @date ： 2021/4/16
 * @description： WEB后端h5页面
 */
@FeignClient(contextId = "remoteWebH5Service2", value = "cloudx-wjy-biz")
public interface RemoteWebH5Service {
    /**
     * 功能描述: WEB后端H5页面URL获取
     *
     * @author huangjj
     * @date 2021/4/16
     * @param projectId 项目id
     * @param moduleType 业务类型
     * @return 返回URL
     */
    @GetMapping("/web/module")
    public R getModule(
            @RequestParam(value = "projectId") Integer projectId,
            @RequestParam(value = "moduleType") String moduleType);
}
