package com.aurine.cloudx.wjy.feign;

import com.aurine.cloudx.wjy.vo.CustomerStandardVo;
import com.aurine.cloudx.wjy.vo.CustomerVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 客户管理
 * @author ：huangjj
 * @date ：2021/4/14
 * @description：客户管理路由
 */
@FeignClient(contextId = "remoteCustomerService2", value = "cloudx-wjy-biz")
public interface RemoteCustomerService {
    /**
     * 功能描述: 添加/更新客户信息
     *
     * @author huangjj
     * @date 2021/4/14
     * @param customerVo 客户信息对象
     * @return 返回添加结果
     */
    @PostMapping("/cus/add")
    public R addCus(@RequestBody CustomerVo customerVo);
    @PostMapping("/cus/standard/add")
    public R addStandardCus(@RequestBody CustomerStandardVo customerVo);
}
