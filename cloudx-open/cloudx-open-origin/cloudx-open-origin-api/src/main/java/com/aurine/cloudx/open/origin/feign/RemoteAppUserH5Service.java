package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ： huangjj
 * @date ： 2021/4/16
 * @description： 移动端用户h5页面
 */
@FeignClient(contextId = "openRemoteAppUserH5Service", value = "cloudx-wjy-biz")
public interface RemoteAppUserH5Service {
    /**
     * 功能描述: 移动端用户H5页面URL获取
     *
     * @author huangjj
     * @date 2021/4/16
     * @param projectId 项目id
     * @param moduleType 业务类型
     * @return 返回URL
     */
    @GetMapping("/user/module")
    public R getModule(
            @RequestParam(value = "projectId") Integer projectId,
            @RequestParam(value = "moduleType") String moduleType,
            @RequestParam(value = "phone") String phone);

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

    /**
     * 功能描述: 移动端工程人员H5页面URL获取
     *
     * @author huangjj
     * @date 2021/4/16
     * @param projectId 项目id
     * @param moduleType 业务类型
     * @param phone 手机号码
     * @return 返回URL
     */
    @GetMapping("/engineer/module")
    public R adminGetModule(
            @RequestParam(value = "projectId") Integer projectId,
            @RequestParam(value = "moduleType") String moduleType,
            @RequestParam(value = "phone") String phone
    );
}
