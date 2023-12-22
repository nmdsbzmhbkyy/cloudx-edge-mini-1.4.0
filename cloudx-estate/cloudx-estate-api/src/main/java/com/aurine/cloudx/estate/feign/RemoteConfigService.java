package com.aurine.cloudx.estate.feign;

import com.aurine.cloudx.estate.vo.ProjectFrameInfoTreeVo;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 楼栋管理接口
 */
@FeignClient(contextId = "remoteConfigService", value = "cloudx-estate-biz")
public interface RemoteConfigService {
    /**
     * 是否开启一车多位
     *
     * @param projectId
     * @return
     */
    @GetMapping("/projectConfig/isEnableMultiCarsPerPlace/{projectId}")
    R<Boolean> isEnableMultiCarsPerPlace(@PathVariable("projectId") Integer projectId);
}
