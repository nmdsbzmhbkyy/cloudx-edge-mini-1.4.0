package com.aurine.cloudx.open.origin.feign;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author ： huangjj
 * @date ： 2021/5/13
 * @description： 楼栋管理接口
 */
@FeignClient(contextId = "openRemoteBuildingInfoService", value = "cloudx-estate-biz")
public interface RemoteBuildingInfoService {

    /**
     * 功能描述: 获取项目下的楼栋列表
     *
     * @author huangjj
     * @date 2021/5/13
     * @param projectId
     * @return
    */
    @GetMapping("/baseBuilding/inner/list/{projectId}")
    R innerListByProjectId(@PathVariable(value = "projectId") Integer projectId, @RequestHeader(SecurityConstants.FROM) String from);

    @GetMapping("/baseBuilding/inner/info/{buildingId}")
    public R innerBuildingInfo(@PathVariable("buildingId") String buildingId, @RequestHeader(SecurityConstants.FROM) String from);


}